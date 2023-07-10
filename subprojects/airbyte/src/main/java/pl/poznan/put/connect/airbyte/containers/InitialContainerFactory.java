package pl.poznan.put.connect.airbyte.containers;

import pl.poznan.put.connect.airbyte.pods.PodParameters;
import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.ContainerBuilder;
import io.fabric8.kubernetes.api.model.VolumeMount;

import java.util.List;
import java.util.Optional;

public class InitialContainerFactory {
    public static final String INIT_CONTAINER_NAME = "init";
    public static final String INIT_IMAGE_NAME = "busybox:latest";

    public static Container create(List<VolumeMount> volumeMounts, Optional<String> configFileContent, Optional<String> catalogFileContent) {
        String command = String.format("mkfifo %s && mkfifo %s ",
                PodParameters.STDOUT_PIPE_FILE,
                PodParameters.STDERR_PIPE_FILE);

        if(configFileContent.isPresent()){
            command += addFileContent(configFileContent.get(), PodParameters.CONFIG_FILE);
        }

        if(catalogFileContent.isPresent()){
            command += addFileContent(catalogFileContent.get(), PodParameters.CATALOG_FILE);
        }

        return new ContainerBuilder()
                .withName(INIT_CONTAINER_NAME)
                .withImage(INIT_IMAGE_NAME)
                .withWorkingDir(PodParameters.WORKING_DIR)
                .withCommand("sh", "-c", command)
                .withVolumeMounts(volumeMounts)
                .build();
    }

    private static String addFileContent(String fileContent, String fileName){
        return String.format("&& echo '%s' > %s/%s ",
                fileContent,
                PodParameters.WORKING_DIR,
                fileName);
    }
}
