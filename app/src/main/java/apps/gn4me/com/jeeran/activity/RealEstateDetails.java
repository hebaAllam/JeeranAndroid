
package apps.gn4me.com.jeeran.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

import apps.gn4me.com.jeeran.R;

public class RealEstateDetails extends BaseActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{


    private SliderLayout mDemoSlider;
    private DrawerLayout mDrawerLayout;
    //    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private ScrimInsetsFrameLayout mScrimInsetsFrameLayout;
    private Toolbar toolbar;
   TextView title, date, location, price, description, area, numOfBathrooms, numOfRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estate_details);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.hannibal);
        file_maps.put("Big Bang Theory", R.drawable.bigbang);
        file_maps.put("House of Cards", R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
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

        setupToolbar();


        bindComponents();
//        Intent i = getIntent();
//        i.getStringExtra("title");

        assignVlues();
    }

    private void assignVlues() {
        Intent i = getIntent();

        String typee = "rent";
        if(i.getIntExtra("type",3) == 0)
            typee = "rent";
        else if(i.getIntExtra("type",3) == 1)
            typee = "sale";

        Log.i("intent result ::::: ", i.getStringExtra("area")+ ", " +i.getStringExtra("number_of_bathrooms") );

        title.setText(i.getStringExtra("title") + " For\t " + typee);
        date.setText(i.getStringExtra("creationDate")+"");
        location.setText(i.getStringExtra("location"));
        price.setText(i.getIntExtra("price",0)+"");
        description.setText(i.getStringExtra("description")+"");
        area.setText(i.getStringExtra("area")+"");
        numOfBathrooms.setText(i.getIntExtra("number_of_bathrooms",0)+"");
        numOfRooms.setText(i.getIntExtra("number_of_rooms",0)+"");
    }

    private void bindComponents() {
        title = (TextView)findViewById(R.id.title_detailsRealEstate);
        date = (TextView)findViewById(R.id.date_text);
        location = (TextView)findViewById(R.id.location_text);
        price = (TextView)findViewById(R.id.price_detailRealEstate);
        description = (TextView)findViewById(R.id.descriptionTxt);
        area = (TextView)findViewById(R.id.areaNu);
        numOfRooms = (TextView)findViewById(R.id.bedRoomsNum);
        numOfBathrooms = (TextView)findViewById(R.id.bathRoomsNum);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
