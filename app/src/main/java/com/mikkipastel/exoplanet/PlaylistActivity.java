package com.mikkipastel.exoplanet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mikkipastel.exoplanet.about.AboutActivity;
import com.mikkipastel.exoplanet.player.PlayerActivity;
import com.mikkipastel.exoplanet.player.PlayerFragment;
import com.mikkipastel.exoplanet.playlist.PlaylistFragment;

public class PlaylistActivity extends AppCompatActivity
        implements PlayerFragment.FragmentListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        if (savedInstanceState == null) {
            //1st created
            //place fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contentContainer, PlaylistFragment.newInstance())
                    .commit();
        }

        SharedPreferences prefs = getSharedPreferences("data_install", MODE_PRIVATE);
        boolean insertBranch = prefs.getBoolean("install_status", false);
        if (insertBranch) {
            addShortcut();
        }
    }

    private void addShortcut() {
        //Adding shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(getApplicationContext(), PlaylistActivity.class);

        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Exoplanet");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.ic_launcher));

        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_aboutapp) {
            Intent goAbout = new Intent(PlaylistActivity.this, AboutActivity.class);
            startActivity(goAbout);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int pos) {
        Intent goPlayer = new Intent(PlaylistActivity.this, PlayerActivity.class);
        goPlayer.putExtra("songTrack", pos);
        startActivity(goPlayer);
    }
}
