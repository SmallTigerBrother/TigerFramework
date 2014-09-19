package com.mn.tiger.core.parser.json;

import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明:使用FastJson库来解析JSON数据： 支持： 1.Json转List 2.Json转Map 3.Object转Json
 * 4.Json转Object
 * 
 * @date 2014年4月16日
 */
public class TGJsonUtils
{
	private static final String LOG_TAG = TGJsonUtils.class.getSimpleName();

	/**
	 * 
	 * 该方法的作用:Json转List
	 * 
	 * @date 2014年6月17日
	 * @param json
	 * @param mClass
	 * @return
	 * @throws NullPointerException
	 */
	public static <T> List<T> parseJson2List(String json, Class<T> mClass)
	{
		if (null == json)
		{
			LogTools.e(LOG_TAG, "json can't be empty!");
			return null;
		}
		if (null == mClass)
		{
			LogTools.e(LOG_TAG, "mClass can't be empty!");
			return null;
		}
		return JSON.parseArray(json, mClass);
	}

	/**
	 * 该方法的作用:Json转Map
	 * 
	 * @date 2014年4月16日
	 * @param json
	 * @return
	 */
	public static HashMap<String, Object> parseJson2Map(String json)
	{
		if (null == json)
		{
			LogTools.e(LOG_TAG, "json can't be empty!");
			return null;
		}
		return JSON.parseObject(json, new TypeReference<HashMap<String, Object>>()
		{
		});
	}

	/**
	 * 该方法的作用:Object转Json
	 * 
	 * @date 2014年4月16日
	 * @param object
	 * @return
	 */
	public static String parseObject2Json(Object object)
	{
		if (null == object)
		{
			LogTools.e(LOG_TAG, "object can't be empty!");
			return null;
		}
		return JSON.toJSONString(object);
	}

	/**
	 * 该方法的作用:Json转Object
	 * 
	 * @date 2014年4月16日
	 * @param json
	 * @param mClass
	 *            类
	 * @return
	 */
	public static <T> T parseJson2Object(String json, Class<T> mClass)
	{
		if (null == json)
		{
			LogTools.e(LOG_TAG, "json can't be empty!");
			return null;
		}
		if (null == mClass)
		{
			LogTools.e(LOG_TAG, "mClass can't be empty!");
			return null;
		}
		return JSON.parseObject(json, mClass);
	}

	/**
	 * 该方法的作用:Json转Object
	 * 
	 * @date 2014年4月16日
	 * @param json
	 * @param className
	 *            类名,格式必须为：com.example.atest.entity.JsonTestW3Bean
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseJson2Object(String json, String className)
	{
		if (null == json)
		{
			LogTools.e(LOG_TAG, "json can't be empty!");
			return null;
		}
		if (null == className)
		{
			LogTools.e(LOG_TAG, "className can't be empty!");
			return null;
		}
		try
		{
			Class<T> mClass = (Class<T>) Class.forName(className);
			if (null != mClass)
			{
				return JSON.parseObject(json, mClass);
			}
			else
			{
				LogTools.e(LOG_TAG, "mClass can't be empty!");
				return null;
			}
		}
		catch (ClassNotFoundException e)
		{
			LogTools.e(LOG_TAG, className + " can't found!");
		}
		return null;
	}

}
