package com.mn.tiger.app;

import android.app.Activity;

/**
 * 该类作用及功能说明
 * 
 * @date 2014年5月23日
 */
public interface IApplication
{
	/**
	 * 该方法的作用:
	 * 添加Activity至终止栈中
	 * @date 2014年5月23日
	 * @param activity
	 */
	void addActivityToStack(Activity activity);
	
	/**
	 * 该方法的作用:
	 * 从终止栈中移除Activity
	 * @date 2014年5月23日
	 * @param activity
	 */
	void removeActivityFromStack(Activity activity);
	
	/**
	 * 该方法的作用:
	 * 完全退出应用
	 * @date 2014年5月23日
	 */
	void exit();
	
	/**
	 * 该方法的作用:
	 * 终止当前所有的Activity
	 * @date 2014年5月23日
	 */
	void finishAllActivity();
}
