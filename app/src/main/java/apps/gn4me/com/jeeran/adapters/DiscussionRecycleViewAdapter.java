package apps.gn4me.com.jeeran.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;

import java.util.List;

import apps.gn4me.com.jeeran.multi_view_list.AddPostViewBinder;
import apps.gn4me.com.jeeran.multi_view_list.PostViewBinder;
import apps.gn4me.com.jeeran.pojo.DiscussionPostData;

/**
 * Created by menna on 6/3/2016.
 */
public class DiscussionRecycleViewAdapter extends UltimateDifferentViewTypeAdapter {

    private int viewType ;
    private int count = 0 ;
    public DiscussionRecycleViewAdapter( int viewType , Context context){
        this.viewType = viewType ;
        if ( viewType == 0 ) {
            putBinder(SampleViewType.SAMPLE2, new PostViewBinder(this ,0,context));
        }else if (viewType == 1){
            putBinder(SampleViewType.SAMPLE1, new AddPostViewBinder(this,context));
            putBinder(SampleViewType.SAMPLE2, new PostViewBinder(this ,1,context));
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public int getAdapterItemCount() {
        return count ;
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if ( viewType == 0 ) {
            ((PostViewBinder) getDataBinder(SampleViewType.SAMPLE2)).bindViewHolder((PostViewBinder.ViewHolder) holder, position);
        }else if ( viewType == 1 ){
            if (position == 0) {
                ((AddPostViewBinder) getDataBinder(SampleViewType.SAMPLE1)).bindViewHolder((AddPostViewBinder.ViewHolder) holder, position);
            } else {
                ((PostViewBinder) getDataBinder(SampleViewType.SAMPLE2)).bindViewHolder((PostViewBinder.ViewHolder) holder, position);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public Enum getEnumFromPosition(int position) {
        if ( viewType == 0 ) {
            return SampleViewType.SAMPLE2;
        }else {
            if (position == 0) {
                return SampleViewType.SAMPLE1;
            } else {
                return SampleViewType.SAMPLE2;
            }
        }
    }

    @Override
    public Enum getEnumFromOrdinal(int ordinal) {
        return SampleViewType.values()[ordinal];
    }

    enum SampleViewType {
        SAMPLE1, SAMPLE2
    }

    public void insert(DiscussionPostData post, int position) {
        count++ ;
        ((PostViewBinder) getDataBinder(SampleViewType.SAMPLE2)).insert(post,position);
    }

    public void insertAll(List<DiscussionPostData> posts) {
        count += posts.size();
        ((PostViewBinder) getDataBinder(SampleViewType.SAMPLE2)).addAll(posts);
    }

    public void refresh() {
        ((PostViewBinder) getDataBinder(SampleViewType.SAMPLE2)).updateList();
    }



    public void remove(int position) {
        ((PostViewBinder) getDataBinder(SampleViewType.SAMPLE2)).remove(position);
    }
}
