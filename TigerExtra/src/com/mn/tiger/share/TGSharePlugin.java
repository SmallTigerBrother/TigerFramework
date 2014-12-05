package com.mn.tiger.share;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

import com.mn.tiger.share.result.TGShareResult;

/**
 * 分享插件
 * @param <T> 分享数据类型
 */
public abstract class TGSharePlugin<T, H extends TGShareResult>
{
	private TGShareMsgFactory<T> msgFactory;
	
	private T shareMsg;
	
	private Context context;
	
	private ConcurrentHashMap<String, IShareResultHandler<H>> resultHandlderMap;
	
	private ConcurrentHashMap<String, Integer> shareTypeMap;
	
	public TGSharePlugin(Context context)
	{
		this.context = context;
		this.resultHandlderMap = new ConcurrentHashMap<String, IShareResultHandler<H>>();
		this.shareTypeMap = new ConcurrentHashMap<String, Integer>();
	}
	
	public synchronized <E extends TGShareMsgFactory<T>> void share(E msgFactory, 
			IShareResultHandler<H> handler)
	{
		shareTypeMap.put(getMsgIndicator(getShareMsg()), msgFactory.getShareType());
		resultHandlderMap.put(getMsgIndicator(getShareMsg()), handler);
		
		sendShareMsg(getShareMsg());
	}
	
	protected abstract void sendShareMsg(T shareMsg);
	
	/**
	 * 接收分享结果
	 * @param result 分享结果
	 */
	protected final void handleShareResult(H result)
	{
		IShareResultHandler<H> handler = getShareResultHandler(result);
		if(null != handler)
		{
			handler.handleShareResult(result);
		}
		
		if(isShareSuccess(result))
		{
			onShareSuccess(result);
		}
		else
		{
			onShareFailed(result);
		}
		
		onShareOver(result);
	}
	
	protected abstract boolean isShareSuccess(H result);
	
	protected final IShareResultHandler<H> getShareResultHandler(H result)
	{
		return resultHandlderMap.get(getMsgIndicator(result));
	}
	
	protected T getShareMsg()
	{
		if(null == shareMsg && null != msgFactory)
		{
			shareMsg = msgFactory.createShareMsg();
		}
		
		return shareMsg;
	}
	
	protected abstract String getMsgIndicator(T shareMsg);
	
	protected abstract String getMsgIndicator(H shareResult);

	protected final void registerResultHandler(IShareResultHandler<H> resultHandler)
	{
		
	}
	
	public void removeResultHandler(IShareResultHandler<H> resultHandler)
	{
		Iterator<Entry<String, IShareResultHandler<H>>> iterator = 
				resultHandlderMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			if(resultHandler.equals(iterator.next().getValue()))
			{
				iterator.remove();
				break;
			}
		}
	}
	
	/**
	 * 分享成功回调（不可添加操作界面相关的代码）
	 * @param result 分享结果
	 */
	public abstract void onShareSuccess(H result);
	
	/**
	 * 分享失败回调（不可添加操作界面相关的代码）
	 * @param result 分享结果
	 */
	public abstract void onShareFailed(H result);
	
	/**
	 * 分享完成，不论分享成功或失败都会回调
	 */
	public abstract void onShareOver(H result);
	
	public Context getContext()
	{
		return context;
	}
	
	public TGShareMsgFactory<T> getMsgFactory()
	{
		return msgFactory;
	}
	
	public static abstract class TGShareMsgFactory<T>
	{
		/**
		 * 分享类型
		 */
		private int shareType;
		
		/**
		 * 分享信息建造者
		 * @param shareType 分享类型，为确保相同分享渠道的不同分享信息之间无冲突，
		 *                  不同界面的相同分享方法，请定义不同的shareType
		 */
		public TGShareMsgFactory(int shareType)
		{
			this.shareType = shareType;
		}
		
		public int getShareType()
		{
			return shareType;
		}
		
		public abstract T createShareMsg();
	}
}
