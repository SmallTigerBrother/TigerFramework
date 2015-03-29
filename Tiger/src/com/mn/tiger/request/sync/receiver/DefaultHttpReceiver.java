package com.mn.tiger.request.sync.receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import android.content.Context;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.error.TGHttpError;
import com.mn.tiger.request.sync.method.TGHttpMethod;

/**
 * 该类作用及功能说明 简单网络请求结果处理类，实现TGHttpReceiver
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
			return readInputStream(httpMethod, httpResult);
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
	protected TGHttpResult readInputStream(TGHttpMethod httpMethod, TGHttpResult httpResult)
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
				
				httpResult.setResult(resultBuffer.toString());
			}
		}
		catch (IOException e)
		{
			// 包装应用内部请求错误
			httpResult.setResponseCode(TGHttpError.IOEXCEPTION);
			httpResult.setResult(TGHttpError.getDefaultErrorMsg(context, TGHttpError.IOEXCEPTION));
			LogTools.e(LOG_TAG, "", e);
			return httpResult;
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
				httpResult.setResponseCode(TGHttpError.IOEXCEPTION);
				httpResult.setResult(TGHttpError.getDefaultErrorMsg(
						context, TGHttpError.IOEXCEPTION));
				LogTools.e(LOG_TAG, "", e);
				return httpResult;
			}
		}
		
		LogTools.d(LOG_TAG, "responseCode == " + httpResult.getResponseCode() + " -- " + 
			    httpResult.getResult());

		return httpResult;
	}

	public Context getContext()
	{
		return context;
	}
}
