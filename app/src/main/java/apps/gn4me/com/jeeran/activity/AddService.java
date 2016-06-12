package apps.gn4me.com.jeeran.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
public class AddService extends BaseActivity
        {

            private static final String TAG="++++++++++";
            Toolbar toolbar;
            MaterialBetterSpinner CategorySpinner;
            MaterialBetterSpinner subCategorySpinner;
            ImageView serviceLogo,takeServiceImages;
            EditText serviceTitle,serviceDescription,serviceAddress,servicePhone,serviceOpeningHours;
            String latitude="";
            String longitude="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        String[] mainCategory={"c1","c2","c3"};
        String[] subCategory={"c1","c2","c3"};
        //-----------getViews---------------
         serviceLogo=(ImageView)findViewById(R.id.add_service_logo);
        takeServiceImages=(ImageView)findViewById(R.id.add_service_images);
        serviceTitle=(EditText)findViewById(R.id.add_service_title);
        serviceDescription=(EditText)findViewById(R.id.add_service_description);
        servicePhone=(EditText)findViewById(R.id.add_service_phone);
        serviceOpeningHours=(EditText)findViewById(R.id.add_service_openingHours);
        serviceAddress=(EditText)findViewById(R.id.add_service_location) ;
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //------set tool Bar----------------------------
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("Add Service");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //for category spinner
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, mainCategory);
        CategorySpinner = (MaterialBetterSpinner)
                findViewById(R.id.choose_service_category);
        CategorySpinner.setAdapter(arrayAdapter);

        //for subcategory spinner
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, subCategory);
        subCategorySpinner = (MaterialBetterSpinner)
                findViewById(R.id.choose_service_subCategory);
        subCategorySpinner.setAdapter(arrayAdapter2);


//listeners
        serviceAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Intent i=new Intent(AddService.this,ShowServiceLocation.class);
                    startActivityForResult(i,1);
                }
            }
        });
        serviceLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        takeServiceImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

            private void addNewServiceRequest() {
                String url = "http://jeeran.gn4me.com/jeeran_v1/serviceplace/add";
                final ProgressDialog pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading...");
                  pDialog.show();

                StringRequest strReq = new StringRequest(Request.Method.POST,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AddService.this,response,Toast.LENGTH_LONG).show();
                        pDialog.hide();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddService.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        pDialog.hide();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("service_main_category_id", "4");
                        params.put("service_sub _category_id", "4");
                        params.put("service_sub _category_id", "4");
                        params.put("title",serviceTitle.getText().toString());
                        params.put("title",serviceAddress.getText().toString());
                        params.put("mobile_1",servicePhone.getText().toString());
                        params.put("neighbarhood_id","1");
                        params.put("Service_place_id","6");
                        params.put("opening_hours",serviceOpeningHours.getText().toString());
                        params.put("latitude",latitude);
                        params.put("longitude",longitude);


                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY1NzUzMzIwLCJleHAiOjE0NjU3NTY5MjAsIm5iZiI6MTQ2NTc1MzMyMCwianRpIjoiOTM4NmQ3MGFiZjJmOTk4MDhkYjkyZTU4M2QyMzEwZmEifQ.quYU3Qjfi-LO0LUnq1ADum_qcWZEnDNJrmLPOYUxzfU");
                        return headers;
                    }

                };

// Adding request to request queue
                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(strReq);
            }
            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {

                if (requestCode == 1) {
                    if(resultCode == Activity.RESULT_OK){
                         latitude=data.getStringExtra("lat");
                         longitude=data.getStringExtra("long");
                        serviceAddress.setText(data.getExtras().getString("address"));
                    }
                    if (resultCode == Activity.RESULT_CANCELED) {
                        //Write your code if there's no result
                    }
                }
            }//onActivityResult
}
