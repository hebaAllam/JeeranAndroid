package apps.gn4me.com.jeeran.pojo;

/**
 * Created by acer on 5/30/2016.
 package apps.gn4me.com.jeeran.pojo;

 /**
 * Created by acer on 5/29/2016.
 */
public class ServiceDetailsPojo {


    private int resLogo;
    private String resName;
    private double rates;

    public ServiceDetailsPojo() {

    }

    public ServiceDetailsPojo(int resLogo, String resName, double rates) {
        this.resLogo = resLogo;
        this.resName = resName;
        this.rates = rates;
    }

    public int getResLogo() {
        return resLogo;
    }

    public String getResName() {
        return resName;
    }

    public double getRates() {
        return rates;
    }

    public void setResLogo(int resLogo) {
        this.resLogo = resLogo;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setRates(double rates) {
        this.rates = rates;
    }
}
