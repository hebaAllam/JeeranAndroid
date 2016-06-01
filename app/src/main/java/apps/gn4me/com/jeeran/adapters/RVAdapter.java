package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;

/**
 * Created by ESCA on 5/28/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<ViewHolder>{

    private List<RealEstate> mRealEstates;

    public RVAdapter(List<RealEstate> mRealEstates) {
        this.mRealEstates = mRealEstates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        for (int i=0; i<mRealEstates.size(); i++) {
            holder.location.setText(mRealEstates.get(i).getLocation());
            holder.price.setText(mRealEstates.get(i).getPhone());
            holder.date.setText(mRealEstates.get(i).getTitle());
        }

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("*-*-*-*-*-"," Item : " + mRealEstates.get(position).getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRealEstates.size();
    }
}
