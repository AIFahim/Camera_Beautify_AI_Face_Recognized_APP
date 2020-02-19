package com.example.camerabeautify.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.camerabeautify.R;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import pl.droidsonroids.gif.GifImageView;

public class Splash_Screen extends AppCompatActivity {

    private Intent intent;
    private static final int REQUEST_PERMISSION = 233;
    private int PERMISSION_CALLBACK_CONSTANT = 1000;

    private Camera mCamera;

    Thread splashTread;
    GifImageView l2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);
        l2 =  findViewById(R.id.l2);


        checkAndGivePermission();
        licence();

        thread();
    }

    private void licence(){
        String licenseText = "hMPthC0oBIbtMp515TWb9jZvrLAKWIMvA4Dhf03n51QvnJr7jZowVe86d0WwU0NK9QGRFaXQn628fRu941qyr3FtsI5R7Y6v1XEpL6YvQNWQCkFEt1SAb0hyawimOYf1tfG2lIaNE63c5e+OxXssOVUWvw8tOr2glVwWVzh79NmZMahrnS8l69SoeoXLMKCYlvAt/qJFFk4+6Aq3QvOv3o72fq5p90yty+YWg7o0HirZpMSP9P5/DHYPFqR/ud7twTJ+Yo2+ZzYvodqRQbGG0HseZn8Xpt7fZdFuZbc2HGRMVk56vNDMRlcGZZXAjENk7m2UMhi1ohhuSf4WmIgXCZFiJXvYFByaY625gXKtEI7+b7t81nWQYHP9BEbzURwL";
        XJGArSdkApi.XJGARSDKInitialization(this,licenseText,"DoctorLuoInvitedUser:teacherluo", "LuoInvitedCompany:www.xiaojigou.cn");

    }

    private void thread() {

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 2500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Splash_Screen.this,
                            CameraWithFilterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Splash_Screen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Splash_Screen.this.finish();
                }

            }
        };
        splashTread.start();


    }



    private void checkAndGivePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        )
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_CALLBACK_CONSTANT);

            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_CALLBACK_CONSTANT);



        } else {
            initialize();
        }
    }

    private void initialize() {
        mCamera = getCameraInstance();
    }
    public static Camera getCameraInstance(){

        // I
        Camera c = null;
        return c;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }


            if(allgranted){
                initialize();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,Manifest.permission.CAMERA)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISSION_CALLBACK_CONSTANT);
                }
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,Manifest.permission.WRITE_EXTERNAL_STORAGE )){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },PERMISSION_CALLBACK_CONSTANT);
                }
            } else {
                Toast.makeText(Splash_Screen.this,"Permission is mandatory, Try giving it from App Settings",Toast.LENGTH_LONG).show();
            }

            if(allgranted){
                initialize();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,Manifest.permission.CAMERA)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISSION_CALLBACK_CONSTANT);
                }
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(Splash_Screen.this,Manifest.permission.READ_EXTERNAL_STORAGE )){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    },PERMISSION_CALLBACK_CONSTANT);
                }
            } else {
                Toast.makeText(Splash_Screen.this,"Permission is mandatory, Try giving it from App Settings",Toast.LENGTH_LONG).show();
            }

//            thread();

        }
    }


}
