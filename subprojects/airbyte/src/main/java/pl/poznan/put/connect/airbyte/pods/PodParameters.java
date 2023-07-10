package pl.poznan.put.connect.airbyte.pods;

public class PodParameters {

    public static final String POD_NAME = "pod-worker";
    public static final String CONFIG_VOLUME_NAME = "config";
    public static final String CONFIG_VOLUME_DIR = "/config";
    public static final String CONFIG_FILE = "config.json";
    public static final String CATALOG_FILE = "catalog.json";

    public static final String PIPE_VOLUME_NAME = "pipes";
    public static final String PIPE_VOLUME_DIR = "/pipes";
    public static final String STDOUT_PIPE_FILE = String.format("%s/stdout", PIPE_VOLUME_DIR);
    public static final String STDERR_PIPE_FILE = String.format("%s/stderr", PIPE_VOLUME_DIR);

    public static final String WORKING_DIR = CONFIG_VOLUME_DIR;

    public static final String MAIN_CONTAINER_NAME = "main";
    public static final String MAIN_CONTAINER_COMMAND_PATH = "main.sh";

    public static final Integer CONTAINER_STDOUT_PORT = 9001;
    public static final Integer CONTAINER_STDERR_PORT = 9002;
}
