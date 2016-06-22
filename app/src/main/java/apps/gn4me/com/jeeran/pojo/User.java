package apps.gn4me.com.jeeran.pojo;

import java.io.Serializable;

/**
 * Created by ESCA on 5/7/2016.
 */

//class that holds component of user data
public class User implements Serializable{

    private Integer id ;
    private String userName;
    private String password;
    private String emailAddress;
    private String image;
    private String lname;
    private String fname;
    private String mobile;
    private String dateOfBirth;

    public User() {
    }

    public User( Integer id , String name , String image ) {
        this.userName = name;
        this.image = image;
        this.id = id ;
    }

    public User(String userName, String password, String emailAddress, String image) {
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.image = image;
    }


    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
