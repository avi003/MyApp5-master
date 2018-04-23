package com.example.dell_1.myapp3.MusicPlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

import com.example.dell_1.myapp3.R;

import io.gresse.hugo.vumeterlibrary.VuMeterView;

public class Equalizer extends AppCompatActivity {

    private VuMeterView mVuMeterView;

    private SeekBar mBarNumberSeekBar;
    private Switch mAnimationSwitch;

    private boolean mEnableAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        mVuMeterView = (VuMeterView) findViewById(R.id.vumeter);
        mBarNumberSeekBar = (SeekBar) findViewById(R.id.numberSeekBar);
        mAnimationSwitch = (Switch) findViewById(R.id.barNumberSwitch);
        mVuMeterView.setEnabled(true);


        mBarNumberSeekBar.incrementProgressBy(1);
        mBarNumberSeekBar.setMax(10);

        mBarNumberSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVuMeterView.setBlockNumber(progress +1);
            }
        });

        mAnimationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEnableAnimation = isChecked;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_stop:
                mVuMeterView.stop(mEnableAnimation);
                return true;
            case R.id.action_resume:
                mVuMeterView.resume(mEnableAnimation);
                return true;
            case R.id.action_pause:
                mVuMeterView.pause();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
