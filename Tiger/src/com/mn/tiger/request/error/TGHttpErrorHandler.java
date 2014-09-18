package com.mn.tiger.request.error;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明:自定义错误处理类
 * 
 * @author l00220455
 * @date 2013-11-1
 */
public class TGHttpErrorHandler implements IHttpErrorHandler
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	private Context context;
	protected static final int EINFO = 0;
	protected static final int ECODE = 1;
	protected static final int POCODE = 7;

	/**
	 * 无数据的错误码
	 */
	protected static final int NODATA = 8;

	public TGHttpErrorHandler(Context dialogContext)
	{
		this.context = dialogContext;
	}

	@Override
	public boolean handleErrorInfo(TGHttpResult httpResult)
	{
		return false;
	}

	/**
	 * 该方法的作用:错误处理Info
	 * 
	 * @author lkf65210
	 * @date 2012-6-18
	 */
	@Override
	@Deprecated
	public boolean handleErrorInfo(JSONObject jsonObject)
	{
		return false;
	}

	/**
	 * 该方法的作用:错误处理code
	 * 
	 * @author lkf65210
	 * @date 2012-6-18
	 */
	@Override
	@Deprecated
	public void handleErrorCode(int code, String result)
	{
	}

	public static boolean hasHttpError(TGHttpResult httpResult)
	{
		int code = httpResult.getResponseCode();
		JSONObject jsonResult = httpResult.getJSONResult();

		if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT)
		{
			if (null != jsonResult)
			{
				try
				{
					return hasHttpError(jsonResult);
				}
				catch (JSONException e)
				{
					LogTools.e(TGHttpErrorHandler.class.getSimpleName(), "", e);
				}
			}
		}
		else
		{
			return true;
		}

		return false;
	}

	/**
	 * 该方法的作用:
	 * 检测是否为登录异常
	 * @author l00220455
	 * @date 2014年8月19日
	 * @param httpResult
	 * @return
	 */
	public static boolean checkLoginError(TGHttpResult httpResult)
	{
		int code = httpResult.getResponseCode();
		if (code == HttpURLConnection.HTTP_OK || code == HttpURLConnection.HTTP_NO_CONTENT)
		{
			String resultStr = httpResult.getResult();
			return resultStr.contains("errorInfo") && resultStr.contains("1000");
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * 该方法的作用: 检测是否存在HttpError
	 * 
	 * @author l00220455
	 * @date 2014年3月18日
	 * @param jsonObject
	 * @return
	 * @throws JSONException
	 */
	private static boolean hasHttpError(JSONObject jsonObject) throws JSONException
	{
		// 处理message类型的异常
		if (!jsonObject.isNull("message"))
		{
			return true;
		}

		// 处理errorInfo类型的异常
		if (jsonObject.has("errorInfo") && (jsonObject.length() == 1)
				&& null != jsonObject.getString("errorInfo")
				&& jsonObject.getString("errorInfo").length() >= 6)
		{
			return true;
		}

		return false;
	}

	protected Context getContext()
	{
		return context;
	}
}
