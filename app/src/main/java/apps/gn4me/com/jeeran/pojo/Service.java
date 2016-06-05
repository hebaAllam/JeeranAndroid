package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 5/28/2016.
 */
public class Service {
    private int serviceId;
    private int logoImg;
    private String Name;
    private long lat;
    private long lang;



    public Service(int serviceId, int logoImg, String name, long lat, long lang) {
        this.serviceId = serviceId;
        this.logoImg = logoImg;
        Name = name;
        this.lat = lat;
        this.lang = lang;
    }

    public Service(int logoImg, String name, long lat, long lang) {

        this.logoImg = logoImg;
        Name = name;
        this.lat = lat;
        this.lang = lang;
    }
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getServiceId() {

        return serviceId;
    }
    public int getLogoImg() {
        return logoImg;
    }

    public String getName() {
        return Name;
    }

    public long getLat() {
        return lat;
    }

    public long getLang() {
        return lang;
    }

    public void setLogoImg(int logoImg) {
        this.logoImg = logoImg;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public void setLang(long lang) {
        this.lang = lang;
    }
}
