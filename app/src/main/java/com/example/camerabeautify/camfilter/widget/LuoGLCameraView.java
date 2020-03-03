package com.example.camerabeautify.camfilter.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES20;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;


import com.example.camerabeautify.camfilter.CameraEngine;
import com.example.camerabeautify.camfilter.LuoGPUCameraInputFilter;
import com.example.camerabeautify.camfilter.LuoGPUImgBaseFilter;
import com.example.camerabeautify.camfilter.SavePictureTask;
import com.example.camerabeautify.camfilter.utils.OpenGlUtils;
import com.xiaojigou.luo.xjgarsdk.XJGArSdkApi;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class LuoGLCameraView extends LuoGLBaseView {

    private LuoGPUCameraInputFilter cameraInputFilter;

    private SurfaceTexture surfaceTexture;

    public LuoGLCameraView(Context context) {
        this(context, null);
    }



    public LuoGLCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getHolder().addCallback(this);
        scaleType = ScaleType.CENTER_CROP;
        XJGArSdkApi.XJGARSDKReleaseAllOpenglResources();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        super.onSurfaceCreated(gl, config);
        if(cameraInputFilter == null)
            cameraInputFilter = new LuoGPUCameraInputFilter();
        cameraInputFilter.init();

        if(filter == null)
            filter = new LuoGPUImgBaseFilter();
        filter.init();

        if (textureId == OpenGlUtils.NO_TEXTURE) {
            textureId = OpenGlUtils.getExternalOESTextureID();
            if (textureId != OpenGlUtils.NO_TEXTURE) {
                surfaceTexture = new SurfaceTexture(textureId);
                surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
            }
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        super.onSurfaceChanged(gl, width, height);
        openCamera();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        super.onDrawFrame(gl);
        if(surfaceTexture == null)
            return;
        surfaceTexture.updateTexImage();
        float[] mtx = new float[16];
        surfaceTexture.getTransformMatrix(mtx);
        cameraInputFilter.setTextureTransformMatrix(mtx);
        int resultTexture=0;
        resultTexture = cameraInputFilter.onDrawToTexture(textureId,gLCubeBuffer,gLTextureBuffer);
        GLES20.glViewport(0,0,surfaceWidth, surfaceHeight);
        int finalTexture = XJGArSdkApi.XJGARSDKRenderGLTexToGLTex(resultTexture,imageWidth,imageHeight);
        GLES20.glViewport(0,0,surfaceWidth, surfaceHeight);

        filter.onDrawFrame(finalTexture,filter.mGLCubeBuffer,filter.mGLTextureBuffer);



        long timer = System.currentTimeMillis();
        timeCounter.add(timer);
        while (start < timeCounter.size() && timeCounter.get(start) < timer - 1000) {
            start++;
        }
        fps = timeCounter.size() - start;
        if (start > 100) {
            timeCounter = timeCounter.subList(start,
                    timeCounter.size() - 1);
            start = 0;
        }
        Log.i("cameraview","fsp:========="+ String.valueOf(fps));

        int targetFPS = 30;
        if(fps>targetFPS)
        {
            float targetFrameTime = 1000.f/(float)targetFPS;
            float currentFrameTime = 1000.f/(float)fps;
            float timeToSleep =  targetFrameTime - currentFrameTime;
            if(timeToSleep>1.0)
            {
                try {
                    Thread.sleep((long)timeToSleep);
                }
                catch (InterruptedException e) {
                }
            }
        }

    }

    List<Long> timeCounter = new ArrayList<Long>();
    int start = 0;
    int fps =0;

    private SurfaceTexture.OnFrameAvailableListener onFrameAvailableListener
            = new SurfaceTexture.OnFrameAvailableListener() {
        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            requestRender();
        }
    };


    private void openCamera(){
        if(CameraEngine.getCamera() == null)
            CameraEngine.openCamera();
        CameraEngine.CameraEngineInfo info = CameraEngine.getCameraInfo();
        if(info.orientation == 90 || info.orientation == 270){
            imageWidth = info.previewHeight;
            imageHeight = info.previewWidth;
        }else{
            imageWidth = info.previewWidth;
            imageHeight = info.previewHeight;
        }
        cameraInputFilter.onInputSizeChanged(imageWidth, imageHeight);
        cameraInputFilter.initCameraFrameBuffer(imageWidth, imageHeight);
        filter.onInputSizeChanged(imageWidth,imageHeight);


        int orientation = info.orientation;
        orientation = (orientation + 180) %360;
        adjustSize(orientation, info.isFront, true);

        if(surfaceTexture != null)
            CameraEngine.startPreview(surfaceTexture);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        CameraEngine.releaseCamera();
        cameraInputFilter.destroyFramebuffers();
        XJGArSdkApi.XJGARSDKReleaseAllOpenglResources();
    }


    @Override
    public void savePicture(final SavePictureTask savePictureTask) {
        CameraEngine.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                CameraEngine.stopPreview();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap photo = drawPhoto(bitmap,CameraEngine.getCameraInfo().isFront);
                        GLES20.glViewport(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        if (photo != null) {
                            savePictureTask.execute(photo);

                        }
                    }
                });
                CameraEngine.startPreview();
            }
        });
    }

    private Bitmap drawPhoto(Bitmap bitmap, boolean isRotated){

        Bitmap result;
        if(isRotated)
            result = XJGArSdkApi.XJGARSDKRenderImage(bitmap,true);
        else
            result = XJGArSdkApi.XJGARSDKRenderImage(bitmap,false);

        return result;
    }


}