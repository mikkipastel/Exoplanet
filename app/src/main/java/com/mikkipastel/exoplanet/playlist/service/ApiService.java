package com.mikkipastel.exoplanet.playlist.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/musiclist")
    Call<List<MusicList>> getMusicList();
}
