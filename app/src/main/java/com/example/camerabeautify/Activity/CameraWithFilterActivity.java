package com.example.camerabeautify.Activity;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camerabeautify.Dev_Infos;
import com.example.camerabeautify.ImageDisplay;
import com.example.camerabeautify.R;
import com.example.camerabeautify.camfilter.FilterRecyclerViewAdapter;
import com.example.camerabeautify.camfilter.FilterTypeHelper;
import com.example.camerabeautify.camfilter.GPUCamImgOperator;

import com.example.camerabeautify.camfilter.SharedPref;
import com.example.camerabeautify.camfilter.widget.LuoGLCameraView;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import java.util.ArrayList;






@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraWithFilterActivity extends Activity implements  View.OnClickListener , SeekBar.OnSeekBarChangeListener {

    //Webview
    WebView webView;


    //Switch
    private Switch Swt_Setting_SDcard,Swt_Setting_Shutter_Sound;
    SharedPref sharedPref;

    //Transparent dev_info
    private LinearLayout Dev_Info_layout;
    private RelativeLayout About_layout;


    //Developer info
    private LinearLayout Dev_Info;
    private LinearLayout About_App;

    //layout more.......
    private LinearLayout llt_layout_more, Timing_layout;
    private RelativeLayout relativeLayout;


    private TextView TimeShow;
    //new seekbar..............
    private LinearLayout llt_face_seek,llt_face_col;

    public static int x=0;
    // public static String rstrt = "0";

    //view
    private ImageView img_face_rosy,img_face_brasion, img_face_white, img_face_lifting, img_face_eyes;

    private SeekBar seekBarScale, seekBarFace;
    private int seekBarValue = 0;
    private enum FACEEFFECT {
        ROSY,
        WHITE,
        BRASION,
        LIFTING,
        BIGEYES
    }
    private FACEEFFECT faceeffect = FACEEFFECT.ROSY;
    private int smoothVal, whiteVal, liftingVal, bigEyesVal, redValue;


    private ImageView BtnSticker;

    private Camera mCamera;
    //Flash............................
    private LinearLayout llt_timer_col;

    //,llt_timer_normal,llt_timer_three,llt_timer_five,llt_timer_ten

    private ImageView img_timer_normal, img_timer_three, img_timer_five, img_timer_ten, img_timer;
    boolean isTorchOn = false;


//........................................

    //    trangle....
    private LinearLayout Trangle_1,Trangle_2,Trangle_3,Trangle_4;

    private LinearLayout mFilterLayout;
    private LinearLayout mFaceSurgeryLayout;
    protected SeekBar mFaceSurgeryFaceShapeSeek;
    protected SeekBar mFaceSurgeryBigEyeSeek;
    protected SeekBar mSkinSmoothSeek;
    protected SeekBar mSkinWihtenSeek;
    protected SeekBar mRedFaceSeek;



    private ArrayList<MenuBean> mStickerData;
    private RecyclerView mMenuView;
    private MenuAdapter mStickerAdapter;

    private RecyclerView mFilterListView;
    private FilterRecyclerViewAdapter mAdapter;
    private com.example.camerabeautify.camfilter.GPUCamImgOperator GPUCamImgOperator;
    private boolean isRecording = false;
    private final int MODE_PIC = 1;
    private final int MODE_VIDEO = 2;
    private int mode = MODE_PIC;

    private ImageView btn_shutter;
    private ImageView btn_more,Btn_Touch;

    private ObjectAnimator animator;
    private int PERMISSION_CALLBACK_CONSTANT = 1000;



    LinearLayout onTouchLayout;
    static int b = 0;

    //final MediaPlayer mp = MediaPlayer.create(this,R.raw.capturesound );
    final static MediaPlayer mp = new MediaPlayer();
    final static MediaPlayer mpp = new MediaPlayer();


    private final com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType[] types = new GPUCamImgOperator.GPUImgFilterType[]{
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.NONE,
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.HEALTHY,
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.NOSTALGIA,
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.COOL,
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.EMERALD,
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.EVERGREEN,
            com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.CRAYON
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_with_filter);


        //rstrt = "0";

        onCreate();

    }

    private void onCreate(){


        sharedPref = new SharedPref(this);

        GPUCamImgOperator =  new GPUCamImgOperator();
        LuoGLCameraView luoGLCameraView = (LuoGLCameraView)findViewById(R.id.glsurfaceview_camera);
        GPUCamImgOperator.context = luoGLCameraView.getContext();
        GPUCamImgOperator.luoGLBaseView = luoGLCameraView;


        //Flash............
        initView();
        TimerInitView();
        Sticker();
        beauty();
        SwitchButton();
        DevInfo();
        About_App_func();




        OffTouchMode();

        XJGArSdkApi.XJGARSDKSetOptimizationMode(0);
        XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);




    }


    private void OffTouchMode(){


        relativeLayout = (RelativeLayout)findViewById(R.id.idrl);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                llt_layout_more.setVisibility(View.GONE);
                llt_timer_col.setVisibility(View.GONE);
                mFilterLayout.setVisibility(View.GONE);
                llt_face_col.setVisibility(View.GONE);
                llt_face_seek.setVisibility(View.GONE);
                mMenuView.setVisibility(View.GONE);


                try {

                    if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.VISIBLE);

                    } else if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.GONE);
                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });


    }




    private void DevInfo(){

        // findViewById(R.id.btn_camera_shutter).setClickable(false);
        Dev_Info = findViewById(R.id.idDev_info);
        Dev_Info_layout = findViewById(R.id.layout_dev_info);

        Dev_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intents = new Intent(CameraWithFilterActivity.this, Dev_Infos.class);
//                startActivity(intents);

                llt_layout_more.setVisibility(View.GONE);
                Dev_Info_layout.setVisibility(View.VISIBLE);


                llt_timer_col.setVisibility(View.GONE);
                mFilterLayout.setVisibility(View.GONE);
                mMenuView.setVisibility(View.GONE);
                llt_face_col.setVisibility(View.GONE);
                llt_face_seek.setVisibility(View.GONE);


            }
        });

    }
    private void About_App_func(){
        About_App = findViewById(R.id.llt_About_layout);
        About_layout = findViewById(R.id.layout_about);

        webView = findViewById(R.id.webViewForHtml);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/like.html");
        webView.setBackgroundColor(Color.TRANSPARENT);

        About_App.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intents = new Intent(CameraWithFilterActivity.this, AboutApp.class);
                startActivity(intents);*/
                llt_layout_more.setVisibility(View.GONE);
                About_layout.setVisibility(View.VISIBLE);

                llt_timer_col.setVisibility(View.GONE);
                mFilterLayout.setVisibility(View.GONE);
                mMenuView.setVisibility(View.GONE);
                llt_face_col.setVisibility(View.GONE);
                llt_face_seek.setVisibility(View.GONE);



            }
        });

    }


    private void SwitchButton(){

//        Swt_Setting_SDcard = findViewById(R.id.swt_setting_sdcard);
        Swt_Setting_Shutter_Sound = findViewById(R.id.swt_setting_shutter_sound);
        if (sharedPref.loadSoundModeState()==true){
            Swt_Setting_Shutter_Sound.setChecked(true);
        }
        Swt_Setting_Shutter_Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("TMP",isChecked+" ");
                if (isChecked){
                    sharedPref.setSoundModeState(true);
                }
                else {
                    sharedPref.setSoundModeState(false);
                }
            }
        });




    }

    private void more_button(){


        onTouchLayout.setVisibility(View.GONE);
        llt_timer_col.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);
        Dev_Info_layout.setVisibility(View.GONE);
        About_layout.setVisibility(View.GONE);


        if (llt_layout_more.getVisibility() == View.GONE )
        {
            llt_layout_more.setVisibility(View.VISIBLE);
        }
        else if (llt_layout_more.getVisibility() == View.VISIBLE) {
            llt_layout_more.setVisibility(View.GONE);
        }



        // Toast.makeText(CameraWithFilterActivity.this, "Work in Process", Toast.LENGTH_SHORT).show();


    }

    //seekbar beauty

    private void beauty(){

        img_face_brasion = findViewById(R.id.img_main_smooth);
        //   img_face_rosy = findViewById(R.id.img_main_face_rosy);
        img_face_white = findViewById(R.id.img_main_white);
        img_face_lifting = findViewById(R.id.img_main_slimming);
        img_face_eyes = findViewById(R.id.img_main_big_eye);

//        findViewById(R.id.llt_main_face).setOnClickListener(this);
//        findViewById(R.id.llt_main_face_rosy).setOnClickListener(this);
        findViewById(R.id.llt_main_smooth).setOnClickListener(this);
        findViewById(R.id.llt_main_white).setOnClickListener(this);
        findViewById(R.id.llt_main_slimming).setOnClickListener(this);
        findViewById(R.id.llt_main_big_eye).setOnClickListener(this);

        seekBarScale = findViewById(R.id.skb_main_scale);
        seekBarScale.setOnSeekBarChangeListener(this);

        seekBarFace = findViewById(R.id.skb_main_face);
        seekBarFace.setOnSeekBarChangeListener(this);

        smoothVal = 0;
        whiteVal = 0;
        liftingVal = 0;
        bigEyesVal = 0;
        redValue = 0;

        initBeautyIcons();

    }

    private void initBeautyIcons() {

        llt_face_col = findViewById(R.id.llt_main_beauty_col);
        llt_face_seek = findViewById(R.id.llt_main_face_seekbar);

        img_face_eyes.setImageResource(R.drawable.ico_bigeye);
        img_face_brasion.setImageResource(R.drawable.ico_smoothly);
        img_face_white.setImageResource(R.drawable.ico_whitening);
//        img_face_rosy.setImageResource(R.drawable.ico_flash);
        img_face_lifting.setImageResource(R.drawable.ico_slimming);

        llt_face_seek.setVisibility(View.GONE);
        llt_face_col.setVisibility(View.GONE);
    }



    private void initFaceMenu(int value) {
//        img_face.setImageResource(R.drawable.ico_face_sel);
        llt_face_col.setVisibility(View.VISIBLE);
        llt_face_seek.setVisibility(View.VISIBLE);
        seekBarFace.setProgress(value);
    }


    //Flash Start...........................................
    private void TimerInitView(){

        img_timer = findViewById(R.id.img_main_timer);
//        img_timer = findViewById(R.id.img_main_timer);
//        img_timer = findViewById(R.id.img_main_timer);
//        img_timer = findViewById(R.id.img_main_timer);


        img_timer_normal = findViewById(R.id.img_main_timer_normal);
        img_timer_three = findViewById(R.id.img_main_timer_three);
        img_timer_five = findViewById(R.id.img_main_timer_five);
        img_timer_ten = findViewById(R.id.img_main_timer_ten);

        llt_timer_col = findViewById(R.id.llt_main_timer_col);

//        llt_timer_normal = findViewById(R.id.llt_main_timer_normal);
//        llt_timer_three = findViewById(R.id.llt_main_timer_three);
//        llt_timer_five = findViewById(R.id.llt_main_timer_five);
//        llt_timer_ten = findViewById(R.id.llt_main_timer_ten);

        //  Btn_Touch = findViewById(R.id.btn_touch);

        findViewById(R.id.img_main_timer).setOnClickListener( this);
        findViewById(R.id.llt_main_timer_normal).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_three).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_five).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_ten).setOnClickListener(this);



        findViewById(R.id.btn_sticker).setOnClickListener(this);
        findViewById(R.id.btn_touch).setOnClickListener(this);



        llt_timer_col.setVisibility(View.GONE);

    }

    private void Sticker(){
        mMenuView= (RecyclerView)findViewById(R.id.mMenuView);
        BtnSticker = findViewById(R.id.btn_sticker);
        mMenuView.setVisibility(View.GONE);

    }

//    private void initFlashMenu() {
//
//        img_flash_on.setImageResource(R.drawable.ico_flash_on);
//        img_flash_off.setImageResource(R.drawable.ico_flash_off);
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.img_main_timer:
                onTouchLayout.setVisibility(View.GONE);
                llt_layout_more.setVisibility(View.GONE);
                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);

                if (llt_timer_col.getVisibility() == View.GONE)
                {
                    llt_timer_col.setVisibility(View.VISIBLE);

                }
                else if (llt_timer_col.getVisibility() == View.VISIBLE)
                {
                    llt_timer_col.setVisibility(View.GONE);
                }
                break;

            case R.id.llt_main_timer_normal:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_normal);
                x=0;

                break;

            case R.id.llt_main_timer_three:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_three);
                x=3;
                break;

            case R.id.llt_main_timer_five:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_five);
                x=5;
                break;

            case R.id.llt_main_timer_ten:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_ten);
                x=10;

                break;








            // ----- Flash column -----

//            case R.id.img_main_flash:
//
//                onTouchLayout.setVisibility(View.GONE);
//
//                //TODO
//                llt_layout_more.setVisibility(View.GONE);
//
//                if (llt_timer_col.getVisibility() == View.GONE)
//                {
//
//
//                    llt_timer_col.setVisibility(View.VISIBLE);
//
//                    Trangle_2.setVisibility(View.VISIBLE);
//
//                }
//                else if (llt_timer_col.getVisibility() == View.VISIBLE) {
//
//                    llt_timer_col.setVisibility(View.GONE);
//
//                    Trangle_2.setVisibility(View.INVISIBLE);
//
//                }
//                break;


//           case R.id.llt_main_flash_on:
//                initFlashMenu();
//                img_flash_on.setImageResource(R.drawable.ico_flash_on_sel);
//                Trangle_2.setVisibility(View.INVISIBLE);
//
//                torchToggle("on");
//
//                break;
//            case R.id.llt_main_flash_off:
//                initFlashMenu();
//                img_flash_off.setImageResource(R.drawable.ico_flash_off_sel);
//                Trangle_2.setVisibility(View.INVISIBLE);
//
//                torchToggle("off");
//                break;


            //Sticker_Button...................


            case R.id.btn_touch:
                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);
                if (onTouchLayout.getVisibility() == View.GONE)
                {

                    onTouchLayout.setVisibility(View.VISIBLE);
                    Btn_Touch.setImageResource(R.drawable.icon_touch_enble_sel);
                    Btn_Touch.setTag(R.drawable.icon_touch_enble_sel);
                    onTouchLayout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {




                            onTouchLayout.setVisibility(View.GONE);
                            if(mode == MODE_PIC){

                                Log.d("TEMP","Touch");
                                startcount();


                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        // Toast.makeText(CameraWithFilterActivity.this, "3sec", Toast.LENGTH_SHORT).show();
                                        onTouchLayout.setVisibility(View.VISIBLE);
                                    }

                                }, 1000);
                            }

                            return false;

                        }
                    });


                }
                else if (onTouchLayout.getVisibility() == View.VISIBLE) {

                    onTouchLayout.setVisibility(View.GONE);
                    Btn_Touch.setImageResource(R.drawable.icon_touch_enble);
                    Btn_Touch.setTag(R.drawable.icon_touch_enble);
                }
                break;


            case R.id.btn_sticker:

                About_layout.setVisibility(View.GONE);
                Dev_Info_layout.setVisibility(View.GONE);
                onTouchLayout.setVisibility(View.GONE);
                mFilterLayout.setVisibility(View.GONE);
                llt_face_col.setVisibility(View.GONE);
                llt_face_seek.setVisibility(View.GONE);


                if (mMenuView.getVisibility() == View.GONE)
                {
                    mMenuView.setVisibility(View.VISIBLE);


                }
                else if (mMenuView.getVisibility() == View.VISIBLE) {

                    mMenuView.setVisibility(View.GONE);
                }
                break;



            //face

//            case R.id.llt_main_face:
//                img_face.setImageResource(R.drawable.ico_face_sel);
//                llt_face_col.setVisibility(View.VISIBLE);
//                break;


//            case R.id.llt_main_face_rosy:
//                faceeffect = FACEEFFECT.ROSY;
//                initFaceMenu(redValue);
//                img_face_rosy.setImageResource(R.drawable.ico_face_sel);
//                break;
            case R.id.llt_main_smooth:
                faceeffect = FACEEFFECT.BRASION;
                initFaceMenu(smoothVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly_sel);
                img_face_white.setImageResource(R.drawable.ico_whitening);
                img_face_lifting.setImageResource(R.drawable.ico_slimming);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye);

                //      txt_face_smooth.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case R.id.llt_main_white:
                faceeffect = FACEEFFECT.WHITE;
                initFaceMenu(whiteVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly);
                img_face_white.setImageResource(R.drawable.ico_whitening_sel);
                img_face_lifting.setImageResource(R.drawable.ico_slimming);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye);

                //    txt_face_white.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case R.id.llt_main_slimming:
                faceeffect = FACEEFFECT.LIFTING;
                initFaceMenu(liftingVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly);
                img_face_white.setImageResource(R.drawable.ico_whitening);
                img_face_lifting.setImageResource(R.drawable.ico_slimming_sel);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye);
                //    txt_face_slimming.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;
            case R.id.llt_main_big_eye:
                faceeffect = FACEEFFECT.BIGEYES;
                initFaceMenu(bigEyesVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly);
                img_face_white.setImageResource(R.drawable.ico_whitening);
                img_face_lifting.setImageResource(R.drawable.ico_slimming);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye_sel);
                //   txt_face_bigeye.setTextColor(getResources().getColor(R.color.color_yellow_sel));
                break;





        }

    }


    //.....................................





    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        seekBarValue = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
//            case R.id.skb_main_scale:
////                activity.changeCameraZoomExposure(seekBarValue, true);
//                break;
            case R.id.skb_main_face:
                switch (faceeffect) {

                    case WHITE:
                        whiteVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(whiteVal);
                        break;
                    case BIGEYES:
                        bigEyesVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetBigEyeParam(bigEyesVal);
                        break;
                    case BRASION:
                        smoothVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(smoothVal);
                        break;
                    case LIFTING:
                        liftingVal = seekBarValue;
                        XJGArSdkApi.XJGARSDKSetThinChinParam(liftingVal);
                        break;
                }
                break;
        }
    }

    private void showFaceBeauty(){

        About_layout.setVisibility(View.GONE);
        Dev_Info_layout.setVisibility(View.GONE);
        onTouchLayout.setVisibility(View.GONE);
        mFilterLayout.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);


        if (llt_face_col.getVisibility() == View.GONE )


        {

            llt_face_col.setVisibility(View.VISIBLE);
            llt_face_seek.setVisibility(View.GONE);

        }
        else if (llt_face_col.getVisibility() == View.VISIBLE || llt_face_seek.getVisibility()==View.VISIBLE) {
            llt_face_col.setVisibility(View.GONE);
            llt_face_seek.setVisibility(View.GONE);
        }
    }




    //.....................................




    private void torchToggle(String command) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null; // Usually back camera is at 0 position.
            try {
                if (camManager != null) {
                    cameraId = camManager.getCameraIdList()[0];
                }
                if (camManager != null) {
                    if (command.equals("on")) {
                        camManager.setTorchMode(cameraId, true);   // Turn ON
                        isTorchOn = true;
                    }
                    else {
                        camManager.setTorchMode(cameraId, false);  // Turn OFF
                        isTorchOn = false;
                    }
                }
            } catch (CameraAccessException e) {
                e.getMessage();
            }
        }
    }


    //Flash End......................................................................



    private void initView(){

        mFilterLayout = (LinearLayout)findViewById(R.id.layout_filter);
        onTouchLayout = (LinearLayout)findViewById(R.id.idOnTouch);

        Timing_layout = (LinearLayout)findViewById(R.id.idtiminglayout);
        TimeShow = (TextView) findViewById(R.id.idshowtime);
        mFaceSurgeryLayout = (LinearLayout)findViewById(R.id.layout_facesurgery);
        mFaceSurgeryFaceShapeSeek = (SeekBar)findViewById(R.id.faceShapeValueBar);
        mFaceSurgeryFaceShapeSeek.setProgress(0);
        mFaceSurgeryBigEyeSeek = (SeekBar)findViewById(R.id.bigeyeValueBar);
        mFaceSurgeryBigEyeSeek.setProgress(0);

        mSkinSmoothSeek = (SeekBar)findViewById(R.id.skinSmoothValueBar);
        mSkinSmoothSeek.setProgress(0);
        mSkinWihtenSeek = (SeekBar)findViewById(R.id.skinWhitenValueBar);
        mSkinWihtenSeek.setProgress(0);
        mRedFaceSeek = (SeekBar)findViewById(R.id.redFaceValueBar);
        mRedFaceSeek.setProgress(0);
        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(0);
        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(0);
        XJGArSdkApi.XJGARSDKSetRedFaceParam(0);


        mFaceSurgeryFaceShapeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int strength = value;//(int)(value*(float)1.0/100);
                XJGArSdkApi.XJGARSDKSetThinChinParam(strength);
            }
        });
        mFaceSurgeryBigEyeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int strength = value;//(int)(value*(float)1.0/100);
                XJGArSdkApi.XJGARSDKSetBigEyeParam(strength);
            }
        });
        mSkinSmoothSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int level = value;//(int)(value/18);
                XJGArSdkApi.XJGARSDKSetSkinSmoothParam(level);
//                GPUCamImgOperator.setBeautyLevel(level);
            }
        });

        mSkinWihtenSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int level = value;//(int)(value/18);
                XJGArSdkApi.XJGARSDKSetWhiteSkinParam(level);
//                GPUCamImgOperator.setBeautyLevel(level);
            }
        });
        mRedFaceSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public int value;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int level = value;//(int)(value/18);
                XJGArSdkApi.XJGARSDKSetRedFaceParam(level);
//                GPUCamImgOperator.setBeautyLevel(level);
            }
        });

        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);

        btn_shutter = (ImageView)findViewById(R.id.btn_camera_shutter);
        btn_more = (ImageView)findViewById(R.id.btn_more);
        llt_layout_more = findViewById(R.id.layout_more);
        Btn_Touch = (ImageView)findViewById(R.id.btn_touch);


        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);
        //findViewById(R.id.btn_camera_closefilter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_shutter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_switch).setOnClickListener(btn_listener);
        findViewById(R.id.btn_more).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_beauty).setOnClickListener(btn_listener);
        findViewById(R.id.btn_gallery).setOnClickListener(btn_listener);
        // findViewById(R.id.btn_touch).setOnClickListener(btn_listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);

        mAdapter = new FilterRecyclerViewAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);

        animator = ObjectAnimator.ofFloat(btn_shutter,"rotation",0,360);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        LuoGLCameraView cameraView = (LuoGLCameraView)findViewById(R.id.glsurfaceview_camera);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.y;//screenSize.x * 4 / 3;
        cameraView.setLayoutParams(params);



        mMenuView= (RecyclerView)findViewById(R.id.mMenuView);
        mStickerData=new ArrayList<>();
        mMenuView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mStickerAdapter=new MenuAdapter(this,mStickerData);
        mStickerAdapter.setOnClickListener(new ClickUtils.OnClickListener() {
            @Override
            public void onClick(View v, int type, int pos, int child) {
                MenuBean m=mStickerData.get(pos);
                String name=m.name;
                String path = m.path;
                if (name.equals("None")) {
                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);
                    mStickerAdapter.checkPos=pos;
                    v.setSelected(true);
//                }else if(name.equals("")){
//
//                    mStickerAdapter.checkPos=pos;
//                    v.setSelected(true);
                }else{
                    String stickerPaperdir = XJGArSdkApi.getPrivateResDataDir(getApplicationContext());
                    stickerPaperdir = stickerPaperdir +"/StickerPapers/"+ path;
                    ZIP.unzipAStickPaperPackages(stickerPaperdir);

                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(true);
                    XJGArSdkApi.XJGARSDKChangeStickpaper(path);
                    mStickerAdapter.checkPos=pos;
                    v.setSelected(true);
                }
                mStickerAdapter.notifyDataSetChanged();
            }
        });
        mMenuView.setAdapter(mStickerAdapter);
        initEffectMenu();
    }


    protected void initEffectMenu() {

        MenuBean bean=new MenuBean();
        bean.name="None";
        bean.path="";
        bean.image = R.drawable.orginal;

        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="Mathal";
        bean.path="mathal"; //mymask
        bean.image = R.drawable.st_mathal;
        mStickerData.add(bean);


        bean=new MenuBean();
        bean.name="";
        bean.path="headflag";
        bean.image = R.drawable.st_headflag;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="cheekflag";
        bean.image = R.drawable.st_cheekflag;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="cutepecha";
        bean.image = R.drawable.st_cute_pecha;
        mStickerData.add(bean);


        bean=new MenuBean();
        bean.name="";
        bean.path="pagri";
        bean.image = R.drawable.st_pagri;
        mStickerData.add(bean);


        bean=new MenuBean();
        bean.name="";
        bean.path="redghomta";
        bean.image = R.drawable.st_ghomta_two;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="flower";
        bean.image = R.drawable.st_flower;
        mStickerData.add(bean);

        //todo
        bean=new MenuBean();
        bean.name="";
        bean.path="golaygamcha";
        bean.image = R.drawable.st_gamcha;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="crowngold";
        bean.image = R.drawable.st_crowngold;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="rabitear";
        bean.image = R.drawable.st_rabitear;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="hgamcha";
        bean.image = R.drawable.st_hgamcha;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="cutetiger";
        bean.image = R.drawable.st_cute_tiger;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="nurse";
        bean.image = R.drawable.st_nurse;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="pinkribon";
        bean.image = R.drawable.st_pinkribon;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="bridal";
        bean.image = R.drawable.st_bridal;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="kanerdul";
        bean.image = R.drawable.st_kanerdul;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="headribon";
        bean.image = R.drawable.st_headribon;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="girlcap";
        bean.image = R.drawable.st_girls_cap;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="eyeliner";
        bean.image = R.drawable.st_eyeliner;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="eyelass";
        bean.image = R.drawable.st_eyelass;
        mStickerData.add(bean);


        bean=new MenuBean();
        bean.name="";
        bean.path="kalacasma";
        bean.image = R.drawable.st_kalachasma;
        mStickerData.add(bean);

        //todo
        
        bean=new MenuBean();
        bean.name="";
        bean.path="hairone";
        bean.image = R.drawable.st_hairone;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="longear";
        bean.image = R.drawable.st_longear;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="scrf";
        bean.image = R.drawable.st_scarf;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="hairtwo";
        bean.image = R.drawable.st_hairtwo;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="noth";
        bean.image = R.drawable.st_noth;
        mStickerData.add(bean);

        bean=new MenuBean();
        bean.name="";
        bean.path="tip";
        bean.image = R.drawable.st_tip;
        mStickerData.add(bean);



        mStickerAdapter.notifyDataSetChanged();
    }

    private FilterRecyclerViewAdapter.onFilterChangeListener onFilterChangeListener = new FilterRecyclerViewAdapter.onFilterChangeListener(){

        @Override
        public void onFilterChanged(com.example.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType filterType) {
//            GPUCamImgOperator.setFilter(filterType);
            String filterName = FilterTypeHelper.FilterType2FilterName(filterType);
            XJGArSdkApi.XJGARSDKChangeFilter(filterName);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (grantResults.length != 1 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(mode == MODE_PIC)
                takePhoto();
            else
                takeVideo();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode==1){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    static boolean bShowFaceSurgery = false;
    static boolean bShowImgFilters = false;
    private View.OnClickListener btn_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int buttonId = v.getId();
            if( buttonId == R.id.btn_more) {
                more_button();
            }
            if (buttonId == R.id.btn_camera_shutter) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_CALLBACK_CONSTANT);
                }  else {
                    if(mode == MODE_PIC)
                    {

                        btn_shutter.setVisibility(View.GONE);
                        //capturewithtime();

                        Log.d("TEMP","Button");
                        startcount();


                    }
                    else
                        takeVideo();
                }
            }
            else if (buttonId == R.id.btn_camera_filter) {
                bShowImgFilters = !bShowImgFilters;
                if(bShowImgFilters)
                    showFilters();
                else
                    hideFilters();
            }
            else if (buttonId == R.id.btn_camera_switch) {
                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);
                GPUCamImgOperator.switchCamera();
            }
            else if (buttonId == R.id.btn_camera_beauty) {

                showFaceBeauty();

            }


            else if (buttonId ==  R.id.btn_gallery) {

                Dev_Info_layout.setVisibility(View.GONE);

                if (ContextCompat.checkSelfPermission(CameraWithFilterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
//                    String pictureFolderPath = "/storage/emulated/0/Pictures/Camera Beautify";
//                    String folderName = "Camera Beautify";
//                    Intent galleryIntent = new Intent(CameraWithFilterActivity.this, ImageDisplay.class);
//                    galleryIntent.putExtra("folderPath",pictureFolderPath);
//                    galleryIntent.putExtra("folderName",folderName);
//                    startActivity(galleryIntent);
                    Intent pickPhoto = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivity(pickPhoto);
                }else{
                    requestStoragePermission();
                }

            }
        }
    };



    private void  requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.shouldShowRequestPermissionRationale(CameraWithFilterActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }




    private void takePhoto(){
        Dev_Info_layout.setVisibility(View.GONE);
        About_layout.setVisibility(View.GONE);
        GPUCamImgOperator.savePicture();
    }

    private void takeVideo(){
        if(isRecording) {
            animator.end();
            GPUCamImgOperator.stopRecord();
        }else {
            animator.start();
            GPUCamImgOperator.startRecord();
        }
        isRecording = !isRecording;
    }


    private void showFaceSurgery()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFaceSurgeryLayout, "translationY", mFaceSurgeryLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                findViewById(R.id.btn_camera_shutter).setClickable(false);
                mFaceSurgeryLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();

    }

    private void hideFaceSurgery()
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFaceSurgeryLayout, "translationY", 0 ,  mFaceSurgeryLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mFaceSurgeryLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mFaceSurgeryLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }
        });
        animator.start();

    }

    //todo
    private void showFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // findViewById(R.id.btn_camera_shutter).setClickable(true);

                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);
                onTouchLayout.setVisibility(View.GONE);
                mFilterLayout.setVisibility(View.VISIBLE);

                llt_face_col.setVisibility(View.GONE);
                llt_face_seek.setVisibility(View.GONE);
                mMenuView.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }


    private void hideFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0 ,  mFilterLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                mFilterLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                mFilterLayout.setVisibility(View.INVISIBLE);
                findViewById(R.id.btn_camera_shutter).setClickable(true);
            }
        });
        animator.start();


    }



    @Override
    protected void onPause() {
        super.onPause();
        if(mCamera!= null)
        {
            mCamera.stopPreview();
            mCamera.release();
        }




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mCamera!= null)
        {

            mCamera.release();
            mCamera=null;
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        onCreate();

    }



    public void startcount(){


        new CountDownTimer(x*1000, 1000) {

            public void onTick(long millisUntilFinished) {

                onTouchLayout.setVisibility(View.GONE);
                btn_shutter.setVisibility(View.GONE);
                Timing_layout.setVisibility(View.VISIBLE);
                TimeShow.setText( ""+ millisUntilFinished / 1000);
            }

            public void onFinish() {

                Toast.makeText(CameraWithFilterActivity.this, "Captured", Toast.LENGTH_SHORT).show();
                takePhoto();
                Timing_layout.setVisibility(View.GONE);
                MediaPlayer mp = MediaPlayer.create(CameraWithFilterActivity.this,R.raw.capturesound );
                if (sharedPref.loadSoundModeState()==true){
                    mp.start();
                }

                Handler handlerr = new Handler();
                handlerr.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        btn_shutter.setVisibility(View.VISIBLE);
                    }

                }, 1000);

                try {

                    if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.VISIBLE);

                    } else if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.GONE);
                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }

        }.start();

    }


//    private void restartFirstActivity()
//    {
//
//        Intent intent = getIntent();
//        rstrt =  intent.getExtras().getString("IntentValue");
//
//        if(rstrt.equals(1)) {
//
//
//            Intent i = getApplicationContext().getPackageManager()
//                    .getLaunchIntentForPackage(getApplicationContext().getPackageName());
//
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//
//        }
//
//        rstrt = "0";
//    }

}