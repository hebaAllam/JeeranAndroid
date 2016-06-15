package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
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
import apps.gn4me.com.jeeran.adapters.ServiceAdapter;
import apps.gn4me.com.jeeran.pojo.Service;

public class ServicesList extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    private RecyclerView recyclerView;
    private ServiceAdapter myAdapter;
    private TextView serviceTitle;
    private List<Service> servicesList = new ArrayList<>();
    Spinner dropdown;
    String ServiceSubName;
    String serviceCatName;
    int serviceListIdentifier;
    private static final String TAG_SERVICES= "response";
    private static final String TAG_SERVICES_ID = "service_place_id";
    private static final String TAG_SERVICES_LOGO = "logo";
    private static final String TAG_SERVICES_TITLE = "title";
    private static final String TAG="++++++++++";
    String allservices="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        //----------getViews------------------------------
        serviceTitle=(TextView)findViewById(R.id.txt_titile) ;
        dropdown = (Spinner)findViewById(R.id.spinner1AreaFood);
        serviceTitle=(TextView)findViewById(R.id.txt_titile) ;
        mDemoSlider = (SliderLayout)findViewById(R.id.sliderFood);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.food_recycleView);

        //------set tool Bar----------------------------
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //-------set  Spinner------------------------
         setSpinner();

        //------------------Check which service should listed--------------
        Intent intent=getIntent();
       if(intent.hasExtra("serviceSubCatId")){
           serviceListIdentifier  = intent.getExtras().getInt("serviceSubCatId");
           ServiceSubName=intent.getExtras().getString("serviceSubCatName");
           serviceCatName=intent.getExtras().getString("serviceCatName");

            setTitle(ServiceSubName);
        }
        else{
           setTitle(ServiceSubName);
       }
        setSlider();
        servicesList.clear();
        myAdapter=new ServiceAdapter(servicesList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        final Intent serviceDetailes=new Intent(ServicesList.this,ServiceDetails.class);
        recyclerView.addOnItemTouchListener(new MyTouchListener(getApplicationContext(), recyclerView, new  MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                Service service=servicesList.get(position);
                serviceDetailes.putExtra("UniqueServiceId",service.getServiceId());
                serviceDetailes.putExtra("ServiceDetailsName",service.getName());
                serviceDetailes.putExtra("serviceSubCatName",ServiceSubName);
                serviceDetailes.putExtra("allServices", allservices);
                startActivity(serviceDetailes);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        requestServicesData();

    }



private void requestServicesData(){


    Ion.with(this)
            .load("http://jeeran.gn4me.com/jeeran_v1/serviceplace/list")
            .setHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY1OTA5NDA5LCJleHAiOjE0NjU5MTMwMDksIm5iZiI6MTQ2NTkwOTQwOSwianRpIjoiNjkzODA2MGZlZjI2ZTZlZGZkMWEzYWJjMzgzYjVhMGEifQ.syGxZCLQgarw96tiY72hoNcVjdImxNR5-np5yf24Kyc")
            .setBodyParameter("service_sub_category_id", "4")
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    if(result!=null){
                        try {


                            Toast.makeText(ServicesList.this,result,Toast.LENGTH_LONG).show();
                            JSONObject jsonObject=new JSONObject(result);
                            JSONArray jsonArr=jsonObject.getJSONArray(TAG_SERVICES);
                            for(int i=0;i<jsonArr.length();i++){
                                JSONObject service1Obj=jsonArr.getJSONObject(i);
                                Service service=new Service();
                                service.setServiceId(service1Obj.getInt(TAG_SERVICES_ID));
                                service.setName(service1Obj.getString(TAG_SERVICES_TITLE));
                                service.setLogo(service1Obj.getString(TAG_SERVICES_LOGO));
                                servicesList.add(service);
                                myAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                    else {
                        Toast.makeText(ServicesList.this,"result null",Toast.LENGTH_LONG).show();
                    }

                }
            });
}


    private void requestServices() {
        String  tag_string_req = "string_req";

        String url = "http://jeeran.gn4me.com/jeeran_v1/serviceplace/list";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
      //  pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                   allservices=response;
                    Toast.makeText(ServicesList.this,response,Toast.LENGTH_LONG).show();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArr=jsonObject.getJSONArray(TAG_SERVICES);
                    for(int i=0;i<jsonArr.length();i++){
                        JSONObject service1Obj=jsonArr.getJSONObject(i);
                        Service service=new Service();
                        service.setServiceId(service1Obj.getInt(TAG_SERVICES_ID));
                        service.setName(service1Obj.getString(TAG_SERVICES_TITLE));
                        service.setLogo(service1Obj.getString(TAG_SERVICES_LOGO));
                        servicesList.add(service);
                        myAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                pDialog.hide();

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
                params.put("service_sub_category_id", "4");


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY1NzU3NDY4LCJleHAiOjE0NjU3NjEwNjgsIm5iZiI6MTQ2NTc1NzQ2OCwianRpIjoiYTYwNDkyMjg1ZTg1ODBlMjcwM2YyNDNmMDhiMDQ3NTcifQ.Mjllr6kp00YCPYDpGM15DKkg2fVhXzHUvWJ1aMTjtDk");
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
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
        private ServicesList.MyClickListener clickListener;

        public MyTouchListener (Context context, final RecyclerView recyclerView, final ServicesList.MyClickListener clickListener) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     switch (item.getItemId()){
         case android.R.id.home:
             Intent i=new Intent(ServicesList.this,SubServices.class);
             i.putExtra("serviceCatName",serviceCatName);
             startActivity(i);
             finish();
     }
        return super.onOptionsItemSelected(item);
    }
    private void setSpinner(){

        String[] items = new String[]{"El-Rehab", "October", "El-Maady"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

    }
    private void setSlider(){

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
            TextSliderView textSliderView = new TextSliderView(this);
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
    }

  public void addNew(View view){
      Intent addServiceIntent=new Intent(ServicesList.this,AddService.class);
      startActivity(addServiceIntent);
      finish();


  }
    public void returnTohome(View view){
        Intent homeIntent=new Intent(ServicesList.this,HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
}



