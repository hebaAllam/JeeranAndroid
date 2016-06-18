package apps.gn4me.com.jeeran.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marshalchen.ultimaterecyclerview.RecyclerItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.AddRealEstate;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.HomeActivity;
import apps.gn4me.com.jeeran.activity.RealEstateDetails;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RealEstateAdapter;
import apps.gn4me.com.jeeran.adapters.ViewHolder;
import apps.gn4me.com.jeeran.pojo.RealEstate;

/**
 * Created by acer on 5/17/2016.
 */
public class RealEstateFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    int color;

    private List<apps.gn4me.com.jeeran.pojo.RealEstate> realEstates;
    apps.gn4me.com.jeeran.pojo.RealEstate mReal;
    private RealEstateAdapter myAdapter = new RealEstateAdapter();
    RecyclerView rv;
    View view;
    private SliderLayout mDemoSlider;
    LinearLayoutManager llm;
    ProgressDialog progressDialog;

    HashMap<String, String> url_maps = new HashMap<String, String>();
    FloatingActionButton search, addRealEstate, home;
    private ViewHolder holder;

    private void openDialog() {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }
    public RealEstateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.real_estate_fragment, container, false);
        openDialog();
        holder = new ViewHolder(view);

        Spinner dropdown = (Spinner)view.findViewById(R.id.spinner1frag);
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i< BaseActivity.neighborhoods.size() ; i++ ){
            items.add(BaseActivity.neighborhoods.get(i).getTitleEnglish());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BaseActivity.currentNeighborhood = BaseActivity.neighborhoods.get(position) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BaseActivity.currentNeighborhood = BaseActivity.neighborhoods.get(0);
            }
        });

        mDemoSlider = (SliderLayout)view.findViewById(R.id.sliderfrag);

        bindSliderImages();



        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        rv = (RecyclerView)view.findViewById(R.id.rv);

//        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        initializeData();

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
//                        Log.i("item ::: ", realEstates.get(position).getCreationDate());//null
                        Log.i("item ::: ", realEstates.get(position).getPrice()+ "");
                        Log.i("item ::: ", realEstates.get(position).getDescription()+"");
                        Log.i("item ::: ", realEstates.get(position).getNumOfRooms()+"");

                        Intent i = new Intent(view.getContext(),RealEstateDetails.class);
                        i.putExtra("realestateID",realEstates.get(position).getId()+"");
                        i.putExtra("fav",realEstates.get(position).isFav()+"");
                        i.putExtra("activityType","realEstateActivity");
                        i.putExtra("realestateID",realEstates.get(position).getId()+"");
                        i.putExtra("title",realEstates.get(position).getTitle());
                        i.putExtra("type",realEstates.get(position).getType());
                        i.putExtra("language",realEstates.get(position).getLanguage());
                        i.putExtra("latitude",realEstates.get(position).getLatitude());
                        i.putExtra("longitude",realEstates.get(position).getLongitude());
                        i.putExtra("location",realEstates.get(position).getLocation());
//                        i.putExtra("owner_name",realEstates.get(position).get());
                        i.putExtra("creationDate",realEstates.get(position).getCreationDate());
                        i.putExtra("owner_mobile",realEstates.get(position).getPhone());
                        i.putExtra("owner_email",realEstates.get(position).getEmail());
                        i.putExtra("description",realEstates.get(position).getDescription());
                        i.putExtra("price",realEstates.get(position).getPrice());
                        i.putExtra("number_of_rooms",realEstates.get(position).getNumOfRooms());
                        i.putExtra("number_of_bathrooms",realEstates.get(position).getNumOfBathreeoms());

                        startActivity(i);
                    }
                })
        );

//        myAdapter = new RVAdapter(realEstates);
        rv.setAdapter(myAdapter);

        home = (FloatingActionButton) view.findViewById(R.id.fab34);
        addRealEstate = (FloatingActionButton)view.findViewById(R.id.fab);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),HomeActivity.class);
                startActivity(i);
            }
        });

        addRealEstate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),AddRealEstate.class);
                startActivity(i);
            }
        });

        return view;
    }

    private void bindSliderImages() {
        String  tag_string_req = "string_req";
    final Context context = getContext();

    final String TAG = "Volley";
    String url = BaseActivity.BASE_URL + "/realstate/imagefeature";

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
            putSliderData(result);
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

            return params;
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            SharedPreferences settings;
            String token ;
            settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
            token = settings.getString("token", null);
            headers.put("Authorization", token);
            return headers;
        }

    };

    // Adding request to request queue
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(strReq);
}

    private void putSliderData(JsonObject result) {

//        JsonArray images = result.getAsJsonArray("response");


        Boolean success = false;
        if (result != null) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if (success) {
            JsonArray slider = result.getAsJsonArray("response");
            Log.i(":::::::",slider.toString());

            BaseActivity.realEstateFeatureImgs = slider.size();

            for (int i = 0; i < BaseActivity.realEstateFeatureImgs; i++) {
                url_maps.put(slider.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString(),
                        slider.get(i).getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
            }

            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(getContext());
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
        }
    }


    private void initializeData(){

        requestJsonObject(0 , 5);

//        realEstates = new ArrayList<>();
//
//
//        SharedPreferences settings;
//        String token ;
//        settings = getContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
//        token = settings.getString("token", null);
////        if ( token != null ) {
//        Ion.with(view.getContext())
//                .load(BaseActivity.BASE_URL + "/realstate/list")
//                .setHeader("Authorization",token)
//                .setBodyParameter("type"," ")
//                .setBodyParameter("start", "0")
//                .setBodyParameter("count", "4")
//                .asJsonObject()
//                .setCallback(new FutureCallback<JsonObject>() {
//                    @Override
//                    public void onCompleted(Exception e, JsonObject result) {
//                        // do stuff with the result or error
//                        //showProgress(false);
//                        if ( e != null ) {
//                                Log.i("Exception:: ", e.getMessage());
//                        }
//                        Boolean success = false ;
//                        if ( result != null ) {
//                            Log.i("All Result ::: " , result.toString());
//                            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
//                        }
//
//                        Log.i("success :::::: ", success.toString());
//
//                        if ( success ){
//                            progressDialog.dismiss();
//
//                            JsonArray myRealEstates = result.getAsJsonArray("response");
//                            if(myRealEstates != null) {
////                                JsonObject realEstateAd = myRealEstates.getAsJsonObject();
//
//                                for (int i = 0; i < myRealEstates.size(); i++) {
//                                    mReal = new apps.gn4me.com.jeeran.pojo.RealEstateActivty();
//
//                                    mReal.setPhone(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_mobile").getAsString());
//                                    mReal.setEmail(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_email").getAsString());
//                                    mReal.setContactPerson(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
////                                    mReal.setCreationDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
////                                    mReal.setUpdateDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
//                                    mReal.setTitle(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString());
////                                    mRealEstate.setAddress(myRealEstates.getAsJsonObject().getAsJsonPrimitive("address").getAsString());
//                                    mReal.setLocation(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("location").getAsString());
//                                    mReal.setType(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("type").getAsInt());
//                                    mReal.setNumOfRooms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_rooms").getAsInt());
//                                    mReal.setNumOfBathreeoms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_bathrooms").getAsInt());
//                                    mReal.setPrice(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("price").getAsInt());
//                                    mReal.setArea(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("area").getAsString());
//                                    mReal.setLanguage(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("language").getAsInt());
//                                    mReal.setLongitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("longitude").getAsDouble());
//                                    mReal.setLatitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("latitude").getAsDouble());
//                                    mReal.setImg(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
////
//                                    realEstates.add(mReal);
//
////                                    Log.i("list /*/* / :" , mRealEstate.getTitle());
////                                    adapter.insertItem(mReal);
//
//                                }
//                            }
//                            else
//                            Toast.makeText(getContext(),"Null Data", Toast.LENGTH_LONG).show();
//
//                            myAdapter.insertAll(realEstates);
////                            Log.i("items */*/*/*/ :: ", realEstates.get(0).getMyRealEstate().getTitle() + " " +realEstates.get(1).getMyRealEstate().getTitle() + realEstates.get(2).getMyRealEstate().getTitle());
////                            progressDialog.dismiss();
//                        } else {
//                            progressDialog.dismiss();
////                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
//                            Toast.makeText(view.getContext(),"reading Failed",Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                });
//        }else
//        {
//            Toast.makeText(getContext(),"you didn't registered..",Toast.LENGTH_LONG).show();
//        }



//        realEstates.add(new RealEstateActivty("Flat1","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstateActivty("Flat2","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstateActivty("Flat3","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
    }

    private void requestJsonObject(final Integer start , final Integer count) {
        String  tag_string_req = "string_req";
    final Context context = getContext();

    final String TAG = "Volley";
    String url = BaseActivity.BASE_URL + "/realstate/list";

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
            params.put("type","");
//            params.put("start", start.toString());
//            params.put("count",count.toString());


            return params;
        }
        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<String, String>();
            SharedPreferences settings;
            String token ;
            settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
            token = settings.getString("token", null);
            headers.put("Authorization", token);
            return headers;
        }

    };

    // Adding request to request queue
    RequestQueue queue = Volley.newRequestQueue(context);
    queue.add(strReq);
}

//    private void sendDataToDetails(RealEstate realEstate){
////        Intent i = new Intent(RealEstateActivty.class, RealEstateDetails.class);
//
//    }

    private void getRealEstateData(JsonObject result) {

        realEstates = new ArrayList<>();
        Boolean success = false;
        if (result != null) {
            Log.i("All Result ::: ", result.toString());
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        Log.i("success :::::: ", success.toString());

        if (success) {
            progressDialog.dismiss();

            JsonArray myRealEstates = result.getAsJsonArray("response");
            if (myRealEstates != null) {
//                                JsonObject realEstateAd = myRealEstates.getAsJsonObject();

                for (int i = 0; i < myRealEstates.size(); i++) {
                    mReal = new apps.gn4me.com.jeeran.pojo.RealEstate();

                    mReal.setId(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("real_estate_ad_id").getAsInt());
                    //mReal.setPhone(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_mobile").getAsString());
                    //mReal.setEmail(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_email").getAsString());
                    mReal.setContactPerson(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
                    mReal.setFav(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("is_fav").getAsBoolean());
                    mReal.setCreationDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
                    mReal.setUpdateDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
                    mReal.setTitle(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString());
//                    mReal.setAddress(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("address").getAsString());
                    mReal.setLocation(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("location").getAsString());
                    mReal.setType(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("type").getAsInt());
                    mReal.setNumOfRooms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_rooms").getAsInt());
                    mReal.setNumOfBathreeoms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_bathrooms").getAsInt());
                    mReal.setPrice(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("price").getAsInt());
                    mReal.setArea(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("area").getAsString());
                    mReal.setArea(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("description").getAsString());
//                    mReal.setLanguage(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("language").getAsInt());
//                    mReal.setLongitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("longitude").getAsDouble());
//                    mReal.setLatitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("latitude").getAsDouble());
                    mReal.setImg(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
//

                    realEstates.add(mReal);

//                                    Log.i("list /*/* / :" , mRealEstate.getTitle());
//                                    adapter.insertItem(mReal)
                }
            } else
                Toast.makeText(getContext(), "Null Data", Toast.LENGTH_LONG).show();

            myAdapter.insertAll(realEstates);
//                            Log.i("items */*/*/*/ :: ", realEstates.get(0).getMyRealEstate().getTitle() + " " +realEstates.get(1).getMyRealEstate().getTitle() + realEstates.get(2).getMyRealEstate().getTitle());
//                            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
            Toast.makeText(view.getContext(), "reading Failed", Toast.LENGTH_LONG).show();
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