package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RealEstateCommentsAdapter;
import apps.gn4me.com.jeeran.pojo.RealEstateCommentPojo;

public class RealEstateComments extends BaseActivity {

    RecyclerView reviewsRecyclerView;
    RealEstateCommentsAdapter myAdapter;
    private List<RealEstateCommentPojo> reviewList=new ArrayList<>();
    int realEstateId;
    String realEstateName;
    TextView title;
    List<RealEstateCommentPojo> realEstates;
    RealEstateCommentPojo mReal;
    ProgressDialog progressDialog;
    AppCompatButton sendComment;
    EditText comment;

    FloatingActionButton addComment, home;
    private void openDialog() {
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_comments);
        openDialog();

        title=(TextView)findViewById(R.id.txt_titile);
        //------------setting toolbar-----------------
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
//            setSupportActionBar(toolbar);
            setTitle("");

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //---------------------------------------------------------
        Intent intent=getIntent();
//        realEstateId=intent.getExtras().getInt("serviceId");
//        realEstateName=intent.getExtras().getString("serviceName");
//        title.setText(realEstateName+" Comments");
//
        reviewsRecyclerView = (RecyclerView) findViewById(R.id.recycle_reviews);
        reviewList.clear();
        myAdapter=new RealEstateCommentsAdapter(this,reviewList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(mLayoutManager);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setAdapter(myAdapter);
        reviewsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        comment = (EditText)findViewById(R.id.comment_comments);
        sendComment = (AppCompatButton)findViewById(R.id.send_comments);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = comment.getText().toString();
                if(data.equals("")|| data.equals(null)){
                    Toast.makeText(getApplicationContext(),"you must enter data",Toast.LENGTH_LONG).show();
                }else {
                    addComment(data);
                }
            }
        });

//        home = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab34);
//        addComment = (com.github.clans.fab.FloatingActionButton)findViewById(R.id.fab);
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(RealEstateComments.this,HomeActivity.class);
//                startActivity(i);
//            }
//        });
//
//        addComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(RealEstateComments.this,AddRealEstate.class);
//                startActivity(i);
//            }
//        });
        prepareData();
    }
    private void requestJsonObject() {
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstatecomments/list";

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
                getRealEstateCommentsData(result);
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
                params.put("realstate_id", "3");
//                params.put("count",count.toString());

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

    private void getRealEstateCommentsData(JsonObject result) {
        realEstates = new ArrayList<>();
        boolean success = false;

        if (result != null) {
            Log.i("All Result ::: ", result.toString());
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
            int msg = result.getAsJsonObject("result").getAsJsonPrimitive("errorcode").getAsInt();
            if(msg == 1){
                Toast.makeText(getApplicationContext(),"No Comments Found",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }

        if (success) {
            progressDialog.dismiss();

            JsonArray myFavoriteRealEstates = result.getAsJsonArray("response");

            for (int i = 0; i < myFavoriteRealEstates.size(); i++) {
                mReal = new RealEstateCommentPojo();
//                mRealEstate = new RealEstate();

                mReal.setRealEstateAdCommentId(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("real_estate_ad_comment_id").getAsInt());
                mReal.setUserComment(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("comment").getAsString());
//                mReal.set(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("comment").getAsString());
                mReal.setIsHide(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("is_hide").getAsInt());
                mReal.setIsOwner(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("is_owner").getAsInt());
                mReal.setCommentDate(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
//                mReal.setUserComment(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("comment").getAsString());
//                mReal.setUserComment(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("comment").getAsString());

                realEstates.add(mReal);

//                                    Log.i("list /*/* / :" , mRealEstate.getTitle());
//                                    adapter.insertItem(mReal);

            }

            myAdapter.insertAll(realEstates);

        } else {
            progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "reading Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void addComment(final String data){
//        Intent intent = new Intent(RealEstateComments.this,AddRealEstate.class);
//        startActivity(intent);
        {
            String  tag_string_req = "string_req";

            final String TAG = "Volley";
            String url = BaseActivity.BASE_URL + "/realstatecomments/add";

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
                    boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                    Log.i("result from add :: ",result.toString());
                    comment.setText(null);
                    if(success)
//                        requestJsonObject();
                        appendIntoList(data,result.getAsJsonObject("response").getAsJsonPrimitive("real_estate_ad_comment_id").getAsInt());
                    else
                        Toast.makeText(getApplicationContext(),"Error in add",Toast.LENGTH_LONG).show();
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
                    params.put("realstate_id", "3");
                    params.put("comment",data);
//                params.put("count",count.toString());

                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    SharedPreferences settings;
                    String token ;
                    settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                    token = settings.getString("token", null);
                    headers.put("Authorization", token);
                    return headers;
                }

            };

// Adding request to request queue
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(strReq);
        }
    }

    private void appendIntoList(String data,int commentId) {
        mReal = new RealEstateCommentPojo();
        mReal.setRealEstateAdCommentId(commentId);
        mReal.setUserComment(data);
        mReal.setCommentDate(new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        mReal.setIsHide(1);
        mReal.setRealEstateAdId(3);
        realEstates.add(mReal);
        myAdapter.insertItem(mReal);
    }

    private void prepareData() {
        requestJsonObject();
        /*
        RealEstateCommentPojo userReview=new RealEstateCommentPojo(1, R.drawable.ic_account_circle_white_64dp,"Mohamed Ali",5,"01/6/2016","Absolutely Amazing ,The menu offers a wide variety of mouth-watering starters,All the products were fresh and the dishes had the warmth of home-made food.");
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        myAdapter.notifyDataSetChanged();
        */

    }
}
