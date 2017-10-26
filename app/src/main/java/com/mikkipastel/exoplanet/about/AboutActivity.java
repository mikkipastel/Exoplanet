package com.mikkipastel.exoplanet.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mikkipastel.exoplanet.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, AboutFragment.newInstance())
                    .commit();
        }
    }
}
