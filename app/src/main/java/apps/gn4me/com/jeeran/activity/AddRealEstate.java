package apps.gn4me.com.jeeran.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;

public class AddRealEstate extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    private Uri outputFileUri;
    EditText title, location, description, address, contactPerson, phone, email;
    Button save;
    RealEstate realEstate;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_real_estate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.arrow_icon);
        getSupportActionBar().setWindowTitle("Add Real Estate");
        toolbar.setNavigationIcon(R.drawable.arrow_icon);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
                onBackPressed();
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bindComponents();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleIsEmpty(title.getText().toString()) && descriptionIsEmpty(description.getText().toString())){
                    openDialog();
                    sendRealEstateData(realEstate);

                    Toast.makeText(getApplicationContext(),"Saved" ,Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    private boolean sendRealEstateData(RealEstate myRealEstate) {
        Toast.makeText(getApplicationContext(),"sending....",Toast.LENGTH_SHORT).show();
//        try {

            sendData();
//            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), title.getText().toString(), Toast.LENGTH_LONG).show();
//        } catch (JSONException e) {
//            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
//        }
        return false;
    }
    private void openDialog() {
        progressDialog = new ProgressDialog(AddRealEstate.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Adding...");
        progressDialog.show();

    }

    private void sendData(){
        Ion.with(getApplicationContext())
                .load("http://realstate/add")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result==null){
                            Toast.makeText(getApplicationContext(),"Check Your Internet Acess Please",Toast.LENGTH_SHORT).show();
                           // progressDialog.dismiss();
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
                });
    }

    private JSONObject createJsonObject(RealEstate myRE){
        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("title", myRE.getTitle());
            jsonUser.put("description", myRE.getDescription());
            jsonUser.put("location", myRE.getLocation());
            jsonUser.put("address", myRE.getAddress());
            jsonUser.put("contactPerson", myRE.getContactPerson());
            jsonUser.put("phone", myRE.getPhone());
            jsonUser.put("emailAddress", myRE.getEmail());

            Toast.makeText(getApplicationContext(),"json user : " +jsonUser.get("title").toString(),Toast.LENGTH_LONG).show();

            return jsonUser;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void bindComponents() {
        title = (EditText)findViewById(R.id.title_addRealEstateActivity);
        description = (EditText)findViewById(R.id.description_addRealEstateActivity);
        email = (EditText)findViewById(R.id.email_addRealEstateActivity);
        location = (EditText)findViewById(R.id.location_addRealEstateActivity);
        address = (EditText)findViewById(R.id.address_addRealEstateActivity);
        contactPerson = (EditText)findViewById(R.id.contactPerson_addRealEstateActivity);
        phone = (EditText)findViewById(R.id.phone_addRealEstateActivity);

        save = (Button) findViewById(R.id.saveBtn_addRealEstateActivity);

       realEstate = new RealEstate(title.getText().toString(), description.getText().toString(), location.getText().toString(),
                                    address.getText().toString(), contactPerson.getText().toString(), phone.getText().toString(), email.getText().toString());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   public void  uploadImg(View view){
       final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "amfb" + File.separator);
       root.mkdir();
       final String fname = "img_" + System.currentTimeMillis() + ".jpg";
       final File sdImageMainDirectory = new File(root, fname);
       outputFileUri = Uri.fromFile(sdImageMainDirectory);

       // Camera.
       final List<Intent> cameraIntents = new ArrayList<Intent>();
       final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       final PackageManager packageManager = getPackageManager();
       final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
       for (ResolveInfo res : listCam){
           final String packageName = res.activityInfo.packageName;
           final Intent intent = new Intent(captureIntent);
           intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
           intent.setPackage(packageName);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
           cameraIntents.add(intent);
       }

       //FileSystem
       final Intent galleryIntent = new Intent();
       galleryIntent.setType("image/");
       galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

       // Chooser of filesystem options.
       final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
       // Add the camera options.
       chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
       startActivityForResult(chooserIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                final boolean isCamera;
                if (data == null) {
                    isCamera = true;
                } else {
                    final String action = data.getAction();
                    if (action == null) {
                        isCamera = false;
                    } else {
                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                    }
                }

                Uri selectedImageUri;
                if (isCamera) {
                    selectedImageUri = outputFileUri;
                    //Bitmap factory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    final Bitmap bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath(), options);
//                    preview.setImageBitmap(bitmap);
                } else {
                    selectedImageUri = data == null ? null : data.getData();
                    Log.d("ImageURI", selectedImageUri.getLastPathSegment());
                    // /Bitmap factory
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // downsizing image as it throws OutOfMemory Exception for larger
                    // images
                    options.inSampleSize = 8;
                    try {//Using Input Stream to get uri did the trick
                        InputStream input = getContentResolver().openInputStream(selectedImageUri);
                        final Bitmap bitmap = BitmapFactory.decodeStream(input);
//                        preview.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(getApplicationContext(),
                    "You cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {
            // failed to capture image
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
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
