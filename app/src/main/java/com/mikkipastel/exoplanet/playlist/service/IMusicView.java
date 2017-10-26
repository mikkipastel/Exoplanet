package com.mikkipastel.exoplanet.playlist.service;

import java.util.List;

public interface IMusicView {
    void onFetchSuccess(List<MusicList> musicLists);
    void onFetchFail();
}
