package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 5/27/2016.
 */
public class ServicesCategory {
   private int serviceCatIcon;
    private String serviceCatName;
    private String serviceCatNumber;



    public int getServiceCatIcon() {
        return serviceCatIcon;
    }

    public String getServiceCatName() {
        return serviceCatName;
    }

    public String getServiceCatNumber() {
        return serviceCatNumber;
    }

    public void setServiceCatName(String serviceCatName) {
        this.serviceCatName = serviceCatName;
    }

    public void setServiceCatNumber(String serviceCatNumber) {
        this.serviceCatNumber = serviceCatNumber;
    }

    public void setServiceCatIcon(int serviceCatIcon) {
        this.serviceCatIcon = serviceCatIcon;
    }
}
