package apps.gn4me.com.jeeran.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.DiscussionCommentData;


public class CustomAdapter extends UltimateViewAdapter<CustomAdapter.SimpleAdapterViewHolder> {

    private List<DiscussionCommentData> mList;
    public CustomAdapter(List<DiscussionCommentData> mList) {
        this.mList = mList;
    }


    @Override
    public void onBindViewHolder(final SimpleAdapterViewHolder holder, int position) {
        Log.i("index here :::" , "" + mList.size());
        holder.name.setText(mList.get(position).getUser().getUserName());
        holder.comment.setText(mList.get(position).getComment());

        holder.timeStamp.setText(mList.get(position).getTimeStamp());



        Picasso.with(holder.context)
                .load(mList.get(position).getUser().getImage())
                .error(R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into(holder.profilePic);


        holder.toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return true;
            }
        });
    }

    @Override
    public int getAdapterItemCount() {
        return mList.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list_item, parent, false);
        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v);
        return vh;
    }


    public void insert(DiscussionCommentData comment, int position) {
        //insertInternal(mList, comment , position);
        notifyDataSetChanged();
    }

    public void insertAll(List<DiscussionCommentData> comments) {
        //insertInternal(mList, comment , position);
        mList.addAll(comments);
        notifyDataSetChanged();
    }

    public void refresh(){
        notifyDataSetChanged();
    }

    public void remove(int position) {
        removeInternal(mList, position);
    }

    public void clear() {
        clearInternal(mList);
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }


    @Override
    public long generateHeaderId(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stick_header_item, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        //TextView textView = (TextView) viewHolder.itemView.findViewById(R.id.stick_text);
        //viewHolder.itemView.setBackgroundColor(Color.parseColor("#AAffffff"));

    }

    @Override
    public void onItemDismiss(int position) {
        if (position > 0)
            remove(position);
        //notifyItemRemoved(position);
        //notifyDataSetChanged();
        super.onItemDismiss(position);
    }


    public class SimpleAdapterViewHolder extends UltimateRecyclerviewViewHolder {

        TextView name;
        TextView timeStamp;
        ImageView profilePic;
        TextView comment ;
        Toolbar toolbar ;
        Context context;

        public SimpleAdapterViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            name = (TextView) itemView.findViewById(R.id.name);
            timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            comment = (TextView) itemView.findViewById(R.id.comment);

            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);

            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
            //toolbar.setTitle("Card Toolbar");
            if (toolbar != null) {
                // inflate your menu
                toolbar.inflateMenu(R.menu.owner_menu);
            }

        }
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }



}