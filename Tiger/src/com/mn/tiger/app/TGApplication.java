package com.mn.tiger.app;

import java.util.LinkedList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.otto.Bus;

import android.app.Activity;
import android.app.Application;

/**
 * 该类作用及功能说明 应用App类
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGApplication extends Application
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	/** 启动Activity列表 */
	private List<Activity> activities = new LinkedList<Activity>();

	/** Application实例 */
	private static TGApplication instance;
	
	/**
	 * 事件总线
	 */
	private static Bus bus;
	
	/**
	 * 图像加载器
	 */
	private ImageLoader imageLoader;

	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
	}

	/** 得到 Application实例 */
	public static TGApplication getInstance()
	{
		return instance;
	}
	
	/**
	 * 获取ImageLoader
	 * @return
	 */
	public ImageLoader getImageLoader()
	{
		if(null == imageLoader)
		{
			imageLoader = initImageLoader();
		}
		return imageLoader;
	}
	
	/**
	 * 初始化ImageLoader
	 */
	protected ImageLoader initImageLoader()
	{
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		return imageLoader;
	}

	/**
	 * 该方法的作用:添加Activity
	 * 
	 * @date 2013-12-3
	 * @param activity
	 */
	public void addActivityToStack(Activity activity)
	{
		activities.add(activity);
	}

	/**
	 * 该方法的作用: 删除Activity
	 * 
	 * @date 2014年1月3日
	 * @param activity
	 */
	public void removeActivityFromStack(Activity activity)
	{
		activities.remove(activity);
	}

	/**
	 * 退出应用时销毁所有启动的Activity
	 */
	public void exit()
	{
		finishAllActivity();
		System.exit(0);
	}

	/**
	 * 该方法的作用: 销毁所有的Activity
	 * 
	 * @date 2014年3月4日
	 */
	public void finishAllActivity()
	{
		Activity activity;
		for (int i = 0; i < activities.size(); i++)
		{
			activity = activities.get(i);
			if (null != activity && !activity.isFinishing())
			{
				activity.finish();
			}
		}

		activities.clear();
	}

	public static Bus getBus()
	{
		if(null == bus)
		{
			bus = new Bus();
		}
		return bus;
	}

}
