package com.example.dell_1.myapp3.VideoPlayer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_1.myapp3.ImageViewer.GridViewAdapter;
import com.example.dell_1.myapp3.ImageViewer.PhotosActivity;
import com.example.dell_1.myapp3.PDFAdapter;
import com.example.dell_1.myapp3.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.dell_1.myapp3.ImageViewer.ImageGallery.al_images;

public class SDVideos extends AppCompatActivity {

    private String[] filename;
    private Cursor videoCursor;
    int videoColumnIndex;
    MenuItem mSort, mSettings, mRename, mSelectAll, mProperties, mCreate;
    GridView videolist;
    int count;
     ImageButton button1 ;
     ImageButton button2 ;
     ImageButton button3 ;
     ImageButton button4 ;
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


         button1 = (ImageButton) findViewById(R.id.button1);
         button2 = (ImageButton) findViewById(R.id.button2);
        button3 = (ImageButton) findViewById(R.id.button3);
        button4 = (ImageButton) findViewById(R.id.button4);
        changeVisibility(0);
        initialization();

    }

    VideoListAdapter adapter;
    private void initialization()
    {
        String[] videoProjection = { MediaStore.Video.Media._ID,MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,MediaStore.Video.Media.SIZE };
        videoCursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoProjection, null, null, null);
        count = videoCursor.getCount();



        videolist = (GridView) findViewById(android.R.id.list);
        adapter=new VideoListAdapter(this.getApplicationContext());
        videolist.setAdapter(adapter);
        videolist.setOnItemClickListener(videogridlistener);


        videolist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

//                for (int j = 0; j < adapterView.getChildCount(); j++)
//                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
//
//                // change the background color of the selected element
//                view.setBackgroundColor(Color.LTGRAY);
                multiselectOn=true;
                if (mSelected.contains(i)) {
                    Integer ob=i;
                    mSelected.remove(ob);
                    //view.setBackgroundColor(Color.TRANSPARENT);// remove item from list
                    // update view (v) state here
                    // eg: remove highlight
                } else {
                    mSelected.add(i);
                    // view.setBackgroundColor(Color.LTGRAY);// add item to list
                    // update view (v) state here
                    // eg: add highlight
                }

                hideMenuItem();
                changeVisibility(1);

                adapter.notifyDataSetChanged();
                return true;
            }
        });



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
                                    for(int i:mSelected){
                                        String filePath=getFileName(i);
                                        File f=new File(filePath);

                                       if(f.exists()){
                                           //removeMedia(SDVideos.this,f);
                                            boolean a=f.delete();
                                            String strFileNamea = f.getName();
                                        }

                                        renameImageFile(SDVideos.this,f,f);

                                    }

                                    clearMultiSelect();
                                    new Reload().execute();
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

        changeVisibility(0);
    }




    //needed for deleting
    public String getFileName(int position){
        videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        videoCursor.moveToPosition(position);
        int i = 0;
        filename = new String[count];
        if (videoCursor.moveToFirst())
            do {
                filename[i] = videoCursor.getString(videoColumnIndex);
                i++;
            } while (videoCursor.moveToNext());

        return filename[position];

    }


    private AdapterView.OnItemClickListener videogridlistener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {

            if (!multiselectOn) {
                videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                videoCursor.moveToPosition(position);
                int i = 0;
                filename = new String[count];
                if (videoCursor.moveToFirst())
                    do {
                        filename[i] = videoCursor.getString(videoColumnIndex);
                        i++;
                    } while (videoCursor.moveToNext());
                Intent intent = new Intent(SDVideos.this, MPlayer.class);
                intent.putExtra("fuckyouasshole", filename);
                intent.putExtra("whatthefuck", position);
                startActivity(intent);
            }else{
                if (mSelected.contains(position)) {
                    Integer ob=position;
                    mSelected.remove(ob);
                    //view.setBackgroundColor(Color.TRANSPARENT);// remove item from list
                    // update view (v) state here
                    // eg: remove highlight
                } else {
                    mSelected.add(position);
                    // view.setBackgroundColor(Color.LTGRAY);// add item to list
                    // update view (v) state here
                    // eg: add highlight
                }
                hideMenuItem();
                adapter.notifyDataSetChanged();

        }

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
        private ViewHolder viewHolder;
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {

                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(vContext).inflate(R.layout.custom_row2, parent, false);
                viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.textView3);
                viewHolder.txtSize = (TextView) convertView.findViewById(R.id.txtSize);
                viewHolder.thumbImage = (ImageView) convertView.findViewById(R.id.imageView);
                viewHolder.imageContainer_Ln = (LinearLayout) convertView.findViewById(R.id.linearVideo);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            videoCursor.moveToPosition(position);
            viewHolder.txtTitle.setText(videoCursor.getString(videoColumnIndex));
            viewHolder.txtTitle.setLines(2);

            videoColumnIndex = videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            videoCursor.moveToPosition(position);
            long abc =Long.parseLong(videoCursor.getString(videoColumnIndex));
            viewHolder.txtSize.setText(" Size(KB):" + abc/1024);
            if(abc>=1024){
                //KB or more
                long rxKb = abc/1024;
                viewHolder.txtSize.setText(" Size(KB):" + rxKb);
                if(rxKb>=1024){
                    //MB or more
                    long rxMB = rxKb/1024;
                    viewHolder.txtSize.setText(" Size(MB):" + rxMB);
                    if(rxMB>=1024){
                        //GB or more
                        long rxGB = rxMB/1024;
                        viewHolder. txtSize.setText(" Size(KB):" + rxKb);
                    }//rxMB>1024
                }//rxKb > 1024
            }//rxBytes>=1024

            int videoId = videoCursor.getInt(videoCursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
            ContentResolver crThumb = getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, videoId, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            viewHolder.thumbImage.setImageBitmap(curThumb);

            //changing background color based on multiselection
            if(mSelected.contains(position)){
                viewHolder.imageContainer_Ln.setBackgroundColor(Color.LTGRAY);
            }else{
                viewHolder.imageContainer_Ln.setBackgroundColor(Color.TRANSPARENT);
            }

            return convertView;
        }
        public class ViewHolder {
            TextView txtTitle, txtSize;
            ImageView thumbImage;
            LinearLayout imageContainer_Ln;

        }
    }

    ////////// making long press work ////////////

    boolean multiselectOn=false;
    ArrayList<Integer> mSelected=new ArrayList<>();
    public void clearMultiSelect(){
        mSelected.clear();
        multiselectOn=false;
        hideMenuItem();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    public void selectAll(){
        int size=count;
        multiselectOn=true;
        mSelected.clear();
        for(int i=0; i<size;i++){
            mSelected.add(i);
        }
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    private void hideMenuItem(){
        mSort.setVisible(false);
        mSettings.setVisible(false);
        mRename.setVisible(true);
        if (mSelected.size()>1){
            mRename.setVisible(false);
        }
        mSelectAll.setVisible(true);
        mProperties.setVisible(true);
        if (mSelected.size()>1){
            mProperties.setVisible(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_settings:
                // search action
                return true;

            case R.id.action_sort:
                // location found
                return true;

            case R.id.action_rename:
                renameFile();
                // location found
                return true;

            case R.id.action_selectAll:

                selectAll();
                // location found
                return true;

            case R.id.action_properties:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /////////Rename Works///////////////
    private void renameFile(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(SDVideos.this);
        builder2.setMessage("Rename File");
        final EditText input = new EditText(SDVideos.this);
        input.setHint("Enter new name");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder2.setView(input);
        builder2.setPositiveButton(
                "Rename",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(mSelected.size()==1)
                            for (int selected : mSelected) {
                                String string = input.getText().toString();
                                File oldName = new File(getFileName(selected));

                                String OldFilename=oldName.getName();
                                String ext=OldFilename.substring(OldFilename.lastIndexOf("."));

                                String OldFileDir=oldName.getAbsolutePath().replace(OldFilename,string)+ext;


                                File newFile = new File(OldFileDir);
                                if (!newFile.exists()) {
                                    boolean success = oldName.renameTo(newFile);
                                    if (success) {
                                        // Log.v(TAG, "not renamed");
                                        makeToast("File Renamed");
                                        renameImageFile(SDVideos.this,oldName,newFile);

                                        new Reload().execute();
                                    }
                                } else {
                                    // Log.e(TAG, "file is already exist");
                                }
                            }
                    }
                });


        builder2.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert12 = builder2.create();
        alert12.show();
    }
    public void  renameImageFile(Context c, File from, File to) {
        removeMedia(c, from);
        addMedia(c, to);

    }

    public static void addMedia(Context c, File f) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(f));
        c.sendBroadcast(intent);
    }

    private static void removeMedia(Context c, File f) {
        ContentResolver resolver = c.getContentResolver();
        resolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "=?", new String[] { f.getAbsolutePath() });
    }

    void changeVisibility(int mode){
        if(mode==1) {
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
        }else{
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            button3.setVisibility(View.GONE);
            button4.setVisibility(View.GONE);
        }
    }
    @Override
    public void onBackPressed() {
        if(!multiselectOn)
        super.onBackPressed();
        else{
            changeVisibility(0);
            clearMultiSelect();
        }

    }

    public class Reload extends AsyncTask<String, Void, File> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(SDVideos.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected File doInBackground(String... strings) {
            //content provider takes time to update
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            pd.dismiss();
            //obj_adapter = new Adapter_PhotosFolder(getApplicationContext(), al_images, int_position,ImageGallery.this);
            //gv_folder.setAdapter(obj_adapter);
            initialization();
            clearMultiSelect();
        }
    }

    void makeToast(String str){
        Toast.makeText(this, str,Toast.LENGTH_SHORT).show();
    }

}



