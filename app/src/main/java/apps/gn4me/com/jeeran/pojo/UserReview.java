package apps.gn4me.com.jeeran.pojo;

import java.util.Date;

/**
 * Created by acer on 5/30/2016.
 */
public class UserReview {

    private int reviewID;
    private int serviceId;
    private String reviewContent;
    private String reviewDate;
    private  String reviewUpdateDate;
    private int numberOfRates;
    private User user;
    private int isHide;

    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setReviewUpdateDate(String reviewUpdateDate) {
        this.reviewUpdateDate = reviewUpdateDate;
    }

    public void setNumberOfRates(int numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIsHide(int isHide) {
        this.isHide = isHide;
    }

    public int getReviewID() {
        return reviewID;
    }

    public int getServiceId() {
        return serviceId;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getReviewUpdateDate() {
        return reviewUpdateDate;
    }

    public int getNumberOfRates() {
        return numberOfRates;
    }

    public User getUser() {
        return user;
    }

    public int getIsHide() {
        return isHide;
    }
}
