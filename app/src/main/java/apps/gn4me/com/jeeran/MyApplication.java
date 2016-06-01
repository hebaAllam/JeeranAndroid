package apps.gn4me.com.jeeran;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by mraouf on 31/05/2016.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("DroidKufi-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

    }
}
