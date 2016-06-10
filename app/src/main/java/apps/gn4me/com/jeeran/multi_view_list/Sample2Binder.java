package apps.gn4me.com.jeeran.multi_view_list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.activity.CommentsActivity;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;

/**
 * Created by cym on 15/5/18.
 */
public class Sample2Binder extends DataBinder<Sample2Binder.ViewHolder> {
    private List<DiscussionPostData> mList;
    private int startIndex ;

    public Sample2Binder(UltimateDifferentViewTypeAdapter dataBindAdapter, List<DiscussionPostData> mList , int startIndex) {
        super(dataBindAdapter);
        this.startIndex = startIndex ;
        this.mList = mList;
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.post_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, final int position) {

        final int index = position - startIndex ;

        Log.i("Titleeeeeeee2" , mList.get(index).getTitle());

        holder.name.setText(mList.get(index).getUser().getUserName());
        holder.title.setText(mList.get(index).getTitle());

        holder.details.setText(mList.get(index).getDetails());
        holder.timeStamp.setText(mList.get(index).getTimeStamp());


        Picasso.with( holder.context )
                .load( mList.get(index).getUser().getImage() )
                .error(R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into(holder.profilePic);


        Picasso.with( holder.context )
                .load(mList.get(index).getImage())
                .error(R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into(holder.feedImageView);


        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });

        final Context context = holder.context ;
        final Integer disc_id = mList.get(index).getId() ;
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CardView :::" , "comment clicked of card no. " + index);
                Intent i = new Intent( context , CommentsActivity.class);
                i.putExtra("disc_id" , disc_id.toString());
                context.startActivity(i);

            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size() ;
    }

    static public class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView name;
        TextView timeStamp;
        TextView title;
        TextView details;
        ImageView profilePic;
        ImageView feedImageView;
        AppCompatButton comment ;
        AppCompatButton favorite ;
        AppCompatButton report ;
        Toolbar toolbar ;
        Context context ;

        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.name);
            timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            title = (TextView) itemView.findViewById(R.id.title);
            details = (TextView) itemView.findViewById(R.id.details);

            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            feedImageView = (ImageView) itemView.findViewById(R.id.feedImage1);


            comment = (AppCompatButton) itemView.findViewById(R.id.commentBtn);
            favorite = (AppCompatButton) itemView.findViewById(R.id.favoriteBtn);
            report = (AppCompatButton) itemView.findViewById(R.id.reportBtn);


            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
            //toolbar.setTitle("Card Toolbar");
            if (toolbar != null) {
                // inflate your menu
                toolbar.inflateMenu(R.menu.main);
            }
        }
    }
    public void addAll(List<DiscussionPostData> dataSet) {
        mList.addAll(dataSet);
        notifyBinderDataSetChanged();
    }

    public void updateList() {
        notifyBinderDataSetChanged();
    }

    public void insert(DiscussionPostData post, int position) {
        mList.add(post);
        notifyBinderDataSetChanged();
    }

    public void remove(int position) {
        mList.remove(position);
        notifyBinderDataSetChanged();
    }

}
