package com.example.camerabeautify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

public class Dev_Infos extends AppCompatActivity {


    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev__info);

        ToolBar();

    }


    private void ToolBar() {

        mToolbar = findViewById( R.id.dev_toolbar );
        TextView mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);

        //toolbar name ==>
        mTitle.setText("Developer Info");
        setSupportActionBar( mToolbar );

        getSupportActionBar().setDisplayShowTitleEnabled( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

    }


}
