package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
import apps.gn4me.com.jeeran.pojo.ServicesCategory;

public class SubServices extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    private SliderLayout mDemoSlider;
    private RecyclerView recyclerView;
    private ServiceCategoryAdapter myAdapter;
    private TextView serviceTitle;
    private static List<ServicesCategory> subServicesList = new ArrayList<>();
    int serviceCatIdentifier;
    Spinner dropdown;
    String serviceName;
    HashMap<String,String> file_maps;
    private static final String TAG_SERVICES_SUB_CATEGORY = "response";
    private static final String TAG_SERVICES_SUB_CATEGORY_ID = "service_main_category_Id";
    private static final String TAG_SERVICES_SUB_CATEGORY_LOGO = "logo";
    private static final String TAG_SERVICES_SUB_CATEGORY_TITLE = "title_en";
    private static final String TAG_SERVICES_MAIN_CATEGORY_SUB_SERVICES = "services";
    private static final String TAG="++++++++++";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_services);

        //----------getViews------------------------------
        serviceTitle=(TextView)findViewById(R.id.txt_titile) ;
        dropdown = (Spinner)findViewById(R.id.subCategory_spinner);
        serviceTitle=(TextView)findViewById(R.id.txt_titile) ;
        mDemoSlider = (SliderLayout)findViewById(R.id.subCategory_slider);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.subCategory_recycleView);

        //------set tool Bar----------------------------
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-------set  Spinner------------------------
        setSpinner();
        //------------------Check which service should listed--------------
        Intent intent=getIntent();
        if(intent.hasExtra("serviceCatId")) {
            serviceCatIdentifier = intent.getExtras().getInt("serviceCatId");
            serviceName = intent.getExtras().getString("serviceCatName");
        }
             setTitle(serviceName);

        setSlider();
        subServicesList.clear();
        myAdapter=new ServiceCategoryAdapter(subServicesList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        final Intent listServices=new Intent(SubServices.this,ServicesList.class);
        recyclerView.addOnItemTouchListener(new MyTouchListener(getApplicationContext(), recyclerView, new  MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                ServicesCategory subService=subServicesList.get(position);
                listServices.putExtra("serviceSubCatId",subService.getServiceCatId());
                listServices.putExtra("serviceSubCatName",subService.getServiceCatName());
                listServices.putExtra("serviceCatName",serviceName);
                listServices.putExtra("serviceCatId",serviceCatIdentifier);
                startActivity(listServices);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        requestSubCategoriesData();




//--------------------------------------------------------------------------------------

    }

    private void requestSubCategoriesData() {
        String  tag_string_req = "string_req";

        String url ="http://jeeran.gn4me.com/jeeran_v1/serviceplacecategory/list";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
       // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, response.toString());
                                            try {

                                JSONObject jsonObject=new JSONObject(response);
                                JSONArray jsonArr=jsonObject.getJSONArray(TAG_SERVICES_SUB_CATEGORY);
                                for(int i=0;i<jsonArr.length();i++){
                                    JSONObject service1=jsonArr.getJSONObject(i);
                                    ServicesCategory servicesCategory=new ServicesCategory();
                                    servicesCategory.setServiceCatId(service1.getInt(TAG_SERVICES_SUB_CATEGORY_ID));
                                    servicesCategory.setServiceCatName(service1.getString(TAG_SERVICES_SUB_CATEGORY_TITLE));
                                    servicesCategory.setServiceCatLogo(service1.getString(TAG_SERVICES_SUB_CATEGORY_LOGO));
                                    String subCatNum=Integer.toString(service1.getInt(TAG_SERVICES_MAIN_CATEGORY_SUB_SERVICES));
                                    servicesCategory.setServiceSubCatNumber(subCatNum);
                                    subServicesList.add(servicesCategory);
                                    myAdapter.notifyDataSetChanged();
                                }
                           } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("main_category",serviceCatIdentifier+"");


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                SharedPreferences settings;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                String mtoken = settings.getString("token", null);
                headers.put("Authorization", mtoken);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }


    private void setSpinner(){

        String[] items = new String[]{"El-Rehab", "October", "El-Maady"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }
    private void setSlider(){

        requestServicesImages();

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
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

    public interface MyClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class MyTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SubServices.MyClickListener clickListener;

        public MyTouchListener (Context context, final RecyclerView recyclerView, final SubServices.MyClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
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

                Log.d(TAG, response.toString());
                try {
                    JSONObject responseObj=new JSONObject(response);
                    JSONArray responseArr=responseObj.getJSONArray("response");
                    for(int i=0;i<responseArr.length();i++){
                        JSONObject imageObj=responseArr.getJSONObject(i);
                        file_maps.put(imageObj.getString("title"),imageObj.getString("cover_image"));
                        for(String name : file_maps.keySet()){
                            TextSliderView textSliderView = new TextSliderView(SubServices.this);
                            // initialize a SliderLayout
                            textSliderView
                                    .description(name)
                                    .image(file_maps.get(name))
                                    .setScaleType(BaseSliderView.ScaleType.Fit)
                                    .setOnSliderClickListener(SubServices.this);

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
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                String mtoken = settings.getString("token", null);
                headers.put("Authorization", mtoken);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

}




