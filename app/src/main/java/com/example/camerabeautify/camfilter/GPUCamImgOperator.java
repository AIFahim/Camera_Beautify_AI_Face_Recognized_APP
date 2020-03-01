package com.example.camerabeautify.camfilter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.camerabeautify.camfilter.widget.LuoGLBaseView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;


public class GPUCamImgOperator {

    public enum GPUImgFilterType {
        NONE,
        COOL,
        HEALTHY,
        EMERALD,
        NOSTALGIA,
        CRAYON,
        EVERGREEN
    }

    public static Context context;
    public static LuoGLBaseView luoGLBaseView;


    public GPUCamImgOperator() {
    }

    public void savePicture() {
        SavePictureTask savePictureTask = new SavePictureTask(getOutputMediaFile(), null);
        luoGLBaseView.savePicture(savePictureTask);
        //luoGLBaseView.savePicture(savePictureTask);
    }

    public void startRecord() {
    }

    public void stopRecord() {
    }


    public void switchCamera() {
        CameraEngine.switchCamera();
    }

    /*getExternalStoragePublicDirectory replace with getExternalStorageDirectory*/
    ////////////











    /*public File getOutputMediaFile() {
     *//*File mediaStorageDir = new File(Environment.  getExternalStorageDirectory(
         Environment.DIRECTORY_PICTURES), "Camera Beautifyytttttt");*//*

     *//*File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/text/");*//*

        File root = Environment.getExternalStorageDirectory();
        //File mediaStorageDir = new File(root + "/saved_images");
        File mediaStorageDir = new File(root.getAbsolutePath() + "/fahim/");
        //wallpaperDirectory.mkdirs();
        //File wallpaperDirectory = new File("/sdcard/MyFolder/");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

  //      FileOutputStream fileOutputStream;
       *//* if (mediaFile.exists()) {
            mediaFile.delete();
        }*//*

     *//* mediaFile = new File(new File("/sdcard/MyFolder/"), fname);
        if (mediaFile.exists()) {
            mediaFile.delete();
        }*//*
     *//*  try {
            fileOutputStream = new FileOutputStream(mediaFile);
            fileOutputStream.flush();
            fileOutputStream.close();
            String fpath;
            fpath = mediaFile.toString();
            //galleryAddPic(fpath)
                    

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*//*
      return mediaFile;
    }*/

    ////////////////////////////////////////////////////
    public File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera Beautify");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

}