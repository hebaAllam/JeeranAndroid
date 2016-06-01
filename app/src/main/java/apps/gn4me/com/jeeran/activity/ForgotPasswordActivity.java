package apps.gn4me.com.jeeran.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;

import apps.gn4me.com.jeeran.R;

public class ForgotPasswordActivity extends BaseActivity {

    AppCompatButton resetPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        resetPassword = (AppCompatButton) findViewById(R.id.forgotPassword) ;
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(ForgotPasswordActivity.this,ResetPasswordActivity.class);
                startActivity(in);
            }
        });

    }

}
