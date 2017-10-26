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
import android.widget.TextView;

import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.player.PlayerActivity;
import com.mikkipastel.exoplanet.playlist.service.IMusicView;
import com.mikkipastel.exoplanet.playlist.service.MusicList;
import com.mikkipastel.exoplanet.playlist.service.MusicPresenter;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment implements ItemListener, IMusicView {

    RecyclerView songListView;
    RecyclerAdapter adapter;
    List mMusicList;

    TextView status;

    MusicPresenter mPresenter;

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

        mPresenter = new MusicPresenter(this);
        mPresenter.fetchMusic();

        initInstances(rootView);
        return rootView;
    }

    public void initInstances(View rootView){
        songListView = rootView.findViewById(R.id.songlist);
        status = rootView.findViewById(R.id.text_status);
    }

    @Override
    public void onFetchSuccess(List<MusicList> musicLists) {
        if (isAdded() && musicLists != null) {
            songListView.setVisibility(View.VISIBLE);
            status.setVisibility(View.GONE);

            mMusicList = musicLists;

            adapter = new RecyclerAdapter(this, getContext(), musicLists);
            songListView.setNestedScrollingEnabled(false);
            songListView.setLayoutManager(new LinearLayoutManager(getActivity()
                    , LinearLayoutManager.VERTICAL
                    , false));
            songListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFetchFail() {
        songListView.setVisibility(View.GONE);
        status.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(getActivity(), PlayerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(PlayerActivity.BUNDLE_POSITION, position);
        bundle.putStringArrayList(PlayerActivity.BUNDLE_MUSIC_LIST, new ArrayList<String>(mMusicList));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
