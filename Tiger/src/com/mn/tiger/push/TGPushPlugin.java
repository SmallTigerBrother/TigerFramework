package com.mn.tiger.push;

/**
 * 消息推送插件
 */
public abstract class TGPushPlugin
{
	/**
	 * 注册到PluginManager时的回调方法
	 */
	public abstract void onRegister();
	
	/**
	 * 启动服务时的回调方法
	 */
	public abstract void onStartPushService();
	
	/**
	 * 接收消息时的回调方法，在此方法中处理消息
	 * @param pushMessage
	 */
	public abstract void onRecieve(Object pushMessage);
	
	/**
	 * 请求绑定推送服务
	 */
	public abstract void requestPushBinding();
}
