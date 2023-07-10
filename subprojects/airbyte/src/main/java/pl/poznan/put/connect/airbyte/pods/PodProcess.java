package pl.poznan.put.connect.airbyte.pods;

import pl.poznan.put.connect.airbyte.containers.InitialContainerFactory;
import pl.poznan.put.connect.airbyte.containers.MainContainerFactory;
import pl.poznan.put.connect.airbyte.containers.SocatContainerFactory;
import pl.poznan.put.connect.airbyte.volumes.VolumeFactory;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.PodResource;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static pl.poznan.put.connect.airbyte.pods.PodParameters.*;
import static pl.poznan.put.connect.airbyte.pods.PodUtils.isPodTerminated;
import static pl.poznan.put.connect.airbyte.pods.PodUtils.refreshedPod;
import static org.slf4j.LoggerFactory.getLogger;

public class PodProcess extends Process{

    private static final Logger LOGGER = getLogger(PodProcess.class);

    KubernetesClient client;

    PodListener podListener;
    Pod pod;

    public PodProcess(String currentPodName, KubernetesClient client, String podNamespace, String imageName, String runCommand) throws IOException {
        this(currentPodName, client, podNamespace, imageName, runCommand, Optional.empty(), Optional.empty());
    }

    public PodProcess(String currentPodName, KubernetesClient client, String podNamespace, String imageName, String runCommand, Optional<String> configFileContent) throws IOException {
        this(currentPodName, client, podNamespace, imageName, runCommand, configFileContent, Optional.empty());
    }

    public PodProcess(String currentPodName, KubernetesClient client, String podNamespace, String imageName, String runCommand, Optional<String> configFileContent, Optional<String> catalogFileContent) throws IOException {
        this.client = client;

        PodResource podResource = client.pods().inNamespace(podNamespace).withName(currentPodName);
        Pod existingPod = podResource.get();
        if (existingPod == null) {
            LOGGER.error("Cannot find pod with the given namespace:{} and name:{}", podNamespace, currentPodName);
        }

        assert existingPod != null;
        String currentPodIP = existingPod.getStatus().getPodIP();


        LOGGER.info("Defining volumes...");
        List<Volume> volumes = List.of(
                VolumeFactory.createVolume(PIPE_VOLUME_NAME),
                VolumeFactory.createVolume(CONFIG_VOLUME_NAME)
        );

        LOGGER.info("Defining volume mounts...");
        List<VolumeMount> volumeMounts = List.of(
                VolumeFactory.createVolumeMount(PIPE_VOLUME_NAME, PIPE_VOLUME_DIR),
                VolumeFactory.createVolumeMount(CONFIG_VOLUME_NAME, CONFIG_VOLUME_DIR)
        );

        LOGGER.info("Defining containers...");
        Container initContainer = InitialContainerFactory.create(volumeMounts, configFileContent, catalogFileContent);

        List<Container> containers = List.of(
                SocatContainerFactory.create(
                        "relay-stdout",
                        SocatContainerFactory.getOutStreamCommand(STDOUT_PIPE_FILE, currentPodIP, CONTAINER_STDOUT_PORT),
                        volumeMounts
                ),
                SocatContainerFactory.create(
                        "relay-stderr",
                        SocatContainerFactory.getOutStreamCommand(STDERR_PIPE_FILE, currentPodIP, CONTAINER_STDERR_PORT),
                        volumeMounts
                ),
                MainContainerFactory.create(imageName, volumeMounts, runCommand, List.of(CONTAINER_STDOUT_PORT, CONTAINER_STDERR_PORT))
        );
        LOGGER.info("Defining pod...");
        Pod podDefinition = PodFactory.create(
                POD_NAME,
                initContainer,
                containers,
                volumes
        );

        LOGGER.info("Running pod...");
        pod = client.pods().inNamespace(podNamespace).resource(podDefinition).create();

        PodUtils.waitForPodToInit(client, pod);
        LOGGER.info("Pod initialized...");

        LOGGER.info("Setting up sockets...");
        this.podListener = new PodListener(CONTAINER_STDOUT_PORT, CONTAINER_STDERR_PORT);

        PodUtils.waitForPodToReadyOrTerminated(client, pod);
    }

    @Override
    public OutputStream getOutputStream() {
        return OutputStream.nullOutputStream();
    }

    @Override
    public InputStream getInputStream() {
        return podListener.getStdOut();
    }

    @Override
    public InputStream getErrorStream() {
        return podListener.getStdErr();
    }

    @Override
    public int waitFor() {
        final Pod refreshedPod = refreshedPod(client, pod);
        client.resource(refreshedPod).waitUntilCondition(PodUtils::isPodTerminated, 10, TimeUnit.DAYS);
        return exitValue();
    }

    @Override
    public int exitValue() {
        if (isPodTerminated(refreshedPod(client, pod))) {
            close();
            return 0;
        }
        throw new IllegalThreadStateException("Pod process has not exited yet");
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroying kube process: {}", pod.getMetadata().getName());
        client.resource(pod).withPropagationPolicy(DeletionPropagation.FOREGROUND).delete();
        close();
        LOGGER.info("Destroyed kube process: {}", pod.getMetadata().getName());
    }

    private void close() {
        LOGGER.debug("Closing process");
        try {
            podListener.getStdOut().close();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        try {
            podListener.getStdErr().close();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        podListener.closeSockets();
        LOGGER.debug("Closed {}", pod.getMetadata().getName());
    }
}
