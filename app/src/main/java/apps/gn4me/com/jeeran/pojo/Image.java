package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 6/5/2016.
 */
public class Image {
    private int serviceImage;
    private String name;

    public String getName() {
        return name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;
    public void setServiceImage(int serviceImage) {
        this.serviceImage = serviceImage;
    }

    public int getServiceImage() {

        return serviceImage;
    }

    public Image(int serviceImage) {

        this.serviceImage = serviceImage;
    }
}
