package apps.gn4me.com.jeeran.pojo;

public class DiscussionPostData {
    private Integer id , commentsNum , favoriteId;
    private String  details , image , timeStamp, title , category ;

    int isOwner = 0 , isFav = 0 ;
    private User user ;
    private Title topic ;

    public DiscussionPostData() {
        user = new User();
        topic = new Title();
    }
    public DiscussionPostData(int id , int userId , String name, String image, String details, String profilePic, String timeStamp, String title) {
        user = new User(userId , name , profilePic);
        this.id = id;
        this.image = image;
        this.details = details;
        this.timeStamp = timeStamp;
        this.title = title;
        topic = new Title();
    }

    public int getIsOwner() {
        return isOwner;
    }
    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public int getIsFav() {
        return isFav;
    }
    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }
    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public Integer getFavoriteId() {
        return favoriteId;
    }
    public void setFavoriteId(Integer favoriteId) {
        this.favoriteId = favoriteId;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user ;
    }

    public Title getTopic() {
        return topic;
    }
    public void setTopic(Title topic) {
        this.topic = topic ;
    }


    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}