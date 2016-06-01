package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.ServicesCategory;

/**
 * Created by acer on 5/27/2016.
 */
public class ServiceCategoryAdapter extends RecyclerView.Adapter<ServiceCategoryAdapter.MyViewHolder>{
    List<ServicesCategory> ServiceList;
    CardView cardView;
    public ServiceCategoryAdapter(List<ServicesCategory> ServiceList) {
        this.ServiceList = ServiceList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceName;
        public Button serviceNumber;

        public ImageView serviceIcon;

        public MyViewHolder(View view) {
            super(view);
            serviceName = (TextView) view.findViewById(R.id.serviceCatName);
            serviceNumber = (Button) view.findViewById(R.id.servicesNumber);
            serviceIcon = (ImageView) view.findViewById(R.id.serviceCatImg);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_services_category, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServicesCategory servicesCategory = ServiceList.get(position);
        holder.serviceName.setText(servicesCategory.getServiceCatName());
        holder.serviceNumber.setText(servicesCategory.getServiceCatNumber());
       holder.serviceIcon.setImageResource(servicesCategory.getServiceCatIcon());
    }

    @Override
    public int getItemCount() {
        return ServiceList.size();
    }


}
