package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import apps.gn4me.com.jeeran.R;

/**
 * Created by ESCA on 5/31/2016.
 */
public class ServiceHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public ImageView logo;

    public ServiceHolder(View view) {
        super(view);
        this.title = (TextView) view.findViewById(R.id.super_market);
        this.logo = (ImageView) view.findViewById(R.id.superMarketImage);
    }
}
