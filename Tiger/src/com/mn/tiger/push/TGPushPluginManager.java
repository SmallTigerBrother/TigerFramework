package com.mn.tiger.push;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息推送插件管理器
 */
public class TGPushPluginManager
{
	/**
	 * 插件map
	 */
	private ConcurrentHashMap<String, TGPushPlugin> pushPluginMap;
	
	/**
	 * 插件管理器单例
	 */
	private static TGPushPluginManager pushPluginManager;
	
	public synchronized TGPushPluginManager getInstance()
	{
		if(null == pushPluginManager)
		{
			pushPluginManager = new TGPushPluginManager();
		}
		
		return pushPluginManager;
	}
	
	private TGPushPluginManager()
	{
		pushPluginMap = new ConcurrentHashMap<String, TGPushPlugin>();
	}
	
	/**
	 * 注册插件
	 * @param tag 插件的tag，用于查找插件
	 * @param plugin 消息推送插件
	 */
	public void registerPlugin(String tag, TGPushPlugin plugin)
	{
		pushPluginMap.put(tag, plugin);
		plugin.onRegister();
	}
	
	/**
	 * 取消注册插件
	 * @param tag 插件的tag，用于查找插件
	 */
	public void unregisterPlugin(String tag)
	{
		pushPluginMap.remove(tag);
	}
	
	/**
	 * 根据tag获取插件 
	 * @param tag 插件的tag，用于查找插件
	 * @return
	 */
	public TGPushPlugin getPlugin(String tag)
	{
		return pushPluginMap.get(tag);
	}
	
	/**
	 * 启动推送服务（请求绑定账号）
	 */
	public void startPushService()
	{
		Iterator<Entry<String, TGPushPlugin>> iterator = pushPluginMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Entry<String, TGPushPlugin> entry = iterator.next();
			entry.getValue().onStartPushService();
			entry.getValue().requestPushBinding();
		}
	}
	
	/**
	 * 接收推送消息
	 * @param tag 推送插件的Tag
	 * @param pushMsg 推送消息
	 */
	public void recievePushMessage(String tag, Object pushMsg)
	{
		TGPushPlugin pushPlugin = pushPluginMap.get(tag);
		if(null != pushPlugin)
		{
			pushPlugin.onRecieve(pushMsg);
		}
		else
		{
			throw new NullPointerException("You must register the pushplugin of " + tag + 
					" before recieve the message");
		}
	}
}
