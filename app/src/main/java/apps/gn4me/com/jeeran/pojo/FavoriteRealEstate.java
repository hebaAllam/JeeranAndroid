package apps.gn4me.com.jeeran.pojo;

/**
 * Created by ESCA on 6/7/2016.
 */
public class FavoriteRealEstate {

    int favoriteRealEstateId;
    int userId;
    int realEstateAdId;
    RealEstate myRealEstate;

    public FavoriteRealEstate(int favoriteRealEstateId, int userId, int realEstateAdId, RealEstate myRealEstate) {
        this.favoriteRealEstateId = favoriteRealEstateId;
        this.userId = userId;
        this.realEstateAdId = realEstateAdId;
        this.myRealEstate = myRealEstate;
    }

    public FavoriteRealEstate() {
    }

    public int getFavoriteRealEstateId() {
        return favoriteRealEstateId;
    }

    public void setFavoriteRealEstateId(int favoriteRealEstateId) {
        this.favoriteRealEstateId = favoriteRealEstateId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRealEstateAdId() {
        return realEstateAdId;
    }

    public void setRealEstateAdId(int realEstateAdId) {
        this.realEstateAdId = realEstateAdId;
    }

    public RealEstate getMyRealEstate() {
        return myRealEstate;
    }

    public void setMyRealEstate(RealEstate myRealEstate) {
        this.myRealEstate = myRealEstate;
    }
}
