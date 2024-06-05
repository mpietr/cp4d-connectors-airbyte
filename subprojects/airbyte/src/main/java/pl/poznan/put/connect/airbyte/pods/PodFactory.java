package pl.poznan.put.connect.airbyte.pods;

import io.fabric8.kubernetes.api.model.Container;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.api.model.Volume;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.List;

public class PodFactory {
    /**
     * Creates a new Pod with the specified name, init container, containers, and volumes.
     *
     * @param name           the name of the Pod
     * @param initContainer  the init container for the Pod
     * @param containers     the list of containers for the Pod
     * @param volumes        the list of volumes for the Pod
     * @return the created Pod
     */
    public static Pod create(String name, Container initContainer, List<Container> containers, List<Volume> volumes) {
        return new PodBuilder()
                .withApiVersion("v1")
                .withNewMetadata()
                .withName(name + "-" + RandomStringUtils.random(15, true, true).toLowerCase())
                .endMetadata()
                .withNewSpec()
                .withRestartPolicy("Never")
                .withInitContainers(initContainer)
                .withContainers(containers)
                .withVolumes(volumes)
                .endSpec()
                .build();

    }
}
