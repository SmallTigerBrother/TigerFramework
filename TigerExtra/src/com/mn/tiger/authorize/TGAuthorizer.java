package com.mn.tiger.authorize;

import android.app.Activity;
import android.content.Intent;

public abstract class TGAuthorizer
{
	private Activity activity;
	
	private String appID;
	
	public TGAuthorizer(Activity activity, String appID)
	{
		this.activity = activity;
		this.appID = appID;
	}
	
	public abstract void authorize(IAuthorizeCallback callback);
	
	public abstract void logout();
	
	protected Activity getActivity()
	{
		return activity;
	}
	
	protected String getAppID()
	{
		return appID;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
	}
}
