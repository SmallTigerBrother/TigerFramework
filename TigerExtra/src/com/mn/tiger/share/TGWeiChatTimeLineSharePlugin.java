package com.mn.tiger.share;

import android.content.Context;

import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.SendMessageToWX.Req;

public class TGWeiChatTimeLineSharePlugin extends TGWeiChatSharePlugin
{
	public TGWeiChatTimeLineSharePlugin(Context context)
	{
		super(context);
	}
	
	@Override
	protected Req initReq()
	{
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = getShareMsg();
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		return req;
	}

}
