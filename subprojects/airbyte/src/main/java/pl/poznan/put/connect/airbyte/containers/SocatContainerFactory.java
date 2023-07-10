package pl.poznan.put.connect.airbyte.containers;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.VolumeMount;

import java.util.List;

public class SocatContainerFactory {
    private static final String SOCAT_IMAGE_NAME = "alpine/socat:latest";

    public static Container create(String containerName, String command, List<VolumeMount> volumeMounts){
        return new ContainerBuilder()
                .withName(containerName)
                .withImage(SOCAT_IMAGE_NAME)
                .withCommand("sh", "-c", command)
                .withVolumeMounts(volumeMounts)
                .build();
    }

    public static String getOutStreamCommand(String pipeFile, String host, Integer port) {
        return String.format("cat %s | socat -d - TCP:%s:%s", pipeFile, host, port);
    }
}
