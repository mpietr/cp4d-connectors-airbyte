package pl.poznan.put.connect.airbyte;

import com.ibm.wdp.connect.common.sdk.api.models.CustomFlightAssetDescriptor;
import com.ibm.wdp.connect.common.sdk.api.models.DiscoveredAssetInteractionProperties;
import com.ibm.wdp.connect.common.sdk.api.models.DiscoveredAssetType;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AirbyteAssetParser {
    private static final Pattern namespacePattern = Pattern.compile("^/[^/]*$", Pattern.CASE_INSENSITIVE);

    private static final Pattern streamPattern = Pattern.compile("^/[^/]*/[^/]*$", Pattern.CASE_INSENSITIVE);

    public static List<CustomFlightAssetDescriptor> parseAssets(String path, List<CustomFlightAssetDescriptor> assets) {
        Matcher namespaceMatcher = namespacePattern.matcher(path);
        Matcher streamMatcher = streamPattern.matcher(path);

        List<String> namespaceAssetList = assets.stream()
                .map(assetDescriptor -> (String) assetDescriptor.getDetails().get("namespace"))
                .distinct()
                .filter(Objects::nonNull).collect(Collectors.toList());

        if (!namespaceAssetList.isEmpty()) {
            return getNamespacedAsset(namespaceMatcher, streamMatcher, assets, path, namespaceAssetList);
        }
        else {
            return getAssetWithoutNamespace(namespaceMatcher, assets, path);
        }
    }
    public static List<CustomFlightAssetDescriptor> getNamespacedAsset(Matcher namespaceMatcher, Matcher streamMatcher,
                                                         List<CustomFlightAssetDescriptor> assets, String path,
                                                         List<String> namespaceAssetList){
        if ("".equals(path)){
            return assets;
        } else if ("/".equals(path)) {
            return namespaceAssetList.stream().map(namespace -> {
                DiscoveredAssetType type = new DiscoveredAssetType().type("schema").dataset(false).datasetContainer(false);
                CustomFlightAssetDescriptor asset = new CustomFlightAssetDescriptor();
                asset.setId(namespace);
                asset.setName(namespace);
                asset.setPath(String.format("/%s", namespace));
                asset.setAssetType(type);
                return asset;
            }).collect(Collectors.toList());
        } else if (namespaceMatcher.matches()) {
            String namespace = path.substring(1);
            return assets.stream()
                    .filter(asset -> namespace.equals(asset.getDetails().get("namespace")))
                    .map(assetDescriptor -> {
                        DiscoveredAssetType type = new DiscoveredAssetType().type("table").dataset(true).datasetContainer(false);
                        CustomFlightAssetDescriptor asset = new CustomFlightAssetDescriptor();
                        asset.setId(assetDescriptor.getName());
                        asset.setName(assetDescriptor.getName());
                        asset.setPath(String.format("/%s/%s", assetDescriptor.getDetails().get("namespace"), assetDescriptor.getName()));
                        asset.setAssetType(type);
                        return asset;
                    })
                    .collect(Collectors.toList());
        } else if (streamMatcher.matches()) {
            String[] pathElements = path.substring(1).split("/");

            return assets.stream()
                    .filter(asset -> pathElements[0].equals(asset.getDetails().get("namespace")))
                    .filter(asset -> pathElements[1].equals(asset.getName()))
                    .map(assetDescriptor -> {
                        String namespace = (String) assetDescriptor.getDetails().get("namespace");
                        DiscoveredAssetType type = new DiscoveredAssetType().type("table").dataset(true).datasetContainer(false);
                        CustomFlightAssetDescriptor asset = new CustomFlightAssetDescriptor();
                        asset.setId(assetDescriptor.getName());
                        asset.setName(assetDescriptor.getName());
                        asset.setPath(String.format("/%s/%s", namespace, assetDescriptor.getName()));
                        asset.setAssetType(type);
                        DiscoveredAssetInteractionProperties properties = new DiscoveredAssetInteractionProperties();
                        properties.put("stream_name", assetDescriptor.getName());
                        properties.put("namespace", namespace);
                        asset.setInteractionProperties(properties);
                        asset.setFields(assetDescriptor.getFields());
                        asset.setDescription(assetDescriptor.getDescription());
                        asset.setDetails(assetDescriptor.getDetails());
                        return asset;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Path is in the incorrect format");
        }
    }
    public static List<CustomFlightAssetDescriptor> getAssetWithoutNamespace(Matcher namespaceMatcher,
                                                                             List<CustomFlightAssetDescriptor> assets, String path){
        if ("".equals(path)){
            return assets;
        } else if ("/".equals(path)) {
            return assets.stream()
                    .map(assetDescriptor -> {
                        DiscoveredAssetType type = new DiscoveredAssetType().type("table").dataset(true).datasetContainer(false);
                        CustomFlightAssetDescriptor asset = new CustomFlightAssetDescriptor();
                        asset.setId(assetDescriptor.getName());
                        asset.setName(assetDescriptor.getName());
                        asset.setPath(String.format("/%s", assetDescriptor.getName()));
                        asset.setAssetType(type);
                        return asset;
                    })
                    .collect(Collectors.toList());
        } else if (namespaceMatcher.matches()) {
            String[] pathElements = path.substring(1).split("/");

            return assets.stream()
                    .filter(asset -> pathElements[0].equals(asset.getName()))
                    .map(assetDescriptor -> {
                        DiscoveredAssetType type = new DiscoveredAssetType().type("table").dataset(true).datasetContainer(false);
                        CustomFlightAssetDescriptor asset = new CustomFlightAssetDescriptor();
                        asset.setId(assetDescriptor.getName());
                        asset.setName(assetDescriptor.getName());
                        asset.setPath(String.format("/%s", assetDescriptor.getName()));
                        asset.setAssetType(type);
                        DiscoveredAssetInteractionProperties properties = new DiscoveredAssetInteractionProperties();
                        properties.put("stream_name", assetDescriptor.getName());
                        asset.setInteractionProperties(properties);
                        asset.setFields(assetDescriptor.getFields());
                        asset.setDescription(assetDescriptor.getDescription());
                        asset.setDetails(assetDescriptor.getDetails());
                        return asset;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Path is in the incorrect format");
        }
    }
}
