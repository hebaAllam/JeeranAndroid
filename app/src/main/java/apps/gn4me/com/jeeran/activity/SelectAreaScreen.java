package apps.gn4me.com.jeeran.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.List;

import apps.gn4me.com.jeeran.R;

public class SelectAreaScreen extends AppCompatActivity {
    ArrayAdapter<String> dataAdapter;
    MaterialBetterSpinner areaSpinner;
    Button continueBtn ;
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

        continueBtn = (Button) findViewById(R.id.button);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(SelectAreaScreen.this,HomeActivity.class);
                startActivity(in);
            }
        });

    }
}
