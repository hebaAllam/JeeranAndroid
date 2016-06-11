
package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;
import apps.gn4me.com.jeeran.pojo.RealEstateImages;

public class RealEstateDetails extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{


    private SliderLayout mDemoSlider;
    private DrawerLayout mDrawerLayout;
    //    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private Toolbar toolbar;
   TextView title, date, location, price, description, area, numOfBathrooms, numOfRooms;
    ProgressDialog progressDialog;
    private RealEstate mReal;

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
//        Spinner dropdown = (Spinner)findViewById(R.id.spinner1frag);
//        ArrayList<String> items = new ArrayList<>();
//        for (int i=0 ; i< BaseActivity.neighborhoods.size() ; i++ ){
//            items.add(BaseActivity.neighborhoods.get(i).getTitleEnglish());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);
//
//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                BaseActivity.currentNeighborhood = BaseActivity.neighborhoods.get(position) ;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                BaseActivity.currentNeighborhood = BaseActivity.neighborhoods.get(0);
//            }
//        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

//        bindSliderImages();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        setupToolbar();

        requestJsonObjectForDetails();

        bindComponents();

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
        Intent i = getIntent();

        String typee = "rent";
        if(i.getIntExtra("type",3) == 0)
            typee = "rent";
        else if(i.getIntExtra("type",3) == 1)
            typee = "sale";

        Log.i("intent result ::::: ", i.getStringExtra("area")+ ", " +i.getStringExtra("number_of_bathrooms") );

        title.setText(i.getStringExtra("title") + " For\t " + typee);
        date.setText(i.getStringExtra("creationDate")+"");
        location.setText(i.getStringExtra("location"));
        price.setText(i.getIntExtra("price",0)+"");
        description.setText(i.getStringExtra("description")+"");
        area.setText(i.getStringExtra("area")+"");
        numOfBathrooms.setText(i.getIntExtra("number_of_bathrooms",0)+"");
        numOfRooms.setText(i.getIntExtra("number_of_rooms",0)+"");
    }

    private void assignData(RealEstate mReal) {
        String typee = "rent";
        if(mReal.getType() == 0)
            typee = "rent";
        else if(mReal.getType() == 1)
            typee = "sale";

        title.setText(mReal.getTitle() + " For\t " + typee);
        date.setText(mReal.getCreationDate()+"");
        location.setText(mReal.getLocation());
        price.setText(mReal.getPrice()+"");
        description.setText(mReal.getDescription()+"");
        area.setText(mReal.getArea()+"");
        numOfBathrooms.setText(mReal.getNumOfBathreeoms()+"");
        numOfRooms.setText(mReal.getNumOfRooms()+"");
    }

    private void bindComponents() {
        title = (TextView)findViewById(R.id.title_detailsRealEstate);
        date = (TextView)findViewById(R.id.date_text);
        location = (TextView)findViewById(R.id.location_text);
        price = (TextView)findViewById(R.id.price_detailRealEstate);
        description = (TextView)findViewById(R.id.descriptionTxt);
        area = (TextView)findViewById(R.id.areaNu);
        numOfRooms = (TextView)findViewById(R.id.bedRoomsNum);
        numOfBathrooms = (TextView)findViewById(R.id.bathRoomsNum);
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

//                mReal.setPhone(myRealEstates.getAsJsonPrimitive("owner_mobile").getAsString());
//                mReal.setEmail(myRealEstates.getAsJsonPrimitive("owner_email").getAsString());
//                mReal.setContactPerson(myRealEstates.getAsJsonPrimitive("owner_name").getAsString());
                date.setText(myRealEstates.getAsJsonPrimitive("created_at").getAsString());
//                mReal.setUpdateDate(myRealEstates.getAsJsonPrimitive("owner_name").getAsString());
                title.setText(myRealEstates.getAsJsonPrimitive("title").getAsString());
//                mReal.setAddress(myRealEstates.getAsJsonObject().getAsJsonPrimitive("address").getAsString());
                location.setText(myRealEstates.getAsJsonPrimitive("location").getAsString() + " For "+ myRealEstates.getAsJsonPrimitive("type").getAsInt());
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
            Toast.makeText(getApplicationContext(), "reading Failed", Toast.LENGTH_LONG).show();
        }
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
}
