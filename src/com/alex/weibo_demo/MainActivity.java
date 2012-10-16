package com.alex.weibo_demo;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements RequestListener{
	JSONObject jsObject=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusesAPI statusApi=new StatusesAPI(LoginActivity.accessToken);
        statusApi.friendsTimeline(0, 0, 50 , 1, false,  FEATURE.ALL,false,this);
    }

	@Override
	public void onComplete(String response) {
		// TODO Auto-generated method stub
		Log.v("alex",""+response);
        try {
            jsObject= new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray array;
        StringBuilder sb=new StringBuilder();
        
        try {
            array = jsObject.getJSONArray("statuses");
            if(array.length()>0){
                JSONObject child=array.getJSONObject(0);
                String id=child.getString("id");
                sb.append("text:"+child.getString("text")).append("\r\n");
                sb.append("created_at:"+child.getString("created_at")).append("\r\n");
                sb.append("source:"+child.getString("source")).append("\r\n");
                sb.append("comments_count:"+child.getString("comments_count")).append("\r\n");
                sb.append("reposts_count:"+child.getString("reposts_count")).append("\r\n");
                JSONObject user=child.getJSONObject("user");
                sb.append("screen_name:"+user.getString("screen_name")).append("\r\n");
                sb.append("name:"+user.getString("name")).append("\r\n");
                sb.append("city:"+user.getString("city")).append("\r\n");
                sb.append("location:"+user.getString("location")).append("\r\n");
                sb.append("description:"+user.getString("description")).append("\r\n");
                sb.append("gender:"+user.getString("gender")).append("\r\n");
            }
           
        } catch (JSONException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(WeiboException e) {
		// TODO Auto-generated method stub
		
	}
}
