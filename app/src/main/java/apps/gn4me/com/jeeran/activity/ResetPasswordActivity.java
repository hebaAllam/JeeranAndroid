package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.R;

public class  ResetPasswordActivity extends BaseActivity {

    AppCompatButton resetPassword ;
    EditText mPasswordView ;
    EditText mRePasswordView ;
    EditText mCodeView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPassword = (AppCompatButton) findViewById(R.id.resetPassword) ;
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptReset();
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mRePasswordView = (EditText) findViewById(R.id.rePassword);
        mCodeView = (EditText) findViewById(R.id.code);
    }

    private void attemptReset(){
        mCodeView.setError(null);
        mPasswordView.setError(null);
        mRePasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        final String password = mPasswordView.getText().toString();
        final String rePassword = mRePasswordView.getText().toString();
        final String code = mCodeView.getText().toString();

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }else if (TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }
        if (!TextUtils.isEmpty(rePassword) && rePassword.equals(password)) {
            mRePasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRePasswordView;
            cancel = true;
        }else if (TextUtils.isEmpty(rePassword)){
            mRePasswordView.setError(getString(R.string.error_field_required));
            focusView = mRePasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(code)) {
            mCodeView.setError(getString(R.string.error_field_required));
            focusView = mCodeView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            requestResetPassword();
        }

    }


    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }


    private void requestResetPassword() {

        final String TAG = "Volley";

        final String password = mPasswordView.getText().toString();
        final String rePassword = mRePasswordView.getText().toString();
        final String code = mCodeView.getText().toString();
        final String email = getIntent().getStringExtra("email") ;

        String url = BaseActivity.BASE_URL + "/user/forgetpassword";

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
                Boolean success = false ;
                if ( result != null ) {
                    success = result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean();
                }

                if ( success ) {
                    Intent in = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(in);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("confirmpassword", rePassword);
                params.put("code", code);
                return params;
            }


        };

        // Adding request to request queue
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(strReq);
    }


}
