package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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
import com.daimajia.slider.library.SliderLayout;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.marshalchen.ultimaterecyclerview.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RVAdapter;
import apps.gn4me.com.jeeran.adapters.RealEstateAdapter;
import apps.gn4me.com.jeeran.pojo.FavoriteRealEstate;
import apps.gn4me.com.jeeran.pojo.RealEstate;

public class MyRealEstates extends AppCompatActivity {

    private List<apps.gn4me.com.jeeran.pojo.RealEstate> realEstates;
    apps.gn4me.com.jeeran.pojo.RealEstate mReal;

    private RealEstateAdapter adapter = new RealEstateAdapter();
    RecyclerView rv;
//    View view;
    LinearLayoutManager llm;
    ProgressDialog progressDialog;
    private String BASE_URL = "http://jeeran.gn4me.com/jeeran_v1";
    FrameLayout favoriteLayout;

    ImageView notFoundImg;

    private void openDialog() {
        progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_real_estates);

        rv = (RecyclerView)findViewById(R.id.rv);

//        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        openDialog();
        initializeData();
        favoriteLayout = (FrameLayout)findViewById(R.id.myFavoriteLayout);

        notFoundImg = (ImageView)findViewById(R.id.notFoundImg);

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
//                        Log.i("item ::: ", realEstates.get(position).getCreationDate());//null
//                        Log.i("item ::: ", realEstates.get(position).getMyRealEstate().getPrice()+ "");
//                        Log.i("item ::: ", realEstates.get(position).getMyRealEstate().getDescription()+"");
//                        Log.i("item ::: ", realEstates.get(position).getNumOfRooms()+"");

                        Intent i = new Intent(MyRealEstates.this,RealEstateDetails.class);
                        i.putExtra("realestateID",realEstates.get(position).getId()+"");
                        i.putExtra("activityType","MyRealEstate");

                        startActivity(i);
                    }
                })
        );




        rv.setAdapter(adapter);
    }


    private void initializeData(){

        requestJsonObject(0 , 5);
    }

    private void requestJsonObject(final Integer start , final Integer count) {
        String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstate/list";

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
                getRealEstateData(result);
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
                params.put("type","");
                params.put("user","1");
                params.put("order","");
//            params.put("start", start.toString());
//            params.put("count",count.toString());


                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }

//    private void sendDataToDetails(RealEstate realEstate){
////        Intent i = new Intent(RealEstateActivty.class, RealEstateDetails.class);
//
//    }

    private void getRealEstateData(JsonObject result) {

        realEstates = new ArrayList<>();
        Boolean success = false;
        if (result != null) {
            Log.i("All Result ::: ", result.toString());
            success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
        }

        Log.i("success :::::: ", success.toString());

        if (success) {
            progressDialog.dismiss();

            JsonArray myRealEstates = result.getAsJsonArray("response");
            if (myRealEstates != null) {
//                                JsonObject realEstateAd = myRealEstates.getAsJsonObject();

                for (int i = 0; i < myRealEstates.size(); i++) {
                    mReal = new apps.gn4me.com.jeeran.pojo.RealEstate();

                    mReal.setId(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("real_estate_ad_id").getAsInt());
                    //mReal.setPhone(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_mobile").getAsString());
                    //mReal.setEmail(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_email").getAsString());
                    mReal.setContactPerson(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
                    mReal.setFav(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("is_fav").getAsBoolean());
                    mReal.setCreationDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
                    mReal.setUpdateDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
                    mReal.setTitle(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("title").getAsString());
//                    mReal.setAddress(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("address").getAsString());
                    mReal.setLocation(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("location").getAsString());
                    mReal.setType(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("type").getAsInt());
                    mReal.setNumOfRooms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_rooms").getAsInt());
                    mReal.setNumOfBathreeoms(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("number_of_bathrooms").getAsInt());
                    mReal.setPrice(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("price").getAsInt());
                    mReal.setArea(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("area").getAsString());
                    mReal.setArea(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("description").getAsString());
//                    mReal.setLanguage(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("language").getAsInt());
//                    mReal.setLongitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("longitude").getAsDouble());
//                    mReal.setLatitude(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("latitude").getAsDouble());
                    mReal.setImg(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
//

                    realEstates.add(mReal);

//                                    Log.i("list /*/* / :" , mRealEstate.getTitle());
//                                    adapter.insertItem(mReal)
                }
            } else
                Toast.makeText(this, "Null Data", Toast.LENGTH_LONG).show();

            adapter.insertAll(realEstates);
//                            Log.i("items */*/*/*/ :: ", realEstates.get(0).getMyRealEstate().getTitle() + " " +realEstates.get(1).getMyRealEstate().getTitle() + realEstates.get(2).getMyRealEstate().getTitle());
//                            progressDialog.dismiss();
        } else {
            progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
            Toast.makeText(getApplicationContext(), "reading Failed", Toast.LENGTH_LONG).show();
        }
    }

}
