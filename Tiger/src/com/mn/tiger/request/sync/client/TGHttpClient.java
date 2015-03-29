package com.mn.tiger.request.sync.client;

import android.content.Context;

import com.mn.tiger.request.sync.method.TGHttpMethod;
import com.mn.tiger.request.sync.receiver.TGHttpReceiver;
import com.mn.tiger.request.sync.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * 网络连接执行类
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1
 */
public abstract class TGHttpClient 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 运行环境
	 */
	private Context context;
	
	/**
	 * @date 2013-12-1
	 * 构造函数
	 * @param context
	 */
	public TGHttpClient(Context context) 
	{
		this.context = context;
	}
	
	/**
	 * 该方法的作用:
	 * 获取Context
	 * @date 2013-12-1
	 * @return
	 */
	public Context getContext() 
	{
		return context;
	}
	
	/**
	 * 该方法的作用:
	 * 执行网络请求
	 * @date 2013-12-1
	 * @param httpMethod 网络请求方法
	 * @param httpReceiver 网络请求接收器，处理请求结果
	 * @return
	 */
	public abstract TGHttpResult executeHttpMethod(TGHttpMethod httpMethod, TGHttpReceiver httpReceiver);
	
}