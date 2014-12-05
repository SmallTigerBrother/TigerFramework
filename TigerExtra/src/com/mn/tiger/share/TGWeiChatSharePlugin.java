package com.mn.tiger.share;

import android.content.Context;

import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

public class TGWeiChatSharePlugin extends TGSharePlugin<WXMediaMessage, TGWeiChatShareResult>
{
	private String appID = "";
	
	private SendMessageToWX.Req req;
	
	public TGWeiChatSharePlugin(Context context, String appID)
	{
		super(context);
		this.appID = appID;
	}
	
	@Override
	protected void sendShareMsg(WXMediaMessage shareMsg)
	{
		// 初始化微信api
		IWXAPI api = WXAPIFactory.createWXAPI(getContext(), appID);
		api.registerApp(appID);

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
	protected boolean isShareSuccess(TGWeiChatShareResult result)
	{
		return result.getShareType() == BaseResp.ErrCode.ERR_OK;
	}

	@Override
	protected String getMsgIndicator(WXMediaMessage shareMsg)
	{
		return req.transaction;
	}

	@Override
	protected String getMsgIndicator(TGWeiChatShareResult shareResult)
	{
		return shareResult.getTransaction();
	}
	
	public String getAppID()
	{
		return appID;
	}
}
