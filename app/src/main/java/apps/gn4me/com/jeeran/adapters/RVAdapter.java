package apps.gn4me.com.jeeran.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.pojo.FavoriteRealEstate;

/**
 * Created by ESCA on 5/28/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<ViewHolder>{

    private List<FavoriteRealEstate> mRealEstates = new ArrayList<>();

    private int size = 0;
    private String typee = "rent";
    View view;

    public RVAdapter(List<FavoriteRealEstate> mRealEstates) {
        this.mRealEstates = mRealEstates;
        size = mRealEstates.size();
    }

    public RVAdapter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public void insertItem(FavoriteRealEstate real){
        mRealEstates.add(real);
        size++;
        notifyDataSetChanged();
    }

    public void insertAll(List<FavoriteRealEstate> real){
        mRealEstates=new ArrayList<>();
        mRealEstates.addAll(real);
//        Log.i("inside adapter -*-*-* ", mRealEstates.get(0).getMyRealEstate().getTitle() + " " + mRealEstates.get(1).getMyRealEstate().getTitle()+ " " + mRealEstates.get(2).getMyRealEstate().getTitle() );
        size = real.size();
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

//        setHasStableIds(true);
        for (int i=0; i<mRealEstates.size(); i++) {
//            holder.next.setImageDrawable(mRealEstates.get(position).getMyRealEstate().getImg().);
//            holder.type.setText(mRealEstates.get(position).getMyRealEstate().getType());
//            holder.title
            if (mRealEstates.get(position).getMyRealEstate().getType() == 0)
                typee = "sale";
            else
            typee = "rent";
            holder.title_realEstate.setText(mRealEstates.get(position).getMyRealEstate().getTitle()
            + " For " + typee);
            Picasso.with(view.getContext())
                    .load(mRealEstates.get(position).getMyRealEstate().getImg())
                    .error(R.drawable.ic_error )
                    .placeholder( R.drawable.progress_animation )
                    .into(holder.img);

            holder.location.setText(mRealEstates.get(position).getMyRealEstate().getLocation());
            holder.price.setText(mRealEstates.get(position).getMyRealEstate().getPrice()+"");
            holder.date.setText(mRealEstates.get(position).getMyRealEstate().getCreationDate().toString());
        }

        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("*-*-*-*-*-"," Item : " + mRealEstates.get(position).getMyRealEstate().getTitle());
            }
        });


    }

    @Override
    public int getItemCount() {
        return size;
    }
}
