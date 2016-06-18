package apps.gn4me.com.jeeran.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DiscussionRecycleViewAdapter;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;

public class MyDiscussion extends BaseActivity {

    private UltimateRecyclerView ultimateRecyclerView;
    private DiscussionRecycleViewAdapter customAdapter = null;
    private LinearLayoutManager linearLayoutManager;

    private List<DiscussionPostData> mList ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_discussion);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            //setTitle("My Favorites");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mList = new ArrayList<>();

        ultimateRecyclerView = (UltimateRecyclerView) findViewById(R.id.ultimate_recycler_view);
        ultimateRecyclerView.setHasFixedSize(false);


        customAdapter = new DiscussionRecycleViewAdapter(0,this);//getArrayList());

        linearLayoutManager = new LinearLayoutManager(this);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(customAdapter);

        initArrayList();
        ultimateRecyclerView.enableLoadmore();

        addCustomLoaderView();
        //ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        infinite_Insertlist();
    }


    public void initArrayList(){
        requestJsonObject(0,4);
    }


    public void addCustomLoaderView(){
        customAdapter.setCustomLoadMoreView(LayoutInflater.from(this)
                .inflate(R.layout.custom_bottom_progressbar, null));
    }


    private void getDiscussionData(JsonObject result){
        boolean success = false ;
        mList = new ArrayList<>();
        if (result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ){

            Log.i(":::::::::::::::",result.toString());
            JsonArray discussions = result.getAsJsonObject("response").getAsJsonArray("mydiscussionlist");

            for (int i=0 ; i<discussions.size() ; i++) {
                DiscussionPostData post = new DiscussionPostData();
                post.setId( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("discussion_id").getAsInt());
                post.setCommentsNum( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("comments_no").getAsInt());
                post.setTitle( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString());

                Log.i("Titleeeeeeee1" ,  discussions.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString());

                post.setDetails( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("details").getAsString());
                post.setTimeStamp( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
                post.setCategory( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("discussion_topic_title_en").getAsString());

                post.setIsOwner( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("is_owner").getAsInt());
                post.setIsFav( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("is_fav").getAsInt());

                JsonArray imgs = discussions.get(i).getAsJsonObject().getAsJsonArray("disc_imgs") ;
                post.getUser().setId( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("user_id").getAsInt());
                post.getUser().setUserName( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("first_name").getAsString());
                post.getUser().setImage( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("user_image").getAsString());


                post.getTopic().setId( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("topic_id").getAsInt());
                post.getTopic().setTitleArabic( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("discussion_topic_title_ar").getAsString());
                post.getTopic().setTitleEnglish( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("discussion_topic_title_en").getAsString());


                if ( imgs != null && imgs.size() > 0 ){
                    post.setImage( imgs.get(0).getAsString() );
                }
                mList.add(post);
            }
            customAdapter.insertAll(mList);
        }
    }
    private void requestJsonObject(final Integer start , final Integer count) {
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussion/list";

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
                getDiscussionData(result);
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
                params.put("start", start.toString());
                params.put("count",count.toString());
                params.put("user_id",profile.getId().toString());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE);
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }



    public  void infinite_Insertlist(){
        //for more ws call
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        requestJsonObject(customAdapter.getAdapterItemCount(),2);
                    }
                }, 1000);
            }
        });
    }


}
