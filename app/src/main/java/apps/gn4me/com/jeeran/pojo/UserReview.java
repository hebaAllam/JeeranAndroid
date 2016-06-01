package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 5/30/2016.
 */
public class UserReview {
    private int restaurantId;
    private int userImage;
    private String userName;
    private int numberOfRates;
    private String reviewDate;
    private String userReview;

    public UserReview(int restaurantId, int userImage, String userName, int numberOfRates, String reviewDate, String userReview) {
        this.restaurantId = restaurantId;
        this.userImage = userImage;
        this.userName = userName;
        this.numberOfRates = numberOfRates;
        this.reviewDate = reviewDate;
        this.userReview = userReview;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public int getUserImage() {
        return userImage;
    }

    public String getUserName() {
        return userName;
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
        this.restaurantId = restaurantId;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
