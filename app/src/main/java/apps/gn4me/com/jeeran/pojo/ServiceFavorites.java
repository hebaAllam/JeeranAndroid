package apps.gn4me.com.jeeran.pojo;

import java.util.List;

/**
 * Created by ESCA on 6/22/2016.
 */
public class ServiceFavorites {
    int favoriteServiceId;
    int serviceId;
    int userId;
    Service myServices;

    public ServiceFavorites() {
    }

    public ServiceFavorites(int favoriteServiceId, int serviceId, int userId, Service myServices) {
        this.favoriteServiceId = favoriteServiceId;
        this.serviceId = serviceId;
        this.userId = userId;
        this.myServices = myServices;
    }

    public int getFavoriteServiceId() {
        return favoriteServiceId;
    }

    public void setFavoriteServiceId(int favoriteServiceId) {
        this.favoriteServiceId = favoriteServiceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Service getMyServices() {
        return myServices;
    }

    public void setMyServices(Service myServices) {
        this.myServices = myServices;
    }
}
