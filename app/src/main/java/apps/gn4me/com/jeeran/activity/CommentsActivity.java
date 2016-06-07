package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.CustomAdapter;
import apps.gn4me.com.jeeran.pojo.DiscussionCommentData;

public class CommentsActivity extends BaseActivity {

    UltimateRecyclerView ultimateRecyclerView;
    CustomAdapter customAdapter = null;
    LinearLayoutManager linearLayoutManager;
    int moreNum = 2;

    private ItemTouchHelper mItemTouchHelper;
    List<DiscussionCommentData> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_list_view);

        ultimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.comments_recycler_view);
        ultimateRecyclerView.setHasFixedSize(false);


        customAdapter = new CustomAdapter(initArrayList());

        linearLayoutManager = new LinearLayoutManager(this);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(customAdapter);
        initArrayList();
        //ultimateRecyclerView.enableLoadmore();

        addCustomLoaderView();
        ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        //infinite_Insertlist();
    }

    public List<DiscussionCommentData> initArrayList(){

        //first ws call
        Log.i("C Here" , "::::::");
        final String discId = getIntent().getStringExtra("disc_id");

        SharedPreferences settings;
        String token ;
        settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1

        token = settings.getString("token", null);

        /*

        Ion.with(getApplicationContext())
                .load(BASE_URL + "/discussioncomments/list")
                .setHeader("Authorization", token)
                .setBodyParameter("disc_id", discId)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        boolean success = false ;

                        if (e != null ) {
                            Log.i(":::::::::::::::", e.getMessage());
                        }

                        if (result != null ) {
                            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                        }

                        if ( success ){
                            JsonArray commentsList = result.getAsJsonArray("response");

                            for (int i=0 ; i<commentsList.size() ; i++) {
                                DiscussionCommentData comment = new DiscussionCommentData();
                                comment.getUser().setId( commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("user_id").getAsInt());
                                comment.getUser().setUserName( commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("first_name").getAsString()
                                        + " " + commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("last_name").getAsString());
                                comment.getUser().setImage( commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("user_image").getAsString());
                                comment.setId( commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("discussion_comment_id").getAsInt());
                                comment.setComment( commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("comment").getAsString());
                                comment.setTimeStamp( commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
                                //comment.setOwnerFlag(commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("is_owner").getAsBoolean());
                                mList.add(comment);
                            }
                            customAdapter.insertAll(mList);
                        }
                    }
                });

        */



        for (int index = 0; index < 20; index++) {
            DiscussionCommentData obj = new DiscussionCommentData(index , index , "name" , "https://ssl.gstatic.com/images/icons/gplus-32.png" , "comment" , "time");
            mList.add(index, obj);
        }
        return mList;

    }

    public void addCustomLoaderView(){
        customAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.custom_bottom_progressbar, null));
    }

    public  void infinite_Insertlist(){
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        DiscussionCommentData obj = new DiscussionCommentData(moreNum , moreNum++ , "name" ,
                                                    "https://ssl.gstatic.com/images/icons/gplus-32.png" , "comment" , "time");
                        customAdapter.insert(obj , customAdapter.getAdapterItemCount());

                        obj = new DiscussionCommentData(moreNum , moreNum++ , "name" ,
                                "https://ssl.gstatic.com/images/icons/gplus-32.png" , "comment" , "time");
                        customAdapter.insert(obj , customAdapter.getAdapterItemCount());

                        obj = new DiscussionCommentData(moreNum , moreNum++ , "name" ,
                                "https://ssl.gstatic.com/images/icons/gplus-32.png" , "comment" , "time");
                        customAdapter.insert(obj , customAdapter.getAdapterItemCount());

                    }
                }, 1000);
            }
        });
    }

}

