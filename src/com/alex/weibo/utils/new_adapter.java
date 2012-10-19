package com.alex.weibo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class new_adapter extends SimpleAdapter {

	private Bitmap bm;
	private String urlstring;
	private URL url;
	public new_adapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}
	@Override
    public void setViewImage(ImageView v, String value) {
		super.setViewImage(v, value);
		urlstring = value;
		Log.v("weibo", "url = " + value);
		try {
			url = new URL(urlstring);
			URLConnection conn = url.openConnection();
			conn.connect();
			InputStream is = conn.getInputStream();
			bm = BitmapFactory.decodeStream(is);
			Log.v("weibo", "11111111111");

			Log.v("weibo", "2222222222");
			v.setImageBitmap(bm);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
