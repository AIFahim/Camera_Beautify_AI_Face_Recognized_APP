package com.loetech.camerabeautify.Activity;

import androidx.annotation.NonNull;
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
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
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

import com.loetech.camerabeautify.R;
import com.loetech.camerabeautify.camfilter.FilterRecyclerViewAdapter;
import com.loetech.camerabeautify.camfilter.FilterTypeHelper;
import com.loetech.camerabeautify.camfilter.GPUCamImgOperator;

import com.loetech.camerabeautify.camfilter.SharedPref;
import com.loetech.camerabeautify.camfilter.widget.LuoGLCameraView;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getExternalStoragePublicDirectory;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CameraWithFilterActivity extends Activity implements  View.OnClickListener , SeekBar.OnSeekBarChangeListener {

    //Webview
    WebView webView;

    //////////////////////////////////////
    private MediaProjectionManager mProjectionManager;
    private static final int REQUEST_CODE = 5588;
    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader.OnImageAvailableListener mImageListener;
    private MediaScannerConnection.OnScanCompletedListener mScanListener;
    private MediaProjection mProjection;
    private Context mContext;
    private final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 5566;
    /////////////////////////////////////

    //Switch
    private Switch Swt_Setting_SDcard, Swt_Setting_Shutter_Sound;
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
    private LinearLayout llt_face_seek, llt_face_col;

    public static int x = 0;

    //view
    private ImageView img_face_rosy, img_face_brasion, img_face_white, img_face_lifting, img_face_eyes;

    private SeekBar seekBarScale, seekBarFace;
    private int seekBarValue = 0;
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

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

    private ImageView img_timer_normal, img_timer_three, img_timer_five, img_timer_ten, img_timer;


//........................................


    private LinearLayout mFilterLayout, HeaderLayout, MotherMenu;


    private ArrayList<MenuBean> mStickerData;
    private RecyclerView mMenuView;
    private MenuAdapter mStickerAdapter;

    private RecyclerView mFilterListView;
    private FilterRecyclerViewAdapter mAdapter;
    private com.loetech.camerabeautify.camfilter.GPUCamImgOperator GPUCamImgOperator;
    private boolean isRecording = false;
    private final int MODE_PIC = 1;
    private final int MODE_VIDEO = 2;
    private int mode = MODE_PIC;

    private ImageView btn_shutter;
    private ImageView btn_more, Btn_Touch;

    private ObjectAnimator animator;
    private int PERMISSION_CALLBACK_CONSTANT = 1000;


    LinearLayout onTouchLayout;

    final static MediaPlayer mp = new MediaPlayer();
    final static MediaPlayer mpp = new MediaPlayer();


    private final com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType[] types = new GPUCamImgOperator.GPUImgFilterType[]{
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.NONE,
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.HEALTHY,
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.NOSTALGIA,
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.COOL,
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.EMERALD,
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.EVERGREEN,
            com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType.CRAYON
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_with_filter);


        mContext = this;
        askWritePermission();
        onCreate();

    }

    private void onCreate() {


        sharedPref = new SharedPref(this);

        GPUCamImgOperator = new GPUCamImgOperator();
        LuoGLCameraView luoGLCameraView = (LuoGLCameraView) findViewById(R.id.glsurfaceview_camera);
        GPUCamImgOperator.context = luoGLCameraView.getContext();
        GPUCamImgOperator.luoGLBaseView = luoGLCameraView;


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


    private void OffTouchMode() {


        relativeLayout = (RelativeLayout) findViewById(R.id.idrl);
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

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });


    }


    private void DevInfo() {

        Dev_Info = findViewById(R.id.idDev_info);
        Dev_Info_layout = findViewById(R.id.layout_dev_info);

        Dev_Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private void About_App_func() {
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

//Sound ON & OFF

    private void SwitchButton() {

        Swt_Setting_Shutter_Sound = findViewById(R.id.swt_setting_shutter_sound);
        if (sharedPref.loadSoundModeState() == true) {
            Swt_Setting_Shutter_Sound.setChecked(true);
        }
        Swt_Setting_Shutter_Sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("TMP", isChecked + " ");
                if (isChecked) {
                    sharedPref.setSoundModeState(true);
                } else {
                    sharedPref.setSoundModeState(false);
                }
            }
        });


    }

    private void more_button() {


        onTouchLayout.setVisibility(View.GONE);
        llt_timer_col.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);
        Dev_Info_layout.setVisibility(View.GONE);
        About_layout.setVisibility(View.GONE);


        if (llt_layout_more.getVisibility() == View.GONE) {
            llt_layout_more.setVisibility(View.VISIBLE);
        } else if (llt_layout_more.getVisibility() == View.VISIBLE) {
            llt_layout_more.setVisibility(View.GONE);
        }


    }

    //seekbar beauty

    private void beauty() {

        img_face_brasion = findViewById(R.id.img_main_smooth);
        img_face_white = findViewById(R.id.img_main_white);
        img_face_lifting = findViewById(R.id.img_main_slimming);
        img_face_eyes = findViewById(R.id.img_main_big_eye);

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
        img_face_lifting.setImageResource(R.drawable.ico_slimming);

        llt_face_seek.setVisibility(View.GONE);
        llt_face_col.setVisibility(View.GONE);
    }


    private void initFaceMenu(int value) {
        llt_face_col.setVisibility(View.VISIBLE);
        llt_face_seek.setVisibility(View.VISIBLE);
        seekBarFace.setProgress(value);
    }


    //Flash Start...........................................
    private void TimerInitView() {

        img_timer = findViewById(R.id.img_main_timer);
        img_timer_normal = findViewById(R.id.img_main_timer_normal);
        img_timer_three = findViewById(R.id.img_main_timer_three);
        img_timer_five = findViewById(R.id.img_main_timer_five);
        img_timer_ten = findViewById(R.id.img_main_timer_ten);

        llt_timer_col = findViewById(R.id.llt_main_timer_col);

        findViewById(R.id.img_main_timer).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_normal).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_three).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_five).setOnClickListener(this);
        findViewById(R.id.llt_main_timer_ten).setOnClickListener(this);
        findViewById(R.id.btn_sticker).setOnClickListener(this);
        findViewById(R.id.btn_touch).setOnClickListener(this);

        llt_timer_col.setVisibility(View.GONE);

    }

    private void Sticker() {
        mMenuView = (RecyclerView) findViewById(R.id.mMenuView);
        BtnSticker = findViewById(R.id.btn_sticker);
        mMenuView.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.img_main_timer:
                onTouchLayout.setVisibility(View.GONE);
                llt_layout_more.setVisibility(View.GONE);
                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);

                if (llt_timer_col.getVisibility() == View.GONE) {
                    llt_timer_col.setVisibility(View.VISIBLE);

                } else if (llt_timer_col.getVisibility() == View.VISIBLE) {
                    llt_timer_col.setVisibility(View.GONE);
                }
                break;

            case R.id.llt_main_timer_normal:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_normal);
                x = 0;

                break;

            case R.id.llt_main_timer_three:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_three);
                x = 3;
                break;

            case R.id.llt_main_timer_five:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_five);
                x = 5;
                break;

            case R.id.llt_main_timer_ten:
                llt_timer_col.setVisibility(View.GONE);
                img_timer.setImageResource(R.drawable.icon_timer_ten);
                x = 10;

                break;


            case R.id.btn_touch:
                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);
                if (onTouchLayout.getVisibility() == View.GONE) {

                    onTouchLayout.setVisibility(View.VISIBLE);
                    Btn_Touch.setImageResource(R.drawable.icon_touch_enble_sel);
                    Btn_Touch.setTag(R.drawable.icon_touch_enble_sel);
                    onTouchLayout.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {


                            onTouchLayout.setVisibility(View.GONE);
                            if (mode == MODE_PIC) {

//                                Log.d("TEMP", "Touch");
//                                startcount();

                                if (Build.VERSION.SDK_INT >= 28) {
                                    //Toast.makeText(CameraWithFilterActivity.this, "ScreenShot", Toast.LENGTH_SHORT).show();
                                    //screenShot();
                                    HeaderLayout.setVisibility(View.GONE);
                                    MotherMenu.setVisibility(View.GONE);
                                    startcountforscreenshot();
                                }else{
                                    // Toast.makeText(CameraWithFilterActivity.this, "photoCapture", Toast.LENGTH_SHORT).show();
                                    startcount();
                                }


                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        onTouchLayout.setVisibility(View.VISIBLE);
                                    }

                                }, 1000);
                            }

                            return false;

                        }
                    });


                } else if (onTouchLayout.getVisibility() == View.VISIBLE) {

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


                if (mMenuView.getVisibility() == View.GONE) {
                    mMenuView.setVisibility(View.VISIBLE);


                } else if (mMenuView.getVisibility() == View.VISIBLE) {

                    mMenuView.setVisibility(View.GONE);
                }
                break;

            case R.id.llt_main_smooth:
                faceeffect = FACEEFFECT.BRASION;
                initFaceMenu(smoothVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly_sel);
                img_face_white.setImageResource(R.drawable.ico_whitening);
                img_face_lifting.setImageResource(R.drawable.ico_slimming);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye);

                break;
            case R.id.llt_main_white:
                faceeffect = FACEEFFECT.WHITE;
                initFaceMenu(whiteVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly);
                img_face_white.setImageResource(R.drawable.ico_whitening_sel);
                img_face_lifting.setImageResource(R.drawable.ico_slimming);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye);

                break;
            case R.id.llt_main_slimming:
                faceeffect = FACEEFFECT.LIFTING;
                initFaceMenu(liftingVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly);
                img_face_white.setImageResource(R.drawable.ico_whitening);
                img_face_lifting.setImageResource(R.drawable.ico_slimming_sel);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye);

                break;
            case R.id.llt_main_big_eye:
                faceeffect = FACEEFFECT.BIGEYES;
                initFaceMenu(bigEyesVal);
                img_face_brasion.setImageResource(R.drawable.ico_smoothly);
                img_face_white.setImageResource(R.drawable.ico_whitening);
                img_face_lifting.setImageResource(R.drawable.ico_slimming);
                img_face_eyes.setImageResource(R.drawable.ico_bigeye_sel);
                break;

        }

    }


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



    private void showFaceBeauty() {

        About_layout.setVisibility(View.GONE);
        Dev_Info_layout.setVisibility(View.GONE);
        onTouchLayout.setVisibility(View.GONE);
        mFilterLayout.setVisibility(View.GONE);
        mMenuView.setVisibility(View.GONE);


        if (llt_face_col.getVisibility() == View.GONE) {

            llt_face_col.setVisibility(View.VISIBLE);
            llt_face_seek.setVisibility(View.GONE);

        } else if (llt_face_col.getVisibility() == View.VISIBLE || llt_face_seek.getVisibility() == View.VISIBLE) {
            llt_face_col.setVisibility(View.GONE);
            llt_face_seek.setVisibility(View.GONE);
        }
    }

    //.....................................


    private void initView() {

        mFilterLayout = (LinearLayout) findViewById(R.id.layout_filter);
        onTouchLayout = (LinearLayout) findViewById(R.id.idOnTouch);
        HeaderLayout = (LinearLayout) findViewById(R.id.header_layout);
        MotherMenu = (LinearLayout) findViewById(R.id.mOtherMenu);

        Timing_layout = (LinearLayout) findViewById(R.id.idtiminglayout);
        TimeShow = (TextView) findViewById(R.id.idshowtime);

        //...........................
        whiteVal = seekBarValue;
        XJGArSdkApi.XJGARSDKSetWhiteSkinParam(whiteVal);
        bigEyesVal = seekBarValue;
        XJGArSdkApi.XJGARSDKSetBigEyeParam(bigEyesVal);
        smoothVal = seekBarValue;
        XJGArSdkApi.XJGARSDKSetSkinSmoothParam(smoothVal);
        liftingVal = seekBarValue;
        XJGArSdkApi.XJGARSDKSetThinChinParam(liftingVal);
        //............................

        mFilterListView = (RecyclerView) findViewById(R.id.filter_listView);

        btn_shutter = (ImageView) findViewById(R.id.btn_camera_shutter);
        btn_more = (ImageView) findViewById(R.id.btn_more);
        llt_layout_more = findViewById(R.id.layout_more);
        Btn_Touch = (ImageView) findViewById(R.id.btn_touch);


        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_shutter).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_switch).setOnClickListener(btn_listener);
        findViewById(R.id.btn_more).setOnClickListener(btn_listener);
        findViewById(R.id.btn_camera_beauty).setOnClickListener(btn_listener);
        findViewById(R.id.btn_gallery).setOnClickListener(btn_listener);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);

        mAdapter = new FilterRecyclerViewAdapter(this, types);
        mFilterListView.setAdapter(mAdapter);
        mAdapter.setOnFilterChangeListener(onFilterChangeListener);

        animator = ObjectAnimator.ofFloat(btn_shutter, "rotation", 0, 360);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        LuoGLCameraView cameraView = (LuoGLCameraView) findViewById(R.id.glsurfaceview_camera);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.y;
        cameraView.setLayoutParams(params);


        mMenuView = (RecyclerView) findViewById(R.id.mMenuView);
        mStickerData = new ArrayList<>();
        mMenuView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mStickerAdapter = new MenuAdapter(this, mStickerData);
        mStickerAdapter.setOnClickListener(new ClickUtils.OnClickListener() {
            @Override
            public void onClick(View v, int type, int pos, int child) {
                MenuBean m = mStickerData.get(pos);
                String name = m.name;
                String path = m.path;
                if (name.equals("None")) {
                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(false);
                    mStickerAdapter.checkPos = pos;
                    v.setSelected(true);
                } else {
                    String stickerPaperdir = XJGArSdkApi.getPrivateResDataDir(getApplicationContext());
                    stickerPaperdir = stickerPaperdir + "/StickerPapers/" + path;
                    ZIP.unzipAStickPaperPackages(stickerPaperdir);

                    XJGArSdkApi.XJGARSDKSetShowStickerPapers(true);
                    XJGArSdkApi.XJGARSDKChangeStickpaper(path);
                    mStickerAdapter.checkPos = pos;
                    v.setSelected(true);
                }
                mStickerAdapter.notifyDataSetChanged();
            }
        });
        mMenuView.setAdapter(mStickerAdapter);
        initEffectMenu();
    }


    protected void initEffectMenu() {

        MenuBean bean = new MenuBean();
        bean.name = "None";
        bean.path = "";
        bean.image = R.drawable.orginal;

        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "Mathal";
        bean.path = "mathal"; //mymask
        bean.image = R.drawable.st_mathal;
        mStickerData.add(bean);


        bean = new MenuBean();
        bean.name = "";
        bean.path = "headflag";
        bean.image = R.drawable.st_headflag;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "cheekflag";
        bean.image = R.drawable.st_cheekflag;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "cutepecha";
        bean.image = R.drawable.st_cute_pecha;
        mStickerData.add(bean);


        bean = new MenuBean();
        bean.name = "";
        bean.path = "pagri";
        bean.image = R.drawable.st_pagri;
        mStickerData.add(bean);


        bean = new MenuBean();
        bean.name = "";
        bean.path = "redghomta";
        bean.image = R.drawable.st_ghomta_two;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "flower";
        bean.image = R.drawable.st_flower;
        mStickerData.add(bean);

        //todo
        bean = new MenuBean();
        bean.name = "";
        bean.path = "golaygamcha";
        bean.image = R.drawable.st_gamcha;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "crowngold";
        bean.image = R.drawable.st_crowngold;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "rabitear";
        bean.image = R.drawable.st_rabitear;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "hgamcha";
        bean.image = R.drawable.st_hgamcha;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "cutetiger";
        bean.image = R.drawable.st_cute_tiger;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "nurse";
        bean.image = R.drawable.st_nurse;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "pinkribon";
        bean.image = R.drawable.st_pinkribon;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "bridal";
        bean.image = R.drawable.st_bridal;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "kanerdul";
        bean.image = R.drawable.st_kanerdul;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "headribon";
        bean.image = R.drawable.st_headribon;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "girlcap";
        bean.image = R.drawable.st_girls_cap;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "eyeliner";
        bean.image = R.drawable.st_eyeliner;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "eyelass";
        bean.image = R.drawable.st_eyelass;
        mStickerData.add(bean);


        bean = new MenuBean();
        bean.name = "";
        bean.path = "kalacasma";
        bean.image = R.drawable.st_kalachasma;
        mStickerData.add(bean);

        //todo

//        bean = new MenuBean();
//        bean.name = "";
//        bean.path = "hairone";
//        bean.image = R.drawable.st_hairone;
//        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "longear";
        bean.image = R.drawable.st_longear;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "scrf";
        bean.image = R.drawable.st_scarf;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "hairtwo";
        bean.image = R.drawable.st_hairtwo;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "noth";
        bean.image = R.drawable.st_noth;
        mStickerData.add(bean);

        bean = new MenuBean();
        bean.name = "";
        bean.path = "btiger";
        bean.image = R.drawable.st_bangla_tiger;
        mStickerData.add(bean);

        mStickerAdapter.notifyDataSetChanged();
    }

    private FilterRecyclerViewAdapter.onFilterChangeListener onFilterChangeListener = new FilterRecyclerViewAdapter.onFilterChangeListener() {

        @Override
        public void onFilterChanged(com.loetech.camerabeautify.camfilter.GPUCamImgOperator.GPUImgFilterType filterType) {
            String filterName = FilterTypeHelper.FilterType2FilterName(filterType);
            XJGArSdkApi.XJGARSDKChangeFilter(filterName);
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                           int[] grantResults) {
//        if (grantResults.length != 1 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            if (mode == MODE_PIC)
//                takePhoto();
//            else
//                takeVideo();
//        } else {
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        }
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    static boolean bShowImgFilters = false;
    private View.OnClickListener btn_listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int buttonId = v.getId();
            if (buttonId == R.id.btn_more) {
                more_button();
            }
            if (buttonId == R.id.btn_camera_shutter) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_CALLBACK_CONSTANT);
                } else {
                    if (mode == MODE_PIC) {

//                        btn_shutter.setVisibility(View.GONE);
//                        Log.d("TEMP", "Button");
//                        startcount();



                        if (Build.VERSION.SDK_INT >= 28) {
                            //Toast.makeText(CameraWithFilterActivity.this, "ScreenShot", Toast.LENGTH_SHORT).show();
                            //screenShot();
                            HeaderLayout.setVisibility(View.GONE);
                            MotherMenu.setVisibility(View.GONE);
                            startcountforscreenshot();
                        }else{
                           // Toast.makeText(CameraWithFilterActivity.this, "photoCapture", Toast.LENGTH_SHORT).show();
                            startcount();
                        }
                        


                    }
                }
            } else if (buttonId == R.id.btn_camera_filter) {
                bShowImgFilters = !bShowImgFilters;
                if (bShowImgFilters)
                    showFilters();
                else
                    hideFilters();
            } else if (buttonId == R.id.btn_camera_switch) {
                Dev_Info_layout.setVisibility(View.GONE);
                About_layout.setVisibility(View.GONE);
                GPUCamImgOperator.switchCamera();
            } else if (buttonId == R.id.btn_camera_beauty) {

                showFaceBeauty();

            } else if (buttonId == R.id.btn_gallery) {

                Dev_Info_layout.setVisibility(View.GONE);

                if (ContextCompat.checkSelfPermission(CameraWithFilterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent pickPhoto = new Intent(Intent.ACTION_VIEW, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivity(pickPhoto);
                } else {
                    requestStoragePermission();
                }

            }
        }
    };


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ActivityCompat.shouldShowRequestPermissionRationale(CameraWithFilterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }


    private void takePhoto() {
        Dev_Info_layout.setVisibility(View.GONE);
        About_layout.setVisibility(View.GONE);
        GPUCamImgOperator.savePicture();
    }

    private void takeVideo() {
        if (isRecording) {
            animator.end();
            GPUCamImgOperator.stopRecord();
        } else {
            animator.start();
            GPUCamImgOperator.startRecord();
        }
        isRecording = !isRecording;
    }


    //todo
    private void showFilters() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

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


    private void hideFilters() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0, mFilterLayout.getHeight());
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
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mCamera != null) {

            mCamera.release();
            mCamera = null;
        }

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        onCreate();

    }


    //TODO
    /////////////////////////////////////////////capture///////////////////////////////////////////

    public void startcount() {


        new CountDownTimer(x * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                onTouchLayout.setVisibility(View.GONE);
                btn_shutter.setVisibility(View.GONE);
                Timing_layout.setVisibility(View.VISIBLE);
                TimeShow.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                Toast.makeText(CameraWithFilterActivity.this, "Captured", Toast.LENGTH_SHORT).show();
                takePhoto();
                Timing_layout.setVisibility(View.GONE);
                MediaPlayer mp = MediaPlayer.create(CameraWithFilterActivity.this, R.raw.capturesound);
                if (sharedPref.loadSoundModeState() == true) {
                    mp.start();
                }

                Handler handlerr = new Handler();
                handlerr.postDelayed(new Runnable() {

                    @Override
                    public void run() {


                        MotherMenu.setVisibility(View.VISIBLE);
                    }

                }, 1000);

                try {

                    if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.VISIBLE);

                    } else if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.GONE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }.start();

    }


    //TODO



    //////////////////////////////////////call screenshot///////////////////////////


    public void startcountforscreenshot() {


        new CountDownTimer(x * 1000, 1000) {

            public void onTick(long millisUntilFinished) {

                HeaderLayout.setVisibility(View.GONE);
                MotherMenu.setVisibility(View.GONE);

                onTouchLayout.setVisibility(View.GONE);
                btn_shutter.setVisibility(View.GONE);
                Timing_layout.setVisibility(View.VISIBLE);
                TimeShow.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

               // Toast.makeText(CameraWithFilterActivity.this, "Captured", Toast.LENGTH_SHORT).show();
                Timing_layout.setVisibility(View.GONE);
                screenShot();
                //takePhoto();

                MediaPlayer mp = MediaPlayer.create(CameraWithFilterActivity.this, R.raw.capturesound);
                if (sharedPref.loadSoundModeState() == true) {
                    mp.start();
                }

                Handler handlerr = new Handler();
                handlerr.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        HeaderLayout.setVisibility(View.VISIBLE);
                        MotherMenu.setVisibility(View.VISIBLE);
                        btn_shutter.setVisibility(View.VISIBLE);

                        Toast.makeText(mContext, "Captured", Toast.LENGTH_SHORT).show();
                    }

                }, 1000);

                try {

                    if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.VISIBLE);

                    } else if (((Integer) Btn_Touch.getTag()) == R.drawable.icon_touch_enble_sel) {
                        onTouchLayout.setVisibility(View.GONE);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }.start();

    }


    ///////////////////////////////////ScreenShot///////////////////////////////////

    private void screenShot(){
        if(mProjectionManager != null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @androidx.annotation.NonNull String permissions[],
                                           @androidx.annotation.NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initState();
                }
                break;
            }
        }
    }

    private void askWritePermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
        else{
            initState();
        }
    }

    private void initState(){
        mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //mButton.setClickable(true);
    }

    private Bitmap createBitmap(Image image){
        Log.d("kanna", "check create bitmap: " + Thread.currentThread().toString());
        Bitmap bitmap;
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * image.getWidth();
        // create bitmap
        bitmap = Bitmap.createBitmap(image.getWidth() + rowPadding / pixelStride,
                image.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        image.close();
        return bitmap;
    }


    private Flowable<Image> getScreenShot(){
        final Point screenSize = new Point();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        Display display = getWindowManager().getDefaultDisplay();
        display.getRealSize(screenSize);
        return Flowable.create(new FlowableOnSubscribe<Image>() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter<Image> emitter) throws Exception {
                mImageReader = ImageReader.newInstance(screenSize.x-20, screenSize.y-20, PixelFormat.RGBA_8888, 2);
                mVirtualDisplay = mProjection.createVirtualDisplay("cap", screenSize.x-20, screenSize.y-20, metrics.densityDpi,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
                mImageListener = new ImageReader.OnImageAvailableListener() {
                    Image image = null;
                    @Override
                    public void onImageAvailable(ImageReader imageReader) {
                        try {
                            image = imageReader.acquireLatestImage();
                            Log.d("kanna", "check reader: " + Thread.currentThread().toString());
                            if (image != null) {
                                emitter.onNext(image);
                                emitter.onComplete();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            emitter.onError(new Throwable("ImageReader error"));
                        }
                        mImageReader.setOnImageAvailableListener(null, null);
                    }

                };
                mImageReader.setOnImageAvailableListener(mImageListener, null);

            }
        }, BackpressureStrategy.DROP);
    }
    private Flowable<String> createFile(){
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> emitter) throws Exception {
                Log.d("kanna", "check create filename: " + Thread.currentThread().toString());
                String directory, fileHead, fileName;
                int count = 0;
                File externalFilesDir = getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
                if (externalFilesDir != null) {
                    directory = getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/screenshots/";

                    Log.d("kanna", directory);
                    File storeDirectory = new File(directory);
                    if (!storeDirectory.exists()) {
                        boolean success = storeDirectory.mkdirs();
                        if (!success) {
                            emitter.onError(new Throwable("failed to create file storage directory."));
                            return;
                        }
                    }
                } else {
                    emitter.onError(new Throwable("failed to create file storage directory," +
                            " getExternalFilesDir is null."));
                    return;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Calendar c = Calendar.getInstance();
                fileHead = simpleDateFormat.format(c.getTime()) + "_";
                fileName = directory + fileHead + count + ".jpeg";
                File storeFile = new File(fileName);
                while (storeFile.exists()) {
                    count++;
                    fileName = directory + fileHead + count + ".jpeg";
                    storeFile = new File(fileName);
                }
                emitter.onNext(fileName);
                emitter.onComplete();
            }
        },BackpressureStrategy.DROP).subscribeOn(Schedulers.io());
    }
    private void writeFile(Bitmap bitmap, String fileName) throws IOException {
        Log.d("kanna", "check write file: " + Thread.currentThread().toString());
        FileOutputStream fos = new FileOutputStream(fileName);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close();
        bitmap.recycle();
    }
    private Flowable<String> updateScan(final String fileName){
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter<String> emitter) throws Exception {
                String[] path = new String[]{fileName};
                mScanListener = new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        Log.d("kanna", "check scan file: " + Thread.currentThread().toString());
                        if (uri == null) {
                            emitter.onError(new Throwable("Scan fail" + s));
                        }
                        else {
                            emitter.onNext(s);
                            emitter.onComplete();
                        }
                    }
                };
                MediaScannerConnection.scanFile(mContext, path, null, mScanListener);
            }
        },BackpressureStrategy.DROP);
    }
    private void finalRelease(){
        if (mVirtualDisplay != null){
           // mVirtualDisplay.release();
        }
        if (mImageReader != null){
            mImageReader = null;
        }
        if(mImageListener != null){
            mImageListener = null;
        }
        if(mScanListener != null){
            mScanListener = null;
        }
        if(mProjection != null){
            mProjection.stop();
            mProjection = null;
        }
    }

    /*
    RXJava
     */
    private void shotScreen(){
        getScreenShot()
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .map(new Function<Image, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Image image) throws Exception {
                        return createBitmap(image);
                    }
                })
                .zipWith(createFile(), new BiFunction<Bitmap, String, String>() {
                    @Override
                    public String apply(@NonNull Bitmap bitmap, @NonNull String fileName) throws Exception {
                        writeFile(bitmap, fileName);
                        return fileName;
                    }
                })
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String fileName) throws Exception {
                        return updateScan(fileName);
                    }
                })
                .observeOn(Schedulers.io())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("kanna", "check do finally: " + Thread.currentThread().toString());
                        finalRelease();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(String filename) {
                        Log.d("kanna", "onNext: " + filename);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w("kanna", "onError: ", t);

                    }

                    @Override
                    public void onComplete() {
                        Log.d("kanna", "onComplete");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            mProjection = mProjectionManager.getMediaProjection(resultCode, data);
            if(mProjection != null) {
                shotScreen();
            }
        }
    }




}