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
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.HomeActivity;
import apps.gn4me.com.jeeran.activity.LoginActivity;
import apps.gn4me.com.jeeran.activity.SplashActivity;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RVAdapter;
import apps.gn4me.com.jeeran.pojo.FavoriteRealEstate;
import apps.gn4me.com.jeeran.pojo.RealEstate;


public  class FavoriteRealEsateFragment extends Fragment {
    int color;

    FavoriteRealEstate mReal;
    RealEstate mRealEstate;
    private List<FavoriteRealEstate> realEstates;
    private RVAdapter adapter = new RVAdapter();
    RecyclerView rv;
    View view;
    LinearLayoutManager llm;
    ProgressDialog progressDialog;
    private String BASE_URL = "http://jeeran.gn4me.com/jeeran_v1";

    public FavoriteRealEsateFragment() {
    }
    private void openDialog() {
        progressDialog = new ProgressDialog(getContext(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

    }

    @SuppressLint("ValidFragment")
    public FavoriteRealEsateFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //openDialog();
        view = inflater.inflate(R.layout.fragment_real_estate, container, false);


//        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
//        frameLayout.setBackgroundColor(color);

        rv = (RecyclerView)view.findViewById(R.id.rv);

//        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        initializeData();


        rv.setAdapter(adapter);



        return view;
    }

//    private void openDialog() {
//
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Loading...");
//        progressDialog.show();
//
//    }

    private void initializeData(){
        realEstates = new ArrayList<>();


        SharedPreferences settings;
        String token ;
        settings = getContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
        token = settings.getString("token", null);
//        if ( token != null ) {
            Ion.with(view.getContext())
                    .load(BASE_URL + "/realstatefavorite/list")
                    .setHeader("Authorization",token)
                    .setBodyParameter("start", "0")
                    .setBodyParameter("count", "4")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // do stuff with the result or error
                            //showProgress(false);
                            if ( e != null ) {
//                                Log.i("Exception:: ", e.getMessage());
                            }
                            Boolean success = false ;
                            if ( result != null ) {
                                Log.i("All Result ::: " , result.toString());
                                success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                            }

                            if ( success ){
                                progressDialog.dismiss();

                                JsonArray myFavoriteRealEstates = result.getAsJsonObject("response").getAsJsonArray("realstate");

//                                JSONObject realEstateAd = myRealEstates.getAsJsonObject("response").getAsJsonPrimitive("real_estate_ad");

                                for (int i=0 ; i<myFavoriteRealEstates.size() ; i++) {
                                    mReal = new FavoriteRealEstate();
                                    mRealEstate = new RealEstate();

                                    mReal.setFavoriteRealEstateId(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("favorite_real_estate_ad_id").getAsInt());
                                    mReal.setRealEstateAdId(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("real_estate_ad_id").getAsInt());
                                    mReal.setUserId(myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("user_id").getAsInt());

                                    JsonObject myRealEstates = myFavoriteRealEstates.get(i).getAsJsonObject().getAsJsonObject("real_estate_ad");


                                    mRealEstate.setPhone(myRealEstates.getAsJsonObject().getAsJsonPrimitive("owner_mobile").getAsString());
                                    mRealEstate.setEmail(myRealEstates.getAsJsonObject().getAsJsonPrimitive("owner_email").getAsString());
                                    mRealEstate.setContactPerson(myRealEstates.getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
//                                    mReal.setCreationDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("created_at").getAsString());
//                                    mReal.setUpdateDate(myRealEstates.get(i).getAsJsonObject().getAsJsonPrimitive("owner_name").getAsString());
                                    mRealEstate.setTitle(myRealEstates.getAsJsonObject().getAsJsonPrimitive("title").getAsString());
//                                    mRealEstate.setAddress(myRealEstates.getAsJsonObject().getAsJsonPrimitive("address").getAsString());
                                    mRealEstate.setLocation(myRealEstates.getAsJsonObject().getAsJsonPrimitive("location").getAsString());
                                    mRealEstate.setType(myRealEstates.getAsJsonObject().getAsJsonPrimitive("type").getAsInt());
                                    mRealEstate.setNumOfRooms(myRealEstates.getAsJsonObject().getAsJsonPrimitive("number_of_rooms").getAsInt());
                                    mRealEstate.setNumOfBathreeoms(myRealEstates.getAsJsonObject().getAsJsonPrimitive("number_of_bathrooms").getAsInt());
                                    mRealEstate.setPrice(myRealEstates.getAsJsonObject().getAsJsonPrimitive("price").getAsInt());
                                    mRealEstate.setArea(myRealEstates.getAsJsonObject().getAsJsonPrimitive("area").getAsString());
//                                    mRealEstate.setLanguage(myRealEstates.getAsJsonObject().getAsJsonPrimitive("language").getAsInt());
//                                    mRealEstate.setLongitude(myRealEstates.getAsJsonObject().getAsJsonPrimitive("longitude").getAsDouble());
//                                    mRealEstate.setLatitude(myRealEstates.getAsJsonObject().getAsJsonPrimitive("latitude").getAsDouble());
                                    mRealEstate.setImg(myRealEstates.getAsJsonObject().getAsJsonPrimitive("cover_image").getAsString());
//
                                    mReal.setMyRealEstate(mRealEstate);
                                    realEstates.add(mReal);

//                                    Log.i("list /*/* / :" , mRealEstate.getTitle());
//                                    adapter.insertItem(mReal);

                                }

                                adapter.insertAll(realEstates);
                                Log.i("items */*/*/*/ :: ", realEstates.get(0).getMyRealEstate().getTitle() + " " +realEstates.get(1).getMyRealEstate().getTitle() + realEstates.get(2).getMyRealEstate().getTitle());

                            } else {
                                progressDialog.dismiss();
//                              Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
                                Toast.makeText(view.getContext(),"reading Failed",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
//        }else
//        {
//            Toast.makeText(getContext(),"you didn't registered..",Toast.LENGTH_LONG).show();
//        }



//        realEstates.add(new RealEstate("Flat1","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstate("Flat2","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
//        realEstates.add(new RealEstate("Flat3","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
    }
}