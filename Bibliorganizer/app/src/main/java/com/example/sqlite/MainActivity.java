package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    private VideoView v;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        v = findViewById(R.id.videoView);

        String videopath = "android.resource://" + getPackageName() + "/" + R.raw.video;
        Uri uri = Uri.parse(videopath);
        v.setVideoURI(uri);

        /*mediaController = new MediaController(this);
        v.setMediaController(mediaController);
        mediaController.setAnchorView(v);*/

        v.start();

        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // El video ha terminado, inicia una nueva actividad
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}