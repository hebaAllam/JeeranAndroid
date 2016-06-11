package apps.gn4me.com.jeeran.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;

import apps.gn4me.com.jeeran.R;

public class HomeActivity extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mDemoSlider;
    private DrawerLayout mDrawerLayout;
//    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private Toolbar toolbar;
    //private AppCompatButton serviceBtn ;
    private FrameLayout discussionBtn ;
    private FrameLayout realEstateBtn ;

    private FrameLayout serviceBtn ;
    private FrameLayout myFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Spinner dropdown = (Spinner)findViewById(R.id.spinner1);
        //String[] items = new String[]{"El-Rehab", "October", "El-Maady"};

        ArrayList<String> items = new ArrayList<>();
        for (int i=0 ; i< BaseActivity.neighborhoods.size() ; i++ ){
            items.add(BaseActivity.neighborhoods.get(i).getTitleEnglish());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BaseActivity.currentNeighborhood = BaseActivity.neighborhoods.get(position) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                BaseActivity.currentNeighborhood = BaseActivity.neighborhoods.get(0);
            }
        });

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = BaseActivity.url_maps;//new HashMap<String, String>();

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        ////////////////
        init_navigator();
        //////////////
        myFavorites = (FrameLayout) findViewById(R.id.navigation_drawer_items_list_linearLayout_myFavorites);
        myFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeActivity.this,MyFavoritesActivity.class);
                startActivity(in);
            }
        });

        /////////////
        serviceBtn = (FrameLayout) findViewById(R.id.serviceLayout);
        discussionBtn = (FrameLayout) findViewById(R.id.discussionLayout);
        realEstateBtn = (FrameLayout) findViewById(R.id.realEstateLayout);

        serviceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent=new Intent(HomeActivity.this,ModulesActivity.class);
                serviceIntent.putExtra("uniqueId","from_service");
                startActivity(serviceIntent);
            }
        });

        discussionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dicussionIntent=new Intent(HomeActivity.this,ModulesActivity.class);
                dicussionIntent.putExtra("uniqueId","from_discussion");
                startActivity(dicussionIntent);
            }
        });

        realEstateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent realEstateIntent=new Intent(HomeActivity.this,ModulesActivity.class);
                realEstateIntent.putExtra("uniqueId","from_realEstate");
                startActivity(realEstateIntent);
            }
        });


        ///////////////

        /*
        SharedPreferences settings;
        String token ;
        settings = getApplicationContext().getSharedPreferences(BaseActivity.PREFS_NAME, Context.MODE_PRIVATE); //1

        token = settings.getString("token", null);

        Ion.with(getApplicationContext())
                .load(BASE_URL + "/discussion/list")
                .setHeader("Authorization",token)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Log.i("::: data discussion", e.getMessage() );

                    }
                });
        */

    }




    private void init_navigator(){
        setupToolbar();

        // Navigation Drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_activity_DrawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

        };
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.primaryDark));
        mScrimInsetsFrameLayout = (ScrimInsetsFrameLayout) findViewById(R.id.main_activity_navigation_drawer_rootLayout);


        // Navigation Drawer layout width
        int possibleMinDrawerWidth = UtilsDevice.getScreenWidth(this) -
                UtilsMiscellaneous.getThemeAttributeDimensionSize(this, android.R.attr.actionBarSize);
        int maxDrawerWidth = getResources().getDimensionPixelSize(R.dimen.navigation_drawer_max_width);

        mScrimInsetsFrameLayout.getLayoutParams().width = Math.min(possibleMinDrawerWidth, maxDrawerWidth);
        // Set the first item as selected for the first time
        getSupportActionBar().setTitle(R.string.toolbar_title_home);

    }



    public void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
//            ((TextView) findViewById(R.id.title)).setText(getTitle());
//            setTitle("");
        }

    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view

        return super.onPrepareOptionsMenu(menu);
    }

}
