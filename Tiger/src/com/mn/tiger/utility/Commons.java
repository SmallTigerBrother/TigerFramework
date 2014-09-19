package com.mn.tiger.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.mn.tiger.app.IApplication;
import com.mn.tiger.app.TGApplication;
import com.mn.tiger.request.async.TGHttpAsyncRequester;
import com.mn.tiger.request.async.TGHttpAsyncTask;
import com.mn.tiger.system.AppConfiguration;
import com.mn.tiger.widget.dialog.IDialogContext;

public class Commons
{
	private static final String LOG_TAG = Commons.class.getSimpleName();

	/*********************** 获取应用配置信息 start *****************************************/

	/**
	 * 该方法的作用:获取配置的登陆界面class包名 + 类名
	 * 
	 * @date 2013-11-21
	 * @param context
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Class getClassFromConfig(Context context, String name)
	{
		return AppConfiguration.getInstance().getClassFromConfig(name);
	}

	/**
	 * 该方法的作用:获取客户端版本号
	 * 
	 * @date 2013-11-18
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context)
	{
		return AppConfiguration.getInstance().getVersionName();
	}

	/**
	 * 获取当前的运行环境
	 * 
	 * @param context
	 * @return
	 */
	public static int getDebugMode(Context context)
	{
		return AppConfiguration.getInstance().getDebugMode();
	}


	/*********************** 获取应用配置信息 end *****************************************/

	/**
	 * 该方法的作用:获取系统语言
	 * 
	 * @date 2014年6月10日
	 * @param context
	 * @return
	 */
	public static String getSystemLanguage(Context context)
	{
		Resources resource = context.getResources();
		Configuration configuration = resource.getConfiguration();
		Locale locale = configuration.locale;
		return locale.getLanguage();
	}

	/**
	 * 该方法的作用:获取本地的设备ID
	 * 
	 * @date 2014年2月15日
	 * @param context
	 * @return
	 */
	@SuppressLint("WorldWriteableFiles")
	public static String getLocalDeviceID(Context context)
	{
		String uid = "";
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (null != telephonyManager)
		{
			// 默认取Device_ID
			uid = telephonyManager.getDeviceId();
		}

		if (null == uid)
		{
			// 默认取Android_ID
			uid = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
		}

		if (null != uid && uid.matches("^[A-Za-z0-9]+$"))
		{
			// 出现0或者000000000000000时，取wifi的mac地址,此段代码在真机运行把此段注释去掉(753行--759行)
			if (AppConfiguration.getInstance().isTrueMobile())
			{// 用于真机调试的代码
				if ("0".equals(uid) || "000000000000000".equals(uid))
				{
					if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) < 9)
					{//
						uid = NetworkUtils.getLocalMacAddress(context);
					}
					else
					{// 2.3及以上版本可用
						uid = android.os.Build.SERIAL;
					}
				}
			}
			else
			{
				uid = uid + AppConfiguration.getInstance().getSimulatorPostfix();
			}
		}
		return uid;
	}


	/**
	 * 该方法的作用: 取消异步任务
	 * 
	 * @date 2014年1月7日
	 */
	public static void cancelAsyncTask(@SuppressWarnings("rawtypes") TGHttpAsyncRequester requester)
	{
		if (null != requester)
		{
			requester.cancel();
		}
	}
	
	/**
	 * 该方法的作用: 取消异步任务
	 * 
	 * @date 2014年1月7日
	 * @param task
	 */
	public static void cancelAsyncTask(@SuppressWarnings("rawtypes") TGHttpAsyncTask task)
	{
		if (null != task)
		{
			task = null;
		}
	}

	/**
	 * 该方法的作用:关闭输入流
	 * 
	 * @date 2013-3-10
	 * @param stream
	 */
	public static void closeInputStream(InputStream stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				LogTools.e(LOG_TAG, "", e);
			}
		}
	}

	/**
	 * 该方法的作用:关闭输出流
	 * 
	 * @date 2013-3-10
	 * @param stream
	 */
	public static void closeOutputStream(OutputStream stream)
	{
		if (stream != null)
		{
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				LogTools.e(LOG_TAG, "", e);
			}
		}
	}

	public static void exit()
	{
		IApplication application = TGApplication.getInstance();
		application.exit();
	}
	
	/**
	 * 该方法的作用: 切换系统语言
	 * 
	 * @date 2014年1月7日
	 * @param context
	 * @param language
	 */
	public static void changeSystemLanguage(Context context, String language)
	{
		Resources resources = context.getResources();
		Configuration config = resources.getConfiguration();
		if (Locale.CHINESE.toString().equals(language))
		{
			config.locale = Locale.CHINESE;
		}
		else
		{
			config.locale = new Locale(language);
		}
		resources.updateConfiguration(config, null);
	}

	/**
	 * 
	 * 该方法的作用:检测当前创建或显示Dialog的Context是否有效
	 * @date 2014年8月12日
	 * @param context
	 * @return true有效可以创建或显示，fasle则相反
	 */
	public static boolean checkContextIsValid(Context context)
	{
		if (context != null && (context instanceof Activity) && (context instanceof IDialogContext)
				&& !((Activity) context).isFinishing())
		{
			return true;
		}
		return false;
	}
}
