package com.mn.tiger.system;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.mn.tiger.log.Logger;

public class AppConfigs
{
	private static final Logger LOG = Logger.getLogger(AppConfigs.class);

	/**
	 * 产品Id号
	 */
	public static int Product_ID = 10;

	/**
	 * 应用程序的版本号，从AndroidManifest.xml中读取
	 */
	public static String appVersion;

	/**
	 * 应用程序的版本代码，从AndroidManifest.xml中读取
	 */
	public static int appVersionCode;

	public static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	public static void initAppConfigs(Context context)
	{
		try
		{
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appVersion = packageInfo.versionName;
			appVersionCode = packageInfo.versionCode;
		}
		catch (NameNotFoundException e)
		{
			LOG.e(e.getMessage(), e);
		}
	}
}
