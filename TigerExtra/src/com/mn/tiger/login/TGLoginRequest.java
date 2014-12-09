package com.mn.tiger.login;

import android.app.Activity;

public abstract class TGLoginRequest
{
	private Activity activity;
	
	private String appID;
	
	public TGLoginRequest(Activity activity, String appID)
	{
		this.activity = activity;
		this.appID = appID;
	}
	
	public abstract void execute(ILoginCallback callback);
	
	protected Activity getActivity()
	{
		return activity;
	}
	
	protected String getAppID()
	{
		return appID;
	}
}
