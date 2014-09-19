package com.mn.tiger.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

import com.mn.tiger.task.invoke.TGTaskInvoker;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 应用配置，目前主要是获取tiger.propeties的配置
 * 
 * @date 2013-11-21
 */
public final class AppConfiguration
{
	private final String LOG_TAG = AppConfiguration.class.getSimpleName();
	/**************************应用类型******************************/

	/******************** 测试模式 *********************************/
	
	/**
	 * 未知的Debug模式
	 */
	public static final int DEBUG_MODE_NONE = 10;
	
	/**
	 * UAT环境
	 */
	public static final int DEBUG_MODE_UAT = 11;
	
	/**
	 * SIT环境
	 */
	public static final int DEBUG_MODE_SIT = 12;
	
	/**
	 * 生产环境
	 */
	public static final int DEBUG_MODE_RELEASE = 13;
	
	/** sit测试环境 */
	private final String DEBUG_MODE_SIT_VALUE = "sit";
	
	/** uat测试环境 */
	private final String DEBUG_MODE_UAT_VALUE = "uat";
	
	/** 正式环境 */
	private final String DEBUG_MODE_RELEASE_VALUE = "release";
	
	/** 是否是调试模式，如果是的话，会有日志打印。默认为生产环境 */
	private final String DEBUG_MODE = "debug_mode";
	
	/** 配置文件名称 */
	private final String CONFIGURATION_FILE_NAME = "tiger.properties";


	/** 客户端版本名称，用于升级检测 */
	private final String VERSION_NAME = "versionName";

	/** 是否是真机，默认为真机 */
	private final String TRUE_MOBILE = "true_mobile";

	/** 模拟器设备号的后缀，用于区别各模拟器。模拟器的设备号都返回0，当true_mobile为false的时候，需要配置此项 */
	private final String SIMULATOR_POSTFIX = "simulator_deviceID_postfix";

	/**代理服务地址*/
	private final String PROXY_URL = "proxy_url";
	
	/**
	 * Debug模式
	 */
	private int debugMode = DEBUG_MODE_NONE;
	
	/**
	 * 配置单例对象
	 */
	private static AppConfiguration config = new AppConfiguration();
	
	/**
	 * properties文件对象
	 */
	private Properties systemProperties = null;
	
	/**
	 * 是否在远程service里面运行所有耗时操作
	 */
	private static boolean isRunAtService = false;

	/**
	 * 该方法的作用:
	 * 获取单例对象
	 * @date 2014年1月7日
	 * @return
	 */
	public static AppConfiguration getInstance()
	{
		return config;
	}

	private AppConfiguration()
	{
		
	}

	/**
	 * 该方法的作用:加载系统配置,在获取配置值之前需要先加载好，建议最好在应用启动时优先加载
	 * @date 2013-11-21
	 * @param context
	 * @throws IOException
	 */
	public void loadSystemConfig(Context context) throws IOException
	{
		if (null == systemProperties)
		{
			Properties properties = new Properties();
			InputStream in = null;
			try
			{
				in = context.getResources().getAssets().open(CONFIGURATION_FILE_NAME);
				properties.load(in);
			}
			finally
			{
				if (in != null)
				{
					in.close();
				}
			}
			systemProperties = properties;
		}
	}

	/**
	 * 该方法的作用: 获取是否在service里运行耗时操作
	 * @date 2014年7月26日
	 * @return
	 */
	public static boolean isRunAtService()
	{
		return isRunAtService;
	}

	/**
	 * 该方法的作用: 设置在service里运行耗时操作
	 * @date 2014年8月11日
	 * @param context
	 * @param action
	 */
	public static void setRunAtService(Context context, String action)
	{
		AppConfiguration.isRunAtService = true;
		TGTaskInvoker.initService(context, action);
	}

	/**
	 * 该方法的作用:获取配置文件中指定键的值
	 * @date 2013-11-25
	 * @param key
	 *            键
	 * @param defaultValue
	 *            默认值
	 * @return String
	 */
	private String getValueFromConfig(String key, String defaultValue)
	{
		if (systemProperties != null && null != systemProperties.get(key))
		{
			return (String) systemProperties.get(key);
		}
		else
		{
			return defaultValue;
		}
	}

	/**
	 * 该方法的作用:获取客户端版本号名称
	 * @date 2013-11-18
	 * @return
	 */
	public String getVersionName()
	{
		return getValueFromConfig(VERSION_NAME, "");
	}

	/**
	 * 该方法的作用:
	 * 从配置文件中获取自定义的启动类
	 * @date 2014年1月7日
	 * @param name
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class getClassFromConfig(String name)
	{
		String className = getValueFromConfig(name, null);
		if (className == null)
		{
			return null;
		}

		Class result = null;
		try
		{
			result = Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			LogTools.e(e);
		}
		return result;
	}

	/**
	 * 该方法的作用:获取环境模式，sit、uat、以及生产环境。设置为true为uat的测试环境
	 * @date 2014年1月3日
	 * @return
	 */
	public int getDebugMode()
	{
		if(debugMode != DEBUG_MODE_NONE)
		{
			return debugMode;
		}
		
		String debugModeStr = getValueFromConfig(DEBUG_MODE, DEBUG_MODE_RELEASE_VALUE);
		
		if(debugModeStr.equalsIgnoreCase(DEBUG_MODE_RELEASE_VALUE))
		{
			debugMode = DEBUG_MODE_RELEASE;
		}
		else if(debugModeStr.equalsIgnoreCase(DEBUG_MODE_UAT_VALUE))
		{
			debugMode = DEBUG_MODE_UAT;
		}
		else if(debugModeStr.equalsIgnoreCase(DEBUG_MODE_SIT_VALUE))
		{
			debugMode = DEBUG_MODE_SIT;
		}
		
		return debugMode;
		
	}

	/**
	 * 该方法的作用:是否为真机，此配置用于模拟器调试
	 * @date 2013-12-2
	 * @return
	 */
	public boolean isTrueMobile()
	{
		String isTrueMobileStr = getValueFromConfig(TRUE_MOBILE, "true");
		if (!isTrueMobileStr.equals("true"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 该方法的作用:获取模拟器设备编号后缀,模拟器调试用
	 * @date 2013-12-2
	 * @return
	 */
	public String getSimulatorPostfix()
	{
		return getValueFromConfig(SIMULATOR_POSTFIX, "");
	}

	/**
	 * 该方法的作用:获取设置的代理服务地址
	 * 
	 * @date 2014年4月28日
	 * @return
	 */
	public String getProxyURL()
	{
		return getValueFromConfig(PROXY_URL, "");
	}
}
