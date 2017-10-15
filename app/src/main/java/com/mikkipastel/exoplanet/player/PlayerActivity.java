package com.mikkipastel.exoplanet.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mikkipastel.exoplanet.R;

public class PlayerActivity extends AppCompatActivity {

    public static final String BUNDLE_POSITION = "position";
    public static final String BUNDLE_FILENAME = "filename";

    int trackNo;
    String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        trackNo = getIntent().getIntExtra(BUNDLE_POSITION, -1);
        filename = getIntent().getStringExtra(BUNDLE_FILENAME);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, MusicPlayerFragment.newInstance(trackNo, filename))
                    .commit();
        }
    }
}
