package apps.gn4me.com.jeeran.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.FavoriteRealEstate;
import apps.gn4me.com.jeeran.pojo.RealEstate;
import apps.gn4me.com.jeeran.pojo.RealEstateImages;

public class RealEstateDetails extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    int clickCount = 0;
    private SliderLayout mDemoSlider;
    private DrawerLayout mDrawerLayout;
    //    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private Toolbar toolbar;
    TextView title, date, location, price, description, area, numOfBathrooms, numOfRooms, typeRealEstate;
    ProgressDialog progressDialog;
    private RealEstate mReal;
    ImageView moreBtn;
    Button saveBtn, changes, callUs;
    String phone;
    EditText areaEdit, bathroomEdit, bedroomEdit, typeEdit, titleEdit, descEdit, locEdit, dateEdit, priceEdit;

    String activityType;
    ImageView favorite, favoriteActive;
    Intent i;
    int edit = 0;
    Button comments, locationOnMap;
    private java.lang.String address="";

    private void openDialog() {

        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_details);

        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);

        openDialog();

        i = getIntent();
        activityType = i.getStringExtra("activityType");

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        setupToolbar();
//        i = getIntent();

        requestJsonObjectForDetails();

        bindComponents();

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = i.getStringExtra("realestateID");
                Intent x = new Intent(RealEstateDetails.this, RealEstateComments.class);
                x.putExtra("realestateID", id);
                startActivity(x);
            }
        });
//        Intent i = getIntent();
//        i.getStringExtra("title");

//        assignVlues();
    }
//
//    private void bindSliderImages() {
////        Log.i(":::::::",slider.toString());
//
//        BaseActivity.realEstateFeatureImgs = slider.size();
//
//        for (int i = 0; i < BaseActivity.realEstateFeatureImgs; i++) {
//            url_maps.put(slider.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString(),
//                    slider.get(i).getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
//        }
//
//        for(String name : url_maps.keySet()){
//            TextSliderView textSliderView = new TextSliderView(this);
//            // initialize a SliderLayout
//            textSliderView
//                    .description(name)
//                    .image(url_maps.get(name))
//                    .setScaleType(BaseSliderView.ScaleType.Fit)
//                    .setOnSliderClickListener(this);
//
//            //add your extra information
//            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle()
//                    .putString("extra",name);
//
//            mDemoSlider.addSlider(textSliderView);
//        }
//    }

    private void assignVlues() {
//        Intent i = getIntent();

        String typee = "rent";
        if (i.getIntExtra("type", 3) == 0)
            typee = "rent";
        else if (i.getIntExtra("type", 3) == 1)
            typee = "sale";

        Log.i("intent result ::::: ", i.getStringExtra("area") + ", " + i.getStringExtra("number_of_bathrooms"));

        title.setText(i.getStringExtra("title") + " For\t " + typee);
        date.setText(i.getStringExtra("creationDate") + "");
        location.setText(i.getStringExtra("location"));
        price.setText(i.getIntExtra("price", 0) + "");
        description.setText(i.getStringExtra("description") + "");
        area.setText(i.getStringExtra("area") + "");
        numOfBathrooms.setText(i.getIntExtra("number_of_bathrooms", 0) + "");
        numOfRooms.setText(i.getIntExtra("number_of_rooms", 0) + "");
    }

    private void assignData(RealEstate mReal) {
        String typee = "rent";
        if (mReal.getType() == 0)
            typee = "rent";
        else if (mReal.getType() == 1)
            typee = "sale";

        title.setText(mReal.getTitle() + " For\t " + typee);
        date.setText(mReal.getCreationDate() + "");
        location.setText(mReal.getLocation());
        price.setText(mReal.getPrice() + "");
        description.setText(mReal.getDescription() + "");
        area.setText(mReal.getArea() + "");
        numOfBathrooms.setText(mReal.getNumOfBathreeoms() + "");
        numOfRooms.setText(mReal.getNumOfRooms() + "");
    }

    private void bindComponents() {
        title = (TextView) findViewById(R.id.title_detailsRealEstate);
        date = (TextView) findViewById(R.id.date_text);
        location = (TextView) findViewById(R.id.location_text);
        price = (TextView) findViewById(R.id.price_detailRealEstate);
        description = (TextView) findViewById(R.id.descriptionTxt);
        area = (TextView) findViewById(R.id.areaNu);
        numOfRooms = (TextView) findViewById(R.id.bedRoomsNum);
        numOfBathrooms = (TextView) findViewById(R.id.bathRoomsNum);
        favorite = (ImageView) findViewById(R.id.favoriteRealEstate);
//        favorite.setVisibility(View.INVISIBLE);
        moreBtn = (ImageView) findViewById(R.id.optionsInRealEstate);
        typeRealEstate = (TextView) findViewById(R.id.type_detailsRealEstate);
        saveBtn = (Button) findViewById(R.id.saveChangesBtn);
        callUs = (Button) findViewById(R.id.callUs);
        favoriteActive = (ImageView) findViewById(R.id.favoriteRealEstateActive);

        if (activityType.equals("favoriteRealEstate")) {
//            favorite.setBackgroundColor(getResources().getColor(R.color.red));
            favorite.setVisibility(View.INVISIBLE);
            favoriteActive.setVisibility(View.VISIBLE);

//            favorite.setImageDrawable(getResources().getDrawable(R.drawable.favorite_icon_active));
            moreBtn.setVisibility(View.INVISIBLE);
        }

//        boolean isFav = i.getBooleanExtra("isFav",false);

        locationOnMap = (Button)findViewById(R.id.locationOnMap_add);

        locationOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"inside",Toast.LENGTH_LONG).show();
                openMap(v);
            }
        });

        comments = (Button) findViewById(R.id.comments);

        areaEdit = (EditText)findViewById(R.id.area_edit);
        bedroomEdit = (EditText)findViewById(R.id.bedroomsNum_edit);
        bathroomEdit = (EditText)findViewById(R.id.bathroomsNum_edit);
        locEdit = (EditText)findViewById(R.id.location_edit);
        typeEdit = (EditText)findViewById(R.id.type_edit);
        titleEdit = (EditText)findViewById(R.id.title_edit);
        priceEdit = (EditText)findViewById(R.id.price_edit);
        dateEdit = (EditText)findViewById(R.id.date_edit);
        descEdit = (EditText)findViewById(R.id.description_edit);
        changes = (Button) findViewById(R.id.saveChangesBtn);

        callUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callOwner();
            }
        });

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.i(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.i(LOG_TAG, "IDLE");

                if (isPhoneCalling) {

                    Log.i(LOG_TAG, "restart app");

                    // restart app
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }


    private void callOwner() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:01113812798"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.i("premisssion :::::::: ","");
            return;
        }
        startActivity(callIntent);
    }

    public void addToRealEstateFavorite(View view){
        clickCount++;
        if(clickCount >= 2)
            clickCount=0;
        if(activityType.equals("favoriteRealEstate") )
            deleteRealEstateFromFavorites();
        else {
            addRealEstateToFavorite();
        }
    }

    private void openMap(View v){
        startActivity(viewOnMap(address));
    }
    public static Intent viewOnMap(String address) {
        return new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("geo:0,0?q=%s",
                        URLEncoder.encode(address))));
    }

    public static Intent viewOnMap(String lat, String lng) {
        return new Intent(Intent.ACTION_VIEW,
                Uri.parse(String.format("geo:%s,%s", lat, lng)));
    }

    private void addRealEstateToFavorite() {
        String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstatefavorite/add";

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
                Log.i("result in details ::: ",result.toString());
                boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                int errorCode = result.getAsJsonObject("result").getAsJsonPrimitive("errorcode").getAsInt();
                if(success) {
                    Toast.makeText(getApplicationContext(), result.getAsJsonObject("result").getAsJsonPrimitive("message").getAsString(), Toast.LENGTH_LONG).show();
//                    favorite.setImageDrawable(getResources().getDrawable(R.drawable.favorite_icon_active));
//                }
//                else if(errorCode == 3) {
//                    deleteRealEstateFromFavorites();
//                    Toast.makeText(getApplicationContext(), result.getAsJsonObject("result").getAsJsonPrimitive("message").getAsString(), Toast.LENGTH_LONG).show();
//
                }else
                    Toast.makeText(getApplicationContext(),"error...",Toast.LENGTH_LONG).show();
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
                i = getIntent();
                String id = i.getStringExtra("realestateID");
                params.put("realstate_id",id );
//            params.put("start", start.toString());
//            params.put("count",count.toString());


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

    private void deleteRealEstateFromFavorites() {
            String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstatefavorite/delete";

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
                Log.i("result in details ::: ",result.toString());
                boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                if(success) {
                    Toast.makeText(getApplicationContext(), result.getAsJsonObject("result").getAsJsonPrimitive("message").getAsString(), Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(this, HomeActivity.class);
//                    favorite.setBackgroundColor(getResources().getColor(R.color.white));
                    favorite.setVisibility(View.VISIBLE);
                    favoriteActive.setVisibility(View.INVISIBLE);
                    onBackPressed();
                }else
                    Toast.makeText(getApplicationContext(),"error...",Toast.LENGTH_LONG).show();
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
//                Intent i = getIntent();
                String id = i.getStringExtra("realestateID");
                params.put("favorite_id",id );
//            params.put("start", start.toString());
//            params.put("count",count.toString());


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            ((TextView) findViewById(R.id.title)).setText(getTitle());
//            setTitle("");
        }

    }


    private void requestJsonObjectForDetails() {
        String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstate/show";

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
                Log.i("result in details ::: ",result.toString());
                getRealEstateData(result);
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
                Intent i = getIntent();
                String id = i.getStringExtra("realestateID");
                params.put("realstate_id",id );
//            params.put("start", start.toString());
//            params.put("count",count.toString());


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.editRealEstate:
                editRealEstate();
                return true;
            case R.id.deleteRealEstate:
                deleteRealEstate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteRealEstate() {
        Toast.makeText(getApplicationContext(),"in edit",Toast.LENGTH_LONG).show();
    }

    private void editRealEstate() {
        Toast.makeText(getApplicationContext(),"in delete",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.real_estate_menu_options, menu);
        return true;
    }*/

//    private void sendDataToDetails(RealEstate realEstate){
////        Intent i = new Intent(RealEstateActivty.class, RealEstateDetails.class);
//
//    }

    private void getRealEstateData(JsonObject result) {

//        realEstates = new ArrayList<>();
        Boolean success = false;
        if (result != null) {
            Log.i("All Result ::: ", result.toString());
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        Log.i("success :::::: ", success.toString());

        if (success) {
            progressDialog.dismiss();

            JsonObject myRealEstates = result.getAsJsonObject("response");
            if (myRealEstates != null) {
//                                JsonObject realEstateAd = myRealEstates.getAsJsonObject();

                mReal = new RealEstate();
                JsonArray myImgArray = myRealEstates.getAsJsonArray("images");
                List<RealEstateImages> imgs = new ArrayList<>();
                RealEstateImages images;

                mReal.setPhone(myRealEstates.getAsJsonPrimitive("owner_mobile").getAsString()+"");
                address = mReal.getAddress();
                phone = mReal.getPhone();
//                mReal.setEmail(myRealEstates.getAsJsonPrimitive("owner_email").getAsString());
//                mReal.setContactPerson(myRealEstates.getAsJsonPrimitive("owner_name").getAsString());
                date.setText(myRealEstates.getAsJsonPrimitive("created_at").getAsString());

                Log.i("Fav ::: ", myRealEstates.getAsJsonPrimitive("is_fav")+"");
                mReal.setFav(myRealEstates.getAsJsonPrimitive("is_fav").getAsBoolean());
                boolean fav = mReal.isFav();
                if(fav){
                    favorite.setVisibility(View.INVISIBLE);
                    favoriteActive.setVisibility(View.VISIBLE);
                }else {
                    favorite.setVisibility(View.VISIBLE);
                    favoriteActive.setVisibility(View.INVISIBLE);
                }
//                mReal.setUpdateDate(myRealEstates.getAsJsonPrimitive("owner_name").getAsString());
                title.setText(myRealEstates.getAsJsonPrimitive("title").getAsString()+ "");
                typeRealEstate.setText(myRealEstates.getAsJsonPrimitive("type").getAsInt()+"");
//                mReal.setAddress(myRealEstates.getAsJsonObject().getAsJsonPrimitive("address").getAsString());
                location.setText(myRealEstates.getAsJsonPrimitive("location").getAsString() );
                numOfRooms.setText(myRealEstates.getAsJsonPrimitive("number_of_rooms").getAsInt()+"");
                numOfBathrooms.setText(myRealEstates.getAsJsonPrimitive("number_of_bathrooms").getAsInt()+"");
                price.setText(myRealEstates.getAsJsonPrimitive("price").getAsInt()+"");
                description.setText(myRealEstates.getAsJsonPrimitive("description").getAsString());
                area.setText(myRealEstates.getAsJsonPrimitive("area").getAsString());


//                mReal.setLanguage(myRealEstates.getAsJsonPrimitive("language").getAsInt());
//                mReal.setLongitude(myRealEstates.getAsJsonPrimitive("longitude").getAsDouble());
//                mReal.setLatitude(myRealEstates.getAsJsonPrimitive("latitude").getAsDouble());
//                mReal.setImg(myRealEstates.getAsJsonPrimitive("cover_image").getAsString());
                for (int i=0; i<myImgArray.size(); i++) {
                    images = new RealEstateImages();
                    images.setImage(myImgArray.get(i).getAsJsonObject().getAsJsonPrimitive("image").getAsString());
                    images.setOriginalimg(myImgArray.get(i).getAsJsonObject().getAsJsonPrimitive("originalimg").getAsString());
                    images.setThumb(myImgArray.get(i).getAsJsonObject().getAsJsonPrimitive("thumb").getAsString());
                    images.setIs_primary(myImgArray.get(i).getAsJsonObject().getAsJsonPrimitive("is_primary").getAsInt());
                    images.setReal_estate_ad_id(myImgArray.get(i).getAsJsonObject().getAsJsonPrimitive("real_estate_ad_image_id").getAsInt());
                    images.setReal_estate_ad_image_id(myImgArray.get(i).getAsJsonObject().getAsJsonPrimitive("real_estate_ad_image_id").getAsInt());

                    imgs.add(images);
                }

                BaseActivity.realEstateFeatureImgs = imgs.size();

                for (int i = 0; i < BaseActivity.realEstateFeatureImgs; i++) {
                    url_maps.put(imgs.get(i).getReal_estate_ad_image_id()+"",
                            imgs.get(i).getOriginalimg());
                }

                for(String name : url_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);

                    mDemoSlider.addSlider(textSliderView);
                }
//
//                    realEstates.add(mReal);

//                                    Log.i("list /*/* / :" , mRealEstate.getTitle());
//                                    adapter.insertItem(mReal);



//                assignData(mReal);

            } else
                Toast.makeText(getApplicationContext(), "Null Data", Toast.LENGTH_LONG).show();

//            myAdapter.insertAll(realEstates);
//                            Log.i("items */*/*/*/ :: ", realEstates.get(0).getMyRealEstate().getTitle() + " " +realEstates.get(1).getMyRealEstate().getTitle() + realEstates.get(2).getMyRealEstate().getTitle());
//                            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "reading realestate Failed", Toast.LENGTH_LONG).show();
        }
    }
    public void showPopup(View v) {
        // inflate menu
        PopupMenu popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.real_estate_menu_options, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.editRealEstate:
                    edit = 1;
//                    preatreGUI();
//                    editRealEstateCode();
//                    editRealEstate();
                    openEditTexts();
//                    Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.deleteRealEstate:
                    deleteRealEstate();
//                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
                    return true;
//                case R.id.addRealEstate:
//                    addRealEstate();
//                    Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_SHORT).show();
//                    return true;
                default:
            }
            return false;
        }
    }

    private void openEditTexts() {
        changes.setVisibility(View.VISIBLE);
        area.setVisibility(View.INVISIBLE);
        typeRealEstate.setVisibility((View.INVISIBLE));
        numOfBathrooms.setVisibility(View.INVISIBLE);
        numOfRooms.setVisibility(View.INVISIBLE);
        title.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        location.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        price.setVisibility(View.INVISIBLE);

        //open edittexts

        areaEdit.setText(area.getText().toString()+"");
        typeEdit.setText(typeRealEstate.getText().toString()+"");
        bathroomEdit.setText(numOfBathrooms.getText().toString()+"");
        bedroomEdit.setText(numOfRooms.getText().toString()+"");
        titleEdit.setText(title.getText().toString()+"");
        dateEdit.setText(date.getText().toString()+"");
        locEdit.setText(location.getText().toString()+"");
        descEdit.setText(description.getText().toString()+"");
        priceEdit.setText(price.getText().toString());

        //make them visible
        areaEdit.setVisibility(View.VISIBLE);
        typeEdit.setVisibility((View.VISIBLE));
        bathroomEdit.setVisibility(View.VISIBLE);
        bedroomEdit.setVisibility(View.VISIBLE);
        titleEdit.setVisibility(View.VISIBLE);
        dateEdit.setVisibility(View.VISIBLE);
        locEdit.setVisibility(View.VISIBLE);
        descEdit.setVisibility(View.VISIBLE);
        priceEdit.setVisibility(View.VISIBLE);

    }

    private void editRealEstateCode() {
//        Intent i = new Intent(RealEstateDetails.this,AddRealEstate.class);
//        RealEstate.myRealEstateObj.setAddress(mReal.getAddress());
//        RealEstate.myRealEstateObj.setFav(mReal.isFav());
//        RealEstate.myRealEstateObj.setTitle(mReal.getTitle());
//        RealEstate.myRealEstateObj.setType(mReal.getType());
//        RealEstate.myRealEstateObj.
    }

    private void addRealEstate() {
        Intent intent = new Intent(RealEstateDetails.this,AddRealEstate.class);
        startActivity(intent);
    }

    private void deleteRealEstate() {
        String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstate/delete";

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
                Log.i("result in delete ::: ",result.toString());
                Toast.makeText(getApplicationContext(),result.getAsJsonObject("result").getAsJsonPrimitive("message").getAsString(),Toast.LENGTH_LONG).show();
//                getRealEstateData(result);
                onBackPressed();
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
                Intent i = getIntent();
                String id = i.getStringExtra("realestateID");
                params.put("realstate_id",id );
//            params.put("start", start.toString());
//            params.put("count",count.toString());


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

    private void editRealEstate() {

        String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstate/edit";

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
                Log.i("result in edit ::: ",result.toString());
                Toast.makeText(getApplicationContext(),result.getAsJsonObject("result").getAsJsonPrimitive("message").getAsString(),Toast.LENGTH_LONG).show();
//                getRealEstateData(result);
                onBackPressed();
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
//                Intent i = getIntent();
//                prepareGUI();
                String id = i.getStringExtra("realestateID");
                params.put("realstate_id",id );
                params.put("type",typeEdit.getText().toString());
                params.put("title",titleEdit.getText().toString());
                params.put("description",descEdit.getText().toString());
                params.put("location",locEdit.getText().toString());
                params.put("number_of_rooms",bedroomEdit.getText().toString());
                params.put("number_of_bathrooms",bathroomEdit.getText().toString());
                params.put("area",areaEdit.getText().toString());
                params.put("price",priceEdit.getText().toString());
                params.put("amenites","amenities");
                params.put("longitude","12");
                params.put("latitude","20");


//            params.put("start", start.toString());
//            params.put("count",count.toString());


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

    private void preatreGUI() {
        makeTxtViewEditable(location);
        makeTxtViewEditable(title);
        makeTxtViewEditable(date);
        makeTxtViewEditable(price);
        makeTxtViewEditable(description);
        makeTxtViewEditable(numOfBathrooms);
        makeTxtViewEditable(numOfRooms);
        saveBtn.setVisibility(View.VISIBLE);
    }

    public void saveChanges(View view){
        editRealEstate();
    }

    private void makeTxtViewEditable(TextView textView){
        textView.setCursorVisible(true);
//        textView.setFocusableInTouchMode(true);
        textView.setInputType(InputType.TYPE_CLASS_TEXT);
        textView.requestFocus();
    }
}
