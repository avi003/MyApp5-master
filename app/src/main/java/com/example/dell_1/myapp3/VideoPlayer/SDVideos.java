package com.example.dell_1.myapp3.VideoPlayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell_1.myapp3.PDFAdapter;
import com.example.dell_1.myapp3.R;

public class SDVideos extends AppCompatActivity {

    private String[] filename;
    private Cursor videoCursor;
    int videoColumnIndex;
    MenuItem mSort, mSettings, mRename, mSelectAll, mProperties, mCreate;
    GridView videolist;
    int count;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdvideos);

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        topToolBar.setTitle("");
        topToolBar.setSubtitle("");
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));
        initialization();

        final ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);

    }

    private void initialization()
    {
        String[] videoProjection = { MediaStore.Video.Media._ID,MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.SIZE };
        videoCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoProjection, null, null, null);
        count = videoCursor.getCount();

        videolist = (GridView) findViewById(android.R.id.list);
        videolist.setAdapter(new VideoListAdapter(this.getApplicationContext()));
        videolist.setOnItemClickListener(videogridlistener);


        videolist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                // change the background color of the selected element
                view.setBackgroundColor(Color.LTGRAY);
                final ImageButton button1 = (ImageButton) findViewById(R.id.button1);
                final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
                final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
                final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                return true;
            }
        });


        final ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        button1.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SDVideos.this);
                        builder1.setMessage("This operation is not allowed here.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });


        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        button2.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SDVideos.this);
                        builder1.setMessage("This operation is not allowed here.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });


        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        button3.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SDVideos.this);
                        builder1.setMessage("Are you sure you want to delete it ?");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                });
    }



    private AdapterView.OnItemClickListener videogridlistener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id)
        {
            videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            videoCursor.moveToPosition(position);
            int i=0;
            filename =new String[count];
            if(videoCursor.moveToFirst())
            do {
                filename[i] = videoCursor.getString(videoColumnIndex);
                i++;
            }while(videoCursor.moveToNext());
            Intent intent= new Intent(SDVideos.this,MPlayer.class);
            intent.putExtra("fuckyouasshole",filename);
            intent.putExtra("whatthefuck",position);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionmenu, menu);
        mSort = menu.findItem(R.id.action_sort);
        mSettings = menu.findItem(R.id.action_settings);
        mCreate = menu.findItem(R.id.action_newFolder);
        mRename = menu.findItem(R.id.action_rename);
        mRename.setVisible(false);
        mSelectAll = menu.findItem(R.id.action_selectAll);
        mSelectAll.setVisible(false);
        mProperties = menu.findItem(R.id.action_properties);
        mProperties.setVisible(false);
        return true;
    }







    public class VideoListAdapter extends BaseAdapter
    {
        private Context vContext;

        public VideoListAdapter(Context c)
        {
            vContext = c;
        }

        public int getCount()
        {
            return videoCursor.getCount();
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            PDFAdapter.ViewHolder holder;
            View listItemRow;
            listItemRow = LayoutInflater.from(vContext).inflate(R.layout.custom_row2, parent, false);

            TextView txtTitle = (TextView)listItemRow.findViewById(R.id.textView3);
            TextView txtSize = (TextView)listItemRow.findViewById(R.id.txtSize);
            ImageView thumbImage = (ImageView)listItemRow.findViewById(R.id.imageView);

            videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            videoCursor.moveToPosition(position);
            txtTitle.setText(videoCursor.getString(videoColumnIndex));
            txtTitle.setLines(2);

            videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            videoCursor.moveToPosition(position);
            long abc =Long.parseLong(videoCursor.getString(videoColumnIndex));
            txtSize.setText(" Size(KB):" + abc/1024);
            if(abc>=1024){
                //KB or more
                long rxKb = abc/1024;
                txtSize.setText(" Size(KB):" + rxKb);
                if(rxKb>=1024){
                    //MB or more
                    long rxMB = rxKb/1024;
                    txtSize.setText(" Size(MB):" + rxMB);
                    if(rxMB>=1024){
                        //GB or more
                        long rxGB = rxMB/1024;
                        txtSize.setText(" Size(KB):" + rxKb);
                    }//rxMB>1024
                }//rxKb > 1024
            }//rxBytes>=1024

            int videoId = videoCursor.getInt(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            ContentResolver crThumb = getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, videoId, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            thumbImage.setImageBitmap(curThumb);
            return listItemRow;
        }
    }
}



