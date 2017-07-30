package com.a3035347861.hku.smartphoneasg1;

/**
 * Created by 10836 on 2016-12-17.
 */
import java.util.ArrayList; import java.util.Arrays;

import android.support.v7.app.AppCompatActivity; import android.os.Bundle; import android.widget.ArrayAdapter; import android.widget.ListView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    /** Called when the activity is first created. */
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.mainListView );
        String[] planets = new String[] { "Mercury", "Venus", "Earth"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets));
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);
        mainListView.setAdapter( listAdapter );

    }
}
