package apps.gn4me.com.jeeran.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.Reviews;
import apps.gn4me.com.jeeran.pojo.UserReview;

/**
 * Created by acer on 5/30/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    int curPos;

    private Context mContext;
    private List<UserReview> reviewsList;
    private Dialog dialog;
    private TextView okBtn,cancelBtn;
    private EditText complaintMsg ;
    private RadioGroup rg;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userReview,reviewDate;
        public ImageView userImage,moreReviewOptions;
        public RatingBar rateBar;

        public MyViewHolder(View view) {
            super(view);
            userName= (TextView) view.findViewById(R.id.userName);
            userReview = (TextView) view.findViewById(R.id.userReview);
            reviewDate = (TextView) view.findViewById(R.id.reviewDate);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            rateBar=(RatingBar)view.findViewById(R.id.reviewrates);
            moreReviewOptions = (ImageView) view.findViewById(R.id.optionsInReviews);
        }
    }


    public ReviewAdapter(Context mContext, List<UserReview> reviewsList) {
        this.mContext = mContext;
        this.reviewsList = reviewsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        createDialog();
        UserReview userReview = reviewsList.get(position);
        holder.userName.setText(userReview.getUser().getUserName());
        holder.reviewDate.setText(userReview.getReviewDate());
        holder.userReview.setText(userReview.getReviewContent());
        holder.rateBar.setRating(userReview.getNumberOfRates());
        holder.moreReviewOptions.setTag(position);
        holder.moreReviewOptions.setTag(position);

        Glide.with(mContext).load(userReview.getUser().getImage()).into(holder.userImage);
        final int isOwner=reviewsList.get(position).getIsOwner();
        holder.moreReviewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOwner==1){
                showPopupMenu(holder.moreReviewOptions);}
                else {

                    showPopupMenuReport(holder.moreReviewOptions);
                }
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestReportReview(mContext,reviewsList.get(position).getReviewID(),rg.getCheckedRadioButtonId(),complaintMsg.getText().toString());
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

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        curPos=(Integer)view.getTag();
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.my_own_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    private void showPopupMenuReport(View view) {
        // inflate menu
        curPos=(Integer)view.getTag();
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.report_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemReportClickListener());
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_delete_service:
                    requestDeleteReview();
                    return true;
                case R.id.action_edit_service:
                    creatEditReviewDialog();
                    return true;
                default:
            }
            return false;

        }

        private void creatEditReviewDialog() {
            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.custom_review_dialog);
            dialog.setTitle("Write Review");

            Button dialogButtonCancel = (Button) dialog.findViewById(R.id.customDialogCancel);
            Button dialogButtonOk = (Button) dialog.findViewById(R.id.customDialogOk);
            final EditText reviewText=(EditText)dialog.findViewById(R.id.review_text);
            RatingBar reviewRates=(RatingBar)dialog.findViewById(R.id.reviewRatingBar) ;
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
                    Toast.makeText(mContext, "in action ", Toast.LENGTH_SHORT).show();


                    txtRates.setText(String.valueOf(rating));

                }
            });
            dialogButtonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String review=reviewText.getText().toString();

                    if(!review.equals("")){
                        // Toast.makeText(Reviews.this,"you success add review with rates "+txtRates.getText().toString(),Toast.LENGTH_LONG).show();
                        requestEditReview();
                        dialog.dismiss();

                    }

                }

                private void requestEditReview() {


                    String url = "http://jeeran.gn4me.com/jeeran_v1/servicereview/edit";

                    final ProgressDialog pDialog = new ProgressDialog(mContext);
                    pDialog.setMessage("Loading...");
                    pDialog.setCancelable(true);
                    pDialog.show();

                    StringRequest strReq = new StringRequest(Request.Method.POST,
                            url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {


                            pDialog.hide();

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            pDialog.hide();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("service_place_review_id", reviewsList.get(curPos).getReviewID()+"");
                            params.put("review",reviewText.getText().toString());
                            params.put("rating",txtRates.getText().toString());


                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            SharedPreferences settings;
                            settings = mContext.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                            String mtoken = settings.getString("token", null);
                            headers.put("Authorization", mtoken);
                            return headers;
                        }
                    };

// Adding request to request queue
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(strReq);
                }

            });

            dialog.show();
        }


    }
    class MyMenuItemReportClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemReportClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_reportReview:
                    dialog.show();
                    return true;
                default:
            }
            return false;
        }
    }



    @Override
    public int getItemCount() {
        return reviewsList.size();
    }


    private void requestDeleteReview() {
        String url = "http://jeeran.gn4me.com/jeeran_v1/servicereview/delete";

        final ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(true);
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                   JSONObject result= jsonObject.getJSONObject("result");

                    if(result.getInt("errorcode")==0){
                        Toast.makeText(mContext,"Deleted ",Toast.LENGTH_LONG).show();
                        reviewsList.remove(curPos);

                    }
                    else
                    {
                        Toast.makeText(mContext,"deleted error "+response,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                pDialog.hide();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_place_review_id", reviewsList.get(curPos).getReviewID()+"");

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                settings = mContext.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                String mtoken = settings.getString("token", null);
                headers.put("Authorization", mtoken);
                return headers;
            }
        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(strReq);
    }






    private void createDialog()
    {
        dialog = new Dialog(mContext);

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
            rb[i]  = new RadioButton(mContext);
            rb[i].setText(BaseActivity.reportReasons.get(i).getTitleEnglish());
            rb[i].setId(i+1);
            rg.addView(rb[i]);
        }
        if ( size > 0 )
            rb[0].setChecked(true);
        complaintMsg = (EditText) dialog.findViewById(R.id.complaintMsg);
    }

    public void requestReportReview(final Context context, final Integer discId , final Integer reasonId , final String msg){

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/report/add";

        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                boolean success = false ;

                if (result != null ){
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }
                if ( success ) {
                   Toast.makeText(mContext,"report sucess",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG , "Error: " + error.getMessage());
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("report_reason_id",reasonId.toString());
                params.put("reported_type_id",BaseActivity.SERVICE_PLACE_REVIEW_REPORT.toString());
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

}
