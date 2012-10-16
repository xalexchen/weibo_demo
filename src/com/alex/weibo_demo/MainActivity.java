package com.alex.weibo_demo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import android.support.v4.app.NavUtils;

public class MainActivity extends Activity{
	private static String TAG="Weibo";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView mloginButton = (ImageView) findViewById(R.id.login_button);
        mloginButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContentView(R.layout.activity_main);
			}
        	
        });
        Log.v(TAG,"oncreate ");
    }
}
