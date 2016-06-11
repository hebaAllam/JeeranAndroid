package apps.gn4me.com.jeeran.pojo;

/**
 * Created by ESCA on 6/11/2016.
 */
public class RealEstateImages {
    private int real_estate_ad_image_id;
    private String image;
    private int is_primary;
    private int real_estate_ad_id;
    private String originalimg;
    private String thumb;

    public RealEstateImages() {
    }

    public RealEstateImages(int real_estate_ad_image_id, String image, int is_primary, int real_estate_ad_id, String originalimg, String thumb) {
        this.real_estate_ad_image_id = real_estate_ad_image_id;
        this.image = image;
        this.is_primary = is_primary;
        this.real_estate_ad_id = real_estate_ad_id;
        this.originalimg = originalimg;
        this.thumb = thumb;
    }

    public int getReal_estate_ad_image_id() {
        return real_estate_ad_image_id;
    }

    public void setReal_estate_ad_image_id(int real_estate_ad_image_id) {
        this.real_estate_ad_image_id = real_estate_ad_image_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIs_primary() {
        return is_primary;
    }

    public void setIs_primary(int is_primary) {
        this.is_primary = is_primary;
    }

    public int getReal_estate_ad_id() {
        return real_estate_ad_id;
    }

    public void setReal_estate_ad_id(int real_estate_ad_id) {
        this.real_estate_ad_id = real_estate_ad_id;
    }

    public String getOriginalimg() {
        return originalimg;
    }

    public void setOriginalimg(String originalimg) {
        this.originalimg = originalimg;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
