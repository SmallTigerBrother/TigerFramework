package com.mn.tiger.share;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.share.result.TGWeChatShareResult;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

/**
 * 微信分享插件
 */
public class TGWeChatSharePlugin extends TGSharePlugin<WXMediaMessage, TGWeChatShareResult>
{
	/**
	 * 微信API
	 */
	private IWXAPI api;
	
	/**
	 * 信息请求类
	 */
	private SendMessageToWX.Req req;
	
	public TGWeChatSharePlugin(Context context, String appID)
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
		LOG.d("[Method:sendShareMsg]" + shareMsg.toString());
		
		// 发送媒体消息
		req = initReq();
		api.sendReq(req);
	}
	
	/**
	 * 初始化请求类
	 * @return
	 */
	protected SendMessageToWX.Req initReq()
	{
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = getShareMsg();
		req.scene = SendMessageToWX.Req.WXSceneSession;
		
		return req;
	}

	@Override
	public void onShareSuccess(TGWeChatShareResult result)
	{
		
	}

	@Override
	public void onShareFailed(TGWeChatShareResult result)
	{
		
	}

	@Override
	public void onShareOver(TGWeChatShareResult result)
	{
		
	}
	
	/**
	 * 获取微信分享API
	 * @return
	 */
	protected IWXAPI getIWXApi()
	{
		return api;
	}

	@Override
	protected String getMsgIndicator(WXMediaMessage shareMsg)
	{
		return req.transaction;
	}
	
	@Override
	protected String getMsgIndicator(TGWeChatShareResult shareResult)
	{
		return shareResult.getTransaction();
	}
	
	/**
	 * 获取微信分享信息建造者
	 */
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
			
			if(!TextUtils.isEmpty(webpageUrl))
			{
				mediaMessage.title = title;
				mediaMessage.description = description;
				WXWebpageObject webpageObject = new WXWebpageObject(webpageUrl);
				mediaMessage.mediaObject = webpageObject;
			}
			else if(!TextUtils.isEmpty(text))
			{
				WXTextObject textObject = new WXTextObject(text);
				if(!TextUtils.isEmpty(description))
				{
					mediaMessage.description = description;
				}
				else
				{
					mediaMessage.description = text;
				}
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
