package apps.gn4me.com.jeeran.pojo;

import java.io.Serializable;
import java.util.ArrayList;

import apps.gn4me.com.jeeran.activity.Reviews;

/**
 * Created by acer on 5/28/2016.
 */
public class Service {
    private int serviceId;
    private int logoImg;
    private String logo;
    private String Name;
    private  String address;
    private  String openingHours;
    private long lat;
    private long lang;
    private long rates;
    private ArrayList<String> images;
    private ArrayList<Reviews> reviews;
    private String discription;
    private String phone1;
    private String phone2;
    private String phone3;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public void setRates(long rates) {
        this.rates = rates;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public void setReviews(ArrayList<Reviews> reviews) {
        this.reviews = reviews;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
    }

    public String getAddress() {
        return address;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public long getRates() {
        return rates;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public ArrayList<Reviews> getReviews() {
        return reviews;
    }

    public String getDiscription() {
        return discription;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public String getPhone3() {
        return phone3;
    }

    public Service() {
    }

    public Service(int serviceId, int logoImg, String name, long lat, long lang) {
        this.serviceId = serviceId;
        this.logoImg = logoImg;
        Name = name;
        this.lat = lat;
        this.lang = lang;

    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
