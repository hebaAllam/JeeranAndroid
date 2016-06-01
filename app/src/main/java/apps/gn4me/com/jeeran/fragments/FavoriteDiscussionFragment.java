package apps.gn4me.com.jeeran.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import apps.gn4me.com.jeeran.R;


public  class FavoriteDiscussionFragment extends Fragment {
    int color;


    public FavoriteDiscussionFragment() {
    }

    @SuppressLint("ValidFragment")
    public FavoriteDiscussionFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_real_estate, container, false);

//        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
//        frameLayout.setBackgroundColor(color);
        

        return view;
    }
}