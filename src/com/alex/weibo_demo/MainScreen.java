package com.alex.weibo_demo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alex.weibo.utils.new_adapter;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;


import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;


import android.view.View;

public class MainScreen extends ListActivity implements RequestListener{
	private static final String TAG = "Weibo-MainScreen";
	private JSONObject jsObject=null;
	private SimpleAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private TopMenuOnTouchListener mTopMenuOnClickListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()                 
		.detectDiskReads()                 
		.detectDiskWrites()                 
		.detectNetwork()   // or .detectAll() for all detectable problems                 
		.penaltyLog()                 
		.build());         
	     StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()                 
		.detectLeakedSqlLiteObjects()                 
		.detectLeakedSqlLiteObjects()                 
		.penaltyLog()                 
		.penaltyDeath()                
		.build());   
        setContentView(R.layout.activity_mainscreen);
        RegisterTopMenuListener();

        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });
        ((PullToRefreshListView) getListView()).setDividerHeight(0);
        listItem = new ArrayList<HashMap<String, Object>>();
		mAdapter = new new_adapter(this,listItem,R.layout.layout_newlistview,
				new String[]{
				"profile_image","screen_name",
				"forward_img","reposts_count","comment_img","comments_count",
				"text",
				"created_at",
				"repost_user",
				"repost_content",
				
				"listview_shadow_left",
				"listview_shadow_right"
				},
				new int[]{
				R.id.listview_user_bg_profile,R.id.listview_user,
				R.id.listview_forward_img,R.id.listview_forward_text,R.id.listview_comment_img,R.id.listview_comment_text,
				R.id.listview_content,
				R.id.listview_info,
				R.id.listview_repost_user,
				R.id.listview_repost_content,
				
				R.id.listview_shadow_left,
				R.id.listview_shadow_right
				});
        setListAdapter(mAdapter);
        
      StatusesAPI statusApi=new StatusesAPI(LoginActivity.accessToken);
      statusApi.friendsTimeline(0, 0, 50 , 1, false,  FEATURE.ALL,false,this);
    }
    private void RegisterTopMenuListener()
    {
        mTopMenuOnClickListener = new TopMenuOnTouchListener();
        ImageView iv = (ImageView) findViewById(R.id.home_group_img);
        ImageView iv2 = (ImageView) findViewById(R.id.home_all_img);
    
        iv.setOnTouchListener(mTopMenuOnClickListener);
        iv2.setOnTouchListener(mTopMenuOnClickListener);
    }
    private class GetDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return "true";
        }

        @Override
        protected void onPostExecute(String result) {
            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }

	private class TopMenuOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
				ImageView iv = (ImageView) findViewById(R.id.home_group_img);
				ImageView iv2 = (ImageView) findViewById(R.id.home_all_img);
				iv.setImageResource(R.drawable.ic_home_friend_group_nor);
				iv2.setImageResource(R.drawable.ic_home_all_nor);
				
				if (v.getId() == R.id.home_all_img) {
					iv2.setImageResource(R.drawable.ic_home_all_chk);
					listItem.clear();
					mAdapter.notifyDataSetChanged();
				} else if (v.getId() == R.id.home_group_img) {
					iv.setImageResource(R.drawable.ic_home_friend_group_chk);
					mAdapter.notifyDataSetChanged();
				}
			}
			return false;
		}
	}

	@Override
	public void onComplete(String response) {
	// TODO Auto-generated method stub
	Log.v(TAG,"response = "+response);
    try {
        jsObject= new JSONObject(response);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    JSONArray array = null;

    
    try {
		array = jsObject.getJSONArray("statuses");
	} catch (JSONException e3) {
		// TODO Auto-generated catch block
		e3.printStackTrace();
	}
	if(array.length()>0){
		
		for (int i=0;i<5;i++)
		{
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject child = null;
			try {
//				child = array.getJSONObject(i);
				String statusesStr = array.getString(i);
				child = new JSONObject(statusesStr);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (child != null)
			{
//            JSONObject child=array.getJSONObject(0);
				try {
					Log.i(TAG,"text: "+child.getString("text"));
					Log.i(TAG,"created_at: "+child.getString("created_at"));
					Log.i(TAG,"source: "+child.getString("source"));
					Log.i(TAG,"comments_count: "+child.getString("comments_count"));
					Log.i(TAG,"reposts_count: "+child.getString("reposts_count"));
					//put into listview

					map.put("text", child.getString("text"));
					map.put("reposts_count", child.getString("reposts_count"));
					map.put("comments_count", child.getString("comments_count"));
					map.put("forward_img",R.drawable.ic_status_forward_nor);
					map.put("comment_img", R.drawable.ic_status_comment_nor);
					
					
					map.put("listview_shadow_left",R.drawable.bg_shadow_left);
					map.put("listview_shadow_right",R.drawable.bg_shadow_right);
					map.put("listview_bg","#FFFFFF");
					
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				String user1 = null;
				try {
					user1 = child.getString("user");
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if (user1 != null)
				{
					JSONObject user;
					try {
						user = new JSONObject(user1);
						Log.i(TAG,"profile_image: "+user.getString("profile_image_url"));
						Log.i(TAG,"screen_name: "+user.getString("screen_name"));
						map.put("screen_name", user.getString("screen_name"));
						map.put("profile_image", user.getString("profile_image_url"));
//						map.put("profile_image", R.drawable.bg_profile_intro);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				String retweeted_status;
				try {
					retweeted_status = child.getString("retweeted_status");
					if (retweeted_status != null)
					{
						JSONObject retweeted_status_obj = new JSONObject(retweeted_status);
						Log.i(TAG,"created_at: "+retweeted_status_obj.getString("created_at"));
						Log.i(TAG,"text: "+retweeted_status_obj.getString("text"));   
						Log.i(TAG,"source: "+retweeted_status_obj.getString("source"));
						SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy",Locale.US);
						SimpleDateFormat outputDate = new SimpleDateFormat("h:mm a");
						String date= null;
						try{
							date = outputDate.format(sdf.parse(retweeted_status_obj.getString("created_at")));
							Log.v(TAG,"new time = " + date);
						}
						catch(Exception e){
							System.out.println("Exception: " + e.getMessage());
						}
						map.put("created_at",date+"  来自: "+Html.fromHtml(child.getString("source")));
						map.put("repost_content", retweeted_status_obj.getString("text"));
	        	
						String user2 = retweeted_status_obj.getString("user");
						if (user2 != null)
						{
							JSONObject user3 = new JSONObject(user2);
							Log.i(TAG,"user3: "+user3.getString("name"));
							map.put("repost_user", user3.getString("name"));
						}
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				listItem.add(0,map);
				
				Log.i(TAG,"num size = : "+listItem.size());
				Message msg1 = new Message();
				msg1.what = 0;
				mHandler.sendMessage(msg1);
			} 
		}

	}
}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onError(WeiboException e) {
		// TODO Auto-generated method stub
		
	}
}

