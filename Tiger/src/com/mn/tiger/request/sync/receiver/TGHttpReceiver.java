package com.mn.tiger.request.sync.receiver;


import com.mn.tiger.request.sync.method.TGHttpMethod;

/**
 * 该类作用及功能说明
 * Http请求接收类，处理请求结果
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1
 */
public abstract class TGHttpReceiver 
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 该方法的作用:
	 * 处理请求结果
	 * @date 2013-12-1
	 * @param httpMethod
	 * @param httpResult
	 * @return
	 */
	public abstract TGHttpResult receiveHttpResult(TGHttpMethod httpMethod, 
			TGHttpResult httpResult);
}
