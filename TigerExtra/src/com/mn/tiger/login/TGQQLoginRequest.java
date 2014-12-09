package com.mn.tiger.login;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class TGQQLoginRequest extends TGLoginRequest
{
	public static final String SCOPE_ALL = "all";
	
	private IUiListener uiListener;
	
	private Tencent tencent;
	
	private ILoginCallback callback;
	
	public TGQQLoginRequest(Activity activity, String appID)
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
					TGLoginResult loginResult = new TGLoginResult();
					loginResult.setLoginID(((JSONObject)reponse).getString("openid"));
					loginResult.setAccessToken(((JSONObject)reponse).getString("access_token"));
					callback.onSuccess(loginResult);
				}
				catch (JSONException e)
				{
					//数据解析出错，登录失败
					callback.onError(0, "", "");
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
	public void execute(ILoginCallback callback)
	{
		tencent.login(getActivity(), SCOPE_ALL, uiListener);
	}

}
