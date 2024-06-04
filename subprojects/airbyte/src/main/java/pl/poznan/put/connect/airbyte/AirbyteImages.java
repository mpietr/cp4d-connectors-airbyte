package pl.poznan.put.connect.airbyte;

public class AirbyteImages {
    public static final String POSTGRES_IMAGE = "airbyte/source-postgres";

    /**
        * Returns the image name for the given image.
        *
        * @param image the name of the image
        * @return the corresponding image name
        * @throws RuntimeException if the image name is illegal
        */
    public static String getImageName(String image) {
        switch (image) {
            case "postgres":
                return POSTGRES_IMAGE;
            default:
                throw new RuntimeException("Illegal image name");
        }
    }
}
