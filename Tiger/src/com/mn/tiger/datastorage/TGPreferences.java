package com.mn.tiger.datastorage;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;

import com.mn.tiger.utility.LogTools;

public class TGPreferences
{
	private static final String LOG_TAG = TGPreferences.class.getSimpleName();
	
	@SuppressLint("InlinedApi")
	public static void save(Context context, String name, String key, String value)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			Editor editer = setting.edit();
			editer.putString(key, value);
			editer.commit();

			return;
		}
		LogTools.e(LOG_TAG, "[Method:save] ——> the context is null");
	}

	@SuppressLint("InlinedApi")
	public static void save(Context context, String name, String key, int value)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			Editor editer = setting.edit();
			editer.putInt(key, value);
			editer.commit();
			
			return;
		}
		
		LogTools.e(LOG_TAG, "[Method:save] ——> the context is null");
	}

	@SuppressLint("InlinedApi")
	public static void save(Context context, String name, String key, long value)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			Editor editer = setting.edit();
			editer.putLong(key, value);
			editer.commit();

			return;
		}
		LogTools.e(LOG_TAG, "[Method:save] ——> the context is null");
	}

	@SuppressLint("InlinedApi")
	public static void save(Context context, String name, String key, boolean value)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			Editor editer = setting.edit();
			editer.putBoolean(key, value);
			editer.commit();

			return;
		}
		LogTools.e(LOG_TAG, "[Method:save] ——> the context is null");
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void save(Context context, String name, String key, Float value)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			Editor editer = setting.edit();
			editer.putFloat(key, value);
			editer.commit();

			return;
		}
		LogTools.e(LOG_TAG, "[Method:save] ——> the context is null");
	}

	@SuppressLint("InlinedApi")
	public static boolean read(Context context, String name, String key, boolean defaultValue)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			return setting.getBoolean(key, defaultValue);
		}
		
		LogTools.e(LOG_TAG, "[Method:read] ——> the context is null");
		return defaultValue;
	}

	@SuppressLint("InlinedApi")
	public static int read(Context context, String name, String key, int defaultValue)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			return setting.getInt(key, defaultValue);
		}
		
		LogTools.e(LOG_TAG, "[Method:read] ——> the context is null");
		return defaultValue;
	}

	@SuppressLint("InlinedApi")
	public static float read(Context context, String name, String key, float defaultValue)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			return setting.getFloat(key, defaultValue);
		}
		
		LogTools.e(LOG_TAG, "[Method:read] ——> the context is null");
		return defaultValue;
	}

	@SuppressLint("InlinedApi")
	public static long read(Context context, String name, String key, long defaultValue)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			return setting.getLong(key, defaultValue);
		}
		
		LogTools.e(LOG_TAG, "[Method:read] ——> the context is null");
		return defaultValue;
	}

	@SuppressLint("InlinedApi")
	public static String read(Context context, String name, String key, String defaultValue)
	{
		SharedPreferences setting = getSharedPreferences(context, name);
		if(null != setting)
		{
			return setting.getString(key, defaultValue);
		}
		
		LogTools.e(LOG_TAG, "[Method:read] ——> the context is null");
		return defaultValue;
	}
	
	@SuppressLint("InlinedApi")
	public static SharedPreferences getSharedPreferences(Context context, String name)
	{
		if(null != context)
		{
			if(Build.VERSION.SDK_INT >= 11)
			{
				return context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS);
			}
			else
			{
			    return context.getSharedPreferences(name, Context.MODE_PRIVATE);
			}
		}
		
		return null;
	}
}
