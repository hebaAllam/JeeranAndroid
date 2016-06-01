package apps.gn4me.com.jeeran.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import apps.gn4me.com.jeeran.R;

/**
 * Created by acer on 5/17/2016.
 */
public class RealEstate  extends Fragment {

    public RealEstate() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.real_estate_fragment, container, false);
    }

}