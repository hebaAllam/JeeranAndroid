package apps.gn4me.com.jeeran.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.Title;
import apps.gn4me.com.jeeran.pojo.User;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mraouf on 08/05/2016.
 */
public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static final String PREFS_NAME = "Jeeran";
    public static final String BASE_URL = "http://jeeran.gn4me.com/jeeran_v1";
    public static final Integer DISCUSSION_REPORT = 4 ;
    public static final Integer DISCUSSION_COMMENT_REPORT = 5 ;
    public static final Integer REALESTATE_REPORT = 6 ;
    public static final Integer REALESTATE_COMMENT_REPORT = 7 ;
    public static final Integer SERVICE_PLACE_REPORT = 1 ;
    public static final Integer SERVICE_PLACE_REVIEW_REPORT = 2 ;


    public static final int EXPIRATION_Duration = 30 * 60 * 1000 ;
    public static ArrayList<Title> neighborhoods = new ArrayList<>();
    public static ArrayList<Title> discussionTopics = new ArrayList<>();
    public static ArrayList<Title> reportReasons = new ArrayList<>();

    public static HashMap<String,String> url_maps = new HashMap<String, String>();
    public static int realEstateCount = 0 , servicePlacesCount = 0 ;
    public static Title currentNeighborhood = new Title();
    public static User profile = new User();
    public static int realEstateFeatureImgs;
    public int countInitRequest = 0 ;
    public Activity activeActivity ;

    protected View progress;
    String android_id;
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(((AppCompatActivity)getApplicationContext()).getIntent());
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        hideKeyboard();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.i("IDDDD" , android_id);

//        requestRealEstateSliderImages();
    }



/*
    private void requestRealEstateSliderImages() {
        {
            String  tag_string_req = "string_req";

            final String TAG = "Volley";
            String url = BaseActivity.BASE_URL + "/realstate/imagefeature";

        /*
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        */
/*
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    //pDialog.hide();
                    JsonParser parser = new JsonParser();
                    JsonObject result = parser.parse(response).getAsJsonObject();
                    getRealEstateSliderData(result);
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    //pDialog.hide();
                }
            }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    SharedPreferences settings;
                    String token ;
                    settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                    token = settings.getString("token", null);
                    headers.put("Authorization", token);
                    return headers;
                }

            };

// Adding request to request queue
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(strReq);
        }
    }

    private void getRealEstateSliderData(JsonObject result) {
        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ) {
            JsonArray slider = result.getAsJsonArray("response");

            BaseActivity.realEstateFeatureImgs = slider.size();

            for (int i = 0; i < BaseActivity.realEstateFeatureImgs; i++) {
                BaseActivity.url_maps.put(slider.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString(),
                        slider.get(i).getAsJsonObject().getAsJsonPrimitive("image").getAsString());
            }
        }
    }
*/



    public void requestUpdateNeighborhood() {

        final String TAG = "Volley";

        final Integer neighborhoodId = BaseActivity.currentNeighborhood.getId();
        String url = BaseActivity.BASE_URL + "/user/updateneighborhood?neighborhood_id=" + neighborhoodId.toString()
                    + "&device_token=" + android_id;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                Boolean success = false ;
                if ( result != null ) {
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if(success){
                    Log.i("update neighborhood" , "true");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };
        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);

    }




    private void getHomeSliderData(JsonObject result){
        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ) {
            JsonObject slider = result.getAsJsonObject("response").getAsJsonObject("slider");
            JsonArray realstates = slider.getAsJsonArray("realstate");
            JsonArray servicePlaces = slider.getAsJsonArray("servicePlace");
            BaseActivity.realEstateCount = realstates.size() ;
            BaseActivity.servicePlacesCount = servicePlaces.size();

            for ( int i=0 ; i<realstates.size() ; i++ ){
                BaseActivity.url_maps.put( realstates.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString() ,
                        realstates.get(i).getAsJsonObject().getAsJsonPrimitive("image").getAsString()) ;
            }
            for ( int i=0 ; i<servicePlaces.size() ; i++ ){
                BaseActivity.url_maps.put( servicePlaces.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString() ,
                        servicePlaces.get(i).getAsJsonObject().getAsJsonPrimitive("image").getAsString()) ;
            }
        }
    }

    public void requestHomeSliderImages(){

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/application/onhome";

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
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                getHomeSliderData(result);
                countInitRequest++;
                gotToHomeActivity();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }

    private void getNeighborhoods(JsonObject result){
        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ) {
            JsonArray neighborhoodsJson = result.getAsJsonObject("response").getAsJsonArray("neighborhoods");

            for ( int i=0 ; i<neighborhoodsJson.size() ; i++ ){
                Title neighborhood = new Title();
                neighborhood.setId(neighborhoodsJson.get(i).getAsJsonObject().getAsJsonPrimitive("id").getAsInt());
                neighborhood.setTitleArabic(neighborhoodsJson.get(i).getAsJsonObject().getAsJsonPrimitive("title_ar").getAsString());
                neighborhood.setTitleEnglish(neighborhoodsJson.get(i).getAsJsonObject().getAsJsonPrimitive("title_en").getAsString());
                neighborhoods.add(neighborhood);
            }
            //currentNeighborhood = neighborhoods.get(0);
        }
    }

    public void requestNeighborhoodsListJson() {

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/neighborhood/list";

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
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                getNeighborhoods(result);
                countInitRequest++;
                gotToHomeActivity();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }



    private void getDiscussionTopics(JsonObject result){
        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ) {
            JsonArray discussionTopicsJson = result.getAsJsonObject("response").getAsJsonArray("topics");
            discussionTopics.clear();
            for ( int i=0 ; i<discussionTopicsJson.size() ; i++ ){
                Title discussionTopic = new Title();
                discussionTopic.setId(discussionTopicsJson.get(i).getAsJsonObject().getAsJsonPrimitive("topic_id").getAsInt());
                discussionTopic.setTitleArabic(discussionTopicsJson.get(i).getAsJsonObject().getAsJsonPrimitive("topic_ar").getAsString());
                discussionTopic.setTitleEnglish(discussionTopicsJson.get(i).getAsJsonObject().getAsJsonPrimitive("topic_en").getAsString());
                discussionTopics.add(discussionTopic);
            }
        }
    }

    public void requestDiscussionTopicsJson() {

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussion/topiclist";

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
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                getDiscussionTopics(result);
                countInitRequest++;
                gotToHomeActivity();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }


    private void getReportReasons(JsonObject result){

        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ) {
            JsonArray reportReasonsJson = result.getAsJsonObject("response").getAsJsonArray("reasons");
            reportReasons.clear();
            for ( int i=0 ; i<reportReasonsJson.size() ; i++ ){
                Title reportReason = new Title();
                reportReason.setId(reportReasonsJson.get(i).getAsJsonObject().getAsJsonPrimitive("reason_id").getAsInt());
                reportReason.setTitleArabic(reportReasonsJson.get(i).getAsJsonObject().getAsJsonPrimitive("reason_ar").getAsString());
                reportReason.setTitleEnglish(reportReasonsJson.get(i).getAsJsonObject().getAsJsonPrimitive("reason_en").getAsString());
                reportReasons.add(reportReason);
            }
        }
    }

    public void requestReportReasonsJson() {


        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/report/reasonlist";

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
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                getReportReasons(result);
                countInitRequest++;
                gotToHomeActivity();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }



    public void getMyProfile(JsonObject result){

        Boolean success = false ;
        if ( result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ){
            profile.setFname(result.getAsJsonObject("response").getAsJsonPrimitive("fName").getAsString());
            profile.setLname(result.getAsJsonObject("response").getAsJsonPrimitive("lName").getAsString());
            profile.setMobile("");
            profile.setDateOfBirth("");

            profile.setId(result.getAsJsonObject("response").getAsJsonPrimitive("user_id").getAsInt());
            profile.setUserName( result.getAsJsonObject("response").getAsJsonPrimitive("fName").getAsString()+
                    " " + result.getAsJsonObject("response").getAsJsonPrimitive("lName").getAsString());

            profile.setImage(result.getAsJsonObject("response").getAsJsonPrimitive("image").getAsString());
            profile.setEmailAddress(result.getAsJsonObject("response").getAsJsonPrimitive("mail").getAsString());

            currentNeighborhood.setId(result.getAsJsonObject("response").getAsJsonPrimitive("neighborhood_id").getAsInt());
            currentNeighborhood.setTitleArabic(result.getAsJsonObject("response").getAsJsonPrimitive("neighborhood_ar").getAsString());
            currentNeighborhood.setTitleEnglish(result.getAsJsonObject("response").getAsJsonPrimitive("neighborhood_en").getAsString());
        }
    }

    public void requestMyProfileJsonObject() {

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/user/myprofile?device_token=" + android_id ;

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                getMyProfile(result);
                countInitRequest++;
                gotToHomeActivity();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }
        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void gotToHomeActivity(){
        if (countInitRequest == 5 ) {
            Intent intent = new Intent(activeActivity, HomeActivity.class);
            startActivity(intent);
        }
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ((TextView) findViewById(R.id.title)).setText(getTitle());
            setTitle("");
        }

    }

}
