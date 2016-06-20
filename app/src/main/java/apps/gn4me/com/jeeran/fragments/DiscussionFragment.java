package apps.gn4me.com.jeeran.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.HomeActivity;
import apps.gn4me.com.jeeran.adapters.DiscussionRecycleViewAdapter;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;
import apps.gn4me.com.jeeran.pojo.Title;

/**
 * Created by acer on 5/17/2016.
 */
public class DiscussionFragment extends Fragment {

    protected static final String BASE_URL = "http://jeeran.gn4me.com/jeeran_v1" ;
    private UltimateRecyclerView ultimateRecyclerView;
    private DiscussionRecycleViewAdapter customAdapter = null;
    private LinearLayoutManager linearLayoutManager;
    private Context context ;
    private int moreNum = 2;
    FloatingActionButton addDiscussion, home;


    private View view ;
    private List<DiscussionPostData> mList ;


    public DiscussionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.discussion_fragment, container, false) ;

        this.view = view ;
        context = getActivity() ;


        home = (FloatingActionButton) view.findViewById(R.id.fab34);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),HomeActivity.class);
                startActivity(i);
            }
        });

        mList = new ArrayList<>();
        ultimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.ultimate_recycler_view);
        ultimateRecyclerView.setHasFixedSize(false);


        customAdapter = new DiscussionRecycleViewAdapter(1,context);//getArrayList());

        linearLayoutManager = new LinearLayoutManager(context);
        ultimateRecyclerView.setLayoutManager(linearLayoutManager);
        ultimateRecyclerView.setAdapter(customAdapter);

        initArrayList();
        ultimateRecyclerView.enableLoadmore();

        addCustomLoaderView();
        //ultimateRecyclerView.setRecylerViewBackgroundColor(Color.parseColor("#ffffff"));
        infinite_Insertlist();


        return view ;
    }



    public void initArrayList(){
         requestJsonObject(0,4);
    }

    public void addCustomLoaderView(){
        customAdapter.setCustomLoadMoreView(LayoutInflater.from(context)
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
            JsonArray discussions = result.getAsJsonObject("response").getAsJsonArray("discussionlist");

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

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussion/list";


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

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(context);
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
                        Log.i("COunt ::::::::::::: " , "" + customAdapter.getAdapterItemCount());
                    }
                }, 1000);

            }
        });

    }
}