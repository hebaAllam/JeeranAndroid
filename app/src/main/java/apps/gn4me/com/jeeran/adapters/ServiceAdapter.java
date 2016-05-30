package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.Service;

/**
 * Created by acer on 5/28/2016.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder>{
    List<Service> ServicesList;

    public ServiceAdapter(List<Service> ServicesList) {
        this.ServicesList = ServicesList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView servicelogo;
        public TextView serviceName;
        public MyViewHolder(View view) {
            super(view);
            serviceName= (TextView) view.findViewById(R.id.superMarketName);
            servicelogo = (ImageView) view.findViewById(R.id.superMarketImage);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_service, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Service service = ServicesList.get(position);
        holder.serviceName.setText(service.getName());
        holder.servicelogo.setImageResource(service.getLogoImg());
    }

    @Override
    public int getItemCount() {
        return ServicesList.size();
    }


}
