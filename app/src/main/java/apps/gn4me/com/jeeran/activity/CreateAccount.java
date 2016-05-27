package apps.gn4me.com.jeeran.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.User;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    //all create account activity components
    EditText userNameEditTxt , passwordEditTxt , retypePassEditTxt , emailEditTxt;
    Button   registerBtn , registerWithFbBtn;
    User myUser;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Uri outputFileUri;
    ImageView preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        bindComponents();
        assignUserData();
        setListeners();
    }

    //method to bind xml components with objects
    private void bindComponents() {
        userNameEditTxt   = (EditText)findViewById(R.id.userNameEditTxt_createAccountActivity);
        passwordEditTxt   = (EditText)findViewById(R.id.passwordEditTxt_createAccountActivity);
        retypePassEditTxt = (EditText)findViewById(R.id.retypePassEditTxt_createAccountActivity);
        emailEditTxt      = (EditText)findViewById(R.id.emailEditTxt_createAccountActivity);
        preview           = (ImageView)findViewById(R.id.imageView);

        registerBtn       = (Button)findViewById(R.id.registerBtn_createAccountActivity);
        registerWithFbBtn = (Button)findViewById(R.id.registerWithFbBtn_createAccountActivity);
    }
    //method that fills user data with xml components
    private void assignUserData() {
        myUser = new User(userNameEditTxt.getText().toString(), passwordEditTxt.getText().toString(), emailEditTxt.getText().toString(), null);
    }
    //method that assigns all needed listeners for this activity
    private void setListeners() {
        registerBtn.setOnClickListener(this);
        registerWithFbBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View createAccountView) {
        int componentID = createAccountView.getId();

        if(componentID == R.id.registerBtn_createAccountActivity){
            if(!isEmpty() && isValidPassword(passwordEditTxt.getText().toString()) && isConfirmedPassword(passwordEditTxt.getText().toString(), retypePassEditTxt.getText().toString()) && isValidEmailAddress(emailEditTxt.getText().toString())) {
                sendRegistrationData();
                openDialog();
            }
        }
        else if(componentID == R.id.registerWithFbBtn_createAccountActivity){
            sendRegistrationDataWithFb();
        }
    }

    private void openDialog() {
        final ProgressDialog progressDialog = new ProgressDialog(CreateAccount.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

    }

    private void sendRegistrationDataWithFb() {
        Intent myIntent = new Intent(CreateAccount.this, RegisterWithFacebook.class);
        startActivity(myIntent);
    }

    private boolean isEmpty() {
        if (userNameEditTxt.getText().toString().isEmpty()) {
            userNameEditTxt.setError("Empty UserName..");
            return false;
        }
        if( passwordEditTxt.getText().toString().isEmpty()) {
            passwordEditTxt.setError("Empty Password..");
            return false;
        }
        if (retypePassEditTxt.getText().toString().isEmpty()) {
            retypePassEditTxt.setError("Empty Confirm Password..");
            return false;
        }
        if (emailEditTxt.getText().toString().isEmpty()) {
            emailEditTxt.setError("Empty email address..");
            return false;
        }
        return true;

    }
    private boolean isValidPassword(String password){
        if (password.equals("") || password.length() < 8) {
            passwordEditTxt.setError("Password must be > 8");
            return false;
        }
        return true;
    }
    private boolean isConfirmedPassword(String password, String confirmPassword){
        if (!password.equals(confirmPassword)) {
            retypePassEditTxt.setError("Not Equal Password..");
            return false;
        }
        return true;
    }
    private JSONObject createJsonObject(){
        JSONObject jsonUser = new JSONObject();
        try {
            jsonUser.put("userName", myUser.getUserName());
            jsonUser.put("password", myUser.getPassword());
            jsonUser.put("image", myUser.getImage());
            jsonUser.put("emailAddress", myUser.getEmailAddress());

            Toast.makeText(getApplicationContext(),"json user : " +jsonUser.get("userName").toString(),Toast.LENGTH_LONG).show();

            return jsonUser;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void sendRegistrationData(){
        Toast.makeText(getApplicationContext(),"sending....",Toast.LENGTH_SHORT).show();
        try {
            Toast.makeText(getApplicationContext(), createJsonObject().get("userName").toString() , Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "registering....", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidEmailAddress(String email){
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditTxt.setError("Invalid Email Address");
            return false;
        }
        return true;
    }

    public static final int RESULT_GALLERY = 0;

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {

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
                    preview.setImageBitmap(bitmap);
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
                        preview.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED){
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


}
