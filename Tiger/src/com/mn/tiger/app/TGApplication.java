package com.mn.tiger.app;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * 该类作用及功能说明 应用App类
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGApplication extends Application implements IApplication
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	/** 启动Activity列表 */
	private List<Activity> activities = new LinkedList<Activity>();

	/** ExitApplication实例 */
	private static IApplication instance;

	@Override
	public void onCreate()
	{
		super.onCreate();
		instance = this;
	}

	/** 得到 BaseApplication实例 */
	public static IApplication getInstance()
	{
		return instance;
	}

	/**
	 * 该方法的作用:添加Activity
	 * 
	 * @date 2013-12-3
	 * @param activity
	 */
	@Override
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
	@Override
	public void removeActivityFromStack(Activity activity)
	{
		activities.remove(activity);
	}

	/**
	 * 退出应用时销毁所有启动的Activity
	 */
	@Override
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
	@Override
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
}
