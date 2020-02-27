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

        String state = Environment.getExternalStorageState();

        // Bitmap bitmap;
        // OutputStream output;

        // Retrieve the image from the res folder
        //bitmap = BitmapFactory.decodeResource(getResources(),
        //      R.drawable.wallpaper);

        // Find the SD Card path
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File filepath = Environment.getExternalStorageDirectory();

            // Create a new folder in SD Card
          /*  File dir = new File(filepath.getAbsolutePath()
                    + "/Save Image Tutorial/");*/
          // "/storage/emulated/1/Pictures/Camera Beautify"
            File dir = new File("SD card/DCIM/CAMERA_BEAUTYFI");

            dir.mkdirs();

            // Create a name for the saved image
            File file = new File(dir, "Fanafilla");

            // Show a toast message on successful save
            //Toast.makeText(MainActivity.this, "Image Saved to SD Card",
            //      Toast.LENGTH_SHORT).show();
            return file;
        }

        return null;
    }


}























   //////////////////////////////
    // last update 2-25-20 10:58AM


  /*  public File getOutputMediaFile() {
    File directory = null;
    File photoDirectory;
     if (Environment.getExternalStorageState() == null) {
        //create new file directory object
         directory = new File(Environment.getDataDirectory()
                + "/RobotiumTestLog/");
       photoDirectory = new File(Environment.getDataDirectory()
                + "/Robotium-Screenshots/");
        *//*
         * this checks to see if there are any previous test photo files
         * if there are any photos, they are deleted for the sake of
         * memory
         *//*
        if (photoDirectory.exists()) {
            File[] dirFiles = photoDirectory.listFiles();
            if (dirFiles.length != 0) {
                for (int ii = 0; ii <= dirFiles.length; ii++) {
                    dirFiles[ii].delete();
                }
            }
        }
        // if no directory exists, create new directory
        if (!directory.exists()) {
            directory.mkdir();
        }

        // if phone DOES have sd card
    } else if (Environment.getExternalStorageState() != null) {
        // search for directory on SD card
        directory = new File(Environment.getExternalStorageDirectory()
                + "/RobotiumTestLog/");
        photoDirectory = new File(
                Environment.getExternalStorageDirectory()
                        + "/Robotium-Screenshots/");
        if (photoDirectory.exists()) {
            File[] dirFiles = photoDirectory.listFiles();
            if (dirFiles.length > 0) {
                for (int ii = 0; ii < dirFiles.length; ii++) {
                    dirFiles[ii].delete();
                }
                dirFiles = null;
            }
        }
        // if no directory exists, create new directory to store test
        // results
        if (!directory.exists()) {
            directory.mkdir();
        }
    }//

        return directory;
    }*/
    ///////////////////////////////////////////////////
    
    
    
    
    
    

   /* private void galleryAddPic(String fpath) {
        *//*Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(fpath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);*//*



        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://" + Environment.getExternalStorageDirectory())));

    }*/

   /* private File getOutputMediaFile(int type) throws Exception{

        // Check that the SDCard is mounted
        File mediaStorageDir;
        if(internalstorage.isChecked())
        {
            mediaStorageDir = new File(getFilesDir().getAbsolutePath() );
        }
        else
        {
            File[] dirs= ContextCompat.getExternalFilesDirs(this, null);
            mediaStorageDir = new File(dirs[dirs.length>1?1:0].getAbsolutePath() );
        }


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){

                output.setText("Failed to create directory.");

                Toast.makeText(this, "Failed to create directory.", Toast.LENGTH_LONG).show();

                Log.d("myapp", "Failed to create directory");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.ENGLISH)  .format(date.getTime());

        File mediaFile;

        if(type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + slpid + "_" + pwsid + "_" + timeStamp + ".mp4");

        }
        else if(type == MEDIA_TYPE_AUDIO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + slpid + "_" + pwsid + "_" + timeStamp + ".3gp");

        } else {
            return null;
        }

        return mediaFile;
    }



    *//** Create a file Uri for saving an image or video
     * @throws Exception *//*
    private  Uri getOutputMediaFileUri(int type) throws Exception{

        return Uri.fromFile(getOutputMediaFile(type));
    }*/






    /*private File SaveImage(*//*Bitmap finalBitmap*//*) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-"+ n +".jpg";
        File file = new File (myDir, fname);
        if (file.exists ())
            file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/