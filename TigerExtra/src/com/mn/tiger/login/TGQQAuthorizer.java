package com.mn.tiger.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class TGQQAuthorizer extends TGAuthorizer
{
	public static final String SCOPE_ALL = "all";
	
	private IUiListener uiListener;
	
	private Tencent tencent;
	
	private IAuthorizeCallback callback;
	
	public TGQQAuthorizer(Activity activity, String appID)
	{
		super(activity, appID);
		tencent = Tencent.createInstance(appID, activity);
		
		uiListener = new IUiListener()
		{
			@Override
			public void onError(UiError uiError)
			{
				callback.onError(uiError.errorCode, uiError.errorMessage, uiError.errorDetail);
			}
			
			@Override
			public void onComplete(Object reponse)
			{
				try
				{
					TGAuthorizeResult loginResult = new TGAuthorizeResult();
					loginResult.setUID(((JSONObject)reponse).getString("openid"));
					loginResult.setAccessToken(((JSONObject)reponse).getString("access_token"));
					callback.onSuccess(loginResult);
				}
				catch (JSONException e)
				{
					//数据解析出错，登录失败
					callback.onError(0, "认证失败！", "认证失败！");
				}
			}
			
			@Override
			public void onCancel()
			{
				callback.onCancel();
			}
		};
	}
	
	@Override
	public void authorize(IAuthorizeCallback callback)
	{
		tencent.login(getActivity(), SCOPE_ALL, uiListener);
	}
	
	@Override
	public void logout()
	{
		tencent.logout(getActivity());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		tencent.onActivityResult(requestCode, resultCode, data);
	}
}
