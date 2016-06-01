package apps.gn4me.com.jeeran.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.ReviewAdapter;
import apps.gn4me.com.jeeran.pojo.UserReview;

public class Reviews extends BaseActivity {
    RecyclerView reviewsRecyclerView;
    ReviewAdapter myAdapter;
    private List<UserReview> reviewList=new ArrayList<>();
  private ImageView backeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        reviewsRecyclerView = (RecyclerView) findViewById(R.id.recycle_reviews);
        backeIcon=(ImageView)findViewById(R.id.backFromReviews);
        reviewList.clear();
        myAdapter=new ReviewAdapter(this,reviewList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        reviewsRecyclerView.setLayoutManager(mLayoutManager);
        reviewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewsRecyclerView.setAdapter(myAdapter);
        reviewsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        prepareData();
        backeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void prepareData() {
        UserReview userReview=new UserReview(1,R.drawable.ic_account_circle_white_64dp,"Mohamed Ali",5,"01/6/2016","Absolutely Amazing ,The menu offers a wide variety of mouth-watering starters,All the products were fresh and the dishes had the warmth of home-made food.");
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        reviewList.add(userReview);
        myAdapter.notifyDataSetChanged();

    }
}
