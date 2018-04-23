package com.example.dell_1.myapp3.VideoPlayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.dell_1.myapp3.R;


public class MPlayer extends Activity {

    private String[] filename;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer);

        final VideoView videoView =(VideoView)findViewById(R.id.videoView1);

        Intent intent = getIntent();
        filename = intent.getStringArrayExtra("fuckyouasshole");
        temp = intent.getIntExtra("whatthefuck",temp);

        //Creating MediaController
        final MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //next button clicked
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //previous button clicked
                playPrevious();
            }
        });
        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(filename[temp]);
        videoView.requestFocus();
        videoView.start();
    }
    public void playNext(){
        final VideoView videoView =(VideoView)findViewById(R.id.videoView1);
        temp = temp +1;
        videoView.setVideoPath(filename[temp]);


    }

    public void playPrevious(){
        final VideoView videoView =(VideoView)findViewById(R.id.videoView1);
        temp = temp -1;
        videoView.setVideoPath(filename[temp]);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
