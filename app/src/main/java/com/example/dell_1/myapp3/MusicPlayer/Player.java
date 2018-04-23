package com.example.dell_1.myapp3.MusicPlayer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.dell_1.myapp3.R;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Player extends Activity {

    private MediaPlayer mMediaPlayer;
    private SeekBar songProgressBar;
    private String[] mAudioPath;
    private Handler mHandler = new Handler();
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    int temp;
    private Utilities utils;

    MediaMetadataRetriever metaRetriver;
    byte[] art;
    ImageView album_art;
    TextView album;
    TextView artist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_building_music_player);
        mMediaPlayer = new MediaPlayer();
        utils = new Utilities();

        Intent intent = getIntent();
        mAudioPath = intent.getStringArrayExtra("whatever");
        temp = intent.getIntExtra("anything",0);
        try {
            playSong(mAudioPath[temp]);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {

        Log.d("ringtone", "playSong :: " + path);

        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path);
//mMediaPlayer.setLooping(true);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                temp = temp + 1;
                try {
                    playSong(mAudioPath[temp]);
                } catch (Exception er) {
                    er.printStackTrace();
                }
            }
        });
        acv(path);
        abc();
        cde();
        fgh();
        ijk();
        lmn();
        jjj();
        updateProgressBar();
        opq();
    }

    public void acv(String path) {
        getInit();

        metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(path);
        try {
            art = metaRetriver.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            album_art.setImageBitmap(songImage);
            album.setText(metaRetriver
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
            artist.setText(metaRetriver
                    .extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        } catch (Exception e) {
            album_art.setBackgroundColor(Color.GRAY);
            album.setText("Unknown Album");
            artist.setText("Unknown Artist");
        }

    }

    public void getInit() {
        album_art = (ImageView) findViewById(R.id.coverart1);
        album = (TextView) findViewById(R.id.Album);
        artist = (TextView) findViewById(R.id.artist_name);
    }


    public void abc() {
        ImageButton btnPlay1 = (ImageButton) findViewById(R.id.btnPlay1);
        btnPlay1.setBackgroundColor(Color.TRANSPARENT);
        btnPlay1.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (mMediaPlayer.isPlaying()) {
                            mMediaPlayer.pause();
                        } else {
                            mMediaPlayer.start();
                        }

                    }
                });
    }


    public void cde() {
        ImageButton btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        temp = temp + 1;
                        try {
                            playSong(mAudioPath[temp]);
                        } catch (Exception er) {
                            er.printStackTrace();
                        }

                    }
                }
        );
    }

    public void fgh() {
        ImageButton btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        temp =temp - 1;
                        try {
                            playSong(mAudioPath[temp]);
                        } catch (Exception er) {
                            er.printStackTrace();
                        }

                    }
                }
        );
    }

    public void ijk() {
        ImageButton btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnRepeat.setOnClickListener
                (new View.OnClickListener() {
                    public void onClick(View arg0) {
                        mMediaPlayer.setLooping(true);
                    }
                });
    }

    public void lmn() {
        ImageButton btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
        btnShuffle.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        List<String> arr = Arrays.asList(mAudioPath);
                        Collections.shuffle(arr);
                        arr.toArray(mAudioPath);
                    }
                }
        );
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();

            songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
            songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);


            // Displaying Total Duration time
            songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

            // Updating progress bar
            int progress = utils.getProgressPercentage(currentDuration, totalDuration);
            //Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public void jjj() {
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);

        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateProgressBar();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                updateProgressBar();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mMediaPlayer.getDuration();
                int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

                // forward or backward to certain seconds
                mMediaPlayer.seekTo(currentPosition);

                // update timer progress again
                updateProgressBar();

            }
        });
    }
    public void opq(){
        ImageButton btnPlayList= (ImageButton) findViewById(R.id.btnPlaylist);
        btnPlayList.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        Intent op = new Intent(Player.this,PlayListActivity.class);
                        startActivity(op);

                    }
                }
        );
    }

    public void onClicking(View view){
        Intent intent = new Intent(AudioEffect
                .ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);

        if((intent.resolveActivity(getPackageManager()) != null)) {
            startActivityForResult(intent, 100);
        } else {
            // No equalizer found :(
        }
    }
}
