package com.alex.weibo_demo;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alex.weibo.utils.new_adapter;
import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;


import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.api.WeiboAPI.FEATURE;
import com.weibo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MainActivity extends FragmentActivity implements RequestListener,OnRefreshListener<ListView> {
	private static final String TAG = "JSON";
	JSONObject jsObject=null;
	private ArrayList<HashMap<String, Object>> listItem;
	private SimpleAdapter mAdapter;
	private PullToRefreshListFragment mPullRefreshListFragment;
	private PullToRefreshListView mPullRefreshListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	if (true) {         
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
    	}     
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusesAPI statusApi=new StatusesAPI(LoginActivity.accessToken);
        statusApi.friendsTimeline(0, 0, 50 , 1, false,  FEATURE.ALL,false,this);
        
		mPullRefreshListFragment = (PullToRefreshListFragment) getSupportFragmentManager().findFragmentById(
				R.id.frag_ptr_list);
		// Get PullToRefreshListView from Fragment
		mPullRefreshListView = mPullRefreshListFragment.getPullToRefreshListView();

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(this);

		// You can also just use mPullRefreshListFragment.getListView()
		ListView actualListView = mPullRefreshListView.getRefreshableView();

//		mListItems = new LinkedList<String>();
//		mListItems.addAll(Arrays.asList(mStrings));
//		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		listItem = new ArrayList<HashMap<String, Object>>();
		mAdapter = new new_adapter(this,listItem,R.layout.layout_content_listviewm,
                new String[]{"listview_userlogo","listview_username","listview_forward_text",
				"listview_comment_text","listview_content_text",
				"listview_info_text","listview_repost_title","listview_repost_content"},
                new int[]{R.id.listview_userlogo,R.id.listview_username,R.id.listview_forward_text,
				R.id.listview_comment_text,R.id.listview_content_text,
				R.id.listview_info_text,R.id.listview_repost_title,R.id.listview_repost_content});

//        setListAdapter(mAdapter);
        
		// You can also just use setListAdapter(mAdapter) or
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListFragment.setListShown(true);
    }
    
	private void addItem() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("listview_userlogo", R.drawable.bg_profile_intro);
		map.put("listview_username", "This is a test");
		map.put("listview_content_text", "Sometimes,all we've got is one chance,to live,love,take flight,or make " +
				"it to the other side.one chance,to hold on to that moment,catch her eye,see the signs,make it right");
		map.put("listview_forward_text", "150");
		map.put("listview_comment_text", "20");
		map.put("listview_info_text", "10-16 16:45 来自android客户端");
		map.put("listview_repost_title", "CCTV");
		map.put("listview_repost_content", "Sometimes,all we've got is one chance,to live,love,take flight,or make " +
				"it to the other side.one chance,to hold on to that moment,catch her eye,see the signs,make it right");
		listItem.add(0,map);
		mAdapter.notifyDataSetChanged();
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
        JSONArray array = null;
        StringBuilder sb=new StringBuilder();
        
        try {
			array = jsObject.getJSONArray("statuses");
		} catch (JSONException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		if(array.length()>0){
			
			for (int i=0;i<3;i++)
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				JSONObject child = null;
				try {
//					child = array.getJSONObject(i);
					String statusesStr = array.getString(i);
					child = new JSONObject(statusesStr);
				} catch (JSONException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				if (child != null)
				{
//                JSONObject child=array.getJSONObject(0);
					try {
						Log.i(TAG,"text: "+child.getString("text"));
						Log.i(TAG,"created_at: "+child.getString("created_at"));
						Log.i(TAG,"source: "+child.getString("source"));
						Log.i(TAG,"comments_count: "+child.getString("comments_count"));
						Log.i(TAG,"reposts_count: "+child.getString("reposts_count"));
						//put into listview
						map.put("listview_content_text", child.getString("text"));
						map.put("listview_forward_text", child.getString("reposts_count"));
						map.put("listview_comment_text", child.getString("comments_count"));
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
							Log.i(TAG,"profile_image_url: "+user.getString("profile_image_url"));
							Log.i(TAG,"screen_name: "+user.getString("screen_name"));
							map.put("listview_username", user.getString("screen_name"));
							map.put("listview_userlogo", user.getString("profile_image_url"));
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
							map.put("listview_info_text",date+"  来自: "+Html.fromHtml(child.getString("source")));
							map.put("listview_repost_content", retweeted_status_obj.getString("text"));
		        	
							String user2 = retweeted_status_obj.getString("user");
							if (user2 != null)
							{
								JSONObject user3 = new JSONObject(user2);
								Log.i(TAG,"user3: "+user3.getString("name"));
								map.put("listview_repost_title", user3.getString("name"));
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
        public void handleMessage (Message msg) {
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

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		new GetDataTask().execute();
		
	}
	private class GetDataTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return "yes";
		}

		@Override
		protected void onPostExecute(String result) {
			listItem.clear();
			addItem();
			
//			mListItems.addFirst("Added after refresh...");
			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
