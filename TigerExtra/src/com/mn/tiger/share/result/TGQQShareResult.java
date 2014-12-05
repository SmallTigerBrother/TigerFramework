package com.mn.tiger.share.result;

import org.json.JSONException;
import org.json.JSONObject;

import com.mn.tiger.log.Logger;
import com.tencent.tauth.UiError;

public class TGQQShareResult extends TGShareResult
{
	private static final Logger LOG = Logger.getLogger(TGQQShareResult.class);
	
	// 返回码参考文档，0表示成功
	// http://wiki.connect.qq.com/com-tencent-tauth-tencent-sharetoqq
	// http://wiki.connect.qq.com/%E5%85%AC%E5%85%B1%E8%BF%94%E5%9B%9E%E7%A0%81%E8%AF%B4%E6%98%8E
	public static final int SUCCESS = 0;
	
	public static final int NETWORK_ERROR = -30001;
	
	public static final int USER_CANCEL = -1;
	
	public static final int UNAUTHORIZED= 100030;
	
	public static final int ACCESS_TOKEN_EXPIRED = 100014;
	
	public static final int ACCESS_TOKEN_INVALID= 100015;
	
	public static final int ACCESS_TOKEN_AUTHORIZE_FAILED= 100016;

	private int resultCode = NETWORK_ERROR;

	private JSONObject reponse;
	
	private UiError uiError;

	public TGQQShareResult(JSONObject response)
	{
		this.reponse = response;
		if (response != null)
		{
			try
			{
				resultCode = response.getInt("ret");
			}
			catch (JSONException e)
			{
				LOG.e(e);
			}
		}
	}
	
	public TGQQShareResult (UiError error)
	{
		this.uiError = error;
	}

	public TGQQShareResult(int resultCode)
	{
		this.resultCode = resultCode;
	}

	public int getResultCode()
	{
		return resultCode;
	}

	@Override
	public boolean isSuccess()
	{
		return resultCode == SUCCESS;
	}

	public JSONObject getReponse()
	{
		return reponse;
	}

	public UiError getUiError()
	{
		return uiError;
	}
}
