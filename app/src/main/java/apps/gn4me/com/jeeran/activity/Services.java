package apps.gn4me.com.jeeran.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.ServiceCategoryAdapter;
import apps.gn4me.com.jeeran.listeners.RecyclerTouchListener;
import apps.gn4me.com.jeeran.pojo.ServicesCategory;

/**
 * Created by acer on 5/27/2016.
 */
public class Services extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    View v;
    private RecyclerView recyclerView;
    private ServiceCategoryAdapter myAdapter;


    private List<ServicesCategory> servicesCatList = new ArrayList<>();
    public Services() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      v= inflater.inflate(R.layout.service_fragment, container, false);
        //Slider

        Spinner dropdown = (Spinner)v.findViewById(R.id.spinner1frag);
        String[] items = new String[]{"El-Rehab", "October", "El-Maady"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);


        mDemoSlider = (SliderLayout)v.findViewById(R.id.sliderfrag);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.hannibal);
        file_maps.put("Big Bang Theory",R.drawable.bigbang);
        file_maps.put("House of Cards",R.drawable.house);
        file_maps.put("Game of Thrones", R.drawable.game_of_thrones);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getContext());
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


     //services category list
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        servicesCatList.clear();
        myAdapter=new ServiceCategoryAdapter(servicesCatList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
       prepareData();
        //services category list listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
              ServicesCategory servicecat=  servicesCatList.get(position);

                switch (servicecat.getServiceCatId()){
                    case 1:
                        Intent foodServices=new Intent(getContext(),FoodAndBeveragesService.class);
                        startActivity(foodServices);
                        break;
                    case 3:
                        Intent superMarketsIntent=new Intent(getContext(),Service.class);
                        startActivity(superMarketsIntent);
                        break;
                    case 2:
                        Toast.makeText(getContext(),"we will go to "+servicecat.getServiceCatName()+" services ",Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getContext(),"we will go to "+servicecat.getServiceCatName()+" services ",Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(getContext(),"we will go to "+servicecat.getServiceCatName()+" services ",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        Toast.makeText(getContext(),"we will go to "+servicecat.getServiceCatName()+" services ",Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return v;
    }

    private void prepareData() {

        ServicesCategory serviceCat=new ServicesCategory(1,R.drawable.ic_food_icon,"Food and Beverages","10");
        servicesCatList.add(serviceCat);
        ServicesCategory servicecat2=new ServicesCategory(2,R.drawable.ic_shopping_icon,"Shopping","16");
        servicesCatList.add(servicecat2);
        ServicesCategory servicecat3=new ServicesCategory(3,R.drawable.ic_supermarket_icon,"SuperMarkets","23"); ;
        servicesCatList.add(servicecat3);
        ServicesCategory servicecat4=new ServicesCategory(4,R.drawable.ic_pharmacies_icon,"Pharmacies","40");
        servicesCatList.add(servicecat4);
        ServicesCategory servicecat5=new ServicesCategory(5,R.drawable.ic_education_icon,"Education","11");
        servicesCatList.add(servicecat5);
        ServicesCategory servicecat6=new ServicesCategory(6,R.drawable.ic_entertainment_icon,"Entertainment","15");
        servicesCatList.add(servicecat6);
        myAdapter.notifyDataSetChanged();

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
    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }
}