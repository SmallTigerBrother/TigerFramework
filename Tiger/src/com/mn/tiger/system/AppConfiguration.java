package com.mn.tiger.system;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.task.invoke.TGTaskInvoker;
import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.LogTools;
import com.mn.tiger.widget.dialog.IDialog;

/**
 * 该类作用及功能说明 应用配置，目前主要是获取mjet.propeties的配置
 * 
 * @author l00220455
 * @date 2013-11-21
 */
public final class AppConfiguration
{
	private final String LOG_TAG = AppConfiguration.class.getSimpleName();
	/**************************应用类型******************************/
	
	/**
	 * 应用类型 ———————— 未知
	 */
	public static final int APP_TYPE_NONE = 0;
	
	/**
	 * 应用类型 ———————— 外网应用
	 */
	public static final int APP_TYPE_UNIPORTAL = 1;
	
	/**
	 * 应用类型 ———————— 内网应用
	 */
	public static final int APP_TYPE_LOGIN = 3;
	
	
	/*****************应用类型*******************************************/
	
	/**外网类型，表示该应用允许外部、内部用户使用*/
	private final String UNIPOTRAL_TYPE = "uniportal";
	
	/**内网类型，表示该应用只允许内部用户使用，只要配置中，设置的不是外网类型，也不是混合类型，都为内网类型*/
	private final String INTRANET_TYPE = "login";
	
	/** 强制绑定w3 */
	public static final String BIND_W3_FORCE = "force";

	/** 不强制绑定w3 */
	public static final String BIND_W3_NOT_FORCE = "not_force";

	/** 不绑定W3 */
	public static final String BIND_W3_NOT_BIND = "not_bind";

	
	/******************* 安装设备类型 start *****************************/
	/** 安装到手机的版本 */
	public static final String DEVICE_MOBILE = "mobile";

	/** 安装到pad的版本 */
	public static final String DEVICE_PAD = "pad";

	/********************** 安装设备类型 end ****************************/

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
	
	/**
	 * 第1个版本的UAT环境的值
	 */
	private final String DEBUG_MODE_UAT_VALUE_VERSION_1  = "true";

	/** 正式环境 */
	private final String DEBUG_MODE_RELEASE_VALUE = "release";
	
	/**
	 * 第1个版本的生产环境的值
	 */
	private final String DEBUG_MODE_RELEASE_VALUE_VERSION_1  = "false";
	
	/*************************对话框样式*************************/
	
	/**
	 * 对话框类型 —————— MPDialog
	 */
	private final String DIALOG_STYLE_MPDIALOG = "mpdialog";
	
	/**
	 * 对话框类型 —————— AlertDialog
	 */
	private final String DIALOG_STYLE_ALERTDIALOG = "alertdialog";
	
	
	/***********************其他设置项*************************/

	/** 是否支持离线登录的配置key值，跟mjet.properties配置的键值一致 */
	private final String SUPPORT_OFFLINE_FLAG_KEY = "support_offline_login";

	/** 配置文件名称 */
	private final String CONFIGURATION_FILE_NAME = "mjet.properties";

	/** 应用类型(外网应用，内网应用，内网+外网应用。默认为外网) */
	private final String MJET_TYPE = "mjet_type";

	/** 客户端版本名称，用于升级检测 */
	private final String VERSION_NAME = "versionName";

	/** edm上传的类型，外网或者内网。默认为外网 */
	private final String EDM_TYPE = "edm_type";

	/** 是否是调试模式，如果是的话，会有日志打印。默认为生产环境 */
	private final String DEBUG_MODE = "debug_mode";

	/** 主题名称，选项有red,orange,blue。默认为红色主题 */
	private final String THEME_NAME = "theme_name";

	/** 应用下载网址 */
	private final String APP_DOWNLOAD_WEBSITE = "app_download_website";

	/** 应用升级下载地址 */
	private final String UPDATE_URL = "update_url";

	/** 是否是真机，默认为真机 */
	private final String TRUE_MOBILE = "true_mobile";

	/** 模拟器设备号的后缀，用于区别各模拟器。模拟器的设备号都返回0，当true_mobile为false的时候，需要配置此项 */
	private final String SIMULATOR_POSTFIX = "simulator_deviceID_postfix";

	/** 应用访问的uri(内网应用配置项) */
	private final String APP_ACCESS_URI = "access_uri";

	private final String USER_TYPE = "user_type";

	/** 配置该应用适配W3最低版本的版本号(内网应用配置项) */
	private final String MIN_REQUIRED_W3_VERSION = "min_required_w3_version";

	/** 配置该应用是否和W3关联 */
	private final String FORCE_BIND_W3 = "force_bind_w3";

	/** 应用安装的设备类型 */
	private final String DEVICE_TYPE = "device_type";
	
	/**代理服务地址*/
	private final String PROXY_URL = "proxy_url";
	
	/**
	 * 对话框样式
	 */
	private final String DIALOG_STYLE = "dialog_style";
	
	/**
	 * 应用类型
	 */
	private int appType = APP_TYPE_NONE;
	
	/**
	 * Debug模式
	 */
	private int debugMode = DEBUG_MODE_NONE;
	
	/**定位需要的密钥*/
	private String LOCATE_KEY = "locate_key";
	
	/**
	 * 配置单例对象
	 */
	private static AppConfiguration config = new AppConfiguration();
	
	/**
	 * properties文件对象
	 */
	private Properties systemProperties = null;
	
	/**
	 * 全局控制，网络请求是否加密。true: 所有请求加密; false: 所有请求不加密
	 */
	private static boolean isEncryptAllRequest = true;
	
	/**
	 * 是否在远程service里面运行所有耗时操作
	 */
	private static boolean isRunAtService = false;

	/**
	 * 该方法的作用:
	 * 获取单例对象
	 * @author l00220455
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
	 * @author l00220455
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
	 * 
	 * 该方法的作用: 设置所有网络请求是否加密。
	 * @author pWX197040
	 * @date 2014年7月16日
	 * @param isEncryptAllRequest
	 */
	public static void setEncryptAllRequest(boolean isEncryptAllRequest)
	{
		AppConfiguration.isEncryptAllRequest = isEncryptAllRequest;
	}
	
	/**
	 * 
	 * 该方法的作用: 获取所有网络请求是否加密。
	 * @author pWX197040
	 * @date 2014年7月16日
	 * @return
	 */
	public static boolean isEncryptAllRequest()
	{
		return isEncryptAllRequest;
	}

	/**
	 * 
	 * 该方法的作用: 获取是否在service里运行耗时操作
	 * @author pWX197040
	 * @date 2014年7月26日
	 * @return
	 */
	public static boolean isRunAtService()
	{
		return isRunAtService;
	}

	/**
	 * 
	 * 该方法的作用: 设置在service里运行耗时操作
	 * @author pWX197040
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
	 * @author l00220455
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
	 * 该方法的作用:获取应用模式（内网应用，外网应用）
	 * @author l00220455
	 * @date 2013-11-21
	 * @param context
	 * @return
	 */
	public int getAppType()
	{
		if(appType != APP_TYPE_NONE)
		{
			return appType;
		}
		
		String appTypeStr = getValueFromConfig(MJET_TYPE, "");
		
		if(INTRANET_TYPE.equals(appTypeStr))
		{
			appType = APP_TYPE_LOGIN;
		}
		else if(UNIPOTRAL_TYPE.equals(appTypeStr))
		{
			appType = APP_TYPE_UNIPORTAL;
		}
		
		return appType;
	}
	
	
	/**
	 * 该方法的作用:获取客户端版本号名称
	 * @author l00220455
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
	 * @author l00220455
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
	 * edm是否是外网环境,无配置默认为生产环境
	 * @param name
	 * @return
	 */
	public boolean isEDMUniportal()
	{
		String edm_type = getValueFromConfig(EDM_TYPE, "uniportal");
		if (edm_type.equals("uniportal"))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 该方法的作用:获取环境模式，sit、uat、以及生产环境。设置为true为uat的测试环境
	 * @author yWX158243
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
		
		if(debugModeStr.equalsIgnoreCase(DEBUG_MODE_RELEASE_VALUE) || 
				debugModeStr.equalsIgnoreCase(DEBUG_MODE_RELEASE_VALUE_VERSION_1))
		{
			debugMode = DEBUG_MODE_RELEASE;
		}
		else if(debugModeStr.equalsIgnoreCase(DEBUG_MODE_UAT_VALUE) || 
				debugModeStr.equalsIgnoreCase(DEBUG_MODE_UAT_VALUE_VERSION_1))
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
	 * 该方法的作用:获取主题名称，无配置默认为红色主题
	 * @date 2013-4-9
	 * @return 返回小写字母的主题名称
	 */
	public String getThemeName()
	{
		String theme = getValueFromConfig(THEME_NAME, "red");
		theme = theme.toLowerCase(Locale.ENGLISH);
		return theme;
	}

	/**
	 * 该方法的作用:判断是否支持离线登录，无配置默认为不支持
	 * @author l00220455
	 * @date 2013-11-21
	 * @return
	 */
	public boolean isSupportOfflineLogin()
	{
		String isOfflineLoginStr = getValueFromConfig(SUPPORT_OFFLINE_FLAG_KEY, "false");
		// 根据配置判断是否支持离线登录
		if (isOfflineLoginStr.equals("true"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 该方法的作用:获取应用下载的网址url
	 * @author l00220455
	 * @date 2013-11-25
	 * @return
	 */
	public String getAppDownloadWebsite()
	{
		return getValueFromConfig(APP_DOWNLOAD_WEBSITE, "");
	}

	/**
	 * 该方法的作用:获取应用升级下载地址
	 * @author l00220455
	 * @date 2013-11-26
	 * @return
	 */
	public String getAppUpdateUrl()
	{
		return getValueFromConfig(UPDATE_URL, "");
	}

	/**
	 * 该方法的作用:是否为真机，此配置用于模拟器调试
	 * @author l00220455
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
	 * @author l00220455
	 * @date 2013-12-2
	 * @return
	 */
	public String getSimulatorPostfix()
	{
		return getValueFromConfig(SIMULATOR_POSTFIX, "");
	}

	/**
	 * 该方法的作用:获取应用访问的uri
	 * @author l00220455
	 * @date 2013-12-3
	 * @param context
	 * @return
	 */
	public String getAppAccessUri()
	{
		return getValueFromConfig(APP_ACCESS_URI, "");
	}

	/**
	 * 该方法的作用:
	 * 获取支持的用户类型
	 * @author l00220455
	 * @date 2014年1月7日
	 * @return
	 */
	public String getSupportUserType()
	{
		return getValueFromConfig(USER_TYPE, "");
	}

	/**
	 * 该方法的作用:获取该应用要求的W3的最低版本
	 * @author l00220455
	 * @date 2013-12-3
	 * @param context
	 * @return
	 */
	public String getMinRequiredW3Version()
	{
		return getValueFromConfig(MIN_REQUIRED_W3_VERSION, "");
	}

	/**
	 * 该方法的作用:应用是否和W3强制关联，默认为FORCE_BIND_W3的值（强制绑定）
	 * @author yWX158243
	 * @date 2013年12月23日
	 * @param context
	 * @return
	 */
	public String getAssociateWithW3()
	{
		return getValueFromConfig(FORCE_BIND_W3, BIND_W3_FORCE);
	}

	/**
	 * 该方法的作用:获取应用安装的设备类型，默认为mobile类型
	 * @author yWX158243
	 * @date 2013年12月31日
	 * @param context
	 * @return
	 */
	public String getDeviceType()
	{
		return getValueFromConfig(DEVICE_TYPE, DEVICE_MOBILE);
	}
	
	/**
	 * 该方法的作用:获取设置的代理服务地址
	 * 
	 * @author yWX158243
	 * @date 2014年4月28日
	 * @return
	 */
	public String getProxyURL(){
		return getValueFromConfig(PROXY_URL, "");
	}
	
	/**
	 * 该方法的作用:
	 * 获取MJet框架对话框样式
	 * @author l00220455
	 * @date 2014年2月7日
	 * @return 默认返回IDialog.DIALOG_STYLE_MPDIALOG
	 */
	public int getMJetDialogStyle()
	{
		String dialogStyle = getValueFromConfig(DIALOG_STYLE, DIALOG_STYLE_MPDIALOG);
		if(dialogStyle.equals(DIALOG_STYLE_ALERTDIALOG))
		{
			return IDialog.DIALOG_STYLE_ALERTDIALOG;
		}
		else
		{
			return IDialog.DIALOG_STYLE_MPDIALOG;
		}
	}
	
	/**
	 * 该方法的作用:获取定位的认证的密钥·
	 * 
	 * @author yWX158243
	 * @date 2014年5月26日
	 * @return
	 */
	public String getLocateKey()
	{
		return getValueFromConfig(LOCATE_KEY, "");
	}
	
	/**
	 * 
	 * 该方法的作用: 检测mjet.properties文件中的配置项是否配置正确
	 * 
	 * @author lWX227301
	 * @date 2014年7月14日
	 * @param context
	 */
	public void checkConfiguration(Context context)
	{
		// 判断mjet_type是否为login或uniportal
		if (getAppType() != APP_TYPE_UNIPORTAL && getAppType() != APP_TYPE_LOGIN)
		{
			LogTools.e(LOG_TAG, "mjet_type配置有误，值只能是login或uniportal");
			throw new RuntimeException(context.getString(CR.getStringsId(context,
					"mjet_errorcode_20002")));
		}

		// 判断mjet_type=login时，access_uri与min_required_w3_version是否配置
		if (getAppType() == APP_TYPE_LOGIN
				&& (TextUtils.isEmpty(getAppAccessUri()) || TextUtils
						.isEmpty(getMinRequiredW3Version())))
		{
			LogTools.e(LOG_TAG, "mjet_type=login情况下，access_uri和min_required_w3_version都必须配置");
			throw new RuntimeException(context.getString(CR.getStringsId(context,
					"mjet_errorcode_20003")));
		}

		// 判断versionName配置是否为空
		if (TextUtils.isEmpty(getVersionName()))
		{
			LogTools.e(LOG_TAG, "versionName不能为空");
			String mmsg = String.format(context.getString(CR.getStringsId(context,
					"mjet_errorcode_20004")), "versionName");
			throw new RuntimeException(mmsg);
		}

		// 判断update_url配置是否为空
		if (TextUtils.isEmpty(getAppUpdateUrl()))
		{
			LogTools.e(LOG_TAG, "update_url不能为空");
			String mmsg = String.format(context.getString(CR.getStringsId(context,
					"mjet_errorcode_20004")), "update_url");
			throw new RuntimeException(mmsg);
		}

		// 判断debug_mode是否为uat、sit或release其中的一种
		if (getDebugMode() != DEBUG_MODE_UAT && getDebugMode() != DEBUG_MODE_SIT
				&& getDebugMode() != DEBUG_MODE_RELEASE)
		{
			LogTools.e(LOG_TAG, "debug_mode的值有误，只能为sit, uat, release中的一个");
			throw new RuntimeException(context.getString(CR.getStringsId(context,
					"mjet_errorcode_20006")));
		}
	}
}
