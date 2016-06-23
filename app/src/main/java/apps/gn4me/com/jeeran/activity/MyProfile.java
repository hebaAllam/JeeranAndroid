package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.User;

public class MyProfile extends BaseActivity {

    TextView fname, lname, email, mobile, dateOfBirth;
    EditText fnameEdit, lnameEdit, emailEdit, mobileEdit, dateEdit;
    TextView username;
    Button save, edit;
    User profile;
    ImageView img;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        hideKeyboard();
        bindComponents();

        assignValues();

//        String check = i.getStringExtra("fromEdit");
//        if(check.equals("yes") ){
//            editProfile();
//        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void editProfile() {
        save.setVisibility(View.VISIBLE);
        fnameEdit.setVisibility(View.VISIBLE);
        lnameEdit.setVisibility(View.VISIBLE);
//                fnameEdit.setVisibility(View.VISIBLE);
        mobileEdit.setVisibility(View.VISIBLE);
        dateEdit.setVisibility(View.VISIBLE);
        emailEdit.setVisibility(View.VISIBLE);

        fname.setVisibility(View.GONE);
        lname.setVisibility(View.GONE);
        mobile.setVisibility(View.GONE);
        dateOfBirth.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);
        email.setVisibility(View.GONE);

        fnameEdit.setText(fname.getText().toString());
        lnameEdit.setText(lname.getText().toString());
        mobileEdit.setText(mobile.getText().toString());
        dateEdit.setText(dateOfBirth.getText().toString());
        emailEdit.setText(email.getText().toString());
    }

    private void updateProfile() {
        String  tag_string_req = "string_req";
//        final Context context = getContext();

        final String TAG = "Volley";
        String url = BaseActivity.BASE_URL + "/user/profileupdate";

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
                Log.i("result in profile ::: ",result.toString());

//                requestMyProfileJsonObject();
//                Toast.makeText(getApplicationContext(),result.getAsJsonObject("result").getAsJsonPrimitive("success").getAsBoolean()+"",Toast.LENGTH_LONG).show();
//                getRealEstateData(result);
//                new BaseActivity().requestMyProfileJsonObject();
                BaseActivity.profile.setFname(fnameEdit.getText().toString());
                BaseActivity.profile.setLname(lnameEdit.getText().toString());
                BaseActivity.profile.setMobile(mobileEdit.getText().toString()+"");
                BaseActivity.profile.setUserName(fnameEdit.getText().toString()+lnameEdit.getText().toString());

                BaseActivity.uname = fnameEdit.getText().toString()+ " " +lnameEdit.getText().toString();
//                onBackPressed();
                Intent i = new Intent(MyProfile.this,HomeActivity.class);
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
//                Intent i = getIntent();
//                prepareGUI();
//                String id = i.getStringExtra("realestateID");
                params.put("fName",fnameEdit.getText().toString());
                params.put("lName",lnameEdit.getText().toString());
//                params.put("location","Maadi");

//                params.put("mobile_number",mobileEdit.getText().toString());
//                params.put("dateOfBirth",dateEdit.getText().toString());
//                params.put("image","");

//            params.put("start", start.toString());
//            params.put("count",count.toString());


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
    }



    private void assignValues() {

        i = getIntent();
        fname.setVisibility(View.VISIBLE);
        lname.setVisibility(View.VISIBLE);
        mobile.setVisibility(View.VISIBLE);
        dateOfBirth.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);

        fnameEdit.setVisibility(View.GONE);
        lnameEdit.setVisibility(View.GONE);
        mobileEdit.setVisibility(View.GONE);
        dateEdit.setVisibility(View.GONE);
        emailEdit.setVisibility(View.GONE);

        Log.i("username:",profile.getUserName());
        Log.i("username:",profile.getDateOfBirth());
        Log.i("username:",profile.getEmailAddress());
        Log.i("username:",profile.getFname());
        Log.i("username:",profile.getLname());
        Log.i("username:",profile.getMobile());

        username.setText(profile.getUserName());
//        img.setImageDrawable();
        fname.setText(profile.getFname());
        lname.setText(profile.getLname());
        mobile.setText(profile.getMobile());
        dateOfBirth.setText(profile.getDateOfBirth());
        email.setText(profile.getEmailAddress());
        Picasso.with(this).load(profile.getImage()).placeholder(R.drawable.my_image).into(img);
    }

    private void bindComponents() {
        //bind textviews
        fname = (TextView)findViewById(R.id.fname_profile_text);
        lname = (TextView)findViewById(R.id.lname_profile_text);
        email = (TextView) findViewById(R.id.email_profile_text);
        mobile = (TextView)findViewById(R.id.phone_profile_text);
        dateOfBirth = (TextView)findViewById(R.id.date_profile_text);

        username = (TextView)findViewById(R.id.userName_profile);
        //bind edittexts
        fnameEdit = (EditText)findViewById(R.id.fname_profile);
        lnameEdit = (EditText)findViewById(R.id.lname_profile);
        mobileEdit= (EditText)findViewById(R.id.mobileNumber_profile);
        dateEdit = (EditText)findViewById(R.id.dateOfBirth_profile);
        emailEdit = (EditText)findViewById(R.id.email_profile);

        save = (Button)findViewById(R.id.save_profile);
        edit = (Button)findViewById(R.id.edit_profile);
        img = (ImageView)findViewById(R.id.imgView_profile);
        profile = BaseActivity.profile;
    }
}
