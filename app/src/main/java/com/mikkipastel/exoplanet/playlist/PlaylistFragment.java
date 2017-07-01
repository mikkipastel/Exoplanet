package com.mikkipastel.exoplanet.playlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.player.PlayerFragment;

/**
 * Created by acer on 6/30/2017.
 */

public class PlaylistFragment extends Fragment {

    ListView songListView;
    listAdapter adapter;

    // sample to show before get from firebase storage
    int[] cover = {R.drawable.cover01,
            R.drawable.cover02,
            R.drawable.cover03,
            R.drawable.cover04,
            R.drawable.cover05};

    String[] songname = {"Breaks 6-29-2560 BE, 12_31 PM.wav",
            "Essential EDM 6-23-2560 BE, 5_24 PM.wav",
            "Hip Hop 6-27-2560 BE, 7_12 PM.wav",
            "House 2 6-28-2560 BE, 9_08 PM.wav",
            "House 6-23-2560 BE, 5_43 PM.wav"};

    public PlaylistFragment() {
        super();
    }

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
        initInstances(rootView);
        return rootView;
    }

    public void initInstances(View rootView){
        songListView = (ListView)rootView.findViewById(R.id.songlist);
        adapter = new listAdapter(getContext(), cover, songname);
        songListView.setAdapter(adapter);

        songListView.setOnItemClickListener(listViewItemClickListener);
    }

    final AdapterView.OnItemClickListener listViewItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position < cover.length){
                PlayerFragment.FragmentListener listener = (PlayerFragment.FragmentListener) getActivity();
                listener.onListItemClick(position);
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }
}
