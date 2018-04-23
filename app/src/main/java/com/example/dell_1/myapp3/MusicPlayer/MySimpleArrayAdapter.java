package com.example.dell_1.myapp3.MusicPlayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell_1.myapp3.R;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {

    public MySimpleArrayAdapter(@NonNull Context context, String[] mMusicList ) {
        super(context, R.layout.custom_row, mMusicList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.custom_row,parent,false);
        TextView textView = (TextView) view.findViewById(R.id.textView3);
        String single = getItem(position);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        textView.setText(single);
        image.setImageResource(R.drawable.adele1);
        textView.setLines(2);
        return view;
    }
}