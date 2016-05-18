package apps.gn4me.com.jeeran.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;

public class SelectAreaScreen extends AppCompatActivity {
    ArrayAdapter<String> dataAdapter;
    MaterialBetterSpinner areaSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area_screen);
        areaSpinner= (MaterialBetterSpinner) findViewById(R.id.areaSpinner);
        List<String> intervalType = new ArrayList<String>();
        intervalType.add("ELrehab");
        intervalType.add("Octobar");
        intervalType.add("Giza");
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, intervalType);
        areaSpinner.setAdapter(dataAdapter);
    }
}
