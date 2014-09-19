package com.mn.tiger.request.client;

import java.io.IOException;
import java.io.InterruptedIOException;

import android.content.Context;

import com.mn.tiger.request.error.TGErrorMsgEnum;
import com.mn.tiger.request.method.TGHttpMethod;
import com.mn.tiger.request.receiver.TGHttpReceiver;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.LogTools;
import com.mn.tiger.utility.NetworkUtils;

/**
 * 该类作用及功能说明
 * 简单网络连接执行器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-12-1
 */
public class DefaultHttpClient extends TGHttpClient 
{
	/**
	 * @date 2013-12-1
	 * 构造函数
	 * @param context
	 */
	public DefaultHttpClient(Context context)
	{
		super(context);
	}
	
	/**
	 * @see TGHttpClient#executeHttpMethod(TGHttpMethod, TGHttpReceiver)
	 */
	@Override
	public TGHttpResult executeHttpMethod(TGHttpMethod httpMethod, TGHttpReceiver httpReceiver) 
	{
		LogTools.d(LOG_TAG, "[Method:executeHttpMethod]" + "connect-->start");
		TGHttpResult httpResult = initHttpResult();
		
		if(null == httpMethod)
		{
			return httpReceiver.receiveHttpResult(httpMethod, httpResult);
		}
		
		int code = 0;
		int sockCount = 0;
		int codeCount = 0;
		while (true) 
		{
			try 
			{
				//处理网络异常
				if (!NetworkUtils.isConnectivityAvailable(getContext())) 
				{
					code = TGErrorMsgEnum.NO_NETWORK.code;
					httpResult.setResult(getContext().getText(
							CR.getStringsId(getContext(), "mjet_network_alert")).toString());
					break;
				}
				
				//连接网络
				if(null != httpMethod)
				{
					code = httpMethod.excute();
				}
				
				if (code == -1) 
				{
					codeCount++;
					LogTools.d(LOG_TAG, "[Method:executeHttpMethod]" + "connect-->retry--->" + codeCount);
					httpMethod.disconnect();
					httpMethod = null;
					if (codeCount >= 2)
					{
						break;
					}
					continue;
				}
				break;
			}
			catch (Exception e)
			{
				LogTools.e(LOG_TAG, e.getMessage(), e);
				
				if (!(e instanceof InterruptedIOException)) 
				{
					String errorMsg = e.getClass().getName() + ":" + e.getMessage();
					if (e instanceof IOException)
					{
						code = TGErrorMsgEnum.IOEXCEPTION.code;
						errorMsg = TGErrorMsgEnum.getErrorMsg(getContext(),
								TGErrorMsgEnum.IOEXCEPTION);
					}
					else
					{
						code = TGErrorMsgEnum.UNKNOW_EXCEPTION.code;
						errorMsg = TGErrorMsgEnum.getErrorMsg(getContext(),
								TGErrorMsgEnum.UNKNOW_EXCEPTION);
					}
					httpResult.setResult(errorMsg);
					break;
				}
				
				sockCount++;
				LogTools.d(LOG_TAG, "[Method:executeHttpMethod]" + "connect-->retry--->" + codeCount);
				httpMethod.disconnect();
				if (sockCount >= 2) 
				{
					code = TGErrorMsgEnum.SOCKET_TIMEOUT.code;
					httpResult.setResult(TGErrorMsgEnum.getErrorMsg(getContext(),
							TGErrorMsgEnum.SOCKET_TIMEOUT));
					break;
				}
			}
		}
		
		httpResult.setResponseCode(Integer.valueOf(code));
		//用请求接收类处理请求结果
		return httpReceiver.receiveHttpResult(httpMethod, httpResult);
	}
	

	/**
	 * 该方法的作用:
	 * 初始化网络请求结果
	 * @date 2013-12-1
	 * @return
	 */
	private TGHttpResult initHttpResult()
	{
		TGHttpResult httpResult = new TGHttpResult();
		httpResult.setResponseCode(Integer.valueOf(4936));
		httpResult.setResult(getContext().getText(CR.getStringsId(getContext(), "mjet_try_later")).toString());
		return httpResult;
	}
}
