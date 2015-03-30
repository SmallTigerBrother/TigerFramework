package com.mn.tiger.demo.authorize;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.authorize.IAuthorizeCallback;
import com.mn.tiger.authorize.ILogoutCallback;
import com.mn.tiger.authorize.IRegisterCallback;
import com.mn.tiger.authorize.TGAuthorizeResult;
import com.mn.tiger.authorize.TGAuthorizer;
import com.mn.tiger.demo.R;
import com.mn.tiger.demo.request.HttpLoader;
import com.mn.tiger.demo.request.SimpleLoadCallback;
import com.mn.tiger.request.sync.receiver.TGHttpResult;
import com.mn.tiger.utility.MD5;
import com.mn.tiger.utility.StringUtils;

/**
 * 登录认证类
 */
public class MediaAuthorizer extends TGAuthorizer
{
	/**
	 * 请求发送验证码
	 */
	private static final String SEND_CODE = "/mobile/sendCode";

	/**
	 * 校验手机验证码
	 */
	private static final String CHECK_CODE = "/mobile/checkCode";

	/**
	 * 用户注册url
	 */
	private static final String USER_REGISTER = "/user/register";

	/**
	 * 用户登录url
	 */
	private static final String USER_LOGIN = "/user/login";

	/**
	 * 用户注销url
	 */
	private static final String USER_LOGOUT = "/user/logout";
	
	/**
	 * @param activity
	 * @param account 手机号（用户名）
	 * @param password 密码
	 */
	public MediaAuthorizer(Activity activity,String account, String password)
	{
		super(activity, account, password);
	}
	
	@Override
	protected void executeAuthorize(final IAuthorizeCallback callback)
	{
		// 设置登录参数
		HttpLoader<UserInfo> httpLoader = new HttpLoader<UserInfo>();
		try
		{
			long currentTime = System.currentTimeMillis();
			httpLoader.addRequestParam("mobile", account);
			httpLoader.addRequestParam("digest", MD5.toHexString((password + currentTime).getBytes()));
			httpLoader.addRequestParam("time", currentTime + "");
		}
		catch (Exception e)
		{
			if (null != callback)
			{
				callback.onError(AUTHORIZE_ERROR_PASSWORD_ENCRYPT, e.getMessage(), "");
			}
			return;
		}

		// 请求登录
		httpLoader.loadByPost(getActivity(), USER_LOGIN, UserInfo.class,
				new SimpleLoadCallback<UserInfo>(getActivity())
				{
					@Override
					public void onLoadSuccess(UserInfo result, TGHttpResult httpResult)
					{
						if (null != result)
						{
							saveUserInfo(getActivity(), result);
							// 登录成功
							if (null != callback)
							{
								// 调用回调方法
								TGAuthorizeResult authorizeResult = new TGAuthorizeResult();
								authorizeResult.setAccessToken(result.getAccessToken());
								authorizeResult.setUID(result.getUid());
								callback.onSuccess(authorizeResult);
							}
						}
						else
						{
							// TODO 处理登录异常
						}
					}
				});
	}

	@Override
	protected void executeLogout(final ILogoutCallback callback)
	{
		// 执行注销
		HttpLoader<Void> httpLoader = new HttpLoader<Void>();
		httpLoader.addRequestParam("accessToken", getUserInfo(getActivity()).getAccessToken());
		httpLoader.loadByPost(getActivity(), USER_LOGOUT, Void.class,
				new SimpleLoadCallback<Void>(getActivity())
				{
					@Override
					public void onLoadSuccess(Void result, TGHttpResult httpResult)
					{
						if (null != callback)
						{
							callback.onSuccess();
						}
					}
				});
	}
	
	/**
	 * args[0]为code（验证码），args[1]为nickName（昵称），arigs[2]为male（性别）
	 */
	@Override
	public void register(String account, String password, final IRegisterCallback callback,
			Object... args)
	{
		// 设置注册参数
				HttpLoader<UserInfo> httpLoader = new HttpLoader<UserInfo>();
				httpLoader.addRequestParam("mobile", account);
				httpLoader.addRequestParam("password", password);
				httpLoader.addRequestParam("code", args[0].toString());
				httpLoader.addRequestParam("nickName", args[1].toString());
				httpLoader.addRequestParam("male", args[2] + "");

				// 请求注册
				httpLoader.loadByPost(getActivity(), USER_REGISTER, UserInfo.class,
						new SimpleLoadCallback<UserInfo>(getActivity())
						{
							@Override
							public void onLoadSuccess(UserInfo result, TGHttpResult httpResult)
							{
								if (null != result)
								{
									saveUserInfo(getActivity(), result);
									// 注册成功
									if (null != callback)
									{
										// 调用回调方法
										TGAuthorizeResult authorizeResult = new TGAuthorizeResult();
										authorizeResult.setAccessToken(result.getAccessToken());
										authorizeResult.setUID(result.getUid());
										callback.onSuccess(authorizeResult);
									}
								}
								else
								{
									// TODO 处理注册异常
								}
							}
						});
	}
	
	/**
	 * 请求发送手机验证码
	 * @param mobile 手机号
	 */
	public void requestVerificationCode(String mobile, final IVertificationCodeCallback callback)
	{
		if(TextUtils.isEmpty(mobile) || !StringUtils.isPhoneNumber(mobile))
		{
			if(null != callback)
			{
				callback.onError(CODE_ERROR_INVALID_MOBILE, 
						getActivity().getString(R.string.please_input_valid_mobile));
			}
			return;
		}
		
		HttpLoader<Void> httpLoader = new HttpLoader<Void>();
		httpLoader.addRequestParam("mobile", mobile);
		httpLoader.loadByPost(getActivity(), SEND_CODE, Void.class, 
				new SimpleLoadCallback<Void>(getActivity())
		{
			@Override
			public void onLoadSuccess(Void result, TGHttpResult httpResult)
			{
				if(null != callback)
				{
					callback.onSuccess();
				}
			}
		});
	}
	
	/**
	 * 校验手机验证码
	 * @param code 验证码
	 */
	public void checkVertificationCode(String mobile, String code, 
			final IVertificationCodeCallback callback)
	{
		if(TextUtils.isEmpty(mobile))
		{
			if(null != callback)
			{
				callback.onError(CODE_ERROR_INVALID_MOBILE, 
						getActivity().getString(R.string.please_input_valid_mobile));
			}
			return;
		}
		
		if(TextUtils.isEmpty(code))
		{
			if(null != callback)
			{
				callback.onError(CODE_ERROR_INVALID_CODE_FORMAT, 
						getActivity().getString(R.string.code_format_error));
			}
			return;
		}
		
		HttpLoader<Void> httpLoader = new HttpLoader<Void>();
		httpLoader.addRequestParam("mobile", mobile);
		httpLoader.addRequestParam("code", code);
		httpLoader.loadByPost(getActivity(), CHECK_CODE, Void.class, 
				new SimpleLoadCallback<Void>(getActivity())
		{
			@Override
			public void onLoadSuccess(Void result, TGHttpResult httpResult)
			{
				if(null != callback)
				{
					callback.onSuccess();
				}
			}
		});
	}
	
	/**
	 * 获取用户信息
	 * @param context
	 * @return
	 */
	public static UserInfo getUserInfo(Context context)
	{
		return (UserInfo) TGAuthorizer.getUserInfo(context);
	}
	
	/**
	 * 获取AccessToken
	 */
	public static String getAccessToken(Context context)
	{
		UserInfo userInfo = getUserInfo(context);
		if(null != userInfo)
		{
			return userInfo.getAccessToken();
		}
		
		return "";
	}
	
	/**
	 * 请求验证码回调方法
	 */
	public static interface IVertificationCodeCallback
	{
		/**
		 * 请求成功
		 */
		void onSuccess();
		
		/**
		 * 请求出错
		 * @param code
		 * @param message
		 * @param detail
		 */
		void onError(int code, String message);
	}

}
