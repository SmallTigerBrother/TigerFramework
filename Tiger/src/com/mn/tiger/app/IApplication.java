package com.mn.tiger.app;

import android.app.Activity;

/**
 * 该类作用及功能说明
 * 
 * @author l00220455
 * @date 2014年5月23日
 */
public interface IApplication
{
	/**
	 * 该方法的作用:
	 * 添加Activity至终止栈中
	 * @author l00220455
	 * @date 2014年5月23日
	 * @param activity
	 */
	void addActivityToStack(Activity activity);
	
	/**
	 * 该方法的作用:
	 * 从终止栈中移除Activity
	 * @author l00220455
	 * @date 2014年5月23日
	 * @param activity
	 */
	void removeActivityFromStack(Activity activity);
	
	/**
	 * 该方法的作用:
	 * 完全退出应用
	 * @author l00220455
	 * @date 2014年5月23日
	 */
	void exit();
	
	/**
	 * 该方法的作用:
	 * 终止当前所有的Activity
	 * @author l00220455
	 * @date 2014年5月23日
	 */
	void finishAllActivity();
}
