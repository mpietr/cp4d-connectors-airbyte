package pl.poznan.put.connect.airbyte.pods;

import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.readiness.Readiness;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static pl.poznan.put.connect.airbyte.pods.PodParameters.MAIN_CONTAINER_NAME;

public class PodUtils {
    public static void waitForPodToInit(KubernetesClient client, Pod pod) {
        client.pods().inNamespace(pod.getMetadata().getNamespace()).withName(pod.getMetadata().getName())
                .waitUntilCondition(p ->
                        p.getStatus().getInitContainerStatuses().size() != 0,
                        5, TimeUnit.MINUTES);
        client.pods().inNamespace(pod.getMetadata().getNamespace()).withName(pod.getMetadata().getName())
                .waitUntilCondition(p ->
                        p.getStatus().getInitContainerStatuses().get(0).getState().getTerminated() != null,
                        5, TimeUnit.MINUTES);

    }

    public static void waitForPodToReadyOrTerminated(KubernetesClient client, Pod pod) {
        client.pods().inNamespace(pod.getMetadata().getNamespace()).withName(pod.getMetadata().getName())
                .waitUntilCondition(p -> isPodReady(p) || isPodTerminated(p), 10, TimeUnit.DAYS);
    }

    public static boolean isPodTerminated(Pod pod) {
        if (Objects.nonNull(pod)) {
            List<ContainerStatus> statuses = pod.getStatus().getContainerStatuses().stream()
                    .filter(containerStatus -> containerStatus.getName().equals(MAIN_CONTAINER_NAME))
                    .collect(Collectors.toList());
            return statuses.size() > 0 && statuses.get(0).getState() != null && statuses.get(0).getState()
                    .getTerminated() != null;
        }
        return false;
    }

    private static boolean isPodReady(Pod pod) {
        return Objects.nonNull(pod) && Readiness.getInstance().isReady(pod);
    }

    public static Pod refreshedPod(KubernetesClient client, Pod pod) {
        return client.pods().inNamespace(pod.getMetadata().getNamespace())
                .withName(pod.getMetadata().getName()).get();
    }
}
