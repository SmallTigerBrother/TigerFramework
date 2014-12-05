package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.share.result.TGWeiChatShareResult;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

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
	
	public static class TGWeiChatMsgBuilder extends TGShareMsgBuilder<WXMediaMessage>
	{
		private String title;
		
		private String text;
		
		private String description;
		
		private String webpageUrl;
		
		private String imagePath;
		
		public TGWeiChatMsgBuilder(int shareType)
		{
			super(shareType);
		}

		@Override
		public WXMediaMessage build()
		{
			WXMediaMessage mediaMessage = new WXMediaMessage();
			mediaMessage.title = title;
			mediaMessage.description = description;
			if(!TextUtils.isEmpty(webpageUrl))
			{
				WXWebpageObject webpageObject = new WXWebpageObject(webpageUrl);
				mediaMessage.mediaObject = webpageObject;
			}
			else if(!TextUtils.isEmpty(text))
			{
				WXTextObject textObject = new WXTextObject(text);
				mediaMessage.mediaObject = textObject;
			}
			else if(!TextUtils.isEmpty(imagePath))
			{
				WXImageObject imageObject = new WXImageObject();
				imageObject.imagePath = imagePath;
				mediaMessage.mediaObject = imageObject;
			}
			
			return mediaMessage;
		}
		
		public void setTitle(String title)
		{
			this.title = title;
		}
		
		public void setDescription(String description)
		{
			this.description = description;
		}
		
		public void setText(String text)
		{
			this.text = text;
		}
		
		public void setWebpageUrl(String webpageUrl)
		{
			this.webpageUrl = webpageUrl;
		}
	}
}
