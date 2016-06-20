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
import apps.gn4me.com.jeeran.pojo.UserReview;

/**
 * Created by acer on 5/30/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<UserReview> reviewsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userReview,reviewDate;
        public ImageView userImage,moreReviewOptions;

        public MyViewHolder(View view) {
            super(view);
            userName= (TextView) view.findViewById(R.id.userName);
            userReview = (TextView) view.findViewById(R.id.userReview);
            reviewDate = (TextView) view.findViewById(R.id.reviewDate);
            userImage = (ImageView) view.findViewById(R.id.userImage);
            moreReviewOptions = (ImageView) view.findViewById(R.id.optionsInReviews);
        }
    }


    public ReviewAdapter(Context mContext, List<UserReview> reviewsList) {
        this.mContext = mContext;
        this.reviewsList = reviewsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UserReview userReview = reviewsList.get(position);
        holder.userName.setText(userReview.getUser().getUserName());
        holder.reviewDate.setText(userReview.getReviewDate());
        holder.userReview.setText(userReview.getReviewContent());
        holder.moreReviewOptions.setTag(position);
        Glide.with(mContext).load(userReview.getUser().getImage()).into(holder.userImage);

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_restaurant, popup.getMenu());
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
            MyViewHolder holder = null;
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:

                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }
}
