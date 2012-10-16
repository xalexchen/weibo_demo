package com.alex.weibo_demo;

import java.text.SimpleDateFormat;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.Utility;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import android.support.v4.app.NavUtils;

public class LoginActivity extends Activity{
	private static String TAG="Weibo";
    private Weibo mWeibo;
	private static final String CONSUMER_KEY = "1036538931";// 替换为开发者的appkey.此处为果冻android;
	private static final String REDIRECT_URL = "http://www.sina.com";
	public static Oauth2AccessToken accessToken ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
        ImageView mloginButton = (ImageView) findViewById(R.id.login_button);
        mloginButton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mWeibo.authorize(LoginActivity.this, new AuthDialogListener());				
			}
        	
        });
		LoginActivity.accessToken=AccessTokenKeeper.readAccessToken(this);
        if(LoginActivity.accessToken.isSessionValid()){
        	Weibo.isWifi=Utility.isWifi(this);
            try {
                Class sso=Class.forName("com.weibo.sdk.android.api.WeiboAPI");//如果支持weiboapi的话，显示api功能演示入口按钮
                setContentView(R.layout.activity_main);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
               
            }
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new java.util.Date(LoginActivity.accessToken.getExpiresTime()));
            Log.v(TAG,"access_token 仍在有效期内,无需再次登录: \naccess_token:"+ LoginActivity.accessToken.getToken() + "\n有效期："+date);
        }
        Log.v(TAG,"oncreate ");
    }
	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			LoginActivity.accessToken = new Oauth2AccessToken(token, expires_in);
			if (LoginActivity.accessToken.isSessionValid()) {
				String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(LoginActivity.accessToken.getExpiresTime()));
				Log.v(TAG,"认证成功: \r\n access_token: "+ token + "\r\n" + "expires_in: " + expires_in+"\r\n有效期："+date);
				try {
	                Class sso=Class.forName("com.weibo.sdk.android.api.WeiboAPI");//如果支持weiboapi的话，显示api功能演示入口按钮
	                setContentView(R.layout.activity_main);
	            } catch (ClassNotFoundException e) {
	                e.printStackTrace();
	                Log.i(TAG, "com.weibo.sdk.android.api.WeiboAPI not found");
	               
	            }
				AccessTokenKeeper.keepAccessToken(LoginActivity.this, accessToken);
				Toast.makeText(LoginActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
		}

	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
