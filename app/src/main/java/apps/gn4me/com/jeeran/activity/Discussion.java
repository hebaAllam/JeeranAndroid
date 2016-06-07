package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DiscussionRecycleViewAdapter;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;

/**
 * Created by acer on 5/17/2016.
 */
public class Discussion extends Fragment {

    protected static final String BASE_URL = "http://jeeran.gn4me.com/jeeran_v1" ;
    private UltimateRecyclerView ultimateRecyclerView;
    private DiscussionRecycleViewAdapter customAdapter = null;
    private LinearLayoutManager linearLayoutManager;
    private Context context ;
    private int moreNum = 2;

    private View view ;
    private List<DiscussionPostData> mList = new ArrayList<>();


    public Discussion() {
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
        context = getContext() ;

        ultimateRecyclerView = (UltimateRecyclerView) view.findViewById(R.id.ultimate_recycler_view);
        ultimateRecyclerView.setHasFixedSize(false);


        customAdapter = new DiscussionRecycleViewAdapter(mList);//getArrayList());

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

        //first ws call
        SharedPreferences settings;
        String token ;
        settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
        token = settings.getString("token", null);


        Ion.with(context)
                .load(BASE_URL + "/discussion/list")
                //.setBodyParameter("neighborhood_id", "")
                //.setBodyParameter("topic_id", "")
                //.setBodyParameter("user_id", "")
                //.setBodyParameter("keyword", "")
                .setHeader("Authorization",token)
                //.setBodyParameter("refresh",(new Date()).toString() )
                .setBodyParameter("start", "0")
                .setBodyParameter("count", "4")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {

                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        boolean success = false ;

                        if (e != null ) {
                            Log.i(":::::::::::::::", e.getMessage());
                        }

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
                                post.setCategory( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("title_en").getAsString());
                                JsonArray imgs = discussions.get(i).getAsJsonObject().getAsJsonArray("disc_imgs") ;

                                post.getUser().setId( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("user_id").getAsInt());
                                post.getUser().setUserName( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("first_name").getAsString());
                                post.getUser().setImage( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("user_image").getAsString());

                                if ( imgs != null && imgs.size() > 0 ){
                                    post.setImage( imgs.get(0).getAsString() );
                                }
                                mList.add(post);
                            }
                            customAdapter.insertAll(mList);
                        }

                    }

                });



        /*
        for (int index = 0; index < 20; index++) {
            DiscussionPostData obj = new DiscussionPostData(index , index , "post" , "http://www.101apps.co.za/images/android/articles/RecyclerView/card.png" ,
                    "very nice shop , give it a try ^_^", "https://ssl.gstatic.com/images/icons/gplus-32.png", "12-23-2014", "New Shop");
            mList.add(index, obj);
        }

        return mList;
        */
    }

    public void addCustomLoaderView(){
        customAdapter.setCustomLoadMoreView(LayoutInflater.from(context)
                .inflate(R.layout.custom_bottom_progressbar, null));
    }


    public  void infinite_Insertlist(){
        //for more ws call
        ultimateRecyclerView.setOnLoadMoreListener(new UltimateRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMore(int itemsCount, final int maxLastVisiblePosition) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        /*
                        DiscussionPostData obj = new DiscussionPostData(moreNum , moreNum++ , "post" , "http://www.101apps.co.za/images/android/articles/RecyclerView/card.png" ,
                                "very nice shop , give it a try ^_^", "https://ssl.gstatic.com/images/icons/gplus-32.png", "12-23-2014", "New Shop");
                        customAdapter.insert(obj , customAdapter.getAdapterItemCount());

                        obj = new DiscussionPostData(moreNum , moreNum++ , "post" , "http://www.101apps.co.za/images/android/articles/RecyclerView/card.png" ,
                                "very nice shop , give it a try ^_^", "https://ssl.gstatic.com/images/icons/gplus-32.png", "12-23-2014", "New Shop");
                        customAdapter.insert(obj , customAdapter.getAdapterItemCount());

                        obj = new DiscussionPostData(moreNum , moreNum++ , "post" , "http://www.101apps.co.za/images/android/articles/RecyclerView/card.png" ,
                                "very nice shop , give it a try ^_^", "https://ssl.gstatic.com/images/icons/gplus-32.png", "12-23-2014", "New Shop");
                        customAdapter.insert(obj , customAdapter.getAdapterItemCount());
                        */

                        SharedPreferences settings;
                        String token ;
                        settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1

                        token = settings.getString("token", null);

                        Integer start = customAdapter.getAdapterItemCount() ;

                        Ion.with(context)
                                .load(BASE_URL + "/discussion/list")
                                //.setBodyParameter("neighborhood_id", "")
                                //.setBodyParameter("topic_id", "")
                                //.setBodyParameter("user_id", "")
                                //.setBodyParameter("keyword", "")
                                .setHeader("Authorization",token)
                                .setBodyParameter("start", start.toString())
                                .setBodyParameter("count", "2")
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {

                                        boolean success = false ;

                                        if (e != null ) {
                                            Log.i(":::::::::::::::", e.getMessage());
                                        }

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

                                                post.setDetails( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("details").getAsString());
                                                post.setTimeStamp( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
                                                post.setCategory( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("title_en").getAsString());
                                                JsonArray imgs = discussions.get(i).getAsJsonObject().getAsJsonArray("disc_imgs") ;

                                                post.getUser().setId( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("user_id").getAsInt());
                                                post.getUser().setUserName( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("first_name").getAsString());
                                                post.getUser().setImage( discussions.get(i).getAsJsonObject().getAsJsonPrimitive("user_image").getAsString());

                                                if ( imgs != null && imgs.size() > 0 ){
                                                    post.setImage( imgs.get(0).getAsString() );
                                                }
                                                customAdapter.insert(post,customAdapter.getAdapterItemCount());
                                            }
                                        }

                                    }

                                });


                    }
                }, 1000);
            }
        });
    }


}