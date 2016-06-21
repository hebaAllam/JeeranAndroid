package apps.gn4me.com.jeeran.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.intent_service.ValidateTokenService;

public class SplashActivity extends BaseActivity {

    Spinner selectArea ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        selectArea = (Spinner)findViewById(R.id.selectAreaSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.select_area, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        selectArea.setAdapter(adapter);


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    SharedPreferences settings;
                    String email , password  , token ;
                    settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1

//                    SharedPreferences.Editor editor = settings.edit();
//                    editor.remove("token");
//                    editor.commit();

                    email = settings.getString("email", null);
                    password = settings.getString("password", null);
                    token = settings.getString("token", null);


                    Log.i("......."," item clicked..." + selectArea.getSelectedItem().toString());

                    String selection = selectArea.getSelectedItem().toString();
                    if (!selection.isEmpty()|| selection != null){
                        if ( token != null ) {
                            Log.i("-*-*-* token : ", token);
                            //start service to keep token valid
                            countInitRequest = 0 ;
                            activeActivity = SplashActivity.this ;
                            requestMyProfileJsonObject();
                            requestNeighborhoodsListJson();
                            requestReportReasonsJson();
                            requestDiscussionTopicsJson();
                            requestHomeSliderImages();

                            Intent in = new Intent(SplashActivity.this , ValidateTokenService.class);
                            startService(in);

                        }else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                    else
                        try {
                            sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                }
            }
        };

        // add logic here
        timerThread.start();
    }



    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();


    }

}
