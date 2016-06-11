package apps.gn4me.com.jeeran.pojo;

/**
 * Created by menna on 5/30/2016.
 */
public class DiscussionCommentData {

    private String timeStamp;
    private String comment ;
    private int id ;

    private int isOwner;
    private User user ;


    public DiscussionCommentData(){
        user = new User();
    }
    public DiscussionCommentData(int id , int userid , String name , String userImage, String comment , String timeStamp ) {
        user = new User(userid , name , userImage);
        this.id = id;
        this.comment = comment;
        this.timeStamp = timeStamp;
    }


    public int getIsOwner() {
        return isOwner;
    }
    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user ;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
