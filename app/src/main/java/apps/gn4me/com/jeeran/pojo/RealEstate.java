package apps.gn4me.com.jeeran.pojo;

/**
 * Created by ESCA on 5/27/2016.
 */
public class RealEstate {

    String title;
    int id;
    String description;
    String Location;
    String address;
    String contactPerson;
    String phone;
    String Email;


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
