package com.mn.tiger.request.error;

import org.json.JSONObject;

import com.mn.tiger.request.receiver.TGHttpResult;

/**
 * 该类作用及功能说明
 * Http异常处理接口
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public interface IHttpErrorHandler
{
	/**
	 * 该方法的作用:
	 * 处理服务返回的错误信息
	 * @author l00220455
	 * @date 2014年4月26日
	 * @param httpResult
	 * @return
	 */
	public boolean handleErrorInfo(TGHttpResult httpResult);
	
	/**
	 * 该方法的作用:
	 * 处理服务返回的错误信息
	 * @author l00220455
	 * @date 2014年1月16日
	 * @param jsonObject
	 * @return
	 */
	@Deprecated
	public boolean handleErrorInfo(JSONObject jsonObject);

	/**
	 * 该方法的作用:
	 * 处理服务返回的错误码
	 * @author l00220455
	 * @date 2014年1月16日
	 * @param code
	 * @param result
	 */
	@Deprecated
	public void handleErrorCode(int code, String result);
}
