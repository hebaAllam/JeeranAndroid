package apps.gn4me.com.jeeran.pojo;

public class DiscussionPostData {
    private int id , commentsNum ;
    private String  details , image , timeStamp, title , category ;

    private User user ;

    public DiscussionPostData() {
        user = new User();
    }

    public DiscussionPostData(int id , int userId , String name, String image, String details, String profilePic, String timeStamp, String title) {
        user = new User(userId , name , profilePic);
        this.id = id;
        this.image = image;
        this.details = details;
        this.timeStamp = timeStamp;
        this.title = title;
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user ;
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