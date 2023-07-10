package pl.poznan.put.connect.airbyte;

import com.ibm.wdp.connect.common.sdk.api.models.ConnectionActionResponse;
import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetDescriptor;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.slf4j.Logger;
import pl.poznan.put.connect.airbyte.pods.PodProcess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class AirbyteProcessRunner {
    private static final Logger LOGGER = getLogger(AirbyteProcessRunner.class);
    private static final String POD_NAME = System.getenv("POD_NAME");
    private static final String POD_NAMESPACE = System.getenv("POD_NAMESPACE");
    private static final KubernetesClient KUBERNETES_CLIENT = new KubernetesClientBuilder().build();

    static ConnectionActionResponse spec(String imageName) {
        ConnectionActionResponse response = new ConnectionActionResponse();
        try {
            Process podProcess = new PodProcess(
                    POD_NAME,
                    KUBERNETES_CLIENT,
                    POD_NAMESPACE,
                    imageName,
                    "spec");

            BufferedReader out = new BufferedReader(new InputStreamReader(new SequenceInputStream(podProcess.getInputStream(), podProcess.getErrorStream()), StandardCharsets.UTF_8));
            LOGGER.info("Reading spec pod output...");

            HashMap<String, Object> responseOut = AirbyteStreamProcessor.processCustomAction(out);

            response.putAll(responseOut);

            podProcess.destroy();
        } catch (IOException e) {
            LOGGER.info("An exception has occurred for the spec action %", e);
            response.put("error", "An exception has occurred for the spec action");
        }
        return response;
    }

    static ConnectionActionResponse check(String imageName, String config) {
        ConnectionActionResponse response = new ConnectionActionResponse();
        try {
            Process podProcess = new PodProcess(
                    POD_NAME,
                    KUBERNETES_CLIENT,
                    POD_NAMESPACE,
                    imageName,
                    "check --config /config/config.json",
                    Optional.of(config));

            BufferedReader out = new BufferedReader(new InputStreamReader(new SequenceInputStream(podProcess.getInputStream(), podProcess.getErrorStream()), StandardCharsets.UTF_8));
            LOGGER.info("Reading configuration pod output...");

            HashMap<String, Object> responseOut = AirbyteStreamProcessor.processCustomAction(out);

            response.putAll(responseOut);

            podProcess.destroy();
        } catch (IOException e) {
            LOGGER.info("An exception has occurred for the discover action %", e);
            response.put("error", "An exception has occurred for the discover action");
        }
        return response;
    }

    public static List<CustomFlightAssetDescriptor> discover(String imageName, String config) {
        List<CustomFlightAssetDescriptor> response = new ArrayList<>();
        try {
            Process podProcess = new PodProcess(
                    POD_NAME,
                    KUBERNETES_CLIENT,
                    POD_NAMESPACE,
                    imageName,
                    "discover --config /config/config.json",
                    Optional.of(config));

            BufferedReader out = new BufferedReader(new InputStreamReader(new SequenceInputStream(podProcess.getInputStream(), podProcess.getErrorStream()), StandardCharsets.UTF_8));
            LOGGER.info("Reading discover pod output...");

            response.addAll(AirbyteStreamProcessor.processDiscoverAction(out));

            podProcess.destroy();
        } catch (IOException e) {
            LOGGER.info("An exception has occurred for the discover action %", e);
        }
        return response;
    }

    public static Process read(String imageName, String config, String catalog) {
        try {
            Process podProcess = new PodProcess(
                    POD_NAME,
                    KUBERNETES_CLIENT,
                    POD_NAMESPACE,
                    imageName,
                    "read --config /config/config.json --catalog /config/catalog.json",
                    Optional.of(config),
                    Optional.of(catalog));

            LOGGER.info("Reading discover pod output...");
            return podProcess;
        } catch (IOException e) {
            LOGGER.info("An exception has occurred for the discover action %", e);
        }
        return null;
    }
}
