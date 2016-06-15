package apps.gn4me.com.jeeran.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
    int serviceid;
    String serviceName;
    TextView title;
    private static final String TAG_SERVICES_DETAILS= "response";
    private static final String TAG_SERVICES_REVIEW= "review";
    private static final String TAG_REVIEW_CONTENT="review";
    private static final String TAG_REVIEW_ID = "service_place_review_id";
    private static final String TAG_SERVICE_ID = "service_place_id";
    private static final String TAG_REVIEW_DATE= "created_at";
    private static final String TAG_REVIEW_RATINGS = "rating";
    private static final String TAG_USER= "user";
    private static final String TAG_IS_HIDE = "is_hide";
    private static final String TAG_REVIEW_UPDATED_DATE="updated_at";


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
        Ion.with(this)
                .load("http://jeeran.gn4me.com/jeeran_v1/serviceplace/show")
                .setHeader("Authorization","Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY1OTA5NDA5LCJleHAiOjE0NjU5MTMwMDksIm5iZiI6MTQ2NTkwOTQwOSwianRpIjoiNjkzODA2MGZlZjI2ZTZlZGZkMWEzYWJjMzgzYjVhMGEifQ.syGxZCLQgarw96tiY72hoNcVjdImxNR5-np5yf24Kyc")
                .setBodyParameter("service_place_id", "6")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if(result!=null){
                            try {

                                JSONObject jsonObject=new JSONObject(result);
                                JSONObject jsonObjectResponse=jsonObject.getJSONObject(TAG_SERVICES_DETAILS);
                                JSONArray jsonArr=jsonObjectResponse.getJSONArray(TAG_SERVICES_REVIEW);
                                for(int i=0;i<jsonArr.length();i++){
                                    JSONObject reviewObj=jsonArr.getJSONObject(i);
                                    UserReview userReview=new UserReview();
                                    userReview.setReviewID(reviewObj.getInt(TAG_REVIEW_ID));
                                    userReview.setServiceId(reviewObj.getInt(TAG_SERVICE_ID));
                                    userReview.setReviewContent(reviewObj.getString(TAG_REVIEW_CONTENT));
                                    userReview.setNumberOfRates(reviewObj.getInt(TAG_REVIEW_RATINGS));
                                    userReview.setReviewDate(reviewObj.getString(TAG_REVIEW_DATE));
                                    userReview.setReviewUpdateDate(reviewObj.getString(TAG_REVIEW_UPDATED_DATE));
                                    userReview.setIsHide(reviewObj.getInt(TAG_IS_HIDE));
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
                        }

                    }
                });
//        UserReview userReview=new UserReview(1, R.drawable.ic_account_circle_white_64dp,"Mohamed Ali",5,"01/6/2016","Absolutely Amazing ,The menu offers a wide variety of mouth-watering starters,All the products were fresh and the dishes had the warmth of home-made food.");
//        reviewList.add(userReview);
//        reviewList.add(userReview);
//        reviewList.add(userReview);
//        reviewList.add(userReview);
//        reviewList.add(userReview);
//        reviewList.add(userReview);
//        myAdapter.notifyDataSetChanged();

    }
    public void addNew(View view){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_review_dialog);
        // Custom Android Allert Dialog Title
        dialog.setTitle("Write Review");

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
        Button dialogButtonOk = (Button) dialog.findViewById(R.id.customDialogOk);
        final EditText reviewText=(EditText)dialog.findViewById(R.id.review_text);
        final RatingBar reviewRates=(RatingBar)dialog.findViewById(R.id.reviewRatingBar) ;
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

             txtRates.setText(String.valueOf(rating));

            }
        });
        dialogButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review=reviewText.getText().toString();

                if(!review.equals("")){
                    Toast.makeText(Reviews.this,"you success add review with rates "+txtRates.getText().toString(),Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }

            }
        });

        dialog.show();



    }
    public void returnTohome(View view){
        Intent homeIntent=new Intent(Reviews.this,HomeActivity.class);
        startActivity(homeIntent);
        finish();
    }
}
