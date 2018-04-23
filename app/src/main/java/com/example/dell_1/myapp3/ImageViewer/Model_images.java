package com.example.dell_1.myapp3.ImageViewer;

import java.util.ArrayList;

public class Model_images {
    String str_folder;
    ArrayList<String> al_imagepath;
    String directory_path;

    public String getStr_folder() {
        return str_folder;
    }

    public void setStr_folder(String str_folder) {
        this.str_folder = str_folder;
    }

    public void setDirectoryPath(String path) {
        directory_path = path;
    }

    public String getDirectoryPath() {
        return directory_path;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }
}