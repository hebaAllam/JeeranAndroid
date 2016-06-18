package apps.gn4me.com.jeeran.pojo;

import java.util.Date;

/**
 * Created by ESCA on 5/27/2016.
 */
public class RealEstate {

    public static RealEstate myRealEstateObj;

    String title;
    int id;
    String description;
    String Location;
    int type;
    int numOfRooms;
    int numOfBathreeoms;
    int price;
    String area;
    double longitude,latitude;
    int language;
    boolean isHide;
    boolean isOwner;
    boolean isFav;
    String creationDate;
    String updateDate;
    boolean onHome;

    String img;
    boolean isFeature;
    int amenitiesId;
    String address;
    String contactPerson;
    String phone;
    String Email;
    RealEstateImages imgs;

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public RealEstateImages getImgs() {
        return imgs;
    }

    public void setImgs(RealEstateImages imgs) {
        this.imgs = imgs;
    }

    public RealEstate() {
    }

    public RealEstate(String title, String description, String location, String address, String contactPerson, String phone, String email) {
        this.title = title;
        this.description = description;
        Location = location;
        this.address = address;
        this.contactPerson = contactPerson;
        this.phone = phone;
        Email = email;
    }

    public RealEstate(String title, int id, String description, String location, int type, int numOfRooms, int numOfBathreeoms, int price, String area, double longitude, double latitude, int language, boolean isHide, String creationDate, String updateDate, boolean onHome, String img, boolean isFeature, int amenitiesId, String address, String contactPerson, String phone, String email) {
        this.title = title;
        this.id = id;
        this.description = description;
        Location = location;
        this.type = type;
        this.numOfRooms = numOfRooms;
        this.numOfBathreeoms = numOfBathreeoms;
        this.price = price;
        this.area = area;
        this.longitude = longitude;
        this.latitude = latitude;
        this.language = language;
        this.isHide = isHide;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.onHome = onHome;
        this.img = img;
        this.isFeature = isFeature;
        this.amenitiesId = amenitiesId;
        this.address = address;
        this.contactPerson = contactPerson;
        this.phone = phone;
        Email = email;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumOfRooms() {
        return numOfRooms;
    }

    public void setNumOfRooms(int numOfRooms) {
        this.numOfRooms = numOfRooms;
    }

    public int getNumOfBathreeoms() {
        return numOfBathreeoms;
    }

    public void setNumOfBathreeoms(int numOfBathreeoms) {
        this.numOfBathreeoms = numOfBathreeoms;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        isHide = hide;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isOnHome() {
        return onHome;
    }

    public void setOnHome(boolean onHome) {
        this.onHome = onHome;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isFeature() {
        return isFeature;
    }

    public void setFeature(boolean feature) {
        isFeature = feature;
    }

    public int getAmenitiesId() {
        return amenitiesId;
    }

    public void setAmenitiesId(int amenitiesId) {
        this.amenitiesId = amenitiesId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
