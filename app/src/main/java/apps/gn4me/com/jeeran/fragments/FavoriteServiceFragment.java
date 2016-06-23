package apps.gn4me.com.jeeran.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.marshalchen.ultimaterecyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.RealEstateDetails;
import apps.gn4me.com.jeeran.activity.ServiceDetails;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.ServiceFavoriteAdapter;
import apps.gn4me.com.jeeran.pojo.FavoriteRealEstate;
import apps.gn4me.com.jeeran.pojo.RealEstate;
import apps.gn4me.com.jeeran.pojo.Service;
import apps.gn4me.com.jeeran.pojo.ServiceFavorites;


public  class FavoriteServiceFragment extends Fragment {
    int color;

    private List<ServiceFavorites> services;
    private ServiceFavoriteAdapter adapter = new ServiceFavoriteAdapter();
    RecyclerView rv;
    View view;
    ImageView notFoundImg;
    LinearLayoutManager llm;
    ProgressDialog progressDialog;
    private ServiceFavorites mservice;
    private Service mservicePlaces;

    private void openDialog() {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }
    public FavoriteServiceFragment() {
    }

    @SuppressLint("ValidFragment")
    public FavoriteServiceFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_favorit, container, false);

//        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
//        frameLayout.setBackgroundColor(color);

        rv = (RecyclerView)view.findViewById(R.id.rv);

//        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        initializeData();
        notFoundImg = (ImageView)view.findViewById(R.id.notFoundImg);
//        adapter = new ServiceFavoriteAdapter(services);

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent i = new Intent(view.getContext(),ServiceDetails.class);
                        i.putExtra("realestateID",services.get(position).getFavoriteServiceId()+"");
                        i.putExtra("fromMyFavoritesServices","ok");
                        startActivity(i);
                    }
                })
        );


        rv.setAdapter(adapter);



        return view;
    }

    private void initializeData(){
//        realEstates = new ArrayList<>();
        openDialog();
        requestJsonObject();
//        realEstates.add(new RealEstate("Flat1","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstate("Flat2","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstate("Flat3","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
    }

    private void requestJsonObject() {
        String  tag_string_req = "string_req";
        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/serviceplacefavorite/list";

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
                getFavoriteData(result);
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
//                params.put("start", start.toString());
//                params.put("count",count.toString());

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

    private void getFavoriteData(JsonObject result) {
        services = new ArrayList<>();
        boolean success = false;

        if (result != null) {
            Log.i("All Result ::: ", result.toString());
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
            int msg = result.getAsJsonObject("result").getAsJsonPrimitive("errorcode").getAsInt();
            if(msg == 1)
                notFoundImg.setVisibility(View.VISIBLE);
//                favoriteLayout.setBackground(view.getResources().getDrawable(R.drawable.don_t_have));
        }

        if (success) {
            progressDialog.dismiss();

            JsonArray myFavoriteServices = result.getAsJsonObject("response").getAsJsonArray("serviceplaces");

//                                JSONObject realEstateAd = myRealEstates.getAsJsonObject("response").getAsJsonPrimitive("real_estate_ad");

            for (int i = 0; i < myFavoriteServices.size(); i++) {
                mservice = new ServiceFavorites();
                mservicePlaces = new Service();
                mservice.setFavoriteServiceId(myFavoriteServices.get(i).getAsJsonObject().getAsJsonPrimitive("favorite_service_place_id").getAsInt());
                mservice.setServiceId(myFavoriteServices.get(i).getAsJsonObject().getAsJsonPrimitive("service_place_id").getAsInt());
                mservice.setUserId(myFavoriteServices.get(i).getAsJsonObject().getAsJsonPrimitive("user_id").getAsInt());

                JsonObject myServices = myFavoriteServices.get(i).getAsJsonObject().getAsJsonObject("service_place");

                mservicePlaces.setServiceId(myServices.getAsJsonObject().getAsJsonPrimitive("service_place_id").getAsInt());
                mservicePlaces.setName(myServices.getAsJsonObject().getAsJsonPrimitive("title").getAsString()+"");
                mservicePlaces.setDiscription(myServices.getAsJsonObject().getAsJsonPrimitive("description").getAsString()+"");
                mservicePlaces.setAddress(myServices.getAsJsonObject().getAsJsonPrimitive("address").getAsString()+"");
//                mservicePlaces.setLang(myServices.getAsJsonObject().getAsJsonPrimitive("longitude").getAsDouble());
//                mservicePlaces.setLat(myServices.getAsJsonObject().getAsJsonPrimitive("latitude").getAsDouble());
                mservicePlaces.setLogo(myServices.getAsJsonObject().getAsJsonPrimitive("logo").getAsString()+"");
//                mservicePlaces.setPhone1(myServices.getAsJsonObject().getAsJsonPrimitive("mobile_1").getAsString()+"");
//                mservicePlaces.setPhone2(myServices.getAsJsonObject().getAsJsonPrimitive("mobile_2").getAsString()+"");
//                mservicePlaces.setPhone3(myServices.getAsJsonObject().getAsJsonPrimitive("mobile_3").getAsString()+"");
//                mservicePlaces.setRates(myServices.getAsJsonObject().getAsJsonPrimitive("total_rate").getAsLong());
//                mservicePlaces.setLogoImg(myServices.getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());

                mservice.setMyServices(mservicePlaces);
                services.add(mservice);

            }
            adapter.insertAll(services);

        } else {
            progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
//            Toast.makeText(view.getContext(), "Error in Loading.. please, check internet connection", Toast.LENGTH_LONG).show();
        }
    }
}