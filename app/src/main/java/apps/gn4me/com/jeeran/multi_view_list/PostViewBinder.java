package apps.gn4me.com.jeeran.multi_view_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.CommentsActivity;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;

/**
 * Created by cym on 15/5/18.
 */
public class PostViewBinder extends DataBinder<PostViewBinder.ViewHolder> {

    private List<DiscussionPostData> mList = new ArrayList<>();
    private int startIndex ;
    private Dialog dialog;
    private TextView okBtn,cancelBtn;
    private Context context ;
    private RadioGroup rg;
    private EditText complaintMsg ;

    public PostViewBinder(UltimateDifferentViewTypeAdapter dataBindAdapter , int startIndex , Context context) {
        super(dataBindAdapter);
        this.context = context ;
        this.startIndex = startIndex ;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.post_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, final int position) {

        final int index = position - startIndex ;
        createDialog();

        ///editText when loss focus hide keyboard

        ///////////////////////////////////////
        Log.i("Titleeeeeeee2" , mList.get(index).getTitle());

        holder.name.setText(mList.get(index).getUser().getUserName());
        holder.title.setText(mList.get(index).getTitle());

        holder.details.setText(mList.get(index).getDetails());
        holder.timeStamp.setText(mList.get(index).getTimeStamp());


        Picasso.with( holder.context )
                .load( mList.get(index).getUser().getImage() )
                .error(R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into(holder.profilePic);


        if ( mList.get(index).getImage() != null && !mList.get(index).getImage().isEmpty()) {
            Picasso.with(holder.context)
                    .load(mList.get(index).getImage())
                    .error(R.drawable.ic_error)
                    .placeholder(R.drawable.progress_animation)
                    .into(holder.feedImageView);
        }

        if(mList.get(index).getIsFav() == 1 || (mList.get(index).getIsFav() == null && startIndex == 0) ){
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.ic_favorite_icon_select);
            holder.favorite.setCompoundDrawablesWithIntrinsicBounds(myDrawable,null,null,null);
        }

        holder.toolbar = (Toolbar) holder.view.findViewById(R.id.card_toolbar);
        holder.toolbar.getMenu().clear();
        if ( mList.get(index).getIsOwner() == 1 ){
            //toolbar.setTitle("Card Toolbar");
            if (holder.toolbar != null) {
                // inflate your menu
                holder.toolbar.inflateMenu(R.menu.owner_menu);
                holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.i("Menu item id = " , "" + menuItem.getTitle() );
                        if( menuItem.getTitle().equals("Edit") ) {
                            holder.details.setVisibility(View.GONE);
                            holder.editLayout.setVisibility(View.VISIBLE);
                            holder.editDetails.setText(mList.get(index).getDetails().toString());
                        }else if( menuItem.getTitle().equals("Delete")){
                            requestDeleteDiscussion(holder.context,mList.get(index).getId());
                        }
                        return true;
                    }
                });
            }
        }else{
            if (holder.toolbar != null) {
                holder.toolbar.inflateMenu(R.menu.other_menu);
                holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getTitle().equals("Report")){
                            dialog.show();
                        }
                        return true;
                    }
                });
            }
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReportDiscussion(context,mList.get(index).getId(),rg.getCheckedRadioButtonId(),complaintMsg.getText().toString());
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final Context context = holder.context ;
        final Integer disc_id = mList.get(index).getId() ;
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CardView :::" , "comment clicked of card no. " + index);
                Intent i = new Intent( context , CommentsActivity.class);
                i.putExtra("disc_id" , disc_id.toString());
                context.startActivity(i);

            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( startIndex == 0 ) {
                    Log.i("Fav :::::::::: Id Del::",mList.get(index).getFavoriteId()+"");
                    requestDeleteFavoriteDiscussion(holder.context, mList.get(index).getId());
                }else if (startIndex == 1 && mList.get(index).getIsFav() == 0){
                    requestAddFavoriteDiscussion(holder.context,mList.get(index).getId());
                    Drawable myDrawable = context.getResources().getDrawable(R.drawable.ic_favorite_icon_select);
                    holder.favorite.setCompoundDrawablesWithIntrinsicBounds(myDrawable,null,null,null);
                }
            }
        });

        holder.doneEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.details.setVisibility(View.VISIBLE);
                holder.editLayout.setVisibility(View.GONE);
                String newPost = holder.editDetails.getText().toString();
                if ( !newPost.isEmpty() ) {
                    mList.get(index).setDetails(newPost);
                    notifyDataSetChanged();
                    requestEditPost(mList.get(index));
                }
                holder.details.setText(newPost);//mList.get(index).getDetails().toString());
            }
        });

        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.details.setVisibility(View.VISIBLE);
                holder.editLayout.setVisibility(View.GONE);
                holder.details.setText(mList.get(index).getDetails().toString());
            }
        });

    }

    private void createDialog()
    {
        dialog = new Dialog(context);

        //SET TITLE
        dialog.setTitle("Report");

        //set content
        dialog.setContentView(R.layout.dialog_custom_layout);

        okBtn= (TextView) dialog.findViewById(R.id.okTxt);
        cancelBtn= (TextView) dialog.findViewById(R.id.cancelTxt);
        rg = (RadioGroup) dialog.findViewById(R.id.radioReasoons);
        rg.removeAllViews();
        int size = BaseActivity.reportReasons.size();
        RadioButton[] rb = new RadioButton[size];
        for(int i=0; i<size; i++){
            rb[i]  = new RadioButton(context);
            rb[i].setText(BaseActivity.reportReasons.get(i).getTitleEnglish());
            rb[i].setId(i+1);
            rg.addView(rb[i]);
        }
        if ( size > 0 )
            rb[0].setChecked(true);
        complaintMsg = (EditText) dialog.findViewById(R.id.complaintMsg);
    }

    private void requestEditPost(final DiscussionPostData newPost ){
        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussion/edit";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                boolean success = false ;

                if (result != null ){
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if ( success ) {
                    Log.i("Discussion :::", result.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG , "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("discussion_id",newPost.getId().toString());
                params.put("title",newPost.getTitle());
                params.put("details",newPost.getDetails());
                params.put("topic_id",newPost.getTopic().getId().toString());
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



    public void requestReportDiscussion(final Context context, final Integer discId , final Integer reasonId , final String msg){

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/report/add";
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
                boolean success = false ;

                if (result != null ){
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if ( success ) {
                    Log.i("Report Discussion :::", result.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG , "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("report_reason_id",reasonId.toString());
                params.put("reported_type_id",BaseActivity.DISCUSSION_REPORT.toString());
                params.put("reported_id",discId.toString());
                if (!msg.isEmpty())
                    params.put("report_message",msg);
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

    public void requestDeleteDiscussion(final Context context, final Integer discId){

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussion/delete";

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
                boolean success = false ;

                if (result != null ){
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if ( success ) {
                    Log.i("Discussion :::", result.toString());
                    ((Activity)context).finish();
                    ((Activity)context).startActivity(((Activity)context).getIntent());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG , "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("discussion_id",discId.toString());
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


    public void requestDeleteFavoriteDiscussion(final Context context, final Integer discId){

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussionfavorite/delete";

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
                boolean success = false ;

                if (result != null ){
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if ( success ) {
                    Log.i("Favorite :::", result.toString());
                    if ( startIndex == 0 ) {
                        ((Activity) context).finish();
                        ((Activity) context).startActivity(((Activity) context).getIntent());
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG , "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("disc_id",discId.toString());
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

    public void requestAddFavoriteDiscussion(final Context context, final Integer discId){

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussionfavorite/add";

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
                boolean success = false ;

                if (result != null ){
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if ( success ) {
                    Log.i("Favorite :::", result.toString());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG , "Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("disc_id",discId.toString());
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
        return mList.size() ;
    }

    static public class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView name;
        TextView timeStamp;
        TextView title;
        TextView details;
        ImageView profilePic;
        ImageView feedImageView;
        AppCompatButton comment ;
        AppCompatButton favorite ;
        LinearLayout editLayout;
        EditText editDetails ;
        AppCompatButton cancelBtn ;
        AppCompatButton doneEditBtn ;
        Toolbar toolbar ;
        Context context ;
        View view ;
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView ;
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.name);
            timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            title = (TextView) itemView.findViewById(R.id.title);
            details = (TextView) itemView.findViewById(R.id.details);

            editDetails = (EditText) itemView.findViewById(R.id.editDetails);
            editLayout = (LinearLayout) itemView.findViewById(R.id.editPostLayout);
            cancelBtn = (AppCompatButton) itemView.findViewById(R.id.cancelBtn);
            doneEditBtn = (AppCompatButton) itemView.findViewById(R.id.doneBtn);


            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);


            comment = (AppCompatButton) itemView.findViewById(R.id.commentBtn);
            favorite = (AppCompatButton) itemView.findViewById(R.id.favoriteBtn);
            //report = (AppCompatButton) itemView.findViewById(R.id.reportBtn);

            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
        }
    }
    public void addAll(List<DiscussionPostData> dataSet) {
        mList.addAll(dataSet);
        notifyBinderDataSetChanged();
    }

    public void updateList() {
        notifyBinderDataSetChanged();
    }

    public void insert(DiscussionPostData post, int position) {
        mList.add(post);
        notifyBinderDataSetChanged();
    }

    public void remove(int position) {
        mList.remove(position);
        notifyBinderDataSetChanged();
    }

}
