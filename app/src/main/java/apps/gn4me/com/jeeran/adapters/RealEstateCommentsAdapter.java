package apps.gn4me.com.jeeran.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.RealEstateComments;
import apps.gn4me.com.jeeran.pojo.RealEstateCommentPojo;
import apps.gn4me.com.jeeran.pojo.UserReview;

/**
 * Created by ESCA on 6/12/2016.
 */
public class RealEstateCommentsAdapter extends RecyclerView.Adapter<RealEstateCommentsAdapter.MyRealEstateViewHolder> {

private Context mContext;
private List<RealEstateCommentPojo> reviewsList;
    int size = 0;

    public void insertItem(RealEstateCommentPojo mReal) {
        size++;
        reviewsList.add(mReal);
        notifyDataSetChanged();
    }

    public class MyRealEstateViewHolder extends RecyclerView.ViewHolder {
    public TextView userName, userReview,reviewDate;
    public ImageView userImage, moreReviewOptions;

    public MyRealEstateViewHolder(View view) {
        super(view);
        userName= (TextView) view.findViewById(R.id.userName);
        userReview = (TextView) view.findViewById(R.id.userReview);
        reviewDate = (TextView) view.findViewById(R.id.reviewDate);
        userImage = (ImageView) view.findViewById(R.id.userImage);
        moreReviewOptions = (ImageView) view.findViewById(R.id.optionsInReviews);
        moreReviewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }
}
    public void insertAll(List<RealEstateCommentPojo> real){
        reviewsList.addAll(real);
//        Log.i("inside adapter -*-*-* ", mRealEstates.get(0).getMyRealEstate().getTitle() + " " + mRealEstates.get(1).getMyRealEstate().getTitle()+ " " + mRealEstates.get(2).getMyRealEstate().getTitle() );
        size = real.size();
        notifyDataSetChanged();
    }


    public RealEstateCommentsAdapter(Context mContext, List<RealEstateCommentPojo> reviewsList) {
        this.mContext = mContext;
        this.reviewsList = reviewsList;
    }

    @Override
    public MyRealEstateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_comment, parent, false);

        return new MyRealEstateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyRealEstateViewHolder holder, int position) {
        RealEstateCommentPojo userReview = reviewsList.get(position);
        holder.userName.setText(userReview.getUsername());
        holder.reviewDate.setText(userReview.getCommentDate());
        holder.userReview.setText(userReview.getUserComment());
        Glide.with(mContext).load(userReview.getUserImage()).into(holder.userImage);


    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    public void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.real_estate_menu_options, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

/**
 * Click listener for popup menu items
 */
class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

    public MyMenuItemClickListener() {
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.editRealEstate:
                Toast.makeText(mContext, "edit", Toast.LENGTH_SHORT).show();
                editComment();
                return true;
            case R.id.deleteRealEstate:
                Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                return true;
//            case R.id.addRealEstate:
//                Toast.makeText(mContext, "add", Toast.LENGTH_SHORT).show();
//                addComment();
//                return true;
            default:
        }
        return false;
    }
}

//    private void addComment() {
//        new RealEstateComments().addComment();
//    }

    private void editComment() {

    }

    @Override
    public int getItemCount() {
        return size;
    }
}
