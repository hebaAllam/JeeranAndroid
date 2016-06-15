package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import apps.gn4me.com.jeeran.pojo.ServiceDetailsPojo;

public class ServiceDetails extends BaseActivity {

    private RecyclerView recyclerView;
    private ServiceDetailsAdapter adapter;
    private List<Service> serviceDetailsList;
    int serviceId;
    String serviceName;
    ImageView showLocation,rateService,favoriteService,serviceLogo;
    String serviceSubCat;
    private  TextView activityTitle,serviceTitle,serviceAddress,serviceOpeningHours,serviceNumberOfRates,serviceDiscHeader,serviceDisc;
    private static final String TAG_SERVICES_DETAILS= "response";
    private static final String TAG_SERVICES_DATA= "serviceplace";
    private static final String TAG_SERVICES_ID = "service_place_id";
    private static final String TAG_SERVICES_LOGO = "logo";
    private static final String TAG_SERVICES_TITLE = "title";
    private static final String TAG_SERVICES_ADDRESS = "address";
    private static final String TAG_SERVICES_DISCRIPTION = "description";
    private static final String TAG_SERVICES_PHOTOS = "images";

    private static final String TAG="++++++++++";
    String allServices="";

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
        if(i.hasExtra("UniqueServiceId")){
        serviceId= i.getExtras().getInt("UniqueServiceId");
        serviceName=i.getExtras().getString("ServiceDetailsName");
            serviceSubCat=i.getExtras().getString("serviceSubCatName");
            allServices=i.getExtras().getString("allServices");
           setTitle(serviceName+" Details");


        }


        final Button allReviews=(Button)findViewById(R.id.allReviews);


        recyclerView = (RecyclerView) findViewById(R.id.restaurant_recycleView);

        serviceDetailsList = new ArrayList<>();
        adapter = new ServiceDetailsAdapter(this, serviceDetailsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        getServiceDetailsData();
        prepareDataForSimilarServicesList();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        allReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allReviewsIntent=new Intent(ServiceDetails.this,Reviews.class);
                allReviewsIntent.putExtra("serviceId",serviceId);
                allReviewsIntent.putExtra("serviceName",serviceName);
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

    private void getServiceDetailsData() {

        String url = "http://jeeran.gn4me.com/jeeran_v1/serviceplace/show";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(ServiceDetails.this,response,Toast.LENGTH_LONG).show();
                Log.d(TAG, response.toString());
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
                        serviceDisc.setText(service.getDiscription());
                        serviceAddress.setText(service.getAddress());
                        serviceTitle.setText(service.getName());
                       //  serviceNumberOfRates.setText(service.getRates());

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
                params.put("service_place_id", "6");


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY1OTA5NDA5LCJleHAiOjE0NjU5MTMwMDksIm5iZiI6MTQ2NTkwOTQwOSwianRpIjoiNjkzODA2MGZlZjI2ZTZlZGZkMWEzYWJjMzgzYjVhMGEifQ.syGxZCLQgarw96tiY72hoNcVjdImxNR5-np5yf24Kyc");
                return headers;
            }
        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }


    private void prepareDataForSimilarServicesList() {
/*        int[] logos = new int[]{
                R.drawable.reslogo
                };

        ServiceDetailsPojo a= new ServiceDetailsPojo( logos[0],"Coupa", 13.6);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        adapter.notifyDataSetChanged();
        */
        try {

            JSONObject jsonObject=new JSONObject(allServices);
            JSONArray jsonArr=jsonObject.getJSONArray(TAG_SERVICES_DETAILS);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(ServiceDetails.this,ServicesList.class);
                i.putExtra("serviceSubCatName",serviceSubCat);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
