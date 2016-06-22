package apps.gn4me.com.jeeran.activity;

import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
import apps.gn4me.com.jeeran.adapters.ReviewAdapter;
import apps.gn4me.com.jeeran.pojo.Service;
import apps.gn4me.com.jeeran.pojo.User;
import apps.gn4me.com.jeeran.pojo.UserReview;

public class Reviews extends BaseActivity {
    RecyclerView reviewsRecyclerView;
    ReviewAdapter myAdapter;
    private List<UserReview> reviewList=new ArrayList<>();
    int serviceid,serviceSubCatId,serviceCatId;
    String serviceName,serviceSubCatName,serviceCatName;
    TextView title;
    private static final String TAG_SERVICES_REVIEWS= "response";
    private static final String TAG_REVIEW_CONTENT="review";
    private static final String TAG_REVIEW_ID = "service_place_review_id";
    private static final String TAG_SERVICE_ID = "service_place_id";
    private static final String TAG_REVIEW_DATE= "created_at";
    private static final String TAG_REVIEW_RATINGS = "rating";
    private static final String TAG_USER= "user";
    private static final String TAG_REVIEW_UPDATED_DATE="updated_at";
    private static final String TAG_IS_OWNER="is_owner";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        title=(TextView)findViewById(R.id.txt_titile);

        //------------setting toolbar-----------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("");

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //---------------------------------------------------------
        Intent intent=getIntent();
        serviceid=intent.getExtras().getInt("serviceId");
        serviceName=intent.getExtras().getString("serviceName");
        serviceCatId=intent.getExtras().getInt("serviceCatId");
        serviceCatName=intent.getExtras().getString("serviceCatName");
        serviceSubCatId=intent.getExtras().getInt("serviceSubCatId");
        serviceSubCatName=intent.getExtras().getString("serviceSubCatName");


        title.setText(serviceName+" Reviews");

        reviewsRecyclerView = (RecyclerView) findViewById(R.id.recycle_reviews);
        reviewList.clear();
        myAdapter=new ReviewAdapter(this,reviewList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(mLayoutManager);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setAdapter(myAdapter);
        reviewsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        prepareData();

    }

    private void prepareData() {
        String url ="http://jeeran.gn4me.com/jeeran_v1/servicereview/list?service_place_id="+serviceid;

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        // pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                                JSONObject jsonObject=new JSONObject(response);
                                JSONArray jsonArr=jsonObject.getJSONArray(TAG_SERVICES_REVIEWS);

                            for(int i=0;i<jsonArr.length();i++){

                                    JSONObject reviewObj=jsonArr.getJSONObject(i);
                                    UserReview userReview=new UserReview();
                                    userReview.setReviewID(reviewObj.getInt(TAG_REVIEW_ID));
                                    userReview.setServiceId(reviewObj.getInt(TAG_SERVICE_ID));
                                    userReview.setReviewContent(reviewObj.getString(TAG_REVIEW_CONTENT));
                                    userReview.setNumberOfRates(reviewObj.getInt(TAG_REVIEW_RATINGS));
                                    userReview.setReviewDate(reviewObj.getString(TAG_REVIEW_DATE));
                                    userReview.setReviewUpdateDate(reviewObj.getString(TAG_REVIEW_UPDATED_DATE));
                                    userReview.setIsOwner(reviewObj.getInt(TAG_IS_OWNER));
                                   JSONObject userObj= reviewObj.getJSONObject(TAG_USER);
                                    User user=new User();
                                    user.setId(userObj.getInt("user_id"));
                                    String s=userObj.getString("first_name");
                                    s+=" ";
                                    s+=userObj.getString("last_name");
                                    user.setUserName(s);
                                    user.setImage(userObj.getString("image"));
                                   userReview.setUser(user);
                                    reviewList.add(userReview);
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
                Toast.makeText(Reviews.this,error.getMessage(),Toast.LENGTH_LONG).show();
                pDialog.hide();
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
    public void addNew(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_review_dialog);
        dialog.setTitle("Write Review");

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.customDialogOk);
        final EditText reviewText=(EditText)dialog.findViewById(R.id.review_text);
        RatingBar reviewRates=(RatingBar)dialog.findViewById(R.id.reviewRatingBar) ;
        final  TextView txtRates=(TextView)dialog.findViewById(R.id.txt_rate);
        // Click cancel to dismiss android custom dialog box
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Your android custom dialog ok action
        // Action for custom dialog ok button click
        reviewRates.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                Toast.makeText(Reviews.this, "in action ", Toast.LENGTH_SHORT).show();


             txtRates.setText(String.valueOf(rating));

            }
        });
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review=reviewText.getText().toString();

                if(!review.equals("")){
                   // Toast.makeText(Reviews.this,"you success add review with rates "+txtRates.getText().toString(),Toast.LENGTH_LONG).show();
                    addReview();
                    dialog.dismiss();
                    finish();
                    startActivity(getIntent());
                }

            }

            private void addReview() {


                    String url = "http://jeeran.gn4me.com/jeeran_v1/servicereview/add";

                    final ProgressDialog pDialog = new ProgressDialog(Reviews.this);
                    pDialog.setMessage("Loading...");
                     pDialog.setCancelable(true);
                    pDialog.show();

                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Reviews.this,"this is response "+response,Toast.LENGTH_LONG).show();

                            pDialog.hide();

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Reviews.this,error.getMessage(),Toast.LENGTH_LONG).show();
                            pDialog.hide();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("service_place_id", serviceid+"");
                            params.put("review",reviewText.getText().toString());
                            params.put("rating",txtRates.getText().toString());


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
                    RequestQueue queue = Volley.newRequestQueue(Reviews.this);
                    queue.add(strReq);
                }

        });

        dialog.show();


    }
    public void returnTohome(View view){
        Intent homeIntent=new Intent(Reviews.this,HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
   private void  reportReview(){


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(Reviews.this,ServiceDetails.class);
                i.putExtra("serviceSubCatName",serviceSubCatName);
                i.putExtra("serviceSubCatId",serviceSubCatId);
                i.putExtra("serviceCatName",serviceCatName);
                i.putExtra("serviceCatId",serviceCatId);
                i.putExtra("UniqueServiceId",serviceid);
                i.putExtra("ServiceDetailsName",serviceName);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }



}
