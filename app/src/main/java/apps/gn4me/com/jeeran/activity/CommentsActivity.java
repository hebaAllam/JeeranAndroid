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
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


        customAdapter = new CustomAdapter(mList);

        linearLayoutManager = new LinearLayoutManager(this);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(customAdapter);
        initArrayList();
        //ultimateRecyclerView.enableLoadmore();

        addCustomLoaderView();
        ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        //infinite_Insertlist();
    }

    public void initArrayList(){
        requestJsonObject();
    }


    private void getCommentData(JsonObject result){

        boolean success = false ;

        if (result != null ) {
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        if ( success ){
            JsonArray commentsList = result.getAsJsonArray("response");

            String msg = "" ;
            for (int i=0 ; i<commentsList.size() ; i++) {
                DiscussionCommentData comment = new DiscussionCommentData();
                comment.getUser().setId( commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("user_id").getAsInt());
                comment.getUser().setUserName( commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("first_name").getAsString() + " " + commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("last_name").getAsString());
                comment.getUser().setImage( commentsList.get(i).getAsJsonObject().getAsJsonObject("user").getAsJsonPrimitive("image").getAsString());
                comment.setId( commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("discussion_comment_id").getAsInt());
                comment.setComment( commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("comment").getAsString());
                comment.setTimeStamp( commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
                //comment.setOwnerFlag(commentsList.get(i).getAsJsonObject().getAsJsonPrimitive("is_owner").getAsBoolean());
                mList.add(comment);
                msg = comment.getComment();
            }
            Log.i("comments data" , msg);
            customAdapter.insertAll(mList);
        }
    }

    private void requestJsonObject() {
        String  tag_string_req = "string_req";

        final String discId = getIntent().getStringExtra("disc_id");

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussioncomments/list";

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
                getCommentData(result);
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
                params.put("disc_id", discId);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        //Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
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
                        requestJsonObject();//(customAdapter.getAdapterItemCount(),2);
                    }
                }, 1000);
            }
        });
    }

}

