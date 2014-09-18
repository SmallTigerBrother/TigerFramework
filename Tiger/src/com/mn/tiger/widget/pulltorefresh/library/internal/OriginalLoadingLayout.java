package com.mn.tiger.widget.pulltorefresh.library.internal;

import android.content.Context;
import android.widget.FrameLayout;


/**
 *
 * 该类作用及功能说明
 * 
 * @author nKF50342
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-4-6
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */

public abstract class OriginalLoadingLayout extends FrameLayout{

	
	/**
	 * @author nKF50342
	 * @date 2012-4-6
	 * 构造函数
	 * @param context
	 */
	
	public OriginalLoadingLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * 该方法的作用:接就刷新接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void pullToRefresh();
	/**
	 * 
	 * 该方法的作用:正在刷新接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void refreshing();
	/**
	 * 
	 * 该方法的作用:刷新前接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void releaseToRefresh();
	/**
	 * 
	 * 该方法的作用:重置接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void reset();
	/**
	 * 
	 * 该方法的作用:设置刷新中文本接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void setRefreshingLabel(String refreshingLabel) ;
	/**
	 * 
	 * 该方法的作用:设置拉的过程中文本接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void setPullLabel(String pullLabel);
	/**
	 * 
	 * 该方法的作用:设置释放文本接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void setReleaseLabel(String releaseLabel);
	/**
	 * 
	 * 该方法的作用:设置第二个文本接口
	 * 在什么情况下调用:
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public abstract void setDownTextLabel(String textLabel);
}
