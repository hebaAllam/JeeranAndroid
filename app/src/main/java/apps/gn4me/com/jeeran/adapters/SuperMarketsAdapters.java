package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.SuperMarket;

/**
 * Created by acer on 5/28/2016.
 */
public class SuperMarketsAdapters extends RecyclerView.Adapter<SuperMarketsAdapters.MyViewHolder>{
    List<SuperMarket> superMarketsList;

    public SuperMarketsAdapters(List<SuperMarket> superMarketsList) {
        this.superMarketsList = superMarketsList;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView superMarketlogo;
        public TextView superMarketName;
        public MyViewHolder(View view) {
            super(view);
            superMarketName= (TextView) view.findViewById(R.id.superMarketName);
            superMarketlogo = (ImageView) view.findViewById(R.id.superMarketImage);
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_supermarket, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SuperMarket superMarket= superMarketsList.get(position);
        holder.superMarketName.setText(superMarket.getName());
        holder.superMarketlogo.setImageResource(superMarket.getLogoImg());
    }

    @Override
    public int getItemCount() {
        return superMarketsList.size();
    }


}
