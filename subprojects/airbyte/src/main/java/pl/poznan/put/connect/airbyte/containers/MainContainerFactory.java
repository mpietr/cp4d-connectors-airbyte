package pl.poznan.put.connect.airbyte.containers;

import com.google.common.io.Resources;
import io.fabric8.kubernetes.api.model.*;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.poznan.put.connect.airbyte.pods.PodParameters.*;

public class MainContainerFactory {
    public static Container create(String imageName, List<VolumeMount> volumeMounts, String args, List<Integer> ports) throws IOException {
        URL resource = Resources.getResource(MAIN_CONTAINER_COMMAND_PATH);
        String command = Resources.toString(resource, StandardCharsets.UTF_8)
                .replaceAll("ARGS", args)
                .replaceAll("STDOUT_PIPE_FILE", STDOUT_PIPE_FILE)
                .replaceAll("STDERR_PIPE_FILE", STDERR_PIPE_FILE);

        List<ContainerPort> containerPorts = ports.stream().map(port -> new ContainerPortBuilder()
                .withContainerPort(port).build()).collect(Collectors.toList());

        return new ContainerBuilder()
                .withName(MAIN_CONTAINER_NAME)
                .withPorts(containerPorts)
                .withImage(imageName)
                .withCommand("sh", "-c", command)
                .withWorkingDir(WORKING_DIR)
                .withVolumeMounts(volumeMounts)
                .build();
    }
}
