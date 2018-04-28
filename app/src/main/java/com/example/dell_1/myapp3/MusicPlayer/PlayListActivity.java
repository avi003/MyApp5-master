package com.example.dell_1.myapp3.MusicPlayer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_1.myapp3.R;
import com.example.dell_1.myapp3.VideoPlayer.SDVideos;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PlayListActivity extends AppCompatActivity {

    private String[] mAudioPath;
    public ArrayList<Integer> mSelected = new ArrayList<>();
    MenuItem mSort, mSettings, mRename, mSelectAll, mProperties, mCreate;
    private ArrayList<Uri> files = new ArrayList<>();
    private String[] mMusicList;
    int fileIndex=0;
    ArrayAdapter<String> adapter;
    ImageButton button1,button2,button3,button4;
    GridView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);

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

        mListView = (GridView) findViewById(android.R.id.list);
        mMusicList = getAudioList();

        adapter = new MySimpleArrayAdapter(this,mMusicList,this);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {
                if (!multiselectOn) {
                    try {
                        Intent i = new Intent(PlayListActivity.this, Player.class);
                        i.putExtra("anything", position);
                        i.putExtra("whatever", mAudioPath);
                        startActivity(i);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
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
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                button1.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);

                button1.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PlayListActivity.this);
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
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PlayListActivity.this);
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

                button4.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                share.setType("*/*");
                                for (int i :mSelected) {
//            Log.v(TAG, mData2.get(Integer.parseInt(internal_activity.multiselect.get(i))));
                                    File file = new File(mAudioPath[fileIndex]);
                                    Uri uri = Uri.fromFile(file);
                                    files.add(uri);
                                }
                                share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                                view.getContext().startActivity(Intent.createChooser(share, "Share file"));

                                }
                        });


        button3.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PlayListActivity.this);
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

                                            renameImageFile(PlayListActivity.this,f,f);

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

                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    private String[] getAudioList() {
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();


        String[] songs = new String[count];
        mAudioPath = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                songs[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                mAudioPath[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songs;
    }


    //needed for deleting
    public String getFileName(int position){
        return mAudioPath[position];
    }

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


//    private void hideMenuItem(){
//        mSort.setVisible(false);
//        mSettings.setVisible(false);
//        mRename.setVisible(true);
//        mSelectAll.setVisible(true);
//        mProperties.setVisible(true);
//    }


    ////////// making long press work ////////////

    boolean multiselectOn=false;
    public void clearMultiSelect(){
        mSelected.clear();
        multiselectOn=false;
        hideMenuItem();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }

    public void selectAll(){
        if(mAudioPath!=null) {
            int size = mAudioPath.length;
            multiselectOn = true;
            mSelected.clear();
            for (int i = 0; i < size; i++) {
                mSelected.add(i);
            }
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    private void hideMenuItem(){
        mSort.setVisible(false);
        mSettings.setVisible(false);
        mRename.setVisible(false);
        if (mSelected.size()==1){
            mRename.setVisible(true);
        }
        mSelectAll.setVisible(true);
        mProperties.setVisible(false);
        if (mSelected.size()==1){
            mProperties.setVisible(true);
        }
    }



    /////////Rename Works///////////////
    private void renameFile(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(PlayListActivity.this);
        builder2.setMessage("Rename File");
        final EditText input = new EditText(PlayListActivity.this);
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
                                        renameImageFile(PlayListActivity.this,oldName,newFile);

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
        resolver.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.Audio.Media.DATA + "=?", new String[] { f.getAbsolutePath() });
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
            pd=new ProgressDialog(PlayListActivity.this);
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
            mMusicList = getAudioList();

            adapter = new MySimpleArrayAdapter(PlayListActivity.this,mMusicList,PlayListActivity.this);
            mListView.setAdapter(adapter);
            clearMultiSelect();
        }
    }




    void makeToast(String str){
        Toast.makeText(this, str,Toast.LENGTH_LONG).show();
    }
}








