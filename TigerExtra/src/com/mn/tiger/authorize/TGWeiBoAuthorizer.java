package com.mn.tiger.authorize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.utils.LogUtil;

public class TGWeiBoAuthorizer extends TGAuthorizer
{
	/**
	 * 登录认证地址
	 */
	private static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
	
	 /** 
	  * 注销地址（URL）
	  */
    private static final String REVOKE_OAUTH_URL = "https://api.weibo.com/oauth2/revokeoauth2";
	
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
	
	/**
	 * HTTP 参数 
	 */
	protected static final String KEY_ACCESS_TOKEN = "access_token";
	
	public static Oauth2AccessToken accessToken;
	
	private SsoHandler ssoHandler;
	
	private WeiboAuthListener authListener;
	
	private IAuthorizeCallback callback;
	
	public TGWeiBoAuthorizer(Activity activity, String appID)
	{
		super(activity, appID);
		AuthInfo authInfo = new AuthInfo(activity, appID, REDIRECT_URL, SCOPE);
		ssoHandler = new SsoHandler(activity, authInfo);
		authListener = new AuthorListener();
	}

	@Override
	public void authorize(IAuthorizeCallback callback)
	{
		this.callback = callback;
		ssoHandler.authorize(authListener);
	}
	
	@Override
	public void logout()
	{
		if (null == accessToken)
		{
			LogUtil.e("TGWeiBoLoginRequest", "Argument error!");
			return;
		}

		WeiboParameters parameters = new WeiboParameters(getAppID());
		parameters.put(KEY_ACCESS_TOKEN, accessToken.getToken());
		new AsyncWeiboRunner(getActivity()).requestAsync(REVOKE_OAUTH_URL, parameters, 
				"post", null);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		ssoHandler.authorizeCallBack(requestCode, resultCode, data);
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
					TGWeiBoAuthorizer.accessToken = accessToken;
					TGAuthorizeResult loginResult = new TGAuthorizeResult();
					loginResult.setUID(accessToken.getUid());
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
