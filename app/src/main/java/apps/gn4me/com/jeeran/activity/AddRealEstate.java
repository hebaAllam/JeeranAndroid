package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;

public class AddRealEstate extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    private Uri outputFileUri;
    EditText title, location, description, address, contactPerson, phone, email, numOfRooms, numOfBathrooms, area, price, type;
    Button save;
    RealEstate realEstate;
    ProgressDialog progressDialog;
    private static final   int PLACE_PICKER_REQUEST = 111;
    String typee;
    EditText loc;

    //multiple imgs
    int PICK_IMAGE_MULTIPLE = 322;
    String imageEncoded;
    List<String> imagesEncodedList;


    private int PICK_IMAGE_REQUEST = 1;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_real_estate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.arrow_icon);
//        getSupportActionBar().setWindowTitle("Add Real Estate");
//        toolbar.setNavigationIcon(R.drawable.arrow_icon);


//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                What to do on back clicked
//                finish();
//                onBackPressed();
//            }
//        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Snackbar.make(getac, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        bindComponents();

        loc = (EditText)findViewById(R.id.location_addRealEstateActivity);

        loc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                    try {
                        startActivityForResult(builder.build(AddRealEstate.this), PLACE_PICKER_REQUEST);
                    } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                    } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleIsEmpty(title.getText().toString()) && descriptionIsEmpty(description.getText().toString())) {
                    openDialog();
                    sendRealEstateData(realEstate);

                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendRealEstateData(RealEstate myRealEstate) {

//        final Context context = getContext();
        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstate/add";

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
                Log.i("ADD REAL ESTATE",result.toString());
                Toast.makeText(getApplicationContext(),"result:::: : " + result.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "ADD REAL ESTATE Error: " + error.getMessage());
                //pDialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", "Amazing Flat");
                params.put("description", "desc");
                params.put("location", "Maadi Star Towers");
//                params.put("images","");
//                params.put("address","1A, Maadi Star Towers, Corniche El Maadi, Floor #22, Flat #1, Nile Corniche");
                typee = type.getText().toString();
                if(typee.equals("rent")) {
                    params.put("type", "0");
                }else {
                    params.put("type", "1");
                }
                params.put("number_of_rooms", "2");
                params.put("number_of_bathrooms", "1");
                params.put("price", "20101");
                params.put("longitude", "36");
                params.put("latitude", "35");
                params.put("language", "1");
                params.put("owner_name", "menna");
                params.put("owner_mobile", "0578789787");
                params.put("owner_email", "mennafcih@gmail.com");
                params.put("neighbarhood_id", "1");
                params.put("unit_type_id", "3");
                params.put("amenities_id", "3");
                params.put("area", "200");

                Log.i("my params::::",params.toString());

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

//        Toast.makeText(getApplicationContext(), "sending....", Toast.LENGTH_SHORT).show();
//        try {

//        sendData();
//            progressDialog.dismiss();
//        Toast.makeText(getApplicationContext(), title.getText().toString(), Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//        }
//        return false;
    }

    private void openDialog() {
        progressDialog = new ProgressDialog(AddRealEstate.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding...");
        progressDialog.show();

    }

    private void sendData() {



        /*
        String tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/realstate/add";
        Log.i("inside","::::::");
        /*
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        */
/*
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                //pDialog.hide();
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();
                Log.i("result:::::", result.toString());
                Toast.makeText(getApplicationContext(),"result:::: : " + result.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                Log.i("here in params::::","ok");

                params.put("title", "Title1");
                params.put("description", "desc");
                params.put("location", "Maadi Star Towers");
//                params.put("images","");
//                params.put("address","1A, Maadi Star Towers, Corniche El Maadi, Floor #22, Flat #1, Nile Corniche");
                typee = type.getText().toString();
                if(typee.equals("rent"))
                    params.put("type", "0");
                else
                    params.put("type", "1");
                params.put("number_of_rooms", "2");
                params.put("number_of_bathrooms", "1");
                params.put("price", "20101");
                params.put("longitude", "36");
                params.put("latitude", "35");
                params.put("language", "1");
                params.put("owner_name", "heba");
                params.put("owner_mobile", "012355425801");
                params.put("owner_email", "heba.mamdouhfcih@gmail.com");
                params.put("neighbarhood_id", "1");
                params.put("unit_type_id", "3");
                params.put("amenities_id", "3");
                params.put("area", "200");

                Log.i("my params::::",params.toString());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token;
                settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
        */




        /*
        typee = type.getText().toString();
        int t;
        if(typee.equals("rent"))
            t = 0;
        else
            t = 1;
        SharedPreferences settings;
        String token = null;
        settings = getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
        Ion.with(getApplicationContext())
                .load(BaseActivity.BASE_URL + "/realstate/add")
                .setHeader("Authorization",token)
                .setMultipartParameter("title", "my Title")
                .setMultipartParameter("description", "desc")
                .setMultipartParameter("location", "Maadi Star Towers")
                .setMultipartParameter("address","1A, Maadi Star Towers, Corniche El Maadi, Floor #22, Flat #1, Nile Corniche")
                .setMultipartParameter("type", t + "")
                .setMultipartParameter("number_of_rooms", "2")
                .setMultipartParameter("number_of_bathrooms", "1")
                .setMultipartParameter("price", "20101")
                .setMultipartParameter("longitude", "30")
                .setMultipartParameter("language", "1")
                .setMultipartParameter("owner_name", "heba")
                .setMultipartParameter("owner_mobile", "012355425801")
                .setMultipartParameter("owner_email", "heba.mamdouhfcih@gmail.com")
                .setMultipartParameter("neighbarhood_id", "1")
                .setMultipartParameter("unit_type_id", "3")
                .setMultipartParameter("amenities_id", "3")
                .setMultipartParameter("area", "200")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result==null){
                            Toast.makeText(getApplicationContext(),"Check Your Internet Acess Please",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                        else{
                            String status=(result.get("status")).toString();
                            Toast.makeText(getBaseContext(),status,Toast.LENGTH_SHORT).show();
                            if(status.equals("\"SUCCESS\""))
                            {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Saved..", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(getBaseContext(),"Error...",Toast.LENGTH_SHORT).show();
                            }
                        }}
                });*/
    }


    private void bindComponents() {
        title = (EditText) findViewById(R.id.title_addRealEstateActivity);
        description = (EditText) findViewById(R.id.description_addRealEstateActivity);
        email = (EditText) findViewById(R.id.email_addRealEstateActivity);
        location = (EditText) findViewById(R.id.location_addRealEstateActivity);
        address = (EditText) findViewById(R.id.address_addRealEstateActivity);
        contactPerson = (EditText) findViewById(R.id.contactPerson_addRealEstateActivity);
        phone = (EditText) findViewById(R.id.phone_addRealEstateActivity);
        area = (EditText)findViewById(R.id.area_addRealEstateActivity);
        numOfBathrooms = (EditText)findViewById(R.id.numOfBatreooms_addRealEstateActivity);
        numOfRooms = (EditText)findViewById(R.id.numOfRomms_addRealEstateActivity);
        type = (EditText)findViewById(R.id.type_addRealEstateActivity);
        price = (EditText)findViewById(R.id.price_addRealEstateActivity);

        save = (Button) findViewById(R.id.saveBtn_addRealEstateActivity);
        preview = (ImageView) findViewById(R.id.preview_addrealestate);

        realEstate = new RealEstate(title.getText().toString(), description.getText().toString(), location.getText().toString(),
                address.getText().toString(), contactPerson.getText().toString(), phone.getText().toString(), email.getText().toString());

    }

    @Override
    public void onBackPressed() {
        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(android.support.v4.view.GravityCompat.START)) {
//            drawer.closeDrawer(android.support.v4.view.GravityCompat.START);
//        } else {
            super.onBackPressed();
//        }
    }

    private void openMap(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(AddRealEstate.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_real_estate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        android.support.v4.widget.DrawerLayout drawer = (android.support.v4.widget.DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(android.support.v4.view.GravityCompat.START);
        return true;
    }

    public void uploadImg(View view) {
//       final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "amfb" + File.separator);
//       root.mkdir();
//       final String fname = "img_" + System.currentTimeMillis() + ".jpg";
//       final File sdImageMainDirectory = new File(root, fname);
//       outputFileUri = Uri.fromFile(sdImageMainDirectory);
//
//       // Camera.
//       final List<Intent> cameraIntents = new ArrayList<Intent>();
//       final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//       final PackageManager packageManager = getPackageManager();
//       final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureI
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);

//       for (ResolveInfo res : listCam){
//           final String packageName = res.activityInfo.packageName;
//           final Intent intent = new Intent(captureIntent);
//           intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//           intent.setPackage(packageName);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//           cameraIntents.add(intent);
//       }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try

        {
            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, this);
                    String toastMsg = String.format("Place: %s", place.getAddress());
                    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//                    Log.i()
                }
            }
            // When an Image is picked
            else if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                for (int i=0;i<filePathColumn.length;i++){
                    Toast.makeText(getApplicationContext(),filePathColumn[i],Toast.LENGTH_SHORT).show();
                }
                imagesEncodedList = new ArrayList<String>();
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
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.i("Selected Images :::::: ", mArrayUri.size() + "");
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        super.      onActivityResult(requestCode, resultCode, data);

    }
    private boolean titleIsEmpty(String titleTxt){
        if(titleTxt.isEmpty()){
            title.setError("Empty Title");
            return false;
        }
        return true;
    }
    private boolean descriptionIsEmpty(String desc){
        if(desc.isEmpty()){
            description.setError("Empty Description");
            return false;
        }
        return true;
    }


}
