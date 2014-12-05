package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;

import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

public class TGWeiChatSharePlugin extends TGSharePlugin<WXMediaMessage, TGWeiChatShareResult>
{
	private IWXAPI api;
	
	private SendMessageToWX.Req req;
	
	public TGWeiChatSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void registerApp()
	{
		// 初始化微信api
		api = WXAPIFactory.createWXAPI(getContext(), getAppID());
		api.registerApp(getAppID());
	}
	
	@Override
	protected void sendShareMsg(Activity activity, WXMediaMessage shareMsg)
	{
		// 发送媒体消息
		api.sendReq(initReq());
	}
	
	protected SendMessageToWX.Req initReq()
	{
		req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = getShareMsg();
		req.scene = SendMessageToWX.Req.WXSceneSession;
		
		return req;
	}

	@Override
	public void onShareSuccess(TGWeiChatShareResult result)
	{
		
	}

	@Override
	public void onShareFailed(TGWeiChatShareResult result)
	{
		
	}

	@Override
	public void onShareOver(TGWeiChatShareResult result)
	{
		
	}
	
	@Override
	protected String getMsgIndicator(WXMediaMessage shareMsg)
	{
		return req.transaction;
	}
	
	protected IWXAPI getIWXApi()
	{
		return api;
	}

	@Override
	protected String getMsgIndicator(TGWeiChatShareResult shareResult)
	{
		return shareResult.getTransaction();
	}
}
