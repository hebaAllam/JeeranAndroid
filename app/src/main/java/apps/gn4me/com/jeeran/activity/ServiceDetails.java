package apps.gn4me.com.jeeran.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.ServiceDetailsAdapter;
import apps.gn4me.com.jeeran.pojo.Service;
import apps.gn4me.com.jeeran.pojo.ServiceDetailsPojo;

public class ServiceDetails extends BaseActivity {

    private RecyclerView recyclerView;
    private ServiceDetailsAdapter adapter;
    private List<ServiceDetailsPojo> serviceDetailsList;
    int serviceId;
    String serviceName;
    ImageView showLocation,rateService,favoriteService,serviceLogo;
    String serviceSubCat;
    private  TextView activityTitle,serviceTitle,serviceAddress,serviceOpeningHours,serviceNumberOfRates,serviceDiscHeader,serviceDisc;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detailes);

        showLocation=(ImageView)findViewById(R.id.showLocation) ;
        rateService=(ImageView)findViewById(R.id.rateService);
        favoriteService=(ImageView)findViewById(R.id.favoriteService);
        serviceLogo=(ImageView)findViewById(R.id.service_logo);
       serviceTitle=(TextView)findViewById(R.id.service_title);
        serviceAddress=(TextView)findViewById(R.id.service_address);
        serviceOpeningHours=(TextView)findViewById(R.id.service_opiningHours);
        serviceNumberOfRates=(TextView)findViewById(R.id.service_numberofRates);
        serviceDiscHeader=(TextView)findViewById(R.id.disc_header);
        serviceDisc=(TextView)findViewById(R.id.service_disc);
        //------------setting tool bar-----
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            activityTitle=(TextView)findViewById(R.id.txt_titile);
            setTitle("");

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //-----------------------------
        //------------get service ID-------------
        Intent i=getIntent();
        if(i.hasExtra("UniqueServiceId")){
        serviceId= i.getExtras().getInt("UniqueServiceId");
        serviceName=i.getExtras().getString("ServiceDetailsName");
            serviceSubCat=i.getExtras().getString("serviceSubCatName");
           setTitle(serviceName+" Details");

       // activityTitle.setText(serviceName);

        /*here i will connect web service and get service details to specific service id

        -----------------
        ------------
        --------

        -----
         */
        }


        final Button allReviews=(Button)findViewById(R.id.allReviews);


        recyclerView = (RecyclerView) findViewById(R.id.restaurant_recycleView);

        serviceDetailsList = new ArrayList<>();
        adapter = new ServiceDetailsAdapter(this, serviceDetailsList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareData();

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        allReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allReviewsIntent=new Intent(ServiceDetails.this,Reviews.class);
                allReviewsIntent.putExtra("serviceId",serviceId);
                allReviewsIntent.putExtra("serviceName",serviceName);
                startActivity(allReviewsIntent);
            }
        });
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(ServiceDetails.this,ShowServiceLocation.class);
                startActivity(intent);
            }
        });
    }


    private void prepareData() {
        int[] logos = new int[]{
                R.drawable.reslogo
                };

        ServiceDetailsPojo a= new ServiceDetailsPojo( logos[0],"Coupa", 13.6);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        serviceDetailsList.add(a);
        adapter.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    public  void showServiceImages(View view){
     Intent intent=new Intent(ServiceDetails.this,ShowServicesImages.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent i=new Intent(ServiceDetails.this,ServicesList.class);
                i.putExtra("serviceSubCatName",serviceSubCat);
                startActivity(i);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
