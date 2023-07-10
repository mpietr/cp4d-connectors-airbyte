package pl.poznan.put.connect.airbyte.volumes;

import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.kubernetes.api.model.VolumeBuilder;
import io.fabric8.kubernetes.api.model.VolumeMount;
import io.fabric8.kubernetes.api.model.VolumeMountBuilder;

public class VolumeFactory {
    public static Volume createVolume(String volumeName) {
        return new VolumeBuilder()
                .withName(volumeName)
                .withNewEmptyDir()
                .endEmptyDir()
                .build();
    }

    public static VolumeMount createVolumeMount(String volumeName, String volumeMountPoint) {
        return new VolumeMountBuilder()
                .withName(volumeName)
                .withMountPath(volumeMountPoint)
                .build();
    }
}
