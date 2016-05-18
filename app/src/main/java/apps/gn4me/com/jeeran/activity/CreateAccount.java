package apps.gn4me.com.jeeran.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.User;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener{

    //all create account activity components
    EditText userNameEditTxt , passwordEditTxt , retypePassEditTxt , emailEditTxt;
    Button   registerBtn , registerWithFbBtn;
    User myUser;


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
        if (userNameEditTxt.getText().toString().isEmpty()){
            userNameEditTxt.setError("Empty UserName..");
                if( passwordEditTxt.getText().toString().isEmpty()) {
                    passwordEditTxt.setError("Empty Password..");
                    if (retypePassEditTxt.getText().toString().isEmpty()) {
                        retypePassEditTxt.setError("Empty Confirm Password..");
                        if (emailEditTxt.getText().toString().isEmpty()) {
                            emailEditTxt.setError("Empty email address..");
                            return true;
                        }
                    }
                }
        }
        return false;
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

    private void sendRequestToWebService(){
        Ion.with(getApplicationContext())
                .load("http://10.118.49.82:8088/RestFulServiceTest/rest/WService/query")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
//                        progressDialog.dismiss();
                        if(result==null){
                            Toast.makeText(getApplicationContext(),"Check Your Internet Acess Please",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String status=(result.get("status")).toString();
                            Toast.makeText(getBaseContext(),status,Toast.LENGTH_SHORT).show();
                            if(status.equals("\"SUCCESS\""))
                            {
//                                SharedPreferences.Editor editor = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE).edit();
//                                editor.putString("name", user_name);
//                                editor.putString("pass", user_pass);
//                                editor.commit();
//                                Intent intent=new Intent(SignIn.this,MedicineHome.class);
//                                intent.putExtra("getDataFromService","medsdata");
//                                startActivity(intent);
                            }
                        }}
                });
    }
}
