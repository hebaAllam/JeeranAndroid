package apps.gn4me.com.jeeran.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;
import apps.gn4me.com.jeeran.adapters.DividerItemDecoration;
import apps.gn4me.com.jeeran.adapters.RVAdapter;
import apps.gn4me.com.jeeran.pojo.RealEstate;


public  class FavoriteRealEsateFragment extends Fragment {
    int color;

    private List<RealEstate> realEstates;
    private RecyclerView.Adapter adapter;
    RecyclerView rv;
    View view;
    LinearLayoutManager llm;

    public FavoriteRealEsateFragment() {
    }

    @SuppressLint("ValidFragment")
    public FavoriteRealEsateFragment(int color) {
        this.color = color;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_real_estate, container, false);

//        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
//        frameLayout.setBackgroundColor(color);

        rv = (RecyclerView)view.findViewById(R.id.rv);
        
//        rv.setHasFixedSize(true);
        llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);
        rv.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
        initializeData();

        adapter = new RVAdapter(realEstates);
        rv.setAdapter(adapter);



        return view;
    }

    private void initializeData(){
        realEstates = new ArrayList<>();
        realEstates.add(new RealEstate("Flat1","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
        realEstates.add(new RealEstate("Flat2","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
        realEstates.add(new RealEstate("Flat3","my Flat", "2255", "address", "0123555333", "0321558875", "email"));
    }
}