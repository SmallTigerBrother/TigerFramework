package com.mn.tiger.upload;

/**
 * 
 * 该类作用及功能说明
 * 上传参数工厂
 * 
 * @author pWX197040
 * @date 2014年8月30日
 */
public class TGUploadParamsFactory
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGUploadParamsFactory.class.getSimpleName();

	
	public TGUploadParamsFactory()
	{
		
	}
	
	/**
	 * 
	 * 该方法的作用:
	 * @author pWX197040
	 * @date 2014年8月30日
	 * @param uploadTaskClassName 上传任务类型名称
	 * @return
	 */
	public static TGUploadParams getUploadParams(String uploadTaskClassName)
	{
		if (TGUploadTask.class.getName().equals(uploadTaskClassName))
		{
			return new TGUploadParams();
		}
	    else
		{
			return new TGUploadParams();
		}
	}
}
