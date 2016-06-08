package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 5/27/2016.
 */
public class ServicesCategory {
    private int ServiceCatId;
    private int serviceCatIcon;
    private String serviceCatLogo;
    private String serviceCatName;
    private String serviceCatNumber;

    public ServicesCategory() {
    }

    public void setServiceCatLogo(String serviceCatLogo) {
        this.serviceCatLogo = serviceCatLogo;
    }

    public String getServiceCatLogo() {

        return serviceCatLogo;
    }

    public ServicesCategory(int serviceCatId, int serviceCatIcon, String serviceCatName, String serviceCatNumber) {
        ServiceCatId = serviceCatId;
        this.serviceCatIcon = serviceCatIcon;
        this.serviceCatName = serviceCatName;
        this.serviceCatNumber = serviceCatNumber;
    }

    public void setServiceCatId(int serviceCatId) {
        ServiceCatId = serviceCatId;
    }

    public int getServiceCatId() {
        return ServiceCatId;
    }

    public int getServiceCatIcon() {
        return serviceCatIcon;
    }

    public String getServiceCatName() {
        return serviceCatName;
    }

    public String getServiceSubCatNumber() {
        return serviceCatNumber;
    }

    public void setServiceCatName(String serviceCatName) {
        this.serviceCatName = serviceCatName;
    }

    public void setServiceSubCatNumber(String serviceCatNumber) {
        this.serviceCatNumber = serviceCatNumber;
    }

    public void setServiceCatIcon(int serviceCatIcon) {
        this.serviceCatIcon = serviceCatIcon;
    }
}
