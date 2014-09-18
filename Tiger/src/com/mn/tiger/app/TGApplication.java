package com.mn.tiger.app;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.mn.tiger.system.AppConfiguration;
import com.mn.tiger.task.invoke.TGTaskInvoker;
import com.mn.tiger.utility.LogTools;
//import com.huawei.mjet.system.MPMjetProperties;

/**
 * 该类作用及功能说明 应用App类
 * 
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日 Copyright Huawei Technologies Co., Ltd. 1998-2011. All
 *       rights reserved.
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
		// 初始化配置项
		initConfiguration(getApplicationContext());
		
		//校验配置项
		AppConfiguration.getInstance().checkConfiguration(this.getApplicationContext());

		// 启动crash日志捕获及上传监听
		initCrashManager();
	}

	/** 得到 BaseApplication实例 */
	public static IApplication getInstance()
	{
		return instance;
	}

	/**
	 * 该方法的作用:初始化应用配置信息
	 * 
	 * @author yWX158243
	 * @date 2014年1月3日
	 * @param context
	 */
	private void initConfiguration(Context context)
	{
		try
		{
			// 此方法的调用需要在所有获取配置设置值之前
			AppConfiguration.getInstance().loadSystemConfig(context);
			initLogConfiguration(context);
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG, "", e);
		}
	}
	
	/**
	 * 该方法的作用:初始化日志的配置信息(默认存放sdcard路径:MEAP/包名，根据配置文件判定是否打开打印
	 * 生产环境，PROGRESS和ERROR的级别日志写入文件，但不写控制台；其他环境日志文件打印到控制台，也写入文件。)
	 * 
	 * @author yWX158243
	 * @date 2014年3月14日
	 * @param context
	 */
	protected void initLogConfiguration(Context context)
	{
		LogTools.setAppLogFileDir("MEAP" + File.separator + context.getPackageName());
		LogTools.switchLog(context);
	}

	/**
	 * 该方法的作用:
	 * 
	 * @author l00220455
	 * @date 2014年5月23日
	 */
	protected void initCrashManager()
	{
		//TODO
	}

	/**
	 * 该方法的作用:添加Activity
	 * 
	 * @author l00220455
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
	 * @author l00220455
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
		TGTaskInvoker.unConnectRemoteService(this);
		System.exit(0);
	}

	/**
	 * 该方法的作用: 销毁所有的Activity
	 * 
	 * @author l00220455
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

	/**
	 * 该方法的作用: 连接到远程服务
	 * 
	 * @author l00220455
	 * @date 2014年5月23日
	 */
	protected void connectedRemoteService(String action)
	{
		AppConfiguration.setRunAtService(this, action);
	}
}
