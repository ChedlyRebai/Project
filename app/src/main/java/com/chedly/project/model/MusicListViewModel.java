package com.chedly.project.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import com.chedly.project.R;


public class MusicListViewModel extends ArrayAdapter<MusicItem> {
    private int resource;
    private List<MusicItem> musicList;

    public MusicListViewModel(@NonNull Context context, int resource, List<MusicItem> musicList) {
        super(context, resource, musicList);
        this.musicList = musicList;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }

        ImageView imageViewArtwork = listItemView.findViewById(R.id.imageviewMusic);
        TextView textViewTrackName = listItemView.findViewById(R.id.texttrackName);
        TextView textViewArtistName = listItemView.findViewById(R.id.textartistName);

        textViewTrackName.setText(getItem(position).trackName);
        textViewArtistName.setText(getItem(position).artistName);

        // Load artwork image asynchronously
        new Thread(() -> {
            try {
                URL imageUrl = new URL(getItem(position).artworkUrl100);
                HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Set the bitmap on the UI thread
                ((Activity) getContext()).runOnUiThread(() -> {
                    imageViewArtwork.setImageBitmap(bitmap);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        return listItemView;
    }
}
