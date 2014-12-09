package com.mn.tiger.login;

public interface IAuthorizeCallback
{
	void onSuccess(TGAuthorizeResult loginResult);
	
	void onError(int code, String message, String detail);
	
	void onCancel();
}
