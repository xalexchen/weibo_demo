package com.alex.weibo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;



import com.alex.weibo.utils.AsyncImageLoader.ImageCallback;
import com.alex.weibo_demo.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class new_adapter extends SimpleAdapter {
	public new_adapter(Context context, List<? extends Map<String, ?>> data,
			int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
	}
	@Override
    public void setViewImage(ImageView v, String value) {
		super.setViewImage(v, value);
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		Drawable cachedImage = asyncImageLoader.loadDrawable(value,v, new ImageCallback(){
            
            public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl) {
                imageView.setImageDrawable(imageDrawable);
            }
        });
		System.out.println(cachedImage);
		if (cachedImage == null) {
			v.setImageResource(R.drawable.bg_profile_intro);
		} else {
			v.setImageDrawable(cachedImage);
		}
		
//		URL url;
//		try {
//			url = new URL(value);
//			URLConnection conn = url.openConnection();
//			conn.connect();
//			InputStream is = conn.getInputStream();
//			Bitmap bm = BitmapFactory.decodeStream(is);
//			v.setImageBitmap(bm);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}
