package com.mn.tiger.utility;

import android.widget.ImageView;

import com.mn.tiger.app.TGApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Imageloader工具类
 */
public class ImageLoaderUitls
{
	public void displayImage(String uri, ImageView imageView)
	{
		TGApplication.getInstance().getImageLoader().displayImage(uri, imageView);
	}
	
	public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener)
	{
		TGApplication.getInstance().getImageLoader().displayImage(uri, imageView, listener);
	}
	
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options)
	{
		TGApplication.getInstance().getImageLoader().displayImage(uri, imageView, options);
	}
	
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, 
			ImageLoadingListener listener)
	{
		TGApplication.getInstance().getImageLoader().displayImage(uri, imageView,
				options, listener);
	}
	
	public void loadImage(String uri, ImageLoadingListener listener)
	{
		TGApplication.getInstance().getImageLoader().loadImage(uri, listener);
	}
	
	public void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener)
	{
		TGApplication.getInstance().getImageLoader().loadImage(uri, options, listener);
	}
}