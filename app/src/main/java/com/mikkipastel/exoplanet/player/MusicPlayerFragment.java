package com.mikkipastel.exoplanet.player;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.ui.TimeBar;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mikkipastel.exoplanet.PlanetAssistance;
import com.mikkipastel.exoplanet.R;
import com.mikkipastel.exoplanet.playlist.service.MusicList;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerFragment extends Fragment implements View.OnClickListener{

    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();

    int repeatMode = 0;
    public static final int REPEAT_NO = 0;
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_ALL = 2;

    private SimpleExoPlayer player;
    private SimpleExoPlayerView playerView;
    private ComponentListener componentListener;

    private long playbackPosition;
    private int currentWindow;

    int position;
    int size;
    List mList;
    String filepath;

    ImageView musicCover;
    TextView songname;
    ImageButton repeat;
    ImageButton skipPrevious;
    ImageButton skipNext;
    ImageButton play;
    ImageButton more;

    TimeBar mTimebar;

    boolean songplayed = false;
    boolean repeatall = false;

    public MusicPlayerFragment() {
        super();
    }

    public static MusicPlayerFragment newInstance(int position, ArrayList<String> list) {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PlayerActivity.BUNDLE_MUSIC_LIST, list);
        bundle.putInt(PlayerActivity.BUNDLE_POSITION, position);
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

        playerView = rootView.findViewById(R.id.player_view);
        playerView.setControllerHideOnTouch(false);
        playerView.setControllerShowTimeoutMs(0);
        playerView.showController();

        musicCover = rootView.findViewById(R.id.cover);
        songname = rootView.findViewById(R.id.text_song);
        repeat = rootView.findViewById(R.id.btn_repeat);
        skipPrevious = rootView.findViewById(R.id.btn_previous);
        play = rootView.findViewById(R.id.btn_play);
        skipNext = rootView.findViewById(R.id.btn_next);
        more = rootView.findViewById(R.id.btn_more);

        mTimebar = rootView.findViewById(R.id.exo_progress);

        repeat.setOnClickListener(this);
        skipPrevious.setOnClickListener(this);
        play.setOnClickListener(this);
        skipNext.setOnClickListener(this);

        Bundle bundle = getArguments();

        if (hasArgument(bundle)) {
            position = bundle.getInt(PlayerActivity.BUNDLE_POSITION);
            mList = bundle.getStringArrayList(PlayerActivity.BUNDLE_MUSIC_LIST);
            size = mList.size();

            setSongInPLayer(position);
        }

    }

    public void setSongInPLayer(int position) {
        MusicList musicList = (MusicList) mList.get(position);
        PlanetAssistance.setImageUrl(getContext(),
                musicList.getCover(),
                musicCover);
        songname.setText(musicList.getFilename());
        filepath = musicList.getUrl();

//        mTimebar.setPosition(Time.valueOf("00:00:00").getTime());
//        mTimebar.setBufferedPosition(1000);
//        mTimebar.setDuration(Time.valueOf("00:04:32").getTime());

    }

    private boolean hasArgument(Bundle bundle) {
        return bundle != null
                && bundle.getInt(PlayerActivity.BUNDLE_POSITION) > -1
                && bundle.getStringArrayList(PlayerActivity.BUNDLE_MUSIC_LIST) != null;
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
            TrackSelection.Factory adaptiveTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(BANDWIDTH_METER);

            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(adaptiveTrackSelectionFactory),
                    new DefaultLoadControl());
            player.addListener(componentListener);
            player.setVideoDebugListener(componentListener);
            player.setAudioDebugListener(componentListener);
            player.seekTo(currentWindow, playbackPosition);
            //player.setVolume(0.8f);
        }
    }

    private void releasePlayer() {
        playbackPosition = player.getCurrentPosition();
        currentWindow = player.getCurrentWindowIndex();
        player.setPlayWhenReady(false);
        player.removeListener(componentListener);
        player.setVideoDebugListener(null);
        player.setAudioDebugListener(null);
        player.release();
        player = null;
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "mediaPlayerSample"), BANDWIDTH_METER);
        return new ExtractorMediaSource(uri, dataSourceFactory, new DefaultExtractorsFactory(), null, null);
    }

    @Override
    public void onClick(View v) {
        if (v == play) {
            ClickToPlaySong();
        } else if (v == repeat) {
            ClickForRepeat();
        } else if (v == skipNext) {
            if (position < size - 1) {
                position += 1;
                setSongInPLayer(position);
            } else if (position == size - 1 && repeatall) {
                position = 0;
                setSongInPLayer(position);
            }

            if (!songplayed) {
                playSong();
            }
        } else if (v == skipPrevious) {
            if (position > 0) {
                position -= 1;
                setSongInPLayer(position);
            } else if (position == 0 && repeatall) {
                position = size - 1;
                setSongInPLayer(position);
            }

            if (!songplayed) {
                playSong();
            }
        }
    }

    public void playSong() {
        MediaSource mediaSource = buildMediaSource(Uri.parse(filepath));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    public void ClickToPlaySong() {
        if (songplayed) {
            play.setImageResource(R.drawable.ic_pause_circle_outline_white_48dp);

            playSong();
        } else {
            play.setImageResource(R.drawable.ic_play_circle_outline_white_48dp);

            player.setPlayWhenReady(false);
        }
        songplayed = !songplayed;
    }

    public void ClickForRepeat() {
        if (repeatMode == REPEAT_NO) {
            repeatMode = REPEAT_ONE;
            repeat.setImageResource(R.drawable.ic_repeat_one_white_24dp);
            player.setRepeatMode(Player.REPEAT_MODE_ONE);
        } else if (repeatMode == REPEAT_ONE) {
            repeatMode = REPEAT_ALL;
            repeat.setImageResource(R.drawable.ic_repeat_white_24dp);
            player.setRepeatMode(Player.REPEAT_MODE_OFF);

            repeatall = true;
        } else {
            repeatMode = REPEAT_NO;
            repeat.setImageResource(R.drawable.ic_repeat_black_24dp);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);

            repeatall = false;
        }
    }

}
