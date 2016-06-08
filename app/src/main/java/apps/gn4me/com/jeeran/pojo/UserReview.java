package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 5/30/2016.
 */
public class UserReview {
    private int serviceId;
    private User user;
    private int userImage;
    private String username;

    public String getUserName() {
        return username;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    private int numberOfRates;
    private String reviewDate;
    private String userReview;

    public UserReview(int restaurantId, int numberOfRates, String reviewDate, String userReview) {
        this. serviceId = restaurantId;
        this.numberOfRates = numberOfRates;
        this.reviewDate = reviewDate;
        this.userReview = userReview;
    }

    public UserReview(int serviceId, int userImage, String username, int numberOfRates, String reviewDate, String userReview) {
        this.serviceId = serviceId;
        this.userImage = userImage;
        this.username = username;
        this.numberOfRates = numberOfRates;
        this.reviewDate = reviewDate;
        this.userReview = userReview;
    }

    public int getRestaurantId() {
        return  serviceId;
    }


    public int getNumberOfRates() {
        return numberOfRates;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getUserReview() {
        return userReview;
    }

    public void setRestaurantId(int restaurantId) {
        this. serviceId = restaurantId;
    }

    public void setNumberOfRates(int numberOfRates) {
        this.numberOfRates = numberOfRates;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setUserReview(String userReview) {
        this.userReview = userReview;
    }
}
