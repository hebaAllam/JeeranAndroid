package apps.gn4me.com.jeeran.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.HomeActivity;

/**
 * Created by menna on 6/8/2016.
 */
public class TokenValidation extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //String android_type = android.os.Build.DEVICE ;


        Log.i("Refresh" , "Token");

        final SharedPreferences settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1

        String email , password , facebookID , img;

        email = settings.getString("email", null);
        password = settings.getString("password", null);
        facebookID = settings.getString("fb_id",null);
        img = settings.getString("profile",null);


        if ( password != null) {
            Ion.with(context)
                    .load(BaseActivity.BASE_URL + "/user/login")
                    .noCache()
                    .setBodyParameter("device_type", "0")
                    .setBodyParameter("email", email) //"testhsmsss@test.com"
                    .setBodyParameter("password", password) //"123456789"
                    .setBodyParameter("device_token", android_id) //"bbbbbbdnssbbsxbxb"
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            Log.i("Done ::: bc success", result.toString());

                            Boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                            if (success) {
                                SharedPreferences.Editor editor;
                                editor = settings.edit();
                                editor.putString("token", "Bearer " + result.getAsJsonPrimitive("token").getAsString());
                                editor.commit();

                            }
                        }
                    });

        }else {

            Ion.with(context)
                    .load(BaseActivity.BASE_URL + "/user/loginfb")
                    .noCache()
                    .setBodyParameter("device_type", "0") //android => 0
                    .setBodyParameter("email", email)
                    .setBodyParameter("fb_id", facebookID)
                    .setBodyParameter("image", img )
                    .setBodyParameter("device_token", android_id)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            Boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                            if ( success ) {
                                Log.i("Done ::: success", result.toString());

                                SharedPreferences settings;
                                SharedPreferences.Editor editor;
                                settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                                editor = settings.edit();

                                editor.putString("token", "Bearer " + result.getAsJsonPrimitive("token").getAsString());
                                editor.commit();
                            }
                        }
                    });

        }
    }
}
