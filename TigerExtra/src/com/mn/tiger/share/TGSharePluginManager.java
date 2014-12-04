package com.mn.tiger.share;

import java.util.concurrent.ConcurrentHashMap;

import com.mn.tiger.app.TGApplication;
import com.mn.tiger.log.Logger;
import com.mn.tiger.share.result.TGShareResult;
import com.mn.tiger.share.result.TGWeiChatShareResult;

/**
 * 分享插件管理器
 */
public class TGSharePluginManager
{
	private static final Logger LOG = Logger.getLogger(TGWXEntryActivity.class);
	
	public static final int TAG_WEI_CHAT = 1234;
	
	/**
	 * 插件map
	 */
	@SuppressWarnings("rawtypes")
	private ConcurrentHashMap<Integer, TGSharePlugin> pushPluginMap;
	
	/**
	 * 插件管理器单例
	 */
	private static TGSharePluginManager sharePluginManager;
	
	/**
	 * 获取单例对象
	 * @return
	 */
	public static synchronized TGSharePluginManager getInstance()
	{
		if(null == sharePluginManager)
		{
			sharePluginManager = new TGSharePluginManager();
		}
		
		return sharePluginManager;
	}
	
	@SuppressWarnings("rawtypes")
	private TGSharePluginManager()
	{
		pushPluginMap = new ConcurrentHashMap<Integer, TGSharePlugin>();
	}
	
	/**
	 * 注册插件
	 * @param tag 插件的tag，用于查找插件
	 * @param plugin 消息推送插件
	 */
	@SuppressWarnings("rawtypes")
	public void registerPlugin(Integer tag, TGSharePlugin plugin)
	{
		pushPluginMap.put(tag, plugin);
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
	@SuppressWarnings("rawtypes")
	public TGSharePlugin getPlugin(Integer tag)
	{
		return pushPluginMap.get(tag);
	}
	
	/**
	 * 发送分享结果
	 * @param shareResult
	 */
	@SuppressWarnings("rawtypes")
	public void postShareResult(int tag, TGShareResult shareResult)
	{
		TGSharePlugin plugin = getPlugin(tag);
		if(null != plugin)
		{
			//设置分享类型，用于定义多种分钟用途
			shareResult.setShareType(plugin.getMsgBuilder().getShareType());
			
			//调用分享结束方法
			plugin.shareOver((TGWeiChatShareResult)shareResult);
		}
		else
		{
			LOG.e("Your had not register this shareplugin that result type is "+ 
		        shareResult.getClass().getSimpleName() +" ever");
		}
		
		TGApplication.getBus().post(shareResult);
	}
	
	public void registerShareResultHandler(Object handler)
	{
		TGApplication.getBus().register(handler);
	}
	
	public void unregisterShareResultHandler(Object handler)
	{
		TGApplication.getBus().unregister(handler);
	}
}
