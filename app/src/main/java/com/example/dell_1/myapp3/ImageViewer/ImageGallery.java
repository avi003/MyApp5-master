package com.example.dell_1.myapp3.ImageViewer;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dell_1.myapp3.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ImageGallery extends AppCompatActivity {
    public static ArrayList<Model_images> al_images = new ArrayList<>();
    ArrayList<String> selectedImages = new ArrayList<>();
    boolean boolean_folder;
    private static final String TAG = " com.example.dell_1.myapp3.ImageViewer";
    Adapter_PhotosFolder obj_adapter;
    GridView gv_folder;
    MenuItem mSort, mSettings, mRename, mSelectAll, mProperties;
    private static final int REQUEST_PERMISSIONS = 100;
    int int_position;
    String MY_PREFS_NAME="MMR";
    ArrayList<Integer> mSelected=new ArrayList<>();
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int mode=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        gv_folder = (GridView) findViewById(android.R.id.list);
        obj_adapter = new Adapter_PhotosFolder(this, al_images, int_position,this);
        gv_folder.setAdapter(obj_adapter);



         sp = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);


        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        topToolBar.setTitle("");
        topToolBar.setSubtitle("");
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));

        final ImageButton buttonpaste = (ImageButton) findViewById(R.id.buttonpaste);
        buttonpaste.setVisibility(View.GONE);
        if (getIntent().getSerializableExtra("selected_images") != null)
            selectedImages = (ArrayList<String>) getIntent().getSerializableExtra("selected_images");
        //Log.v(TAG, "The size of arraylist " + selectedImages.size());

        final ImageButton buttoncut = (ImageButton) findViewById(R.id.button1);
        final ImageButton button2 = (ImageButton) findViewById(R.id.button2);
        final ImageButton button3 = (ImageButton) findViewById(R.id.button3);
        final ImageButton button4 = (ImageButton) findViewById(R.id.button4);
        buttoncut.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        button4.setVisibility(View.GONE);

        gv_folder.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), PhotosActivity.class);
                intent.putExtra("value", i);
                startActivity(intent);
            }
        });

        gv_folder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()

        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {


                for (int j = 0; j < adapterView.getChildCount(); j++)
                    adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                // change the background color of the selected element
                view.setBackgroundColor(Color.LTGRAY);

                hideMenuItem();

                buttoncut.setVisibility(View.VISIBLE);
                button2.setVisibility(View.VISIBLE);
                button3.setVisibility(View.VISIBLE);
                button4.setVisibility(View.VISIBLE);
                button2.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                buttoncut.setVisibility(View.GONE);
                                button2.setVisibility(View.GONE);
                                button3.setVisibility(View.GONE);
                                button4.setVisibility(View.GONE);
                                buttonpaste.setVisibility(View.VISIBLE);

                                new LongOperation(i).execute();
                            }

                        });
                button3.setOnClickListener(
                        new View.OnClickListener() {
                            public void onClick(View view) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(ImageGallery.this);
                                builder1.setMessage("Are you sure you want to delete it ?");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                File file = new File(al_images.get(int_position).getAl_imagepath().get(i));
                                                file.delete();
                                                MediaScannerConnection.scanFile(ImageGallery.this, new String[]{file.toString()}, null,
                                                        new MediaScannerConnection.OnScanCompletedListener() {
                                                            public void onScanCompleted(String path, Uri uri) {
                                                                Log.i("ExternalStorage", "Scanned " + path + ":");
                                                                Log.i("ExternalStorage", "-> uri=" + uri);
                                                            }
                                                        });
                                                al_images.remove(i);
                                                obj_adapter.notifyDataSetChanged();
                                                finish();
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

        if ((ContextCompat.checkSelfPermission(

                getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

        {
            if ((ActivityCompat.shouldShowRequestPermissionRationale(ImageGallery.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) && (ActivityCompat.shouldShowRequestPermissionRationale(ImageGallery.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(ImageGallery.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        } else

        {
            Log.e("Else", "Else");
            //fn_imagespath();
            new Reload().execute();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mode= sp.getInt("grant", -1);
        if(mode!=-1 && mode==1)
        new Reload().execute();
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



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults.length > 0 && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        //fn_imagespath();
                        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putInt("grant", 1);
                        editor.apply();
                        new Reload().execute();
                    } else {
                        Toast.makeText(ImageGallery.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private class LongOperation extends AsyncTask<String, Void, File> {

        int id, count = 0;
        File destinationImage;
       // private static final String TAG = " com.example.dell_1.myapp3.ImageViewer";

        public LongOperation(int id) {
            this.id = id;
        }


        @Override
        protected File doInBackground(String... params) {

            for (String imagePath : selectedImages) {
                File sourceImage = new File(imagePath); //returns the image File from model class to
                // be// moved.

                count++;
                //Log.v(TAG, "The size " + count);
                destinationImage = new File(al_images.get(id).getDirectoryPath() +
                        File.separator + sourceImage.getName());

                try {
                    moveFile(sourceImage, destinationImage, false);
                    notifyMediaStoreScanner(destinationImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return destinationImage;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //fn_imagespath();
                    new Reload().execute();
                }
            }, 1000); // additional delay time of 1 sec to update media scanner
        }
    }

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

    public final void notifyMediaStoreScanner(File file) {
//        try {
//            MediaStore.Images.Media.insertImage(getBaseContext().getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), null);
        getBaseContext().sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        getBaseContext().sendBroadcast(new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
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
                // location found
                return true;

            case R.id.action_selectAll:
                // location found
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



    void makeToast(String str){
        Toast.makeText(this, str,Toast.LENGTH_LONG).show();
    }


    public class Reload extends AsyncTask<String, Void, File>{
    ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ImageGallery.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected File doInBackground(String... strings) {
            fn_imagespath();
            return null;
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            pd.dismiss();
            obj_adapter = new Adapter_PhotosFolder(getApplicationContext(), al_images, int_position,ImageGallery.this);
            gv_folder.setAdapter(obj_adapter);
        }
    }

}