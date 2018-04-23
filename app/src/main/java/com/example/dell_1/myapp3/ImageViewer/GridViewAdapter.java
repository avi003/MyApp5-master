package com.example.dell_1.myapp3.ImageViewer;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.dell_1.myapp3.R;

import java.io.File;
import java.util.ArrayList;

import static com.example.dell_1.myapp3.ImageViewer.ImageGallery.al_images;

public class GridViewAdapter extends ArrayAdapter<Model_images> {

    private Context context;
    private ViewHolder viewHolder;
    private ArrayList<Model_images> al_menu = new ArrayList<>();
    private int int_position;

    public GridViewAdapter(Context context, ArrayList<Model_images> al_menu,int position) {
        super(context, R.layout.activity_adapter__photos_folder, al_menu);
        this.al_menu = al_menu;
        this.context = context;
        this.int_position = position;
    }

    @Override
    public int getCount() {
        Log.e("ADAPTER LIST SIZE ", al_menu.size() + "");
        Log.e("ADAPTER LIST SIZE", al_menu.get(int_position).getAl_imagepath().size() + "");
        return al_menu.get(int_position).getAl_imagepath().size();
    }

    public void updateUpdater(ArrayList<Integer> mSelected) {
        for (int selected : mSelected) {
            File file = new File(al_images.get(int_position).getAl_imagepath().get(selected));
            file.delete();
            MediaScannerConnection.scanFile(context,new String[] { file.toString() }, null, new MediaScannerConnection.OnScanCompletedListener()
            {
                public void onScanCompleted(String path, Uri uri)
                {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });


                        }
        notifyDataSetChanged();
                    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        if (al_menu.size() > 0) {
            return al_menu.get(int_position).getAl_imagepath().size();
        } else {
            return 1;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_adapter__photos_folder, parent, false);
            viewHolder.tv_foldern = (TextView) convertView.findViewById(R.id.tv_folder);
            viewHolder.tv_foldersize = (TextView) convertView.findViewById(R.id.tv_folder2);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv_foldern.setVisibility(View.GONE);
        viewHolder.tv_foldersize.setVisibility(View.GONE);


        Glide.with(context).load("file://" + al_menu.get(int_position).getAl_imagepath().get(position))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(viewHolder.iv_image);


        return convertView;

    }

    public static class ViewHolder {
        TextView tv_foldern, tv_foldersize;
        ImageView iv_image;

    }

    public interface Interface{
        void convert(View convertView);
    }

}