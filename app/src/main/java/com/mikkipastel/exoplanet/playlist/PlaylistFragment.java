package com.mikkipastel.exoplanet.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.player.PlayerActivity;

public class PlaylistFragment extends Fragment implements ItemListener {

    RecyclerView songListView;
    RecyclerAdapter adapter;

    // sample to show before get from firebase storage
    int[] cover = {R.drawable.cover01,
            R.drawable.cover02,
            R.drawable.cover03,
            R.drawable.cover04,
            R.drawable.cover05};

    public String[] songname = {"capsule_glitchstep.mp3",
            "DataAnalysis_Glitchstep.mp3"};
//            "Breaks 6-29-2560 BE, 12_31 PM.wav",
//            "Essential EDM 6-23-2560 BE, 5_24 PM.wav",
//            "Hip Hop 6-27-2560 BE, 7_12 PM.wav",
//            "House 2 6-28-2560 BE, 9_08 PM.wav",
//            "House 6-23-2560 BE, 5_43 PM.wav"};

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
        songListView = (RecyclerView) rootView.findViewById(R.id.songlist);

        adapter = new RecyclerAdapter(this, getContext(), cover, songname);
        songListView.setNestedScrollingEnabled(false);
        songListView.setLayoutManager(new LinearLayoutManager(getActivity()
                , LinearLayoutManager.VERTICAL
                , false));
        songListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

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

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        intent.putExtra(PlayerActivity.BUNDLE_POSITION, position);
        intent.putExtra(PlayerActivity.BUNDLE_FILENAME, songname[position]);
        startActivity(intent);
    }
}
