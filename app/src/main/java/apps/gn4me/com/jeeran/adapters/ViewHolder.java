package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import apps.gn4me.com.jeeran.R;

/**
 * Created by EMAN on 14/05/2016.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView date;
    public TextView price;
    public TextView location;
    public ImageView next;

    public ViewHolder(View view) {
        super(view);
        this.date = (TextView) view.findViewById(R.id.date_text);
        this.price = (TextView) view.findViewById(R.id.price_text);
        this.location = (TextView) view.findViewById(R.id.location_text);
        this.next = (ImageView)view.findViewById(R.id.next);


    }


}
