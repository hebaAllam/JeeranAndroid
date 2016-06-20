package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import apps.gn4me.com.jeeran.adapters.ServiceDetailsAdapter;
import apps.gn4me.com.jeeran.pojo.Service;


public class ServiceDetails extends BaseActivity {

    private RecyclerView recyclerView;
    private ServiceDetailsAdapter adapter;
    private List<Service> serviceDetailsList;
    int serviceId,serviceSubCatId,serviceCatId;
    String serviceName;
    ImageView showLocation,rateService,favoriteService,serviceLogo;
    String serviceSubCatName,serviceCatName;
    private  TextView activityTitle,serviceTitle,serviceAddress,serviceOpeningHours,serviceNumberOfRates,serviceDiscHeader,serviceDisc;
    private static final String TAG_SERVICES_DETAILS= "response";
    private static final String TAG_SERVICES_DATA= "serviceplace";
    private static final String TAG_SERVICES_ID = "service_place_id";
    private static final String TAG_SERVICES_LOGO = "logo";
    private static final String TAG_SERVICES_TITLE = "title";
    private static final String TAG_SERVICES_ADDRESS = "address";
    private static final String TAG_SERVICES_DISCRIPTION = "description";
    private static final String TAG_SERVICES_LONGITUDE = "longitude";
    private static final String TAG_SERVICES_LATITUDE= "latitude";
    private static final String TAG_SERVICES_PHONE1= "mobile_1";
    private static final String TAG_SERVICES_PHOTOS = "images";

    private static final String TAG="++++++++++";


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detailes);

        showLocation=(ImageView)findViewById(R.id.showLocation) ;
        rateService=(ImageView)findViewById(R.id.rateService);
        favoriteService=(ImageView)findViewById(R.id.favoriteService);
        serviceLogo=(ImageView)findViewById(R.id.service_logo);
        serviceTitle=(TextView)findViewById(R.id.service_title);
        serviceAddress=(TextView)findViewById(R.id.service_address1);
        serviceOpeningHours=(TextView)findViewById(R.id.service_opiningHours);
        serviceNumberOfRates=(TextView)findViewById(R.id.service_numberofRates);
        serviceDiscHeader=(TextView)findViewById(R.id.disc_header);
        serviceDisc=(TextView)findViewById(R.id.service_disc);
        //------------setting tool bar-----
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            activityTitle=(TextView)findViewById(R.id.txt_titile);
            setTitle("");

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //-----------------------------
        //------------get service ID-------------
        Intent i=getIntent();
      if(i.hasExtra("fromMyService")){
          serviceId= i.getExtras().getInt("UniqueServiceId");
      }
        serviceId= i.getExtras().getInt("UniqueServiceId");
        serviceName=i.getExtras().getString("ServiceDetailsName");
            serviceSubCatName=i.getExtras().getString("serviceSubCatName");
            serviceSubCatId=i.getExtras().getInt("serviceSubCatId");
        serviceCatName=i.getExtras().getString("serviceCatName");
                serviceCatId=i.getExtras().getInt("serviceCatId");


           setTitle(serviceName+" Details");




        final Button allReviews=(Button)findViewById(R.id.allReviews);


        recyclerView = (RecyclerView) findViewById(R.id.restaurant_recycleView);

        serviceDetailsList = new ArrayList<>();
        adapter = new ServiceDetailsAdapter(this, serviceDetailsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        requestServicesDetailsData() ;
        prepareDataForSimilarServicesList();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        allReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allReviewsIntent=new Intent(ServiceDetails.this,Reviews.class);
                allReviewsIntent.putExtra("serviceId",serviceId);
                allReviewsIntent.putExtra("serviceName",serviceName);
                allReviewsIntent.putExtra("serviceSubCatName",serviceSubCatName);
                allReviewsIntent.putExtra("serviceSubCatId",serviceSubCatId);
               allReviewsIntent.putExtra("serviceCatName",serviceCatName);
                allReviewsIntent.putExtra("serviceCatId",serviceCatId);


                startActivity(allReviewsIntent);
            }
        });
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(ServiceDetails.this,ShowServiceLocation.class);
                startActivity(intent);
            }
        });
    }


    private void requestServicesDetailsData() {
        String  tag_string_req = "string_req";

        String url ="http://jeeran.gn4me.com/jeeran_v1/serviceplace/show";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
       // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject jsonObjectResponse=jsonObject.getJSONObject(TAG_SERVICES_DETAILS);
                    JSONArray jsonArr=jsonObjectResponse.getJSONArray(TAG_SERVICES_DATA);
                    for(int i=0;i<jsonArr.length();i++){
                        JSONObject service1Obj=jsonArr.getJSONObject(i);
                        Service service=new Service();
                        service.setServiceId(service1Obj.getInt(TAG_SERVICES_ID));
                        service.setName(service1Obj.getString(TAG_SERVICES_TITLE));
                        service.setLogo(service1Obj.getString(TAG_SERVICES_LOGO));
                        service.setAddress(service1Obj.getString(TAG_SERVICES_ADDRESS));
                        service.setDiscription(service1Obj.getString(TAG_SERVICES_DISCRIPTION));
                       // service.setLang(service1Obj.getDouble(TAG_SERVICES_LONGITUDE));
                        service.setPhone1(service1Obj.getString(TAG_SERVICES_PHONE1));
                        serviceDisc.setText(service.getDiscription());
                        serviceAddress.setText(service.getAddress());
                        serviceTitle.setText(service.getName());
                        //  serviceNumberOfRates.setText(service.getRates());

                    }

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_place_id",serviceId+"");


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

    private void prepareDataForSimilarServicesList() {

        String url ="http://jeeran.gn4me.com/jeeran_v1/serviceplace/list";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {


                    Toast.makeText(ServiceDetails.this,response,Toast.LENGTH_LONG).show();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArr=jsonObject.getJSONArray("response");
                    for(int i=0;i<jsonArr.length();i++){
                        JSONObject service1Obj=jsonArr.getJSONObject(i);
                        Service service=new Service();
                        service.setServiceId(service1Obj.getInt(TAG_SERVICES_ID));
                        service.setName(service1Obj.getString(TAG_SERVICES_TITLE));
                        service.setLogo(service1Obj.getString(TAG_SERVICES_LOGO));
                        serviceDetailsList.add(service);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_sub_category_id",serviceSubCatId+"");
                params.put("count","4");


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
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public  void showServiceImages(View view){
     Intent intent=new Intent(ServiceDetails.this,ShowServicesImages.class);
        startActivity(intent);
    }
    public void addServiceToFavorites(View view){
        favoriteService();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(ServiceDetails.this,ServicesList.class);
                i.putExtra("serviceSubCatName",serviceSubCatName);
                i.putExtra("serviceSubCatId",serviceSubCatId);
                i.putExtra("serviceCatName",serviceCatName);
                i.putExtra("serviceCatId",serviceCatId);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void favoriteService() {
        String url ="http://jeeran.gn4me.com/jeeran_v1/serviceplacefavorite/add";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    Toast.makeText(ServiceDetails.this,response,Toast.LENGTH_LONG).show();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject resultObj=jsonObject.getJSONObject("result");
                    int errCode=resultObj.getInt("errorcode");
                    if(errCode==0){
                       favoriteService.setImageResource(R.drawable.ic_favorites_icon_active);
                    }


                }
                catch (JSONException e1) {
                    e1.printStackTrace();
                }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ServiceDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                VolleyLog.d("********************", "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_places_id", "6");


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
}

