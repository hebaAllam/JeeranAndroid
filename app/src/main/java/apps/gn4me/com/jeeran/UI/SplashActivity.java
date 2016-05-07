package apps.gn4me.com.jeeran.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import apps.gn4me.com.jeeran.R;

public class SplashActivity extends AppCompatActivity {

    Button start ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        start = (Button) findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(in);
            }
        });

    }
}
