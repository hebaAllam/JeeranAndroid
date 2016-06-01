package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import apps.gn4me.com.jeeran.R;

/**
 * Created by ESCA on 5/31/2016.
 */
public class ServiceHolder extends RecyclerView.ViewHolder {

    public TextView date;

    public ServiceHolder(View view) {
        super(view);
        this.date = (TextView) view.findViewById(R.id.super_market);
    }
}
