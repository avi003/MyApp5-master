package com.example.dell_1.myapp3.MusicPlayer;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import com.example.dell_1.myapp3.R;

import java.io.File;
import java.util.ArrayList;

public class PlayListActivity extends AppCompatActivity {

    private String[] mAudioPath;
    private ArrayList<Integer> mSelected = new ArrayList<>();
    MenuItem mSort, mSettings, mRename, mSelectAll, mProperties, mCreate;
    private ArrayList<Uri> files = new ArrayList<>();
    private String[] mMusicList;
    int fileIndex=0;

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

        final ImageButton button1 = (ImageButton) findViewById(R.id.button1);
        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        button1.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);

        GridView mListView = (GridView) findViewById(android.R.id.list);
        mMusicList = getAudioList();

        final ArrayAdapter<String> mAdapter = new MySimpleArrayAdapter(this,mMusicList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                try {
                    Intent i= new Intent(PlayListActivity.this,Player.class);
                    i.putExtra("anything",arg2);
                    i.putExtra("whatever",mAudioPath);
                    startActivity(i);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (mSelected.contains(i)) {
                    mSelected.remove(i);
                    view.setBackgroundColor(Color.TRANSPARENT);// remove item from list
                    // update view (v) state here
                    // eg: remove highlight
                } else {
                    mSelected.add(i);
                    view.setBackgroundColor(Color.LTGRAY);// add item to list
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
                                        for (int position = 0; position < mSelected.size(); position++) {
                                            File deleteFile = new File(mAudioPath[fileIndex]);
                                            deleteFile.delete();
                                            mSelected.remove(position);
                                            MediaScannerConnection.scanFile(getApplicationContext(),new String[] { deleteFile.toString() }, null, new MediaScannerConnection.OnScanCompletedListener()
                                            {
                                                public void onScanCompleted(String path, Uri uri)
                                                {
                                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                                }
                                            });
                                        }
                                        mAdapter.notifyDataSetChanged();
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

            case R.id.action_newFolder:
                String foldername = "New Folder2";
                return true;

            case R.id.action_sort:
                // location found
                return true;

            case R.id.action_rename:
                // location found
                return true;

            case R.id.action_selectAll:
                return true;

            case R.id.action_properties:
                // location found
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void hideMenuItem(){
        mSort.setVisible(false);
        mSettings.setVisible(false);
        mRename.setVisible(true);
        mSelectAll.setVisible(true);
        mProperties.setVisible(true);
    }
}








