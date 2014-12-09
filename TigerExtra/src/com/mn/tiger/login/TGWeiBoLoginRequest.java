package com.mn.tiger.login;

import android.app.Activity;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class TGWeiBoLoginRequest extends TGLoginRequest
{
	public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	
	public static Oauth2AccessToken accessToken;
	
	private SsoHandler ssoHandler;
	
	private WeiboAuthListener authListener;
	
	private ILoginCallback callback;
	
	public TGWeiBoLoginRequest(Activity activity, String appID)
	{
		super(activity, appID);
		AuthInfo authInfo = new AuthInfo(activity, appID, REDIRECT_URL, SCOPE);
		ssoHandler = new SsoHandler(activity, authInfo);
		authListener = new AuthorListener();
	}

	@Override
	public void execute(ILoginCallback callback)
	{
		this.callback = callback;
		ssoHandler.authorize(authListener);
	}
	
	protected SsoHandler getSsoHandler()
	{
		return ssoHandler;
	}
	
	protected WeiboAuthListener getAuthListener()
	{
		return authListener;
	}
	
	private class AuthorListener implements WeiboAuthListener
	{
		@Override
		public void onCancel()
		{
			if(null != callback)
			{
				callback.onCancel();
			}
		}

		@Override
		public void onComplete(Bundle response)
		{
			if(null != callback)
			{
				Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(response);
				if(accessToken.isSessionValid())
				{
					TGWeiBoLoginRequest.accessToken = accessToken;
					TGLoginResult loginResult = new TGLoginResult();
					loginResult.setLoginID(accessToken.getUid());
					loginResult.setAccessToken(accessToken.getToken());
					callback.onSuccess(loginResult);
				}
				else
				{
					callback.onError(Integer.valueOf(response.getString("code")), "授权失败！",
							"// 以下几种情况，您会收到 Code：\n" + "// 1. 当您未在平台上注册的应用程序的包名与签名时；\n" +
					    "// 2. 当您注册的应用程序包名与签名不正确时；" + "// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。");
				}
			}
		}

		@Override
		public void onWeiboException(WeiboException exception)
		{
			if(null != callback)
			{
				callback.onError(0, exception.getMessage(), exception.getMessage());
			}
		}
	}

}
