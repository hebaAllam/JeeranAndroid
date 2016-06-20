package apps.gn4me.com.jeeran.adapters;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.MyServices;
import apps.gn4me.com.jeeran.pojo.Service;

/**
 * Created by acer on 5/28/2016.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder>{
    List<Service> ServicesList;
    Context context;
    int curPos=0;

    public ServiceAdapter(List<Service> ServicesList,Context context) {
        this.ServicesList = ServicesList;
        this.context=context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView servicelogo,serviceIcone;
        public TextView serviceName;

        public MyViewHolder(View view) {
            super(view);
            serviceName= (TextView) view.findViewById(R.id.superMarketName);
            servicelogo = (ImageView) view.findViewById(R.id.superMarketImage);
            serviceIcone=(ImageView)view.findViewById(R.id.serviceIcone);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_service, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Service service = ServicesList.get(position);
        holder.serviceName.setText(service.getName());
        Glide.with(context).load(service.getLogo()).into( holder.servicelogo);
        if(context instanceof  MyServices){
            holder.serviceIcone.setImageResource(R.drawable.ic_options_icon);
            holder.serviceIcone.setTag(position);
            holder.serviceIcone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(holder.serviceIcone);
                }
            });

        }

    }

    private void showPopupMenu(View view) {
        curPos=(Integer)view.getTag();
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.my_own_menu, popup.getMenu());
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
                case R.id.action_edit_service:
                    Toast.makeText(context, "edit", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_delete_service:
                    Toast.makeText(context, " delete", Toast.LENGTH_SHORT).show();
                    requestDeleteService();
                    return true;
                default:
            }
            return false;
        }

        private void requestDeleteService() {

            String url ="http://jeeran.gn4me.com/jeeran_v1/serviceplace/delete";

//            final ProgressDialog pDialog = new ProgressDialog(this);
//            pDialog.setMessage("Loading...");
            // pDialog.show();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject resultObj= jsonObject.getJSONObject("result");
                      if(resultObj.getInt("errorcode")==0){
                          Toast.makeText(context,"deleted...",Toast.LENGTH_LONG).show();

                      }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                  //  pDialog.dismiss();

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("service_place_id",curPos+"");


                    return params;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    SharedPreferences settings;
                    settings = context.getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                    String mtoken = settings.getString("token", null);
                    headers.put("Authorization", mtoken);
                    return headers;
                }

            };

// Adding request to request queue
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(strReq);

        }
    }
    @Override
    public int getItemCount() {
        return ServicesList.size();
    }


}
