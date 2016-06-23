package apps.gn4me.com.jeeran.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.RealEstateComments;
import apps.gn4me.com.jeeran.pojo.RealEstateCommentPojo;
import apps.gn4me.com.jeeran.pojo.UserReview;

/**
 * Created by ESCA on 6/12/2016.
 */
public class RealEstateCommentsAdapter extends RecyclerView.Adapter<RealEstateCommentsAdapter.MyRealEstateViewHolder> {

private Context mContext;
private List<RealEstateCommentPojo> reviewsList;
    private String myData= "";
    int size = 0;

    public void insertItem(RealEstateCommentPojo mReal) {
        size++;
        reviewsList.add(mReal);
        notifyDataSetChanged();
    }

    public class MyRealEstateViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userReview, reviewDate;
        public ImageView userImage, moreReviewOptions;
        public EditText commentEdit;

        public MyRealEstateViewHolder(View view) {
            super(view);
            userName = (TextView) view.findViewById(R.id.userName);
            userReview = (TextView) view.findViewById(R.id.userReview);
            reviewDate = (TextView) view.findViewById(R.id.reviewDate);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            moreReviewOptions = (ImageView) view.findViewById(R.id.optionsInReviews);
            commentEdit = (EditText)view.findViewById(R.id.userReviewEdit);
//        moreReviewOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(v);
//            }
//        });
        }
    }
    public void insertAll(List<RealEstateCommentPojo> real){
        reviewsList.addAll(real);
//        Log.i("inside adapter -*-*-* ", mRealEstates.get(0).getMyRealEstate().getTitle() + " " + mRealEstates.get(1).getMyRealEstate().getTitle()+ " " + mRealEstates.get(2).getMyRealEstate().getTitle() );
        size = real.size();
        notifyDataSetChanged();
    }


    public RealEstateCommentsAdapter(Context mContext, List<RealEstateCommentPojo> reviewsList) {
        this.mContext = mContext;
        this.reviewsList = reviewsList;
    }

    @Override
    public MyRealEstateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_comment, parent, false);

        return new MyRealEstateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyRealEstateViewHolder holder, final int position) {
        RealEstateCommentPojo userReview = reviewsList.get(position);
        holder.userName.setText(userReview.getUsername());
        holder.reviewDate.setText(userReview.getCommentDate());
        holder.userReview.setText(userReview.getUserComment());
//        Glide.with(mContext).load(userReview.getUserImage()).into(holder.userImage);

        if(reviewsList.get(position).getIsOwner() == 1)
            holder.moreReviewOptions.setVisibility(View.VISIBLE);
        else
            holder.moreReviewOptions.setVisibility(View.INVISIBLE);

        holder.moreReviewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.real_estate_menu_options, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.editRealEstate:
                                Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
                                openDialog(reviewsList.get(position));
//                                holder.commentEdit.setVisibility(View.VISIBLE);
//                                holder.userReview.setVisibility(View.INVISIBLE);
//                                holder.commentEdit.setText(holder.userReview.getText().toString());
//                                editComment(reviewsList.get(position));
                                return true;
                            case R.id.deleteRealEstate:
                                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                                deleteComment(reviewsList.get(position));
                                return true;
                            default:
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    private void openDialog(final RealEstateCommentPojo mReal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Edit");

// Set up the input
        final EditText input = new EditText(mContext);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
//        input.setBackgroundColor(mContext.getColor(R.color.grey_200));
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myData = input.getText().toString();
                requestObjectForEdit(mReal,myData);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void deleteComment(RealEstateCommentPojo mReal) {
        requestObject(mReal);
    }

//    private void editComment(RealEstateCommentPojo mReal) {
//        requestObjectForEdit(mReal);
//    }

    private void requestObjectForEdit(final RealEstateCommentPojo mReal, final String data) {
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstatecomments/edit";

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
                Log.i("result from edit :: ",result.toString());
                if(success) {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                    ((Activity)mContext).finish();
                    ((Activity)mContext).startActivity(((Activity)mContext).getIntent());
                }else
                    Toast.makeText(mContext,"Error",Toast.LENGTH_LONG).show();
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
                params.put("realstate_id", RealEstateComments.realestateId);
                params.put("comment_id",mReal.getRealEstateAdCommentId()+"");
                params.put("comment",data+"");
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = mContext.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(strReq);
    }

    private void requestObject(final RealEstateCommentPojo mReal){
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstatecomments/delete";

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
                Log.i("result from delete :: ",result.toString());
                if(success) {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                    ((Activity)mContext).finish();
                    ((Activity)mContext).startActivity(((Activity)mContext).getIntent());
                }else
                    Toast.makeText(mContext,"Error",Toast.LENGTH_LONG).show();
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
                params.put("realstate_id", RealEstateComments.realestateId);
                params.put("comment_id",mReal.getRealEstateAdCommentId()+"");

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = mContext.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(strReq);
    }


//    private void addComment() {
//        new RealEstateComments().addComment();
//    }

//    private int getListItem(View view){
//        parentRow = (View) view.getParent();
//        rlistView = (RelativeLayout) parentRow.getParent();
//        card=(CardView) rlistView.getParent();
//        list=(RecyclerView) card.getParent();
//        position = list.getChildAdapterPosition(card);

//        return position;
//    }

    @Override
    public int getItemCount() {
        return size;
    }
}
