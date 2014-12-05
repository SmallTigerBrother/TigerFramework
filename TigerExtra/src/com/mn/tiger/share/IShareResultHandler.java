package com.mn.tiger.share;

import com.mn.tiger.share.result.TGShareResult;

public interface IShareResultHandler<T extends TGShareResult>
{
	void handleShareResult(T shareResult);
}
