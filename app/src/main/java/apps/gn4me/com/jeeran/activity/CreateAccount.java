package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.User;

public class CreateAccount extends BaseActivity implements View.OnClickListener{

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 1;
    //all create account activity components
    EditText userNameEditTxt , passwordEditTxt , retypePassEditTxt , emailEditTxt;
    Button   registerBtn , registerWithFbBtn;
    User myUser;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Uri outputFileUri;
    File f;

    ImageView preview;
    private  int android_type = 0;
    private CallbackManager callbackManager;
    private LoginButton loginButton ;

    ProgressDialog progressDialog;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_create_account);

        hideKeyboard();

        progressDialog = new ProgressDialog(CreateAccount.this,
                R.style.AppTheme_Dark_Dialog);

        bindComponents();
        assignUserData();
        setListeners();



        //for facebook
        loginButton = (LoginButton)findViewById(R.id.login_with_facebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final AccessToken accessToken = loginResult.getAccessToken();
                final User fbUser = new User();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse graphResponse) {
                        fbUser.setEmailAddress(user.optString("email"));
                        fbUser.setUserName(user.optString("name"));
                        fbUser.setPassword(user.optString("id"));
                        //fbUser.setImage(user.optString("picture"));
                        //Snackbar.make(coordinatorLayout, "Login Success " + fbUser.getUserName() , Snackbar.LENGTH_LONG).show();

                    }
                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                Log.i("Success :::" ,
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
                Log.i("::::::::::::" , loginResult.getAccessToken().toString() );

                //Intent in = new Intent(LoginActivity.this,HomeActivity.class);
                //startActivity(in);
            }

            @Override
            public void onCancel() {
                Log.i("Cancel :::" , "Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.i("Error :::" ,"Login attempt failed.");
            }
        });


//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);

    }


    //method to bind xml components with objects
    private void bindComponents() {
        userNameEditTxt   = (EditText)findViewById(R.id.userNameEditTxt_createAccountActivity);
        passwordEditTxt   = (EditText)findViewById(R.id.passwordEditTxt_createAccountActivity);
        retypePassEditTxt = (EditText)findViewById(R.id.retypePassEditTxt_createAccountActivity);
        emailEditTxt      = (EditText)findViewById(R.id.emailEditTxt_createAccountActivity);
        preview           = (ImageView)findViewById(R.id.imgView);

        registerBtn       = (Button)findViewById(R.id.registerBtn_createAccountActivity);
//        registerWithFbBtn = (Button)findViewById(R.id.registerWithFbBtn_createAccountActivity);
    }
    //method that fills user data with xml components
    private void assignUserData() {
        myUser = new User(userNameEditTxt.getText().toString(), passwordEditTxt.getText().toString(), emailEditTxt.getText().toString(), null);
    }
    //method that assigns all needed listeners for this activity
    private void setListeners() {
        registerBtn.setOnClickListener(this);
//        registerWithFbBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View createAccountView) {
        int componentID = createAccountView.getId();

        if(componentID == R.id.registerBtn_createAccountActivity){
            if(isEmpty() && isValidPassword(passwordEditTxt.getText().toString()) && isConfirmedPassword(passwordEditTxt.getText().toString(), retypePassEditTxt.getText().toString())) {
                sendRegistrationData();
                openDialog();
            }
        }
//        else if(componentID == R.id.registerWithFbBtn_createAccountActivity){
//            sendRegistrationDataWithFb();
//        }
    }

    private void openDialog() {

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
    /*
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
    }*/

    private void requestJsonObject() {
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/register";

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
                JsonParser parser = new JsonParser();
                JsonObject result = parser.parse(response).getAsJsonObject();

                Log.i("Reslt", result.toString());
                progressDialog.dismiss();

                Intent i = new Intent(CreateAccount.this,LoginActivity.class);
                startActivity(i);
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
//                params.put("image",f);
                params.put("full_name",userNameEditTxt.getText().toString());
                params.put("email",emailEditTxt.getText().toString());
                params.put("password",passwordEditTxt.getText().toString());
                params.put("password_confirmation",retypePassEditTxt.getText().toString());
                params.put("device_type","1");
                params.put("device_token",android_id);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                return headers;
            }

        };

// Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strReq);
    }



    public void sendRegistrationData(){


        Toast.makeText(getApplicationContext(),"sending....",Toast.LENGTH_SHORT).show();
//
//
        preview.getDrawable();
        byte[] myImg = convertBitmapToByteArray(convertImgViewToBtitmap(preview));
        String img = Base64.encodeToString(myImg,  Base64.NO_WRAP + Base64.URL_SAFE);

        Bitmap mm = convertImgViewToBtitmap(preview);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        mm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        byte[] data = bos.toByteArray();



        //create a file to write bitmap data
        f = new File(getApplicationContext().getCacheDir(), userNameEditTxt.getText().toString());
        FileOutputStream fos  = null;
        try {
            f.createNewFile();


//Convert bitmap to byte array
        Bitmap bitmap = mm;
        ByteArrayOutputStream boss = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, boss);
        byte[] bitmapdata = boss.toByteArray();

//write the bytes in file
            fos  = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


     //   requestJsonObject();





        Ion.with(getApplicationContext())
                .load(BASE_URL + "/user/register")
                .setMultipartFile("image",f)
//                .setMultipartParameter("first_name","heba")
                .setMultipartParameter("full_name",userNameEditTxt.getText().toString())
                .setMultipartParameter("email",emailEditTxt.getText().toString())
                .setMultipartParameter("password",passwordEditTxt.getText().toString())
                .setMultipartParameter("password_confirmation",retypePassEditTxt.getText().toString())
                .setMultipartParameter("device_type","1")
                .setMultipartParameter("device_token",android_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
//                        showProgress(false);
                        Log.i("All Result ::: " , result.toString());

                        Boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                        if ( success ){
//                            SharedPreferences settings;
//                            SharedPreferences.Editor editor;
//                            settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
//                            editor = settings.edit();

//                            editor.putString("password", passwordEditTxt.getText().toString());
//                            editor.putString("email", emailEditTxt.getText().toString());
//                            editor.commit();
                            progressDialog.dismiss();

                            Intent i = new Intent(CreateAccount.this,HomeActivity.class);
                            startActivity(i);
                        } else {
                            progressDialog.dismiss();
//                            Snackbar.make(coordinatorLayout, "Login Failed", Snackbar.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"Registeration Failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });
        //            Toast.makeText(getApplicationContext(), createJsonObject().get("userName").toString() , Toast.LENGTH_LONG).show();
    }
    private Bitmap convertImgViewToBtitmap(ImageView imageView){
//        imageView.setDrawingCacheEnabled(true);
//
        imageView.buildDrawingCache();

        return imageView.getDrawingCache();
    }

    private byte[] convertBitmapToByteArray(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
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

        Intent intent = new Intent();
// Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

//        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "amfb" + File.separator);
//        root.mkdir();
//        final String fname = "img_" + System.currentTimeMillis() + ".jpg";
//        final File sdImageMainDirectory = new File(root, fname);
//        outputFileUri = Uri.fromFile(sdImageMainDirectory);
//
//        // Camera.
//        final List<Intent> cameraIntents = new ArrayList<Intent>();
//        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        final PackageManager packageManager = getPackageManager();
//        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam){
//            final String packageName = res.activityInfo.packageName;
//            final Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(packageName);
////            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            cameraIntents.add(intent);
//        }
//
//        //FileSystem
//        final Intent galleryIntent = new Intent();
//        galleryIntent.setType("image/");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//        // Chooser of filesystem options.
//        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
//        // Add the camera options.
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
//        startActivityForResult(chooserIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

//                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                preview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        if (resultCode == RESULT_OK) {
//            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//                final boolean isCamera;
//                if (data == null) {
//                    isCamera = true;
//                } else {
//                    final String action = data.getAction();
//                    if (action == null) {
//                        isCamera = false;
//                    } else {
//                        isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//                    }
//                }
//
//                Uri selectedImageUri;
//                if (isCamera) {
//                    selectedImageUri = outputFileUri;
//                    //Bitmap factory
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    // downsizing image as it throws OutOfMemory Exception for larger
//                    // images
//                    options.inSampleSize = 8;
//                    final Bitmap bitmap = BitmapFactory.decodeFile(selectedImageUri.getPath(), options);
//                    preview.setImageBitmap(bitmap);
//                } else {
//                    selectedImageUri = data == null ? null : data.getData();
//                    Log.d("ImageURI", selectedImageUri.getLastPathSegment());
//                    // /Bitmap factory
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    // downsizing image as it throws OutOfMemory Exception for larger
//                    // images
//                    options.inSampleSize = 8;
//                    try {//Using Input Stream to get uri did the trick
//                        InputStream input = getContentResolver().openInputStream(selectedImageUri);
//                        final Bitmap bitmap = BitmapFactory.decodeStream(input);
//                        preview.setImageBitmap(bitmap);
//
//                        Picasso.with(this)
//                                .load(selectedImageUri)
//                                .noFade()
//                                .into(preview);
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else if (resultCode == RESULT_CANCELED){
//            // user cancelled Image capture
//            Toast.makeText(getApplicationContext(),
//                    "You cancelled image capture", Toast.LENGTH_SHORT)
//                    .show();
//        } else {
//            // failed to capture image
//            Toast.makeText(getApplicationContext(),
//                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                    .show();
//        }
    }


}

