package apps.gn4me.com.jeeran.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.FilePart;
import com.koushikdutta.async.http.body.Part;
import com.koushikdutta.ion.Ion;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import apps.gn4me.com.jeeran.R;

public class AddService extends BaseActivity {

    String imageEncoded;
    Toolbar toolbar;
    MaterialBetterSpinner CategorySpinner;
    MaterialBetterSpinner subCategorySpinner;
    ImageView serviceLogo, takeServiceImages;
    EditText serviceTitle, serviceDescription, serviceAddress, servicePhone, serviceOpeningHours;
    Button addServiceBut;
    String latitude = "";
    String longitude = "";
    private static final int PLACE_PICKER_REQUEST = 111;
    private static final int CAMERA_REQUEST = 222;
    private static final int CAMERA_REQUEST_FOR_LOGO = 333;

    private List<File> allImageFiles;
    private File logoUploadFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        String[] mainCategory = {"c1", "c2", "c3"};
        String[] subCategory = {"c1", "c2", "c3"};
        //-----------getViews---------------
        serviceLogo = (ImageView) findViewById(R.id.add_service_logo);
        takeServiceImages = (ImageView) findViewById(R.id.add_service_images);
        serviceTitle = (EditText) findViewById(R.id.add_service_title);
        serviceDescription = (EditText) findViewById(R.id.add_service_description);
        servicePhone = (EditText) findViewById(R.id.add_service_phone);
        serviceOpeningHours = (EditText) findViewById(R.id.add_service_openingHours);
        serviceAddress = (EditText) findViewById(R.id.add_service_location);
        addServiceBut = (Button) findViewById(R.id.addService_but);
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
                if (hasFocus) {
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                    try {
                        startActivityForResult(builder.build(AddService.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        serviceLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_REQUEST_FOR_LOGO);

            }
        });
        takeServiceImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAMERA_REQUEST);

            }
        });
        addServiceBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewServiceRequestByIon();
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
                Toast.makeText(AddService.this, response, Toast.LENGTH_LONG).show();
                pDialog.hide();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddService.this, error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("service_main_category_id", "4");
                params.put("title", serviceTitle.getText().toString());
                params.put("title", serviceAddress.getText().toString());
                params.put("mobile_1", servicePhone.getText().toString());
                params.put("neighbarhood_id", "1");
                params.put("Service_place_id", "6");
                params.put("opening_hours", serviceOpeningHours.getText().toString());
                params.put("latitude", latitude);
                params.put("longitude", longitude);


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

    private void addNewServiceRequestByIon() {
        List<Part> files = new ArrayList();
        for (int i = 0; i < allImageFiles.size(); i++) {
            files.add(new FilePart("UploadForm[" + i + "]", allImageFiles.get(i)));
        }

        Ion.with(AddService.this)
                .load("http://jeeran.gn4me.com/jeeran_v1/serviceplace/add")
                .setHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjIwLCJpc3MiOiJodHRwOlwvXC9qZWVyYW4uZ240bWUuY29tXC9qZWVyYW5fdjFcL3VzZXJcL2xvZ2luIiwiaWF0IjoxNDY2MDE2NTk4LCJleHAiOjE0NjYwMjAxOTgsIm5iZiI6MTQ2NjAxNjU5OCwianRpIjoiODQyZDJkMTJkYzFmYzgzYTUwOWE2OTUzMzgzOTQ2ODgifQ.LlPyzFqc3WZD_KVHF4XLbDL2RBxmVpElhRIAwUr5oGg")
                .setMultipartParameter("service_main_category_id", "5")
                .setMultipartParameter("service_sub _category_id", "7")
                .setMultipartParameter("title", serviceTitle.getText().toString())
                .setMultipartParameter("description", serviceDescription.getText().toString())
           //     .addMultipartParts(files)
                .setMultipartParameter("mobile1", servicePhone.getText().toString())
                .setMultipartFile("logo", logoUploadFile)
                .setMultipartParameter("neighbarhood_id", "1")
                .setMultipartParameter("opening_hours", serviceOpeningHours.getText().toString())
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (result != null) {
                            Toast.makeText(AddService.this, result, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(AddService.this, "result is null", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == CAMERA_REQUEST_FOR_LOGO && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri mImageUri = data.getData();
                logoUploadFile = new File(mImageUri.getPath());

            }
        }
        try

        {
            // When an Image is picked
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                //  imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        allImageFiles = new ArrayList<>();
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            File fileToUpload = new File(mArrayUri.get(i).getPath());
                            allImageFiles.add(fileToUpload);

                        }

                        Log.i("Selected Images :::::: ", mArrayUri.size() + "" + mArrayUri.get(0).getPath() + " " + mArrayUri.get(1).getPath());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }//onActivityResult
}
