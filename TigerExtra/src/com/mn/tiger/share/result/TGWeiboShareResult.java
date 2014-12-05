package com.mn.tiger.share.result;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.constant.WBConstants;

public class TGWeiboShareResult extends TGShareResult
{
	public static final int SUCCESS = WBConstants.ErrorCode.ERR_OK;
	
	public static final int USER_CANCEL = WBConstants.ErrorCode.ERR_CANCEL;
	
	public static final int SENT_FAILED = WBConstants.ErrorCode.ERR_FAIL;
	
	private String transaction = "";
	
	private int resultCode = 0;
	
	private String reqPackageName = "";
	
	private String errMsg = "";
	
	public TGWeiboShareResult(BaseResponse response)
	{
		this.transaction = response.transaction;
		this.resultCode = response.errCode;
		this.reqPackageName = response.reqPackageName;
		this.errMsg = response.errMsg;
	}
	
	@Override
	public boolean isSuccess()
	{
		return resultCode == SUCCESS;
	}
	
	public String getErrMsg()
	{
		return errMsg;
	}
	
	public int getResultCode()
	{
		return resultCode;
	}
	
	public String getReqPackageName()
	{
		return reqPackageName;
	}
	
	public String getTransaction()
	{
		return transaction;
	}
}
