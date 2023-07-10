package pl.poznan.put.connect.airbyte;

public class AirbyteImages {
    public static final String POSTGRES_IMAGE = "airbyte/source-postgres";

    public static String getImageName(String image) {
        switch (image) {
            case "postgres":
                return POSTGRES_IMAGE;
            default:
                throw new RuntimeException("Illegal image name");
        }
    }
}
