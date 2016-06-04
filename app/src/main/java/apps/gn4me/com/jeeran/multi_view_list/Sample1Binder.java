package apps.gn4me.com.jeeran.multi_view_list;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.marshalchen.ultimaterecyclerview.UltimateDifferentViewTypeAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.multiViewTypes.DataBinder;

import apps.gn4me.com.jeeran.R;

/**
 * Created by cym on 15/5/18.
 */
public class Sample1Binder extends DataBinder<Sample1Binder.ViewHolder> {


    public Sample1Binder(UltimateDifferentViewTypeAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_post_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {
        String[] items = new String[]{"Taxs", "Transportation", "Expensive Food"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.context, android.R.layout.simple_spinner_dropdown_item, items);
        holder.problemSpinner.setAdapter(adapter);

        holder.postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = holder.postEditTxt.getText().toString();
                Log.i("add post :::" , post);
            }
        });

        holder.photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static public class ViewHolder extends UltimateRecyclerviewViewHolder {

        EditText postEditTxt ;
        Spinner problemSpinner ;
        ImageView profilePic ;
        AppCompatButton photoBtn ;
        AppCompatButton postBtn ;
        Context context ;
        public ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            postEditTxt = (EditText) itemView.findViewById(R.id.post_edit_text);
            problemSpinner = (Spinner) itemView.findViewById(R.id.spinner);
            profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
            photoBtn = (AppCompatButton) itemView.findViewById(R.id.photo_btn);
            postBtn = (AppCompatButton) itemView.findViewById(R.id.add_btn);
        }
    }
}
