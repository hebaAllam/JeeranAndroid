package apps.gn4me.com.jeeran.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.Action;
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
    private Button CallUsBut;
    int serviceId, serviceSubCatId, serviceCatId;
    String serviceName;
    ImageView showLocation, rateService, favoriteService, serviceLogo;
    String serviceSubCatName, serviceCatName;
    private TextView activityTitle, serviceTitle, serviceAddress, serviceOpeningHours, serviceNumberOfRates, serviceDiscHeader, serviceDisc;
    private static final String TAG_SERVICES_DETAILS = "response";
    private static final String TAG_SERVICES_DATA = "serviceplace";
    private static final String TAG_SERVICES_ID = "service_place_id";
    private static final String TAG_SERVICES_LOGO = "logo";
    private static final String TAG_SERVICES_TITLE = "title";
    private static final String TAG_SERVICES_ADDRESS = "address";
    private static final String TAG_SERVICES_DISCRIPTION = "description";
    private static final String TAG_SERVICES_LONGITUDE = "longitude";
    private static final String TAG_SERVICES_LATITUDE = "latitude";
    private static final String TAG_SERVICES_PHONE1 = "mobile_1";
    private static final String TAG_SERVICES_PHOTOS = "images";
    private static final String TAG_ISFAVORITE = "is_favorite";
    private static final String TAQ_ISREVIEW = "is_review";
    private static final String TAG_TOTALRATES = "total_rate";

    private static final String TAG = "++++++++++";
    Service service;

    Intent i;
    int serviceIdIntent;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detailes);

        showLocation = (ImageView) findViewById(R.id.showLocation);
        rateService = (ImageView) findViewById(R.id.rateService);
        favoriteService = (ImageView) findViewById(R.id.favoriteService);
        serviceLogo = (ImageView) findViewById(R.id.service_logo);
        serviceTitle = (TextView) findViewById(R.id.service_title);
        serviceAddress = (TextView) findViewById(R.id.service_address1);
        serviceOpeningHours = (TextView) findViewById(R.id.service_opiningHours);
        serviceNumberOfRates = (TextView) findViewById(R.id.service_numberofRates);
        serviceDiscHeader = (TextView) findViewById(R.id.disc_header);
        serviceDisc = (TextView) findViewById(R.id.service_disc);
        CallUsBut = (Button) findViewById(R.id.callUs);
        service = new Service();
        i = getIntent();
        //------------setting tool bar-----
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            activityTitle = (TextView) findViewById(R.id.txt_titile);
            setTitle("");

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //-----------------------------
        //------------get service ID-------------
        Intent i = getIntent();
        if (i.hasExtra("fromMyService")) {
            serviceId = i.getExtras().getInt("UniqueServiceId");
        }
        serviceId = i.getExtras().getInt("UniqueServiceId");
        serviceName = i.getExtras().getString("ServiceDetailsName");
        serviceSubCatName = i.getExtras().getString("serviceSubCatName");
        serviceSubCatId = i.getExtras().getInt("serviceSubCatId");
        serviceCatName = i.getExtras().getString("serviceCatName");
        serviceCatId = i.getExtras().getInt("serviceCatId");


        setTitle(serviceName + " Details");


        final Button allReviews = (Button) findViewById(R.id.allReviews);


        recyclerView = (RecyclerView) findViewById(R.id.restaurant_recycleView);

        serviceDetailsList = new ArrayList<>();
        adapter = new ServiceDetailsAdapter(this, serviceDetailsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new MyTouchListener(getApplicationContext(), recyclerView, new MyClickListener() {
            @Override
            public void onClick(View view, int position) {
                Service service = serviceDetailsList.get(position);
                serviceId = service.getServiceId();
                requestServicesDetailsData();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        requestServicesDetailsData();
        prepareDataForSimilarServicesList();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        allReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allReviewsIntent = new Intent(ServiceDetails.this, Reviews.class);
                allReviewsIntent.putExtra("serviceId", serviceId);
                allReviewsIntent.putExtra("serviceName", serviceName);
                allReviewsIntent.putExtra("serviceSubCatName", serviceSubCatName);
                allReviewsIntent.putExtra("serviceSubCatId", serviceSubCatId);
                allReviewsIntent.putExtra("serviceCatName", serviceCatName);
                allReviewsIntent.putExtra("serviceCatId", serviceCatId);


                startActivity(allReviewsIntent);
            }
        });
        CallUsBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServiceOwner();
            }
        });
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDetails.this, ShowServiceLocation.class);
                intent.putExtra("lang", service.getLang());
                intent.putExtra("lat", service.getLat());
                startActivity(intent);
            }
        });
    }


    private void requestServicesDetailsData() {
        String tag_string_req = "string_req";

        String url = "http://jeeran.gn4me.com/jeeran_v1/serviceplace/show";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObjectResponse = jsonObject.getJSONObject(TAG_SERVICES_DETAILS);
                    JSONArray jsonArr = jsonObjectResponse.getJSONArray(TAG_SERVICES_DATA);
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject service1Obj = jsonArr.getJSONObject(i);

                        service.setServiceId(service1Obj.getInt(TAG_SERVICES_ID));
                        service.setName(service1Obj.getString(TAG_SERVICES_TITLE));
                        service.setLogo(service1Obj.getString(TAG_SERVICES_LOGO));
                        service.setAddress(service1Obj.getString(TAG_SERVICES_ADDRESS));
                        service.setDiscription(service1Obj.getString(TAG_SERVICES_DISCRIPTION));
                        service.setLang(service1Obj.getDouble(TAG_SERVICES_LONGITUDE));
                        service.setLat(service1Obj.getDouble(TAG_SERVICES_LATITUDE));
                        service.setIsFavorite(service1Obj.getInt(TAG_ISFAVORITE));
                        service.setIsReview(service1Obj.getInt(TAQ_ISREVIEW));
                        service.setRates(service1Obj.getInt(TAG_TOTALRATES));
                        service.setPhone1(service1Obj.getString(TAG_SERVICES_PHONE1));
                        Glide.with(ServiceDetails.this).load(service.getLogo()).into(serviceLogo);
                        serviceDisc.setText(service.getDiscription());
                        serviceAddress.setText(service.getAddress());
                        serviceTitle.setText(service.getName());
                        serviceNumberOfRates.setText(service.getRates() + "");
                        if (service.getIsFavorite() == 0) {
                            favoriteService.setImageResource(R.drawable.ic_favorites_icon_active);
                            favoriteService.setClickable(false);
                        }
                        if (service.getIsReview() == 0) {
                            rateService.setImageResource(R.drawable.ic_rate_icon_active);
                            rateService.setClickable(false);
                        }
                        serviceTitle.setText("About " + service.getName());


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
                params.put("service_place_id", serviceId + "");


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

        String url = "http://jeeran.gn4me.com/jeeran_v1/serviceplace/list";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArr = jsonObject.getJSONArray("response");
                    for (int i = 0; i < jsonArr.length(); i++) {
                        JSONObject service1Obj = jsonArr.getJSONObject(i);
                        Service service = new Service();
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
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_sub_category_id", serviceSubCatId + "");
                params.put("count", "4");


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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ServiceDetails Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://apps.gn4me.com.jeeran.activity/http/host/path")
        );
        //  AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ServiceDetails Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://apps.gn4me.com.jeeran.activity/http/host/path")
        );
        //  AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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

    public void showServiceImages(View view) {
        Intent intent = new Intent(ServiceDetails.this, ShowServicesImages.class);
        startActivity(intent);
    }

    public void addServiceToFavorites(View view) {
        favoriteService();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void favoriteService() {
        String url = "http://jeeran.gn4me.com/jeeran_v1/serviceplacefavorite/add";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject resultObj = jsonObject.getJSONObject("result");
                    int errCode = resultObj.getInt("errorcode");
                    if (errCode == 0) {
                        favoriteService.setImageResource(R.drawable.ic_favorites_icon_active);
                    }


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                pDialog.dismiss();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
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


    private void callServiceOwner() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:01113812798"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Log.i("premisssion :::::::: ", "");
            return;
        }
        startActivity(callIntent);
    }

    public interface MyClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class MyTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ServiceDetails.MyClickListener clickListener;

        public MyTouchListener(Context context, final RecyclerView recyclerView, final ServiceDetails.MyClickListener clickListener) {
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

}

