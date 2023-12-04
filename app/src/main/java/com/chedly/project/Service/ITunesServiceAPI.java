package com.chedly.project.Service;

import com.chedly.project.model.ITunesMusicResponse;
import com.chedly.project.model.MusicItem;
import com.chedly.project.model.MusicResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ITunesServiceAPI {
    @GET("search")
    Call<ITunesMusicResponse> artistsongs(
            @Query("term") String artist,
            @Query("media") String media
    );

    @GET("lookup")
    Call<ITunesMusicResponse> getById(
            @Query("id") String trackId
    );

}

