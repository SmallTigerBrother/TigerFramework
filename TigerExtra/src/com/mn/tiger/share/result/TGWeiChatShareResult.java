package com.mn.tiger.share.result;

import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;

public class TGWeiChatShareResult extends TGShareResult
{
	public static final int SUCCESS = BaseResp.ErrCode.ERR_OK;
	
	public static final int USER_CANCEL = BaseResp.ErrCode.ERR_USER_CANCEL;
	
	public static final int AUTH_DENIEND = BaseResp.ErrCode.ERR_AUTH_DENIED;
	
	public static final int ERROR_COMM = BaseResp.ErrCode.ERR_COMM;
	
	public static final int SENT_FAILED = BaseResp.ErrCode.ERR_SENT_FAILED;
	
	public static final int UNSUPPORT = BaseResp.ErrCode.ERR_UNSUPPORT;
	
	private int resultCode = SUCCESS;
	
	private String transaction;
	
	private String errorMsg;
	
	public TGWeiChatShareResult(BaseReq req)
	{
		this.resultCode = req.getType();
		this.transaction = req.transaction;
	}
	
	public TGWeiChatShareResult(BaseResp req)
	{
		this.resultCode = req.getType();
		this.transaction = req.transaction;
		this.errorMsg = req.errStr;
	}
	
	public int getResultCode()
	{
		return resultCode;
	}
	
	public String getTransaction()
	{
		return transaction;
	}
	
	public String getErrorMsg()
	{
		return errorMsg;
	}

	@Override
	public boolean isSuccess()
	{
		return resultCode == SUCCESS;
	}
}
