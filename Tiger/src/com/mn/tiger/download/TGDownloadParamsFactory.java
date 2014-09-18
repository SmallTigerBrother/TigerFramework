package com.mn.tiger.download;


/**
 * 
 * 该类作用及功能说明
 * 下载参数工厂
 * 
 * @author pWX197040
 * @date 2014年8月30日
 */
public class TGDownloadParamsFactory
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGDownloadParamsFactory.class.getSimpleName();

	
	public TGDownloadParamsFactory()
	{
		
	}
	
	/**
	 * 
	 * 该方法的作用:
	 * @author pWX197040
	 * @date 2014年8月30日
	 * @param downloadTaskClassName 下载任务类型名称
	 * @return
	 */
	public static TGDownloadParams getDownloadParams(String downloadTaskClassName)
	{
		if (TGDownloadTask.class.getName().equals(downloadTaskClassName))
		{
			return new TGDownloadParams();
		}
	    else
		{
			return new TGDownloadParams();
		}
	}
}
