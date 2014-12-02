package com.mn.tiger.utility;

import java.lang.reflect.Field;

import android.app.Activity;
import android.view.View;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.log.LogTools;

/**
 * 视图注入工具类
 * @author za4480
 *
 */
public class ViewInjector
{
	/**
	 * 初始化注入视图
	 * @param injectedSource 需要注入的对象
	 * @param sourceView View基类
	 */
	public static void initInjectedView(Object injectedSource, View sourceView)
	{
		Field[] fields = injectedSource.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0)
		{
			for (Field field : fields)
			{
				ViewById viewById = field.getAnnotation(ViewById.class);
				if (viewById != null)
				{
					int viewId = viewById.id();
					try
					{
						field.setAccessible(true);
						field.set(injectedSource, sourceView.findViewById(viewId));
					}
					catch (Exception e)
					{
						LogTools.e("Can not find" + field.getName() + " by id " + viewId + 
								", please check if you was used wrong View");
					}
				}
			}
		}
	}
	
	/**
	 * 初始化注入视图
	 * @param injectedSource 需要注入的对象
	 * @param activity
	 */
	public static void initInjectedView(Object injectedSource, Activity activity)
	{
		initInjectedView(injectedSource, activity.getWindow().getDecorView());
	}
}
