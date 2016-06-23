package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.RealEstate;
import apps.gn4me.com.jeeran.pojo.Service;
import apps.gn4me.com.jeeran.pojo.ServiceFavorites;

/**
 * Created by ESCA on 5/31/2016.
 */
public class ServiceFavoriteAdapter extends RecyclerView.Adapter<ServiceHolder>{

    private List<ServiceFavorites> mservices = new ArrayList<>();
    int size = 0;
    View view;

    public ServiceFavoriteAdapter() {
        mservices = new ArrayList<>();
    }

    public ServiceFavoriteAdapter(List<ServiceFavorites> mservices, int size) {
        this.mservices = mservices;
        this.size = size;
    }

    public void insertItem(ServiceFavorites real){
        mservices.add(real);
        size++;
        notifyDataSetChanged();
    }

    public void insertAll(List<ServiceFavorites> real){
        mservices=new ArrayList<>();
        mservices.addAll(real);
//        Log.i("inside adapter -*-*-* ", mRealEstates.get(0).getMyRealEstate().getTitle() + " " + mRealEstates.get(1).getMyRealEstate().getTitle()+ " " + mRealEstates.get(2).getMyRealEstate().getTitle() );
        size = real.size();
        notifyDataSetChanged();
    }
    public ServiceFavoriteAdapter(List<ServiceFavorites> mservices) {
        this.mservices = mservices;
        this.size=mservices.size();
    }

    @Override
    public ServiceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_cell,parent,false);
        ServiceHolder viewHolder = new ServiceHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceHolder holder, int position) {
//        for (int i=0; i<mservices.size(); i++) {
//            holder.date.setText(mservices.get(i).getName());
        Picasso.with(view.getContext())
                .load(mservices.get(position).getMyServices().getLogo())
                .error(R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into(holder.logo);

//        Picasso.with(view.getContext()).load(mservices.get(position).getMyServices().getLogo()).placeholder(R.drawable.ic_error);
            holder.title.setText(mservices.get(position).getMyServices().getName());
//        }
    }


    @Override
    public int getItemCount() {
        return size;
    }
}
