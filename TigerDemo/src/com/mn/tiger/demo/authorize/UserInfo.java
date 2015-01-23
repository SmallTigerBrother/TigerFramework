package com.mn.tiger.demo.authorize;

import java.io.Serializable;

/**
 * 用户信息类
 */
public class UserInfo implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户访问的token
	 */
	private String accessToken;
	
	/**
	 * 用户ID
	 */
	private String uid;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 性别（1表示男 0表示女）
	 */
	private int male;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken(String accessToken)
	{
		this.accessToken = accessToken;
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getNickName()
	{
		return nickName;
	}

	public void setNickName(String nickName)
	{
		this.nickName = nickName;
	}

	public int getMale()
	{
		return male;
	}

	public void setMale(int male)
	{
		this.male = male;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}
}
