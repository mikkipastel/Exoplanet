package com.mikkipastel.exoplanet.player;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.dash.DashChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikkipastel.exoplanet.R;

import static com.mikkipastel.exoplanet.player.PlayerActivity.BUNDLE_FILENAME;
import static com.mikkipastel.exoplanet.player.PlayerActivity.BUNDLE_POSITION;

public class MusicPlayerFragment extends Fragment {

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;
    private ComponentListener componentListener;

    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;

    MediaSource mediaSource;
    int position;
    String filename;
    String filepath;

    public MusicPlayerFragment() {
        super();
    }

    public static MusicPlayerFragment newInstance(int position, String filename) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_POSITION, position);
        bundle.putString(BUNDLE_FILENAME, filename);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        initInstances(rootView);
        return rootView;
    }

    public void initInstances(View rootView){
        componentListener = new ComponentListener();
        playerView = (SimpleExoPlayerView) rootView.findViewById(R.id.video_view);

        Bundle bundle = getArguments();

        if (hasArgument(bundle)) {
            position = bundle.getInt(BUNDLE_POSITION);
            filename = bundle.getString(BUNDLE_FILENAME);
            getFirebaseStorage();
        }
    }

    private boolean hasArgument(Bundle bundle) {
        return bundle != null
                && bundle.getInt(BUNDLE_POSITION) > -1
                && bundle.getString(BUNDLE_FILENAME) != null;
    }

    public void getFirebaseStorage() {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference musicRef = storageRef.child("/Music/"+filename);
        //gs://mikkipastel.appspot.com/Music/DataAnalysis_Glitchstep.wav
        Log.d("__musicRef__", musicRef.toString());

        //https://firebasestorage.googleapis.com/v0/b/mikkipastel.appspot.com/o/Music%2Fcapsule_glitchstep.wav?alt=media
        musicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                downloadUrl.getPath();
                filepath = downloadUrl.toString();
                Log.d("__filepath__", filepath);
                mediaSource = buildMediaSource(Uri.parse(filepath));
                player.prepare(mediaSource, true, false);
            }
        });

        //return filepath;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        if (player == null) {
            // a factory to create an AdaptiveVideoTrackSelection
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);
            // using a DefaultTrackSelector with an adaptive video selection factory
            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory), new DefaultLoadControl());
            player.addListener(componentListener);
            player.setVideoDebugListener(componentListener);
            player.setAudioDebugListener(componentListener);
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
        }
        //MediaSource mediaSource = buildMediaSource(Uri.parse(getString(R.string.media_url_mp4)));
        //player.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        playWhenReady = player.getPlayWhenReady();
        player.removeListener(componentListener);
        player.setVideoListener(null);
        player.setVideoDebugListener(null);
        player.setAudioDebugListener(null);
        player.release();
        player = null;
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory("ua", BANDWIDTH_METER);
        DashChunkSource.Factory dashChunkSourceFactory = new DefaultDashChunkSource.Factory(
                dataSourceFactory);
        return new DashMediaSource(uri, dataSourceFactory, dashChunkSourceFactory, null, null);
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }
}
