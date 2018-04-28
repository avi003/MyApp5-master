package com.example.dell_1.myapp3.ImageViewer;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell_1.myapp3.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.dell_1.myapp3.ImageViewer.ImageGallery.al_images;

public class PhotosActivity extends AppCompatActivity {
    int int_position;
    private GridView gridView;
    private static final String  TAG = " com.example.dell_1.myapp3.ImageViewer";
    GridViewAdapter adapter;
    private ArrayList<Uri> files = new ArrayList<>();
    ArrayList<Model_images> al_menu = new ArrayList<>();
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;

    public ArrayList<Integer> mSelected = new ArrayList<>();
    String absolutePathOfImage;
    boolean boolean_folder;
    MenuItem mSort,mSettings,mRename,mSelectAll, mProperties ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);

        final ImageButton buttoncut = (ImageButton) findViewById(R.id.button1);
        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        final ImageButton buttonpaste = (ImageButton) findViewById(R.id.buttonpaste);
        buttoncut.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);
        buttonpaste.setVisibility(View.GONE);

        gridView = (GridView) findViewById(android.R.id.list);

        int_position = getIntent().getIntExtra("value", 0);
        adapter = new GridViewAdapter(this, al_images, int_position,this);
        gridView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        gridView.setAdapter(adapter);

        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        topToolBar.setTitle("");
        topToolBar.setSubtitle("");
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!multiselectOn) {
                    String abc = "file://" + al_images.get(int_position).getAl_imagepath().get(position);

                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                    i.putExtra("id", position);
                    i.putExtra("folderPosition", int_position);
                    i.putExtra("abc", abc);
                    startActivity(i);
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

                    adapter.notifyDataSetChanged();
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                multiselectOn=true;
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

                buttoncut.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                buttoncut.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                buttoncut.setVisibility(View.GONE);
                                button2.setVisibility(View.GONE);
                                button3.setVisibility(View.GONE);
                                button4.setVisibility(View.GONE);
                                Intent moveIntent = new Intent(PhotosActivity.this, ImageGallery.class);
                                moveIntent.putExtra("selected_images", getImagePaths(mSelected));
                                moveIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(moveIntent);
                                finish();
                            }
                        });

                button2.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                buttoncut.setVisibility(View.GONE);
                                button2.setVisibility(View.GONE);
                                button3.setVisibility(View.GONE);
                                button4.setVisibility(View.GONE);
                            }

                        });
                button3.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PhotosActivity.this);
                                builder1.setMessage("Are you sure you want to delete it ?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                adapter.updateUpdater(mSelected);
//                                                for (int position = 0; position < mSelected.size(); position++) {
//                                                    al_images.get(int_position).getAl_imagepath().remove(position);
//                                                }
                                                adapter.notifyDataSetChanged();
                                                clearMultiSelect();
                                                //finish();
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

                button4.setOnClickListener(
                        new View.OnClickListener(){
                            public void onClick(View view){
                                shareItem(view);
                            }
                        }
                );
               adapter.notifyDataSetChanged();
                return true;
            }


        });
    }

    protected void shareItem(View v) {

        Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
        share.setType("*/*");
        for (int i :mSelected) {
//            Log.v(TAG, mData2.get(Integer.parseInt(internal_activity.multiselect.get(i))));
            File file = new File(al_images.get(int_position).getAl_imagepath().get(i));
            Uri uri = Uri.fromFile(file);
            files.add(uri);
        }
        share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        v.getContext().startActivity(Intent.createChooser(share, "Share file"));
    }


    public ArrayList<Model_images> fn_imagespath() {
        al_images.clear();

        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            Log.e("Column", absolutePathOfImage);
            Log.e("Folder", cursor.getString(column_index_folder_name));

            for (int i = 0; i < al_images.size(); i++) {
                if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                    boolean_folder = true;
                    int_position = i;
                    break;
                } else {
                    boolean_folder = false;
                }
            }


            if (boolean_folder && al_images.size() > 0) {

                ArrayList<String> al_path = new ArrayList<>();
                al_path.addAll(al_images.get(int_position).getAl_imagepath());
                al_path.add(absolutePathOfImage);
                al_images.get(int_position).setAl_imagepath(al_path);

            } else {
                ArrayList<String> al_path = new ArrayList<>();
                al_path.add(absolutePathOfImage);
                Model_images obj_model = new Model_images();
                obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                obj_model.setDirectoryPath(new File(absolutePathOfImage).getParent());
                obj_model.setAl_imagepath(al_path);

                al_images.add(obj_model);

            }
        }
        for (int i = 0; i < al_images.size(); i++) {
            Log.e("FOLDER", al_images.get(i).getStr_folder());
            for (int j = 0; j < al_images.get(i).getAl_imagepath().size(); j++) {
                Log.e("FILE", al_images.get(i).getAl_imagepath().get(j));
            }
        }
        //obj_adapter = new Adapter_PhotosFolder(getApplicationContext(), al_images, int_position,this);
        //gv_folder.setAdapter(obj_adapter);
        return al_images;
    }

    private ArrayList<String> getImagePaths(ArrayList<Integer> selectedIndexList) {
        ArrayList<String> listOfImages = new ArrayList<>();
        for(Integer index : selectedIndexList) {
            listOfImages.add(al_images.get(int_position).getAl_imagepath().get(index));
        }

        return listOfImages;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionmenu, menu);
        mSort = menu.findItem(R.id.action_sort);
        mSettings = menu.findItem(R.id.action_settings);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PhotosActivity.this);
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setMessage("Properties");
                builder.setView(R.layout.properties);
                View dialogView = inflater.inflate(R.layout.properties, null);
                builder.setView(dialogView);
                TextView displayname = (TextView) dialogView.findViewById(R.id.displayname);
                TextView displaysize = (TextView) dialogView.findViewById(R.id.displaysize);
                TextView displaylastmodified = (TextView) dialogView.findViewById(R.id.displaylastmodified);
                TextView displaydatetaken = (TextView) dialogView.findViewById(R.id.displaydatetaken);
                TextView displaypath  = (TextView) dialogView.findViewById(R.id.displaypath);
                for(int i: mSelected){
                    File file = new File(al_images.get(int_position).getAl_imagepath().get(i));
                    float fileSizeInBytes = file.length();
                    String calString = Float.toString(fileSizeInBytes);
                    displaysize.setText(calString + " bytes");
                    if(fileSizeInBytes>1024){
                        float fileSizeInKB = fileSizeInBytes / 1024;
                        String calString2 = Float.toString(fileSizeInKB);
                        displaysize.setText(calString2 + " KB");
                        if(fileSizeInKB>1024){
                            float fileSizeInMB = fileSizeInKB / 1024;
                            String calString3 = Float.toString(fileSizeInMB);
                            displaysize.setText(calString3 + " MB");
                        }
                    }
                    String strFileName = file.getName();
                    displayname.setText(strFileName);


                    Date lastModified = new Date(file.lastModified());
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    String formattedDateString = formatter.format(lastModified);
                    displaylastmodified.setText(formattedDateString);
                    displaydatetaken.setText(formattedDateString);

                    String path = al_images.get(int_position).getAl_imagepath().get(i);
                    displaypath.setText(path);

                }
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert12 = builder.create();
                alert12.show();
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
        if (mSelected.size()>1){
            mRename.setVisible(false);
        }
        mSelectAll.setVisible(true);
        mProperties.setVisible(true);
        if (mSelected.size()>1){
            mProperties.setVisible(false);
        }
    }

    private void renameFile(){
        AlertDialog.Builder builder2 = new AlertDialog.Builder(PhotosActivity.this);
        builder2.setMessage("Rename File");
        final EditText input = new EditText(PhotosActivity.this);
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
                            File oldName = new File(al_images.get(int_position).getAl_imagepath().get(selected));

                            String OldFilename=oldName.getName();
                            String ext=OldFilename.substring(OldFilename.lastIndexOf("."));

                            String OldFileDir=oldName.getAbsolutePath().replace(OldFilename,string)+ext;


                            File newFile = new File(OldFileDir);
                            if (!newFile.exists()) {
                                boolean success = oldName.renameTo(newFile);
                                if (success) {
                                   // Log.v(TAG, "not renamed");
                                    makeToast("File Renamed");
                                    renameImageFile(PhotosActivity.this,oldName,newFile);

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
        resolver.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?", new String[] { f.getAbsolutePath() });
    }

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
    int size=al_images.get(int_position).getAl_imagepath().size();
    multiselectOn=true;
    mSelected.clear();
    for(int i=0; i<size;i++){
        mSelected.add(i);
    }
    if(adapter!=null)
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {

        if(!mSelected.isEmpty()){
            clearMultiSelect();
        }else{
            super.onBackPressed();
        }
    }



    //move files
    private void moveFile(File file_Source, File file_Destination, boolean isCopy) throws IOException {
        FileChannel source = null;
        FileChannel destination = null;
        if (!file_Destination.exists()) {
            file_Destination.createNewFile();
        }

        try {
            source = new FileInputStream(file_Source).getChannel();
            destination = new FileOutputStream(file_Destination).getChannel();

            long count = 0;
            long size = source.size();
            while ((count += destination.transferFrom(source, count, size - count)) < size) ;
            if (!isCopy) {
                file_Source.delete();
                MediaScannerConnection.scanFile(this,
                        new String[]{file_Source.toString()}, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            public void onScanCompleted(String path, Uri uri) {
                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                Log.i("ExternalStorage", "-> uri=" + uri);
                            }
                        });
            }

        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    public class Reload extends AsyncTask<String, Void, File> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(PhotosActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected File doInBackground(String... strings) {
            //content provider takes time to update
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            fn_imagespath();
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            pd.dismiss();
            //obj_adapter = new Adapter_PhotosFolder(getApplicationContext(), al_images, int_position,ImageGallery.this);
            //gv_folder.setAdapter(obj_adapter);
            adapter = new GridViewAdapter(PhotosActivity.this, al_images, int_position,PhotosActivity.this);
            gridView.setAdapter(adapter);
            clearMultiSelect();
        }
    }


    void makeToast(String str){
        Toast.makeText(this, str,Toast.LENGTH_LONG).show();
    }

}
