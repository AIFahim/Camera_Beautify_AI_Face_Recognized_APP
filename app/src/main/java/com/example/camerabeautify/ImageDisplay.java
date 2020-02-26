package com.example.camerabeautify;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.camerabeautify.Activity.CameraWithFilterActivity;
import com.example.camerabeautify.camfilter.utils.MarginDecoration;
import com.example.camerabeautify.camfilter.utils.PicHolder;
import com.example.camerabeautify.camfilter.utils.itemClickListener;
import com.example.camerabeautify.camfilter.utils.pictureFacer;
import com.example.camerabeautify.camfilter.utils.picture_Adapter;
import com.example.camerabeautify.R;

import java.util.ArrayList;

public class ImageDisplay extends AppCompatActivity implements itemClickListener {

    RecyclerView imageRecycler;
    ArrayList<pictureFacer> allpictures;
    ProgressBar load;
    String foldePath;
    TextView folderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);

        folderName = findViewById(R.id.foldername);
        folderName.setText(getIntent().getStringExtra("folderName"));
        foldePath = getIntent().getStringExtra("folderPath");


        allpictures = new ArrayList<>();
        imageRecycler = findViewById(R.id.recycler);
        imageRecycler.addItemDecoration(new MarginDecoration(this));
        imageRecycler.hasFixedSize();
        load = findViewById(R.id.loader);

        if (allpictures.isEmpty()) {
            load.setVisibility(View.VISIBLE);
            allpictures = getAllImagesByFolder(foldePath);
            imageRecycler.setAdapter(new picture_Adapter(allpictures, ImageDisplay.this, (itemClickListener) this));
            load.setVisibility(View.GONE);
        } else {

        }
    }

    public ArrayList<pictureFacer> getAllImagesByFolder(String path){
        ArrayList<pictureFacer> images = new ArrayList<>();
        Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};
        Cursor cursor = ImageDisplay.this.getContentResolver().query( allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[] {"%"+path+"%"}, null);
        try {
            cursor.moveToFirst();
            do{
                pictureFacer pic = new pictureFacer();

                pic.setPicturName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));

                pic.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));

                pic.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                images.add(pic);
            }while(cursor.moveToNext());
            cursor.close();
            ArrayList<pictureFacer> reSelection = new ArrayList<>();
            for(int i = images.size()-1;i > -1;i--){
                reSelection.add(images.get(i));
            }
            images = reSelection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return images;
    }


    @Override
    public void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics) {
        pictureBrowserFragment browser = pictureBrowserFragment.newInstance(pics,position,ImageDisplay.this);

        // Note that we need the API version check here because the actual transition classes (e.g. Fade)
        // are not in the support library and are only available in API 21+. The methods we are calling on the Fragment
        // ARE available in the support library (though they don't do anything on API < 21)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //browser.setEnterTransition(new Slide());
            //browser.setExitTransition(new Slide()); uncomment this to use slide transition and comment the two lines below
            browser.setEnterTransition(new Fade());
            browser.setExitTransition(new Fade());
        }

        getSupportFragmentManager()
                .beginTransaction()
                .addSharedElement(holder.picture, position+"picture")
                .add(R.id.displayContainer, browser)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onPicClicked(String pictureFolderPath,String folderName) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent=new Intent(ImageDisplay.this,CameraWithFilterActivity.class);
        intent.putExtra("IntentValue","1");
        //intent.putExtra("IntentValue","9");
        startActivity(intent);
    }
}