package com.mn.tiger.share;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mn.tiger.log.Logger;
import com.mn.tiger.share.result.TGQQShareResult;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class TGQQSharePlugin extends TGSharePlugin<Bundle, TGQQShareResult> implements IUiListener
{
	private static final Logger LOG = Logger.getLogger(TGQQSharePlugin.class);
	
	private static final String indicatorKey = "indicator";
	
	private Tencent tencent;
	
	private Activity activity;
	
	public TGQQSharePlugin(Context context, String appID)
	{
		super(context, appID);
	}
	
	@Override
	protected void registerApp()
	{
		tencent = Tencent.createInstance(getAppID(), getContext());
	}
	
	@Override
	protected void sendShareMsg(Activity activity, Bundle shareMsg)
	{
		this.activity = activity;
		Intent intent = new Intent(activity, TGQQEntryActivity.class);
		activity.startActivity(intent);
	}
	
	public void share2QQ()
	{
		if(null != getShareMsg())
		{
			tencent.shareToQQ(activity, getShareMsg(), null);
		}
		//清空actvity，避免内存泄露
		this.activity = null;
	}

	@Override
	protected String getMsgIndicator(Bundle shareMsg)
	{
		return indicatorKey;
	}

	@Override
	protected String getMsgIndicator(TGQQShareResult shareResult)
	{
		return indicatorKey;
	}
	
	protected Tencent getTencent()
	{
		return tencent;
	}

	@Override
	public void onShareSuccess(TGQQShareResult result)
	{
		
	}

	@Override
	public void onShareFailed(TGQQShareResult result)
	{
		
	}

	@Override
	public void onShareOver(TGQQShareResult result)
	{
		
	}

	@Override
	public void onCancel()
	{
		JSONObject response = new JSONObject();
		try
		{
			response.put("ret", TGQQShareResult.USER_CANCEL);
		}
		catch (JSONException e)
		{
			LOG.e(e);
		}
		
		handleShareResult(new TGQQShareResult(response));
	}

	@Override
	public void onComplete(Object response)
	{
		handleShareResult(new TGQQShareResult(response));
	}

	@Override
	public void onError(UiError uiError)
	{
		handleShareResult(new TGQQShareResult(uiError));
	}
}
