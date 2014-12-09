package com.mn.tiger.login;

public class TGAuthorizeResult
{
	private String UID;
	
	private String accessToken;

	public String getUID()
	{
		return UID;
	}

	public void setUID(String UID)
	{
		this.UID = UID;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}
}
