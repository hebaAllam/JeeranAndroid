package apps.gn4me.com.jeeran.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;

public class ModulesActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private  TextView moduleLabel;
    private ImageView backImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);
        Intent intent = this.getIntent();
          moduleLabel=(TextView)findViewById(R.id.selected_module) ;
        backImg=(ImageView)findViewById(R.id.backIcon);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        if(intent!=null){
            String activityIdentifier=intent.getExtras().getString("uniqueId");
            switch (activityIdentifier){
                case "from_service":
                    viewPager.setCurrentItem(0);
                    moduleLabel.setText("Services");
                    break;
                case "from_discussion":
                    viewPager.setCurrentItem(1);
                    moduleLabel.setText("discussions");
                    break;
                case "from_realEstate":
                    viewPager.setCurrentItem(2);
                    moduleLabel.setText("Real Estates");
                    break;
                default:
                    viewPager.setCurrentItem(3);
                    moduleLabel.setText("Notifications");
                    break;
            }

        }
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void setupTabIcons() {

        TextView tabNotification = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabNotification.setText("Notifications");
        tabNotification.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_notification_icon, 0, 0);
        tabNotification.setBackgroundColor(getResources().getColor(R.color.notificationsColor));
        tabNotification.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        tabLayout.getTabAt(3).setCustomView(tabNotification);



        TextView tabServices = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabServices.setText("Services");
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
        tabRealEstate.setText("RealEstate");
        tabRealEstate.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_real_estate_icon, 0, 0);
        tabRealEstate.setBackgroundColor(getResources().getColor(R.color.realEstateColor));
        tabRealEstate.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tabLayout.getTabAt(2).setCustomView(tabRealEstate);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Services(), "Services");
        adapter.addFragment(new Discussion(), "Discussion");
        adapter.addFragment(new RealEstate(), "RealEstate");
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
