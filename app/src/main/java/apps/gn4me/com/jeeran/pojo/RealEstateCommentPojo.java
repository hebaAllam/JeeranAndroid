package apps.gn4me.com.jeeran.pojo;

/**
 * Created by ESCA on 6/12/2016.
 */
public class RealEstateCommentPojo {
    private String userComment;
    private String commentDate;
    private int realEstateId;
    private User user;
    private int userImage;
    private String username;
    private int realEstateAdCommentId;
    private int realEstateAdId;
    private int isHide;
    private int isOwner;

    public int getRealEstateAdCommentId() {
        return realEstateAdCommentId;
    }

    public void setRealEstateAdCommentId(int realEstateAdCommentId) {
        this.realEstateAdCommentId = realEstateAdCommentId;
    }

    public int getRealEstateAdId() {
        return realEstateAdId;
    }

    public void setRealEstateAdId(int realEstateAdId) {
        this.realEstateAdId = realEstateAdId;
    }

    public int getIsHide() {
        return isHide;
    }

    public void setIsHide(int isHide) {
        this.isHide = isHide;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public RealEstateCommentPojo() {
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public RealEstateCommentPojo(int realEstateId, User user, int userImage, String username) {
        this.realEstateId = realEstateId;
        this.user = user;
        this.userImage = userImage;
        this.username = username;
    }

    public int getRealEstateId() {
        return realEstateId;
    }
    public RealEstateCommentPojo(int serviceId, int userImage, String username, int numberOfRates, String reviewDate, String userReview) {
        this.realEstateId = serviceId;
        this.userImage = userImage;
        this.username = username;
        this.commentDate = reviewDate;
        this.userComment = userReview;
    }

    public void setRealEstateId(int realEstateId) {
        this.realEstateId = realEstateId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getUserImage() {
        return userImage;
    }

    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
