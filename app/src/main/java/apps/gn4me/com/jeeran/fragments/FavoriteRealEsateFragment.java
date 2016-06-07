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

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.BaseActivity;
import apps.gn4me.com.jeeran.activity.HomeActivity;
import apps.gn4me.com.jeeran.activity.LoginActivity;
import apps.gn4me.com.jeeran.activity.SplashActivity;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RVAdapter;
import apps.gn4me.com.jeeran.pojo.RealEstate;


public  class FavoriteRealEsateFragment extends Fragment {
    int color;

    private List<RealEstate> realEstates;
    private RecyclerView.Adapter adapter;
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
        openDialog();
        view = inflater.inflate(R.layout.fragment_real_estate, container, false);


//        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
//        frameLayout.setBackgroundColor(color);

        rv = (RecyclerView)view.findViewById(R.id.rv);

//        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        initializeData();

        adapter = new RVAdapter(realEstates);
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
                                Log.i("Exception:: ", e.getMessage());
                            }
                            Boolean success = false ;
                            if ( result != null ) {
                                Log.i("All Result ::: " , result.toString());
                                success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                            }

                            if ( success ){
                                progressDialog.dismiss();
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