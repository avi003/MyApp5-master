package com.example.dell_1.myapp3.ImageViewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.dell_1.myapp3.R;

import static com.example.dell_1.myapp3.ImageViewer.ImageGallery.al_images;

public class FullImageActivity extends AppCompatActivity {
    int position;
    int folderPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        Intent i = getIntent();

        // Selected image id
        position = i.getExtras().getInt("id");
        folderPosition = i.getExtras().getInt("folderPosition");
        Bundle extras = getIntent().getExtras();
        // TODO : check if it require
//        String value = extras.getString("abc");

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new TouchImageAdapter(this,al_images, folderPosition));
        mViewPager.setCurrentItem(position);
    }
}
