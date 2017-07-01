package com.mikkipastel.exoplanet.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.about.AboutFragment;

/**
 * Created by acer on 6/30/2017.
 */

public class PlayerActivity extends AppCompatActivity {

    int trackNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        trackNo = getIntent().getIntExtra("songTrack", 0);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, PlayerFragment.newInstance())
                    .commit();
        }
    }

    /*public void onListItemClick() {
        finish();
        getSupportFragmentManager().beginTransaction()
                .remove(PlayerFragment.newInstance())
                .commit();
    }*/
}
