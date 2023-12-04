package com.chedly.project;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.chedly.project.Service.ITunesServiceAPI;
import com.chedly.project.model.ITunesMusicResponse;
import com.chedly.project.model.MusicItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Music_player extends AppCompatActivity {

    long trackId;
    private MediaPlayer mediaPlayer;
    private Button playButton;
    ImageView image;
    MaterialToolbar toolbar;
    private boolean isPlaying = false;

    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        image = findViewById(R.id.imageView);
        Intent intent = getIntent();
        trackId = Long.valueOf(intent.getStringExtra("trackId"));
        toolbar =findViewById(R.id.materialToolbar2);
        playButton = findViewById(R.id.playButton);
        mediaPlayer = new MediaPlayer();
        loadingIndicator = findViewById(R.id.loadingIndicator);
        toolbar.setVisibility(View.GONE);
        image.setVisibility(View.GONE);
        playButton.setVisibility(View.GONE);
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ITunesServiceAPI iTunesServiceAPI = retrofit.create(ITunesServiceAPI.class);
        Call<ITunesMusicResponse> result = iTunesServiceAPI.getById(String.valueOf(trackId));

        result.enqueue(new Callback<ITunesMusicResponse>() {

            @Override
            public void onResponse(Call<ITunesMusicResponse> call, Response<ITunesMusicResponse> response) {
                if (response.isSuccessful()) {
                    ITunesMusicResponse iTunesMusicResponse = response.body();
                    if (iTunesMusicResponse != null) {
                        List<MusicItem> currentMusic = iTunesMusicResponse.getResults();
                        if (!currentMusic.isEmpty()) {
                            MusicItem musicItem = currentMusic.get(0);
                            String art400= updateImageUrl(musicItem.artworkUrl100);
                            Picasso.get().load(art400).into(image);
                            toolbar.setVisibility(View.VISIBLE);
                            image.setVisibility(View.VISIBLE);
                            playButton.setVisibility(View.VISIBLE);
                            toolbar.setTitle(musicItem.trackName);
                            toolbar.setSubtitle(musicItem.artistName);
                            setupMediaPlayer(musicItem);
                        } else {
                            showToast("No music found for trackId: " + trackId);
                        }
                    } else {
                        showToast("Response body is null");
                    }
                } else {
                    showToast("Request not successful. Code: " + response.code());
                }

                loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ITunesMusicResponse> call, Throwable t) {
                showToast("Failed to retrieve music information");
            }
        });
    }

    private void setupMediaPlayer(MusicItem musicItem) {
        try {
            mediaPlayer.setDataSource(musicItem.previewUrl);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }


        playButton.setOnClickListener(view -> {
            if (isPlaying) {
                stopMusic();
            } else {
                playMusic();
            }
        });
    }

    private void playMusic() {
        mediaPlayer.start();
        playButton.setText("Stop");
        isPlaying = true;
    }

    private void stopMusic() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);
        playButton.setText("Play");
        isPlaying = false;
    }

    private void showToast(String message) {
        Toast.makeText(Music_player.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    protected String updateImageUrl(String url) {
        String[] parts = url.split("/100x100bb.jpg");
        // Check if the split resulted in two parts
        if (parts.length == 2) {
            String newUrl = parts[0] + "/400*400bb.jpg";
            System.out.println(newUrl);
            return newUrl;
        }
        return url;
    }

}
