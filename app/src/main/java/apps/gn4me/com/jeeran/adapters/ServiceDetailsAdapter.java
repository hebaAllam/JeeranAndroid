package apps.gn4me.com.jeeran.adapters;

import android.content.Context;
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
import apps.gn4me.com.jeeran.pojo.Service;
import apps.gn4me.com.jeeran.pojo.ServiceDetailsPojo;

/**
 * Created by acer on 5/29/2016.
 */
public class ServiceDetailsAdapter extends RecyclerView.Adapter<ServiceDetailsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Service> serviceDetailsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView serviceName, serviceRates;
        public ImageView serviceLogo, moreOptions;

        public MyViewHolder(View view) {
            super(view);
            serviceName= (TextView) view.findViewById(R.id.resName);
            serviceRates = (TextView) view.findViewById(R.id.rates);
            serviceLogo = (ImageView) view.findViewById(R.id.resImage);
            moreOptions = (ImageView) view.findViewById(R.id.more_options);
        }
    }


    public ServiceDetailsAdapter(Context mContext, List<Service> serviceDetailsList) {
        this.mContext = mContext;
        this.serviceDetailsList = serviceDetailsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.restaurant_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Service service = serviceDetailsList.get(position);
        holder.serviceName.setText(service.getName());
        holder.serviceRates.setText(service.getRates() + "*");
        Glide.with(mContext).load(service.getLogo()).into(holder.serviceLogo);
        holder.moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.moreOptions);
            }
        });

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
        return serviceDetailsList.size();
    }
}