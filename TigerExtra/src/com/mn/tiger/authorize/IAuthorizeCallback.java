package com.mn.tiger.authorize;

public interface IAuthorizeCallback
{
	void onSuccess(TGAuthorizeResult loginResult);
	
	void onError(int code, String message, String detail);
	
	void onCancel();
}
