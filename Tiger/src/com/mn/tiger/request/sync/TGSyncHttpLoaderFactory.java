package com.mn.tiger.request.sync;

import com.mn.tiger.log.Logger;


public class TGSyncHttpLoaderFactory
{
	private static final Logger LOG = Logger.getLogger(TGSyncHttpLoaderFactory.class);
	
	public static AbstractSyncHttpLoader createSyncHttpLoader(HttpImplementionType syncHttpLoaderType)
	{
		LOG.d("[Method:createSyncHttpLoader] HttpImplementionType = " + syncHttpLoaderType.name());
		
		return new ApacheSyncHttpLoader();
	}
}
