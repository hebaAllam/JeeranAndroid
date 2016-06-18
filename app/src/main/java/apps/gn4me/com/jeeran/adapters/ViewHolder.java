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
    public ImageView img;
    public TextView title_realEstate;

    public ViewHolder(View view) {
        super(view);
        this.date = (TextView) view.findViewById(R.id.date_text);
        this.price = (TextView) view.findViewById(R.id.price_textView);
        this.location = (TextView) view.findViewById(R.id.location_text);
        this.next = (ImageView)view.findViewById(R.id.next);
        this.title_realEstate = (TextView)view.findViewById(R.id.title_txtView_real);
        img = (ImageView)view.findViewById(R.id.superMarketImage);
    }


}
