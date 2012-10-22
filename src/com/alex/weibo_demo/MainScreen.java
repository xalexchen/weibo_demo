package com.alex.weibo_demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import com.alex.weibo.utils.new_adapter;
import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;


import android.view.View;

public class MainScreen extends ListActivity{
	private SimpleAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> listItem;
	private TopMenuOnTouchListener mTopMenuOnClickListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
				"listview_user_bg_profile","listview_user","listview_content",
				"listview_comment_text","listview_content_text","listview_info",
				"listview_repost_user","listview_repost_content","listview_shadow_left",
				"listview_shadow_right","listview_bg"
				},
				new int[]{
				R.id.listview_user_bg_profile,R.id.listview_user,R.id.listview_content,
				R.id.listview_comment_text,R.id.listview_content_text,R.id.listview_info,
				R.id.listview_repost_user,R.id.listview_repost_content,R.id.listview_shadow_left,
				R.id.listview_shadow_right,R.id.listview_bg
				});
		addItem();
        setListAdapter(mAdapter);
    }
    private void RegisterTopMenuListener()
    {
        mTopMenuOnClickListener = new TopMenuOnTouchListener();
        ImageView iv = (ImageView) findViewById(R.id.home_group_img);
        ImageView iv2 = (ImageView) findViewById(R.id.home_all_img);
    
        iv.setOnTouchListener(mTopMenuOnClickListener);
        iv2.setOnTouchListener(mTopMenuOnClickListener);
    }
	private void addItem() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("listview_user_bg_profile", R.drawable.bg_profile_intro);
		map.put("listview_user", "This is a test");
		map.put("listview_content", "Sometimes,all we've got is one chance,to live,love,take flight,or make " +
			"it to the other side.one chance,to hold on to that moment,catch her eye,see the signs,make it right");
		map.put("listview_forward_text", "150");
		map.put("listview_comment_text", "20");
		map.put("listview_info", "10-16 16:45 来自android客户端");
		map.put("listview_repost_user", "CCTV");
		map.put("listview_repost_content", "Sometimes,all we've got is one chance,to live,love,take flight,or make " +
			"it to the other side.one chance,to hold on to that moment,catch her eye,see the signs,make it right");
		listItem.add(0,map);
		mAdapter.notifyDataSetChanged();
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
        	addItem();
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
					addItem();
					mAdapter.notifyDataSetChanged();
				}
			}
			return false;
		}
	}
}

