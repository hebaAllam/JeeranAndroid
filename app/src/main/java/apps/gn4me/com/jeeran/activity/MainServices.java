package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.ServiceCategoryAdapter;
import apps.gn4me.com.jeeran.listeners.RecyclerTouchListener;
import apps.gn4me.com.jeeran.pojo.ServicesCategory;

/**
 * Created by acer on 5/27/2016.
 */
public class MainServices extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    View v;
    private RecyclerView recyclerView;
    private ServiceCategoryAdapter myAdapter;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private static final String TAG_SERVICES_MAIN_CATEGORY = "response";
    private static final String TAG_SERVICES_MAIN_CATEGORY_ID = "service_main_category_id";
    private static final String TAG_SERVICES_MAIN_CATEGORY_LOGO = "logo";
    private static final String TAG_SERVICES_MAIN_CATEGORY_TITLE = "title_en";
    private static final String TAG_SERVICES_MAIN_CATEGORY_SUB_CATEGORY = "subcats";
    SharedPreferences sharedpreferences;


    private static List<ServicesCategory> servicesCatList ;
    public MainServices() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Ion.with(getContext())
                .load("http://jeeran.gn4me.com/jeeran_v1/serviceplacecategory/list")
                .setHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY1NjQ2NDAzLCJleHAiOjE0NjU2NTAwMDMsIm5iZiI6MTQ2NTY0NjQwMywianRpIjoiZDQ4NjFiNzY4YWE0MTBiMmY0MjZhYWFhZTQ2ZTZiNjIifQ.d-jlLWJoUh7QUABjqTE2wT063UiF0kwrZmg3Hzq8T1Q")
                .setBodyParameter("main_category", "0")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result!=null){
                            sharedpreferences = getContext().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("mainCategoryJsonObject",result);
                            editor.commit();

                        }

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      v= inflater.inflate(R.layout.service_fragment, container, false);
        //Slider

        Spinner dropdown = (Spinner)v.findViewById(R.id.spinner1frag);
        String[] items = new String[]{"El-Rehab", "October", "El-Maady"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        mDemoSlider = (SliderLayout)v.findViewById(R.id.sliderfrag);

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


     //services category list
        servicesCatList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        servicesCatList.clear();
        myAdapter=new ServiceCategoryAdapter(servicesCatList,getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        //services category list listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
              ServicesCategory servicecat=  servicesCatList.get(position);
                Intent subService=new Intent(getContext(),SubServices.class);
                subService.putExtra("serviceCatId",servicecat.getServiceCatId());
                subService.putExtra("serviceCatName",servicecat.getServiceCatName());
                startActivity(subService);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        prepareDataFromServer();

        return v;
    }

    private void prepareDataFromServer() {
        SharedPreferences prefs = getContext().getSharedPreferences(MyPREFERENCES,0);
       String JsonResult= prefs.getString("mainCategoryJsonObject","noObject");
        try {
                                JSONObject jsonObject=new JSONObject(JsonResult);
                                JSONArray jsonArr=jsonObject.getJSONArray(TAG_SERVICES_MAIN_CATEGORY);
                                for(int i=0;i<jsonArr.length();i++){
                                    JSONObject service1=jsonArr.getJSONObject(i);
                                    ServicesCategory servicesCategory=new ServicesCategory();
                                    servicesCategory.setServiceCatId(service1.getInt(TAG_SERVICES_MAIN_CATEGORY_ID));
                                    servicesCategory.setServiceCatName(service1.getString(TAG_SERVICES_MAIN_CATEGORY_TITLE));
                                    servicesCategory.setServiceCatLogo(service1.getString(TAG_SERVICES_MAIN_CATEGORY_LOGO));
                                    String subCatNum=Integer.toString(service1.getInt(TAG_SERVICES_MAIN_CATEGORY_SUB_CATEGORY));
                                    servicesCategory.setServiceSubCatNumber(subCatNum);
                                    servicesCatList.add(servicesCategory);
                                    myAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }


    }

//    private void prepareData() {
//
//        ServicesCategory serviceCat=new ServicesCategory(1, R.drawable.ic_food_icon,"Food and Beverages","10");
//        servicesCatList.add(serviceCat);
//        ServicesCategory servicecat2=new ServicesCategory(2, R.drawable.ic_shopping_icon,"Shopping","16");
//        servicesCatList.add(servicecat2);
//        ServicesCategory servicecat3=new ServicesCategory(3, R.drawable.ic_supermarket_icon,"SuperMarkets","23"); ;
//        servicesCatList.add(servicecat3);
//        ServicesCategory servicecat4=new ServicesCategory(4, R.drawable.ic_pharmacies_icon,"Pharmacies","40");
//        servicesCatList.add(servicecat4);
//        ServicesCategory servicecat5=new ServicesCategory(5, R.drawable.ic_education_icon,"Education","11");
//        servicesCatList.add(servicecat5);
//        ServicesCategory servicecat6=new ServicesCategory(6, R.drawable.ic_entertainment_icon,"Entertainment","15");
//        servicesCatList.add(servicecat6);
//        myAdapter.notifyDataSetChanged();
//
//    }


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
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
public static List<String> getMainCategoryServices(){
    List<String> mainCategoryNames=new ArrayList<>();
    for(ServicesCategory servicesCategory:servicesCatList){
        mainCategoryNames.add(servicesCategory.getServiceCatName());
    }

    return mainCategoryNames;
}
}