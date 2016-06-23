package apps.gn4me.com.jeeran.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.ServicesList;
import apps.gn4me.com.jeeran.pojo.Service;
import apps.gn4me.com.jeeran.pojo.ServiceDetailsPojo;

/**
 * Created by acer on 5/29/2016.
 */
public class ServiceDetailsAdapter extends RecyclerView.Adapter<ServiceDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Service> serviceDetailsList;
    int curPos=0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceName, serviceRates;
        public ImageView serviceLogo, moreOptions;

        public MyViewHolder(View view) {
            super(view);
            serviceName= (TextView) view.findViewById(R.id.resName);
            serviceRates = (TextView) view.findViewById(R.id.rates);
            serviceLogo = (ImageView) view.findViewById(R.id.resImage);
            moreOptions = (ImageView) view.findViewById(R.id.more_options);
        }
    }


    public ServiceDetailsAdapter(Context mContext, List<Service> serviceDetailsList) {
        this.mContext = mContext;
        this.serviceDetailsList = serviceDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Service service = serviceDetailsList.get(position);
        holder.serviceName.setText(service.getName());
        holder.serviceRates.setText(service.getRates() + "*");
        Glide.with(mContext).load(service.getLogo()).into(holder.serviceLogo);
        holder.moreOptions.setTag(position);
        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.moreOptions);
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
        inflater.inflate(R.menu.menu_restaurant, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(mContext, serviceDetailsList.get(curPos).getName()+" "+serviceDetailsList.get(curPos).getServiceId(), Toast.LENGTH_SHORT).show();
                    favoriteService();
                    return true;

                default:
            }
            return false;
        }

        private void favoriteService() {
            String url ="http://jeeran.gn4me.com/jeeran_v1/serviceplacefavorite/add";

            final ProgressDialog pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(true);
             pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {

                        Toast.makeText(mContext,response,Toast.LENGTH_LONG).show();
                        JSONObject jsonObject=new JSONObject(response);
                       JSONObject resultObj=jsonObject.getJSONObject("result");
                        int errCode=resultObj.getInt("errorcode");
                        if(errCode==0){
                            Toast.makeText(mContext,serviceDetailsList.get(curPos).getName()+" added to your favorites",Toast.LENGTH_LONG).show();
                        }
                        else if(errCode==3){
                            Toast.makeText(mContext,serviceDetailsList.get(curPos).getName()+" already in your favorites",Toast.LENGTH_LONG).show();

                        }

                        }
                    catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    pDialog.dismiss();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG).show();
                    VolleyLog.d("********************", "Error: " + error.getMessage());
                    pDialog.hide();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("service_places_id", curPos+"");


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
    }



    @Override
    public int getItemCount() {
        return serviceDetailsList.size();
    }
}