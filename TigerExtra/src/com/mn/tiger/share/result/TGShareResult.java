package com.mn.tiger.share.result;

public abstract class TGShareResult
{
	private int shareType = -1;

	public int getShareType()
	{
		return shareType;
	}

	public void setShareType(int shareType)
	{
		this.shareType = shareType;
	}
	
	public abstract boolean isSuccess();
}
