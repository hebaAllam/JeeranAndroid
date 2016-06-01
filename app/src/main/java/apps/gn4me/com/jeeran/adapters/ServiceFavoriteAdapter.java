package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;

/**
 * Created by ESCA on 5/31/2016.
 */
public class ServiceFavoriteAdapter extends RecyclerView.Adapter<ServiceHolder>{

    private List<RealEstate> mRealEstates;

    public ServiceFavoriteAdapter(List<RealEstate> mRealEstates) {
        this.mRealEstates = mRealEstates;
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_cell,parent,false);
        ServiceHolder viewHolder = new ServiceHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
        for (int i=0; i<mRealEstates.size(); i++) {
            holder.date.setText(mRealEstates.get(i).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mRealEstates.size();
    }
}
