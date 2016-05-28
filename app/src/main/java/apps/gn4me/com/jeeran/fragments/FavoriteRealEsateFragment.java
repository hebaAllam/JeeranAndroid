package apps.gn4me.com.jeeran.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import apps.gn4me.com.jeeran.R;


public  class FavoriteRealEsateFragment extends Fragment {
    int color;


    public FavoriteRealEsateFragment() {
    }

    @SuppressLint("ValidFragment")
    public FavoriteRealEsateFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);

        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
        frameLayout.setBackgroundColor(color);
        

        return view;
    }
}