package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;

/**
 * Created by ESCA on 6/9/2016.
 */
public class RealEstateAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<RealEstate> mRealEstates = new ArrayList<>();

    private int size = 0;
    private String typee = "rent";

    public RealEstateAdapter(List<RealEstate> mRealEstates) {
        this.mRealEstates = mRealEstates;
        size = mRealEstates.size();
    }

    public RealEstateAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void insertItem(RealEstate real){
        mRealEstates.add(real);
        size++;
        notifyDataSetChanged();
    }

    public void insertAll(List<RealEstate> real){
        mRealEstates.addAll(real);
//        Log.i("inside adapter -*-*-* ", mRealEstates.get(0).getMyRealEstate().getTitle() + " " + mRealEstates.get(1).getMyRealEstate().getTitle()+ " " + mRealEstates.get(2).getMyRealEstate().getTitle() );
        size = real.size();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        setHasStableIds(true);
        for (int i=0; i<mRealEstates.size(); i++) {
            if (mRealEstates.get(position).getType() == 0)
                typee = "sale";
            else
                typee = "rent";
            holder.title_realEstate.setText(mRealEstates.get(position).getTitle()
                    + " For " + typee);
            holder.location.setText(mRealEstates.get(position).getLocation());
            holder.price.setText(mRealEstates.get(position).getPhone());
            holder.date.setText(mRealEstates.get(position).getTitle());
        }

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.i("*-*-*-*-*-"," Item : " + mRealEstates.get(position).getTitle());
            }
        });
    }


    @Override
    public int getItemCount() {
        return size;
    }
}
