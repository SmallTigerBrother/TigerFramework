package com.mn.tiger.request.sync;


public class TGSyncHttpLoaderFactory
{
	public static ISyncHttpLoader createSyncHttpLoader(HttpImplementionType syncHttpLoaderType)
	{
		switch (syncHttpLoaderType)
		{
			case HttpUrlConnection:
				return new TGSyncHttpLoader();
				
			case ApacheHttpClient:
				return new ApacheSyncHttpLoader();

			default:
				return new TGSyncHttpLoader();
		}
	}
}
