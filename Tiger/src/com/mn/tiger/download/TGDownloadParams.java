package com.mn.tiger.download;

import java.io.Serializable;

/**
 * 
 * 该类作用及功能说明: 下载请求参数
 * 
 * @author pWX197040
 * @date 2014年8月18日
 */
public class TGDownloadParams implements Cloneable, Serializable 
{
	// 序列化ID
	private static final long serialVersionUID = 1L;
	// 下载url地址
    private String urlString; 
	// 下载请求参数
	private String params;
	// 文件下载保存的位置
	private String savePath;
	// 请求方式
	private int requestType;
	// 下载类型
	private String downloadType;
	// 执行下载的类名
	private String taskClsName = "";
	// 任务权重
	private int weight;
	
	public String getUrlString()
	{
		return urlString;
	}
	public void setUrlString(String urlstring)
	{
		this.urlString = urlstring;
	}
	public String getParams()
	{
		return params;
	}
	public void setParams(String params)
	{
		this.params = params;
	}
	public String getSavePath()
	{
		return savePath;
	}
	public void setSavePath(String savePath)
	{
		this.savePath = savePath;
	}
	public int getRequestType()
	{
		return requestType;
	}
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}
	public String getDownloadType()
	{
		return downloadType;
	}
	public void setDownloadType(String downloadType)
	{
		this.downloadType = downloadType;
	}
	public String getTaskClsName()
	{
		return taskClsName;
	}
	public void setTaskClsName(String taskClsName)
	{
		this.taskClsName = taskClsName;
	}
	public int getWeight()
	{
		return weight;
	}
	public void setWeight(int weight)
	{
		this.weight = weight;
	}
}
