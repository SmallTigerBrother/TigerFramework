package com.mn.tiger.request.receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.content.Context;

import com.mn.tiger.request.error.TGErrorMsgEnum;
import com.mn.tiger.request.method.TGHttpMethod;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 简单网络请求结果处理类，实现MPHttpReceiver
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class DefaultHttpReceiver extends TGHttpReceiver
{
	/**
	 * 运行环境
	 */
	private Context context;

	/**
	 * @date 2013-12-1 构造函数
	 * @param context
	 * @param httpErrorHandler
	 */
	public DefaultHttpReceiver(Context context)
	{
		this.context = context;
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

		if (httpResult.getResponseCode() == HttpURLConnection.HTTP_OK || 
				httpResult.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT)
		{
			if (!readInputStream(httpMethod, httpResult))
			{
				return httpResult;
			}
		}

		// 处理异常
		return httpResult;
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

	public Context getContext()
	{
		return context;
	}
}
