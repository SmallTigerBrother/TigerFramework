package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;

import com.mn.tiger.share.result.TGWeiboShareResult;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;

public class TGWeiboSharePlugin extends TGSharePlugin<WeiboMultiMessage, TGWeiboShareResult>
{
	private IWeiboShareAPI weiboShareAPI;
	
	private SendMultiMessageToWeiboRequest request;
	
	public TGWeiboSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void registerApp()
	{
		weiboShareAPI = WeiboShareSDK.createWeiboAPI(getContext(), getAppID());
		weiboShareAPI.registerApp();
	}

	@Override
	protected void sendShareMsg(Activity activity, WeiboMultiMessage shareMsg)
	{
		request = new SendMultiMessageToWeiboRequest();
		request.multiMessage = shareMsg;
		request.packageName = getContext().getPackageName();
		request.transaction = String.valueOf(System.currentTimeMillis());
		weiboShareAPI.sendRequest(activity, request);
	}

	@Override
	protected String getMsgIndicator(WeiboMultiMessage shareMsg)
	{
		return request.transaction;
	}

	@Override
	protected String getMsgIndicator(TGWeiboShareResult shareResult)
	{
		return shareResult.getTransaction();
	}

	@Override
	public void onShareSuccess(TGWeiboShareResult result)
	{
		
	}

	@Override
	public void onShareFailed(TGWeiboShareResult result)
	{
		
	}

	@Override
	public void onShareOver(TGWeiboShareResult result)
	{
		
	}
	
	protected IWeiboShareAPI getWeiboShareAPI()
	{
		return weiboShareAPI;
	}
}
