package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RVAdapter;

/**
 * Created by acer on 5/17/2016.
 */
public class RealEstate extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    int color;

    private List<apps.gn4me.com.jeeran.pojo.RealEstate> realEstates;
    private RecyclerView.Adapter myAdapter;
    RecyclerView rv;
    View view;
    private SliderLayout mDemoSlider;
    LinearLayoutManager llm;
    ProgressDialog progressDialog;

    FloatingActionButton search, addRealEstate, home;

    private void openDialog() {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }
    public RealEstate() {
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

        Spinner dropdown = (Spinner)view.findViewById(R.id.spinner1frag);
        String[] items = new String[]{"El-Rehab", "October", "El-Maady"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        mDemoSlider = (SliderLayout)view.findViewById(R.id.sliderfrag);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.hannibal);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
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

    private void initializeData(){
        realEstates = new ArrayList<>();
        final apps.gn4me.com.jeeran.pojo.RealEstate mReal = new apps.gn4me.com.jeeran.pojo.RealEstate();
        Log.i("-*-*-*-* ", "inside ");

        SharedPreferences settings;
        String token ;
        settings = getContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
        token = settings.getString("token", null);
//        if ( token != null ) {
        Ion.with(view.getContext())
                .load(BaseActivity.BASE_URL + "/realstate/list?type= ")
                .setHeader("Authorization",token)
//                .setBodyParameter("type"," ")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //showProgress(false);
                        if ( e != null ) {
                                Log.i("Exception:: ", e.getMessage());
                        }
//                        Log.i("-*-*-*-* ", "inside callback");
                        Boolean success = false ;
                        if ( result != null ) {
                            Log.i("All Result ::: " , result.toString());
                            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                        }

                        if ( success ){
                            progressDialog.dismiss();

                            JsonArray myRealEstates = result.getAsJsonObject("response").getAsJsonArray("real_state");

                            for (int i=0 ; i<myRealEstates.size() ; i++) {
                                mReal.setId(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("favorite_real_estate_ad_id").getAsInt());
                                mReal.setPhone(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_mobile").getAsString());
                                mReal.setEmail(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_email").getAsString());
                                mReal.setContactPerson(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
//                                    mReal.setCreationDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
//                                    mReal.setUpdateDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
                                mReal.setTitle(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString());
                                mReal.setAddress(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("address").getAsString());
                                mReal.setLocation(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("location").getAsString());
                                mReal.setType(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("type").getAsInt());
                                mReal.setNumOfRooms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_rooms").getAsInt());
                                mReal.setNumOfBathreeoms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_bathrooms").getAsInt());
                                mReal.setPrice(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("price").getAsInt());
                                mReal.setArea(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("area").getAsString());
                                mReal.setLanguage(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("language").getAsInt());
                                mReal.setLongitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("longitude").getAsDouble());
                                mReal.setLatitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("latitude").getAsDouble());
                                mReal.setImg(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
//
                                realEstates.add(mReal);

//                                    mList.add(post);
                            }

                        } else {
                            progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(view.getContext(),"reading Failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });
//        }else
//        {
//            Toast.makeText(getContext(),"you didn't registered..",Toast.LENGTH_LONG).show();
//        }



//        realEstates.add(new RealEstate("Flat1","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstate("Flat2","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstate("Flat3","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
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