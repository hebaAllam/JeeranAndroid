package apps.gn4me.com.jeeran.multi_view_list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

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
import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;
import apps.gn4me.com.jeeran.pojo.Title;

/**
 * Created by cym on 15/5/18.
 */
public class Sample1Binder extends DataBinder<Sample1Binder.ViewHolder> {

    private Context context ;
    private Title discussionTopic ;


    public Sample1Binder(UltimateDifferentViewTypeAdapter dataBindAdapter  , Context context) {
        super(dataBindAdapter);
        this.context = context ;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_post_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {
        //String[] items = new String[]{"Taxs", "Transportation", "Expensive Food"};
        ArrayList<String> items = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.context, android.R.layout.simple_spinner_dropdown_item, items);
        holder.problemSpinner.setAdapter(adapter);
        items.clear();
        adapter.notifyDataSetChanged();
        for (int i=0 ; i< BaseActivity.discussionTopics.size() ; i++ ){
            items.add(BaseActivity.discussionTopics.get(i).getTitleEnglish());
        }
        adapter.notifyDataSetChanged();
        //String text = holder.problemSpinner.getSelectedItem().toString();

        holder.problemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discussionTopic = BaseActivity.discussionTopics.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                discussionTopic = BaseActivity.discussionTopics.get(0) ;
            }
        });


        holder.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = holder.postEditTxt.getText().toString();
                Log.i("add post :::" , post);
                if ( discussionTopic != null ) {
                    requestAddDiscussionPost(post);
                    holder.postEditTxt.setText("");
                }
            }
        });

        holder.photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void requestAddDiscussionPost(final String postTxt){
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussion/add";

        final Integer neighborhoodsId = BaseActivity.currentNeighborhood.getId() ;
        final Integer topicId = discussionTopic.getId() ;

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
                Boolean success = false ;
                if ( result != null ) {
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if(success){
                    /*
                    Log.i("post added" , "true");
                    DiscussionPostData post = new DiscussionPostData();
                    post.setTitle( discussionTopic.getTitleEnglish());
                    post.setDetails(postTxt);
                    mList.add(post);
                    notifyDataSetChanged();
                    */
                    ((Activity)context).finish();
                    ((Activity)context).startActivity(((Activity)context).getIntent());
                }
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
                params.put("title" , discussionTopic.getTitleEnglish());
                params.put("details" , postTxt);
                params.put("topic_id" , topicId.toString() );
                params.put("neighborhood_id" , neighborhoodsId.toString() );

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

    @Override
    public int getItemCount() {
        return 1;
    }

    static public class ViewHolder extends UltimateRecyclerviewViewHolder {

        EditText postEditTxt ;
        Spinner problemSpinner ;
        ImageView profilePic ;
        AppCompatButton photoBtn ;
        AppCompatButton postBtn ;
        Context context ;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            postEditTxt = (EditText) itemView.findViewById(R.id.post_edit_text);
            problemSpinner = (Spinner) itemView.findViewById(R.id.spinner);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
            photoBtn = (AppCompatButton) itemView.findViewById(R.id.photo_btn);
            postBtn = (AppCompatButton) itemView.findViewById(R.id.add_btn);
        }
    }
}
