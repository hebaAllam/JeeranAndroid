package apps.gn4me.com.jeeran.intent_service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.activity.BaseActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ValidateTokenService extends IntentService {

    public ValidateTokenService() {
        super("ValidateTokenService");
    }

    public void getLoginData(JsonObject result){

        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ){
            SharedPreferences settings;
            SharedPreferences.Editor editor;
            settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
            editor = settings.edit();

            editor.putString("token",  "Bearer " + result.getAsJsonPrimitive("token").getAsString());
            editor.commit();
        }
    }

    private void requestJsonObject() {
        final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String  tag_string_req = "string_req";
        final String TAG = "Volley";


        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1

        final String email = settings.getString("email",null);
        final String password = settings.getString("password",null);

        final String facebookID = settings.getString("fb_id",null);
        final String name = settings.getString("name",null);
        final String img = settings.getString("image",null);


        String url = BaseActivity.BASE_URL ;
        if (password != null ) {
            url += "/user/login";
        }else{
            url += "/user/loginfb";
        }

        /*
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        */


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                getLoginData(result);
                refreshToken();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if ( password != null ) {
                    params.put("device_type", "0");
                    params.put("email", email);
                    params.put("password", password);
                    params.put("device_token", android_id);
                }else{
                    params.put("name", name);
                    params.put("fb_id", facebookID);
                    params.put("email", email);
                    params.put("image", img);
                    params.put("device_type", "0");
                    params.put("device_token", android_id);
                }

                return params;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }



    private void refreshToken(){
        final int delay = 30 * 60 * 1000; //milliseconds
        try {
            Thread.sleep(delay);
            Log.i("Service Called","Hereeee");
            requestJsonObject();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        refreshToken();
    }


}
