package com.loetech.camerabeautify.Activity;

import android.view.View;


public class ClickUtils {

    public static final int TAG_TYPE=0x0F000001;
    public static final int TAG_POS=0x0F000002;
    public static final int TAG_POS_CHILD=0x0F000003;
    public static final int TAG_DATA=0x0F000010;


    public static void setType(View v, int type){
        v.setTag(TAG_TYPE,type);
    }


    public static void setPos(View v, int pos){
        v.setTag(TAG_POS,pos);
    }



    public static int getType(View v){
        if(v!=null&&v.getTag(TAG_TYPE)!=null){
            return (int) v.getTag(TAG_TYPE);
        }
        return -1;
    }


    public static int getPos(View v){
        if(v!=null&&v.getTag(TAG_POS)!=null){
            return (int) v.getTag(TAG_POS);
        }
        return -1;
    }


    public static int getPosChild(View v){
        if(v!=null&&v.getTag(TAG_POS_CHILD)!=null){
            return (int) v.getTag(TAG_POS_CHILD);
        }
        return -1;
    }


    public static void addClickTo(View v, View.OnClickListener listener, int type){
        if(listener!=null&&v!=null){
            setType(v,type);
            v.setOnClickListener(listener);
        }
    }




    public abstract static class OnClickListener implements View.OnClickListener{

        public OnClickListener(){

        }

        @Override
        public void onClick(View v) {
            onClick(v,ClickUtils.getType(v),ClickUtils.getPos(v),ClickUtils.getPosChild(v));
        }


        public abstract void onClick(View v, int type, int pos, int child);

    }

}
