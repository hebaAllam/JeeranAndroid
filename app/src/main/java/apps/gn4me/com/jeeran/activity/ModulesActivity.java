package apps.gn4me.com.jeeran.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;

public class ModulesActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  TextView moduleLabel;
    private ImageView backImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setTitle("MainServices");

            // ((TextView) findViewById(R.id.title)).setText(getTitle());

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = this.getIntent();
//      moduleLabel=(TextView)findViewById(R.id.selected_module) ;
//     backImg=(ImageView)findViewById(R.id.backIcon);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        if(intent!=null && intent.getExtras()!=null){
            String activityIdentifier=intent.getExtras().getString("uniqueId");
            switch (activityIdentifier){
                case "from_service":
                    viewPager.setCurrentItem(0);
                    break;
                case "from_discussion":
                    viewPager.setCurrentItem(1);

                    break;
                case "from_realEstate":
                    viewPager.setCurrentItem(2);

                    break;
                default:
                    viewPager.setCurrentItem(3);
                    break;
            }
        }
    }
    private void setupTabIcons() {

        TextView tabNotification = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabNotification.setText("Notifications");
        tabNotification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notification_icon, 0, 0);
        tabNotification.setBackgroundColor(getResources().getColor(R.color.notificationsColor));
        tabNotification.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        tabLayout.getTabAt(3).setCustomView(tabNotification);

        TextView tabServices = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabServices.setText("MainServices");
        tabServices.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_service_icon, 0, 0);
        tabServices.setBackgroundColor(getResources().getColor(R.color.servicesColor));

        tabServices.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        tabLayout.getTabAt(0).setCustomView(tabServices);

        TextView tabDiscussion = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabDiscussion.setText("Discussion");
        tabDiscussion.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_discussion_icon, 0, 0);
        tabDiscussion.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabDiscussion.setBackgroundColor(getResources().getColor(R.color.dicussionsColor));

        tabLayout.getTabAt(1).setCustomView(tabDiscussion);
        TextView tabRealEstate = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabRealEstate.setText("RealEstateActivty");
        tabRealEstate.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_real_estate_icon, 0, 0);
        tabRealEstate.setBackgroundColor(getResources().getColor(R.color.realEstateColor));
        tabRealEstate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabLayout.getTabAt(2).setCustomView(tabRealEstate);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainServices(), "MainServices");
        adapter.addFragment(new Discussion(), "Discussion");
        adapter.addFragment(new RealEstateActivty(), "RealEstateActivty");
        adapter.addFragment(new Notification(),"Notification");
        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
