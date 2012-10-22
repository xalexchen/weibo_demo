package com.alex.weibo.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;


public class AsyncImageLoader {

	 //SoftReference�������ã���Ϊ�˸�õ�Ϊ��ϵͳ���ձ���
    private static HashMap<String, SoftReference<Drawable>> imageCache;
    
    static {
    	imageCache = new HashMap<String, SoftReference<Drawable>>();
    }
    
    
    public AsyncImageLoader() {
        
    }
    public Drawable loadDrawable(final String imageUrl,final ImageView imageView, final ImageCallback imageCallback){
        if (imageCache.containsKey(imageUrl)) {
            //�ӻ����л�ȡ
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            Drawable drawable = softReference.get();
            System.out.println("111111111111111111111111111111");
            if (drawable != null) {
            	 System.out.println("11111111111122222222211111111111111111");
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageView,imageUrl);
            }
        };
        //������һ���µ��߳�����ͼƬ
        new Thread() {
            @Override
            public void run() {
            	System.out.println("11111111111133333333333211111111111111111");
                Drawable drawable = null;
				try {
					drawable = ImageUtil.geRoundDrawableFromUrl(imageUrl, 20);
				} catch (Exception e) {
					e.printStackTrace();
				}
                imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                Message message = handler.obtainMessage(0, drawable);
                handler.sendMessage(message);
            }
        }.start();
        return null;
    }
    //�ص��ӿ�
    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl);
    }
}