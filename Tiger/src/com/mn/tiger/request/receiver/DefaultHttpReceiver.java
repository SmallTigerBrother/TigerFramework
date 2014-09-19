package com.mn.tiger.request.receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.request.error.TGErrorMsgEnum;
import com.mn.tiger.request.error.TGHttpErrorHandler;
import com.mn.tiger.request.method.TGHttpMethod;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 简单网络请求结果处理类，实现MPHttpReceiver
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1 Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights
 *       reserved.
 */
public class DefaultHttpReceiver extends TGHttpReceiver
{
	/**
	 * 运行环境
	 */
	private Context context;

	/**
	 * 网络异常处理结果
	 */
	private IHttpErrorHandler httpErrorHandler;

	/**
	 * @date 2013-12-1 构造函数
	 * @param context
	 * @param httpErrorHandler
	 */
	public DefaultHttpReceiver(Context context, IHttpErrorHandler httpErrorHandler)
	{
		this.context = context;
		this.httpErrorHandler = httpErrorHandler;
	}

	/**
	 * @see MPSpHttpReceiver#receiveHttpResult(TGHttpMethod, TGHttpResult)
	 */
	@Override
	public TGHttpResult receiveHttpResult(TGHttpMethod httpMethod, TGHttpResult httpResult)
	{
		if (null == httpMethod)
		{
			return httpResult;
		}

		if (httpResult.getResponseCode() == HttpURLConnection.HTTP_OK || httpResult.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT)
		{
			if (!readInputStream(httpMethod, httpResult))
			{
				return httpResult;
			}
		}

		// 处理异常
		return dealHttpResult(httpMethod, httpResult);
	}
	
	/**
	 * 该方法的作用: 读取网络请求输入流
	 * @date 2014年4月25日
	 * @param httpMethod
	 * @param httpResult
	 * @return
	 */
	protected boolean readInputStream(TGHttpMethod httpMethod, TGHttpResult httpResult)
	{
		// 读取请求结果
		StringBuffer resultBuffer = new StringBuffer();
		BufferedReader reader = null;
		try
		{
			LogTools.d(LOG_TAG, "[Method:receiveHttpResult]" + "-->readStream");
			String strCurrentLine = null;
			InputStream inputStream = httpMethod.getInputStream();
			if (null != inputStream)
			{
				reader = new BufferedReader(new InputStreamReader(inputStream));
				while ((strCurrentLine = reader.readLine()) != null)
				{
					resultBuffer.append(strCurrentLine);
				}
			}
		}
		catch (IOException e)
		{
			// 包装应用内部请求错误
			httpResult.setResponseCode(TGErrorMsgEnum.IOEXCEPTION.code);
			httpResult.setResult(TGErrorMsgEnum.getErrorMsg(context, TGErrorMsgEnum.IOEXCEPTION));
			LogTools.e(LOG_TAG, "", e);
			return false;
		}
		finally
		{
			try
			{
				if (null != reader)
				{
					reader.close();
				}
				httpMethod.disconnect();
			}
			catch (IOException e)
			{
				httpResult.setResponseCode(TGErrorMsgEnum.IOEXCEPTION.code);
				httpResult.setResult(TGErrorMsgEnum
						.getErrorMsg(context, TGErrorMsgEnum.IOEXCEPTION));
				LogTools.e(LOG_TAG, "", e);
				return false;
			}
		}

		String result = resultBuffer.toString();

		// 解密结果
		httpResult = decodeResult(httpResult, httpMethod, result);

		LogTools.d("HttpResult", httpResult.getResult().toString());

		// 将数据解析为JSON格式
		if(result != null && !result.isEmpty()) 
		{
			httpResult = convertResultToJson(httpResult);
		}

		return true;
	}

	/**
	 * 该方法的作用: 解密请求结果
	 * 
	 * @date 2013-12-1
	 * @param httpResult
	 * @param httpMethod
	 * @param result
	 * @return
	 */
	protected TGHttpResult decodeResult(TGHttpResult httpResult, TGHttpMethod httpMethod,
			String result)
	{
		return httpResult;
	}

	/**
	 * 该方法的作用: 将String结果解析为JSON数据
	 * 
	 * @date 2014年3月13日
	 * @param httpResult
	 * @return
	 */
	protected TGHttpResult convertResultToJson(TGHttpResult httpResult)
	{
		String result = (String) httpResult.getResult();
		try
		{
			JSONObject jsonObj = new JSONObject(result);
			httpResult.setJSONResult(jsonObj);
		}
		catch (JSONException e)
		{
			LogTools.e(LOG_TAG, "result type is not json.");
		}

		return httpResult;
	}

	/**
	 * 该方法的作用: 处理网络异常
	 * 
	 * @date 2013-12-1
	 * @param httpResult
	 * @return 出现异常时，返回值为null，无异常正常返回
	 */
	protected TGHttpResult dealHttpResult(TGHttpMethod httpMethod, TGHttpResult httpResult)
	{
		LogTools.p(LOG_TAG, "[Method:dealHttpResult] --> " + httpMethod.getUrl());

		if (catchHttpError(httpResult))
		{
			return null;
		}

		return httpResult;
	}

	/**
	 * 该方法的作用: 捕获Http错误
	 * 
	 * @date 2014年4月18日
	 * @param httpResult
	 * @return 出现异常时返回true，异常交由httpErrorHandler处理，无异常返回false，
	 */
	protected boolean catchHttpError(TGHttpResult httpResult)
	{
		LogTools.d(LOG_TAG, "[Method:catchHttpError]");
		if (null != httpErrorHandler && TGHttpErrorHandler.hasHttpError(httpResult))
		{
			return httpErrorHandler.handleErrorInfo(httpResult);
		}

		return false;
	}

	/**
	 * 该方法的作用: 设置网络请求异常处理接口
	 * 
	 * @date 2014年1月15日
	 * @param httpErrorHandler
	 */
	public void setHttpErrorHandler(IHttpErrorHandler httpErrorHandler)
	{
		this.httpErrorHandler = httpErrorHandler;
	}

	/**
	 * 该方法的作用: 获取网络请求异常处理接口
	 * 
	 * @date 2014年1月15日
	 * @return
	 */
	public IHttpErrorHandler getHttpErrorHandler()
	{
		return httpErrorHandler;
	}

	public Context getContext()
	{
		return context;
	}
}
