package apps.gn4me.com.jeeran.pojo;

import java.io.Serializable;

/**
 * Created by ESCA on 5/7/2016.
 */

//class that holds component of user data
public class User implements Serializable{
    private String userName;
    private String password;
    private String emailAddress;
    private String[] image;

    public User() {
    }
    public User(String userName, String password, String emailAddress, String[] image) {
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.image = image;
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

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }
}
