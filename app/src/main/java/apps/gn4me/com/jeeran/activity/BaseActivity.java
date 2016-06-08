package apps.gn4me.com.jeeran.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.broadcast_receiver.TokenValidation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by mraouf on 08/05/2016.
 */
public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    public static final String PREFS_NAME = "Jeeran";
    public static final String BASE_URL = "http://jeeran.gn4me.com/jeeran_v1";
    public static final Long EXPIRATION_Duration = AlarmManager.INTERVAL_FIFTEEN_MINUTES ;


    protected View progress;
    String android_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_scale);
        hideKeyboard();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null)
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //closing transition animations
        overridePendingTransition(R.anim.activity_open_scale, R.anim.activity_close_translate);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void validateToken() {
        SharedPreferences settings;
        String email, password, token, dateStr;
        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1

        email = settings.getString("email", null);
        password = settings.getString("password", null);
        token = settings.getString("token", null);

        Date when = new Date(System.currentTimeMillis());

        try{
            Intent someIntent = new Intent(getApplicationContext(),TokenValidation.class); // intent to be launched

            // note this could be getActivity if you want to launch an activity
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getApplicationContext(),
                    0, // id, optional
                    someIntent, // intent to launch
                    PendingIntent.FLAG_CANCEL_CURRENT); // PendintIntent flag

            AlarmManager alarms = (AlarmManager) getApplicationContext().getSystemService(
                    Context.ALARM_SERVICE);

            alarms.setRepeating(AlarmManager.RTC,
                    when.getTime(),
                    EXPIRATION_Duration,
                    pendingIntent);

        }catch(Exception e){
            e.printStackTrace();
        }

    }



    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ((TextView) findViewById(R.id.title)).setText(getTitle());
            setTitle("");
        }

    }
}
