package com.example.camerabeautify.camfilter;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;


public class CameraEngine {
    private static Camera camera = null;
    private static int cameraID = 1;
    private static SurfaceTexture surfaceTexture;
    private static SurfaceView surfaceView;

    public static Camera getCamera(){
        return camera;
    }

    public static boolean openCamera(){
        if(camera == null){
            try{
                camera = Camera.open(cameraID);
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static boolean openCamera(int id){
        if(camera == null){
            try{
                camera = Camera.open(id);
                cameraID = id;
                setDefaultParameters();
                return true;
            }catch(RuntimeException e){
                return false;
            }
        }
        return false;
    }

    public static void releaseCamera(){
        if(camera != null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    public void resumeCamera(){
        openCamera();
    }

    public void setParameters(Parameters parameters){
        camera.setParameters(parameters);
    }

    public Parameters getParameters(){
        if(camera != null)
            camera.getParameters();
        return null;
    }

    public static void switchCamera(){
        releaseCamera();
        cameraID = cameraID == 0 ? 1 : 0;
        openCamera(cameraID);
        startPreview(surfaceTexture);
    }

    private static void setDefaultParameters(){
        Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
        {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
//        Size previewSize = getLargePreviewSize(camera);
        Size previewSize = getNearestPreviewSize(camera, 1280, 720);
        parameters.setPreviewSize(previewSize.width, previewSize.height);
//        Size pictureSize = getLargePictureSize(camera);
        Size pictureSize = getNearestPictureSize(camera, 1280, 720);
        parameters.setPictureSize(pictureSize.width, pictureSize.height);
        parameters.setRotation(90);
        camera.setParameters(parameters);
    }

    public static Size getPreviewSize(){
        return camera.getParameters().getPreviewSize();
    }

    private static Size getPictureSize(){
        return camera.getParameters().getPictureSize();
    }

    public static void startPreview(SurfaceTexture surfaceTexture){
        if(camera != null)
            try {
                camera.setPreviewTexture(surfaceTexture);
                CameraEngine.surfaceTexture = surfaceTexture;
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public static void startPreview(){
        if(camera != null)
            camera.startPreview();
    }

    public static void stopPreview(){
        camera.stopPreview();
    }

    //by luo
    private static int mCameraID = 0;
    public static int getOrientation(){
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);
        return cameraInfo.orientation;
    }
    //by luo
    public static boolean isFlipHorizontal(){
        return true;
        //return LuoCameraEngine.getCameraInfo().facing == CameraInfo.CAMERA_FACING_FRONT ? true : false;
    }


    public static void setRotation(int rotation){
        Parameters params = camera.getParameters();
        params.setRotation(rotation);
        camera.setParameters(params);
    }

    //Todo
    public static void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawCallback,
                                   Camera.PictureCallback jpegCallback){
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static class CameraEngineInfo {

        public int previewWidth;

        public int previewHeight;

        public int orientation;

        public boolean isFront;

        public int pictureWidth;

        public int pictureHeight;
    }
    public static CameraEngineInfo getCameraInfo(){
        CameraEngineInfo info = new CameraEngineInfo();
        Size size = getPreviewSize();
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(cameraID, cameraInfo);
        info.previewWidth = size.width;
        info.previewHeight = size.height;
        info.orientation = cameraInfo.orientation;
        info.isFront = cameraID == 1 ? true : false;
        size = getPictureSize();
        info.pictureWidth = size.width;
        info.pictureHeight = size.height;
        return info;
    }


    public static Size getNearestPictureSize(Camera camera, int targetWidth, int targetHeight){
        if(camera != null){

            float minDistance = 1000000000;
            int minIndex =-1;
            List<Size> sizes = camera.getParameters().getSupportedPictureSizes();
            Size temp = sizes.get(0);
            for(int i = 0;i < sizes.size();i ++){

                float curWidth = sizes.get(i).width;
                float curHeight = sizes.get(i).height;
                float distance = (curWidth - targetWidth)*(curWidth - targetWidth)
                        + (curHeight - targetHeight)*(curHeight - targetHeight);

                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
//                if(scale < 0.6f && scale > 0.5f)
//                {
//
//                }

                if(distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }

            temp = sizes.get(minIndex);
            return temp;
        }
        return null;
    }


    public static Size getNearestPreviewSize(Camera camera, int targetWidth, int targetHeight){
        if(camera != null){


            float minDistance = 1000000000;
            int minIndex =-1;
            List<Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Size temp = sizes.get(0);
            for(int i = 0;i < sizes.size();i ++){

                float curWidth = sizes.get(i).width;
                float curHeight = sizes.get(i).height;
                float distance = (curWidth - targetWidth)*(curWidth - targetWidth)
                        + (curHeight - targetHeight)*(curHeight - targetHeight);

//                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
                if(distance < minDistance) {
                    minDistance = distance;
                    minIndex = i;
                }
            }

            temp = sizes.get(minIndex);
            return temp;
        }
        return null;
    }


    public static Size getLargePictureSize(Camera camera){
        if(camera != null){
            List<Size> sizes = camera.getParameters().getSupportedPictureSizes();
            Size temp = sizes.get(0);
            for(int i = 1;i < sizes.size();i ++){
                float scale = (float)(sizes.get(i).height) / sizes.get(i).width;
                if(temp.width < sizes.get(i).width && scale < 0.6f && scale > 0.5f)
                    temp = sizes.get(i);
            }
            return temp;
        }
        return null;
    }


    public static Size getLargePreviewSize(Camera camera){
        if(camera != null){

            List<Size> sizes = camera.getParameters().getSupportedPreviewSizes();
            Size temp = sizes.get(0);
            for(int i = 1;i < sizes.size();i ++){
                if(temp.width < sizes.get(i).width)
                    temp = sizes.get(i);
            }
            return temp;
        }
        return null;
    }
}