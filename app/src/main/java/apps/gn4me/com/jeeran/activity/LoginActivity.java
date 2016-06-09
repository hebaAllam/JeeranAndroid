package apps.gn4me.com.jeeran.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CallbackManager callbackManager;

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private CoordinatorLayout coordinatorLayout;

    private AppCompatButton mEmailSignInButton ;
    private AppCompatButton forgotPasswordButton ;
    private LoginButton loginButton ;
    private AppCompatButton createAccountButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        if (mEmailSignInButton != null) {
            mEmailSignInButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
        }

        createAccountButton = (AppCompatButton) findViewById(R.id.sign_up);
        createAccountButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this,CreateAccount.class);
                startActivity(in);
            }
        });


        forgotPasswordButton =  (AppCompatButton) findViewById(R.id.forgot_password);
        forgotPasswordButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(in);
            }
        });


        loginButton = (LoginButton)findViewById(R.id.login_with_facebook);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final AccessToken accessToken = loginResult.getAccessToken();
                final User fbUser = new User();
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(final JSONObject user, GraphResponse graphResponse) {
                        Log.i("FB Data:: Email" , user.optString("email"));
                        Log.i("FB Data:: Id" , user.optString("id"));
                        Log.i("FB Data:: Name" , user.optString("name"));
                        Log.i("FB Picture:: Picture" , user.optString("picture"));

                        String img = null ;

                        try {
                            img = user.getJSONObject("picture").getJSONObject("data").getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        /// test login by fb

                        final String saveImgURl = img ;

                        showProgress(true);
                        Ion.with(getApplicationContext())
                                .load(BASE_URL + "/user/loginfb")
                                .noCache()
                                .setBodyParameter("device_type", "0") //android => 0
                                .setBodyParameter("email", user.optString("email"))
                                .setBodyParameter("fb_id", user.optString("id"))
                                .setBodyParameter("image", img )
                                .setBodyParameter("device_token", android_id)
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {
                                        // do stuff with the result or error
                                        showProgress(false);
                                        Boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                                        if ( success ){
                                            Log.i("Done ::: success" , result.toString() );

                                            SharedPreferences settings;
                                            SharedPreferences.Editor editor;
                                            settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
                                            editor = settings.edit();
                                            if ( saveImgURl != null ) {
                                                editor.putString("profile", saveImgURl);
                                            }
                                            editor.putString("fb_id", user.optString("id"));
                                            editor.putString("email",  user.optString("email"));
                                            editor.putString("token",  "Bearer " + result.getAsJsonPrimitive("token").getAsString());
                                            editor.commit();

                                            validateToken();
                                            Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                                            startActivity(i);
                                        } else {
                                            Log.i( "Login Failed ::: " , result.toString() );
                                            Snackbar.make(coordinatorLayout, "Login Failed" , Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                });


                    }
                });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture,first_name,last_name");
                request.setParameters(parameters);
                request.executeAsync();

                /*
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
                */
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


        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }


    private void makeJsonObjReq(final int type , final String[] parameter) {
        String  tag_string_req = "string_req";

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL ;
        if (type == 0 ) {
            url += "/user/login";
        }else{
            url += "/user/loginfb";
        }

        /*
        final ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.show();
        */

        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                //pDialog.hide();
                JsonObject result = new JsonObject();
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

                if ( type == 0 ) {
                    params.put("device_type", "0");
                    params.put("email", email);
                    params.put("password", password);
                    params.put("device_token", android_id);
                }else{
                    params.put("fb_id", parameter[0]);
                    params.put("email", parameter[1]);
                    params.put("image", parameter[2]);
                    params.put("device_type", "0");
                    params.put("device_token", android_id);
                }

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                SharedPreferences settings;
                String token ;
                settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1
                token = settings.getString("token", null);
                headers.put("Authorization", token);
                return headers;
            }

        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }


    /*

    public static Bitmap getFacebookProfilePicture(String url){
        URL facebookProfileURL= null;
        Bitmap bitmap = null;
        try {
            facebookProfileURL = new URL(url);
            bitmap = BitmapFactory.decodeStream(facebookProfileURL.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }




    Bitmap profilePic = getFacebookProfilePicture(profilePicUrl);
    mImageView.setBitmap(profilePic);

    */


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

            final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            String android_type = android.os.Build.DEVICE ;

            showProgress(true);
            Ion.with(getApplicationContext())
            .load(BASE_URL + "/user/login")
                    .noCache()
            .setBodyParameter("device_type", "0")
            .setBodyParameter("email", email ) //"testhsmsss@test.com"
            .setBodyParameter("password", password ) //"123456789"
            .setBodyParameter("device_token", android_id ) //"bbbbbbdnssbbsxbxb"
            .asJsonObject()
            .setCallback(new FutureCallback<JsonObject>() {
               @Override
                public void onCompleted(Exception e, JsonObject result) {
                    // do stuff with the result or error
                    showProgress(false);
                   Log.i("Done ::: success" , result.toString() );

                   Boolean success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                    if ( success ){

                        SharedPreferences settings;
                        SharedPreferences.Editor editor;
                        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
                        editor = settings.edit();

                        editor.putString("password", password);
                        editor.putString("email", email );
                        editor.putString("device_token", android_id);
                        editor.putString("token",  "Bearer " + result.getAsJsonPrimitive("token").getAsString());
//                        Log.i("-*-*-*-* token : ")
                        editor.commit();

                        validateToken();
                        Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(i);
                    } else {
                        Log.i( "Login Failed ::: " , result.toString() );
                        Snackbar.make(coordinatorLayout, "Login Failed" , Snackbar.LENGTH_LONG).show();
                    }
                }
            });

            //Intent i = new Intent(LoginActivity.this,HomeActivity.class);
            //startActivity(i);
        }
    }

    private boolean isEmailValid(String email) {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}

