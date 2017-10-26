package com.mikkipastel.exoplanet.playlist.service;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicPresenter implements IMusicPresenter {
    IMusicView mView;

    public MusicPresenter(IMusicView view) {
        this.mView = view;
    }

    @Override
    public void fetchMusic() {
        Call<List<MusicList>> call = HttpManager.getInstance().getService().getMusicList();
        call.enqueue(new Callback<List<MusicList>>() {
            @Override
            public void onResponse(Call<List<MusicList>> call, Response<List<MusicList>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mView.onFetchSuccess(response.body());
                } else {
                    mView.onFetchFail();
                }
            }

            @Override
            public void onFailure(Call<List<MusicList>> call, Throwable t) {
                mView.onFetchFail();
            }
        });
    }
}
