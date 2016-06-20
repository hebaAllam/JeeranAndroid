package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    HashMap<String,String> file_maps;
    String token ;

    private static List<ServicesCategory> servicesCatList ;
    public MainServices() {
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings;

        settings = getContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
        token = settings.getString("token", null);
        Ion.with(getContext())
                .load("http://jeeran.gn4me.com/jeeran_v1/serviceplacecategory/list")
                .setHeader("Authorization",token)
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
           requestServicesImages();


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

    private void requestServicesImages() {
        file_maps = new HashMap<String, String>();
        final String TAG = "*************************";
        String url = BaseActivity.BASE_URL + "/serviceplace/imagefeature";

        /*
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        */


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(),response,Toast.LENGTH_LONG).show();
                Log.d(TAG, response.toString());
                try {
                    JSONObject responseObj=new JSONObject(response);
                        JSONArray responseArr=responseObj.getJSONArray("response");
                        for(int i=0;i<responseArr.length();i++){
                            JSONObject imageObj=responseArr.getJSONObject(i);
                              file_maps.put(imageObj.getString("title"),imageObj.getString("cover_image"));
                            for(String name : file_maps.keySet()){
                                TextSliderView textSliderView = new TextSliderView(getContext());
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(file_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit)
                                        .setOnSliderClickListener(MainServices.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra",name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                settings = getContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                String mtoken = settings.getString("token", null);
                headers.put("Authorization", mtoken);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(strReq);
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


}