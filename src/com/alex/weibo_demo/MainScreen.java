package com.alex.weibo_demo;

import java.util.Arrays;
import java.util.LinkedList;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;


import android.view.View;

public class MainScreen extends ListActivity{
	private LinkedList<String> mListItems;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

    TopMenuOnTouchListener mTopMenuOnClickListener = new TopMenuOnTouchListener();
    View iv = (View) findViewById(R.id.home_group_img);
    View iv2 = (View) findViewById(R.id.home_all_img);
    
    iv.setOnTouchListener(mTopMenuOnClickListener);
    iv2.setOnTouchListener(mTopMenuOnClickListener);

        // Set a listener to be invoked when the list should be refreshed.
        ((PullToRefreshListView) getListView()).setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mListItems);

        setListAdapter(adapter);
    }
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");

            // Call onRefreshComplete when the list has been refreshed.
            ((PullToRefreshListView) getListView()).onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = {
            "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam",
            "Abondance", "Ackawi", "Acorn", "Adelost", "Affidelice au Chablis",
            "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler"};
	 
	    private class TopMenuOnTouchListener implements OnTouchListener{

	    	@Override
	    	public boolean onTouch(View v, MotionEvent event) {
	    		// TODO Auto-generated method stub
	    		Log.v("aaa","tag = " + v.getTag() + " event = "+event.getAction() + "id= " +v.getId());
	    		mListItems.clear();
	    		return false;
	    	} 	
	    }
}

