package com.mn.tiger.login;

public interface ILoginCallback
{
	void onSuccess(TGLoginResult loginResult);
	
	void onError(int code, String message, String detail);
	
	void onCancel();
}
