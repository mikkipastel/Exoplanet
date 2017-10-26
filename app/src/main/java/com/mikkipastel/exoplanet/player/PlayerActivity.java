package com.mikkipastel.exoplanet.player;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.playlist.service.MusicList;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    public static final String BUNDLE_MUSIC_LIST = "musiclist";
    public static final String BUNDLE_POSITION = "position";

    MusicList mMusicList;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt(BUNDLE_POSITION);
        mMusicList = bundle.getParcelable(BUNDLE_MUSIC_LIST);


        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, MusicPlayerFragment.newInstance(position, mMusicList))
                    .commit();
        }
    }
}
