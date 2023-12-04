package com.chedly.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chedly.project.Service.ITunesServiceAPI;
import com.chedly.project.model.ITunesMusicResponse;
import com.chedly.project.model.MusicItem;
import com.chedly.project.model.MusicListViewModel;
import com.chedly.project.model.MusicResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutBtn,searchBtn;
    TextView textView;
    ListView listviewMusic;
    FirebaseUser user;
    EditText searchInput;

    List<MusicItem> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // logoutBtn = findViewById(R.id.button);
        //textView = findViewById(R.id.text);
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        listviewMusic=findViewById(R.id.listview);
        //searchBtn=findViewById(R.id.searchBtn);
        searchInput=findViewById(R.id.searchInput);

        MusicListViewModel listviewmodal=new MusicListViewModel(this,R.layout.music_list_view,data);
        listviewMusic.setAdapter(listviewmodal);


        final Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String query = charSequence.toString();
                ITunesServiceAPI iTunesServiceAPI = retrofit.create(ITunesServiceAPI.class);
                Call<ITunesMusicResponse> callItunesMusic = iTunesServiceAPI.artistsongs(query, "music");


                callItunesMusic.enqueue(new Callback<ITunesMusicResponse>() {
                    @Override

                    public void onResponse(Call<ITunesMusicResponse> call, Response<ITunesMusicResponse> response) {
                        Log.i("info", call.request().url().toString());
                        if (!response.isSuccessful()) {
                            Log.i("indo", String.valueOf(response.code()));
                        }

                        if (response.isSuccessful()) {
                            ITunesMusicResponse iTunesMusicResponse = response.body();
                            if (iTunesMusicResponse != null) {
                                data.clear();
                                for (MusicItem item : iTunesMusicResponse.getResults()) {
                                    if (item != null) {
                                        data.add(item);
                                    }
                                }
                                listviewmodal.notifyDataSetChanged();
                            } else {
                                // Handle the case where iTunesMusicResponse is null
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ITunesMusicResponse> call, Throwable t) {
                        // Handle failure
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for your case
            }
        });

        listviewMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String trackId = String.valueOf(data.get(position).trackId);
                Intent redirectToPlayerSongPage = new Intent(getApplicationContext(), Music_player.class);
                redirectToPlayerSongPage.putExtra("trackId", trackId);
                startActivity(redirectToPlayerSongPage);



            }
        });

       /* searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query=searchInput.getText().toString();
                /*ITunesServiceAPI iTunesServiceAPI=retrofit.create(ITunesServiceAPI.class);
                Call<ITunesMusicResponse> callItunesMusic=iTunesServiceAPI.artistsongs(query);
                ITunesServiceAPI iTunesServiceAPI = retrofit.create(ITunesServiceAPI.class);
                Call<ITunesMusicResponse> callItunesMusic = iTunesServiceAPI.artistsongs(query, "music");
                data.clear();
                callItunesMusic.enqueue(new Callback<ITunesMusicResponse>() {
                    @Override
                    public void onResponse(Call<ITunesMusicResponse> call, Response<ITunesMusicResponse> response) {
                        Log.i("info",call.request().url().toString());
                        //Toast.makeText(MainActivity.this,query, Toast.LENGTH_LONG).show();
                        if(!response.isSuccessful()){
                            //Toast.makeText(MainActivity.this,"sucees" , Toast.LENGTH_LONG).show();
                            Log.i("indo",String.valueOf(response.code()));
                        }

                        ITunesMusicResponse iTunesMusicResponse= response.body();
                        for(MusicItem item:iTunesMusicResponse.getResults()){
                            data.add(item);

                        }

                        listviewmodal.notifyDataSetChanged();



                    }

                    @Override
                    public void onFailure(Call<ITunesMusicResponse> call, Throwable t) {

                    }
                });

            }
        });*/







        if(user==null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }else {
         //   textView.setText(user.getEmail());
        }

       /* logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
            }
        });*/



    }




}