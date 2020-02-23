package com.example.camerabeautify.camfilter;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mySharedPref;
    public SharedPref(Context context){
        mySharedPref = context.getSharedPreferences("filename",Context.MODE_PRIVATE);
    }
    public void setSoundModeState(Boolean state){
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("SoundMode",state);
        editor.commit();
    }
    public Boolean loadSoundModeState(){
        Boolean state = mySharedPref.getBoolean("SoundMode",false);
        return state;
    }
}
