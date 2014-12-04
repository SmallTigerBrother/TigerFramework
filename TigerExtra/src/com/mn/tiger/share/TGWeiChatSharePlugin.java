package com.mn.tiger.share;

import android.content.Context;

import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;

public class TGWeiChatSharePlugin extends TGSharePlugin<WXMediaMessage>
{
	public TGWeiChatSharePlugin(Context context)
	{
		super(context);
	}
	
	public String getAppID()
	{
		if(getMsgBuilder() instanceof TGWeiChatShareMsgBuilder)
		{
			return ((TGWeiChatShareMsgBuilder)getMsgBuilder()).getAppID();
		}
		else
		{
			throw new IllegalArgumentException("the ShareMsgBuilder must extends TGWeiChatShareMsgBuilder");
		}
	}
	
	@Override
	protected void sendShareMsg(WXMediaMessage shareMsg)
	{
		// 初始化微信api
		IWXAPI api = WXAPIFactory.createWXAPI(getContext(), getAppID());
		api.registerApp(getAppID());

		// 发送媒体消息
		api.sendReq(initReq());
	}
	
	protected SendMessageToWX.Req initReq()
	{
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = getShareMsg();
		req.scene = SendMessageToWX.Req.WXSceneSession;
		
		return req;
	}

	@Override
	public void onShareOver()
	{
		
	}
	
	@Override
	public void onShareSuccess(TGWeiChatShareResult result)
	{
		
	}

	@Override
	public void onShareFailed(TGWeiChatShareResult result)
	{
		
	}
	
	public abstract class TGWeiChatShareMsgBuilder extends TGShareMsgBuilder<WXMediaMessage>
	{
		private String appID = "";
		
		public TGWeiChatShareMsgBuilder(int shareType)
		{
			super(shareType);
		}
		
		public String getAppID()
		{
			return appID;
		}

		public void setAppID(String appID)
		{
			this.appID = appID;
		}
	}
}
