package apps.gn4me.com.jeeran.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.pojo.DiscussionCommentData;


public class CustomAdapter extends UltimateViewAdapter<CustomAdapter.SimpleAdapterViewHolder> {

    private List<DiscussionCommentData> mList;
    private Dialog dialog;
    private TextView okBtn,cancelBtn;
    private Context context ;
    private RadioGroup rg;
    private EditText complaintMsg ;


    public CustomAdapter(List<DiscussionCommentData> mList , Context context) {
        this.mList = mList;
        this.context = context ;
    }

    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, final int position) {
        Log.i("index here :::" , "" + mList.size());
        createDialog();

        holder.name.setText(mList.get(position).getUser().getUserName());
        holder.comment.setText(mList.get(position).getComment());

        holder.timeStamp.setText(mList.get(position).getTimeStamp());



        Picasso.with(holder.context)
                .load(mList.get(position).getUser().getImage())
                .error(R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into(holder.profilePic);

        if ( mList.get(position).getIsOwner() == 1 ){
            holder.toolbar = (Toolbar) holder.view.findViewById(R.id.card_toolbar);
            //toolbar.setTitle("Card Toolbar");
            if (holder.toolbar != null) {
                // inflate your menu
                holder.toolbar.inflateMenu(R.menu.owner_menu);
                holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        Log.i("Menu item id = " , "" + menuItem.getTitle() ) ;
                        if( menuItem.getTitle().equals("Edit") ) {

                        }else if( menuItem.getTitle().equals("Delete")){
                            requestDeteteDiscussionComment(holder.context,mList.get(position).getId());
                        }
                        return true;
                    }
                });
            }
        }else{
            if (holder.toolbar != null) {
                holder.toolbar = (Toolbar) holder.view.findViewById(R.id.card_toolbar);
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
                requestReportDiscussionComment(context,mList.get(position).getId(),rg.getCheckedRadioButtonId(),complaintMsg.getText().toString());
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void requestDeteteDiscussionComment(final Context context, final Integer discId){
        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/discussioncomments/delete";

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
                params.put("discussion_comment_id",discId.toString());
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

        int size = BaseActivity.reportReasons.size() ;
        RadioButton[] rb = new RadioButton[size];
        for(int i=0; i<size; i++){
            rb[i]  = new RadioButton(context);
            rb[i].setText(BaseActivity.reportReasons.get(i).getTitleEnglish());
            rb[i].setId(i+1);
            rg.addView(rb[i]);
        }
        rb[0].setChecked(true);
        complaintMsg = (EditText) dialog.findViewById(R.id.complaintMsg);
    }

    public void requestReportDiscussionComment(final Context context, final Integer discId , final Integer reasonId , final String msg){

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
                params.put("reported_type_id",BaseActivity.DISCUSSION_COMMENT_REPORT.toString());
                params.put("reported_id",discId.toString());
                if (!msg.isEmpty())
                    params.put("report_message",BaseActivity.DISCUSSION_REPORT.toString());
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
    public int getAdapterItemCount() {
        return mList.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list_item, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v);
        return vh;
    }


    public void insert(DiscussionCommentData comment, int position) {
        //insertInternal(mList, comment , position);
        notifyDataSetChanged();
    }

    public void insertAll(List<DiscussionCommentData> comments) {
        //insertInternal(mList, comment , position);
        mList.addAll(comments);
        notifyDataSetChanged();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    public void remove(int position) {
        removeInternal(mList, position);
    }

    public void clear() {
        clearInternal(mList);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    @Override
    public long generateHeaderId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        //TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
        //viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));

    }

    @Override
    public void onItemDismiss(int position) {
        if (position > 0)
            remove(position);
        //notifyItemRemoved(position);
        //notifyDataSetChanged();
        super.onItemDismiss(position);
    }


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView name;
        TextView timeStamp;
        ImageView profilePic;
        TextView comment ;
        Toolbar toolbar ;
        Context context;
        View view ;
        public SimpleAdapterViewHolder(View itemView) {
            super(itemView);
            view = itemView ;
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.name);
            timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            comment = (TextView) itemView.findViewById(R.id.comment);

            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);

            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }



}