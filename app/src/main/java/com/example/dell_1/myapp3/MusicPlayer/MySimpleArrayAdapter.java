package com.example.dell_1.myapp3.MusicPlayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell_1.myapp3.R;
import com.example.dell_1.myapp3.VideoPlayer.SDVideos;

import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

    PlayListActivity activity_my;
    private ViewHolder viewHolder;
    Context vContext;


    public MySimpleArrayAdapter(@NonNull Context context, String[] mMusicList , PlayListActivity activity ) {
        super(context, R.layout.custom_row, mMusicList);
        vContext=context;
        activity_my=activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(vContext).inflate(R.layout.custom_row2, parent, false);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.textView3);
            viewHolder.thumbImage = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.imageContainer_Ln = (LinearLayout) convertView.findViewById(R.id.linearVideo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }



        String single = getItem(position);
        viewHolder.txtTitle.setText(single);
        viewHolder.thumbImage.setImageResource(R.drawable.adele1);
        viewHolder.txtTitle.setLines(2);


        //changing background color based on multiselection
        if(activity_my.mSelected.contains(position)){
            viewHolder.imageContainer_Ln.setBackgroundColor(Color.LTGRAY);
        }else{
            viewHolder.imageContainer_Ln.setBackgroundColor(Color.TRANSPARENT);
        }


        return convertView;
    }

    public class ViewHolder {
        TextView txtTitle;
        ImageView thumbImage;
        LinearLayout imageContainer_Ln;

    }
}