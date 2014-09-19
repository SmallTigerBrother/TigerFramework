package com.mn.tiger.download;

import java.io.Serializable;

import com.mn.tiger.datastorage.db.annotation.Column;
import com.mn.tiger.datastorage.db.annotation.Id;
import com.mn.tiger.datastorage.db.annotation.Table;
import com.mn.tiger.datastorage.db.annotation.Transient;

/**
 * 
 * 该类作用及功能说明: 下载任务记录
 * 
 * @date 2014年8月18日
 */
@Table(name = "Downloader", execAfterTableCreated = "CREATE INDEX index_name ON Downloader(urlstring,params)")
public class TGDownloader implements Serializable
{
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	// 下载url地址
	@Column(column = "urlString")
	private String urlString;
	// 下载请求参数
	@Column(column = "params")
	private String params;
	// 文件大小（字节）
	@Column(column = "fileSize")
	private long fileSize;
	// 文件下载完成的字节数
	@Column(column = "completeSize")
	private long completeSize;
	// 文件下载状态，DonwnloadConstant:INIT:0
	// DOWNLOADING:1,SUCCESS:2,FAILURE:3,PAUSE:4,SOURCE_ERROR:5(下载来源错误，文件长度或MD5校验出错)
	@Column(column = "downloadStatus")
	private int downloadStatus = TGDownloadManager.DOWNLOAD_WAITING;
	// 文件下载保存的位置
	@Column(column = "savePath")
	private String savePath;
	// 请求类型
	@Column(column = "requestType")
	private int requestType;
	// 文件流校验加密字符串
	@Column(column = "checkKey")
	private String checkKey;
	// 是否使用断点下载
	@Column(column = "isBreakPoints")
	private boolean isBreakPoints = false;
	// 下载出错时，错误码
	@Column(column = "errorCode")
	private int errorCode;
	// 下载出错时，错误信息
	@Column(column = "errorMsg")
	private String errorMsg;

	// 自定义执行的任务类的名称
	@Column(column = "taskClsName")
	private String taskClsName = "";
	// 下载文档id（edm用）
	@Column(column = "docId")
	private String docId;
	// 获取业务服务器token的url（edm用）
	@Column(column = "requetsTokenUrl")
	private String requetsTokenUrl;
	// 下载缩略图用（edm用）
	@Column(column = "thumb")
	private String thumb;
	// edm文档版本（edm用）
	@Column(column = "version")
	private String version;
	// 下载类型
	@Column(column = "type")
	private String type;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	/**
	 * @return the fileSize
	 */
	public long getFileSize()
	{
		return fileSize;
	}

	/**
	 * @param fileSize
	 *            the fileSize to set
	 */
	public void setFileSize(long fileSize)
	{
		this.fileSize = fileSize;
	}

	/**
	 * @return the completeSize
	 */
	public long getCompleteSize()
	{
		return completeSize;
	}

	/**
	 * @param completeSize
	 *            the completeSize to set
	 */
	public void setCompleteSize(long completeSize)
	{
		this.completeSize = completeSize;
	}

	/**
	 * @return the urlstring
	 */
	public String getUrlString()
	{
		return urlString;
	}

	/**
	 * @param urlstring
	 *            the urlstring to set
	 */
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

	/**
	 * @return the downloadStatus
	 */
	public int getDownloadStatus()
	{
		return downloadStatus;
	}

	/**
	 * @param downloadStatus
	 *            the downloadStatus to set
	 */
	public void setDownloadStatus(int downloadStatus)
	{
		this.downloadStatus = downloadStatus;
	}

	/**
	 * @return the savePath
	 */
	public String getSavePath()
	{
		return savePath;
	}

	/**
	 * @param savePath
	 *            the savePath to set
	 */
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

	public String getCheckKey()
	{
		return checkKey;
	}

	public void setCheckKey(String checkKey)
	{
		this.checkKey = checkKey;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorMsg()
	{
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg)
	{
		this.errorMsg = errorMsg;
	}

	public boolean isBreakPoints()
	{
		return isBreakPoints;
	}

	public void setBreakPoints(boolean isBreakPoints)
	{
		this.isBreakPoints = isBreakPoints;
	}

	public void setTaskClsName(String taskClsName)
	{
		this.taskClsName = taskClsName;
	}

	public String getTaskClsName()
	{
		return taskClsName;
	}

	public String getDocId()
	{
		return docId;
	}

	public void setDocId(String docId)
	{
		this.docId = docId;
	}

	public String getRequetsTokenUrl()
	{
		return requetsTokenUrl;
	}

	public void setRequetsTokenUrl(String requetsTokenUrl)
	{
		this.requetsTokenUrl = requetsTokenUrl;
	}

	public String getThumb()
	{
		return thumb;
	}

	public void setThumb(String thumb)
	{
		this.thumb = thumb;
	}

	public String getVersion()
	{
		return version;
	}

	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override
	public String toString()
	{
		return "Downloader [fileSize=" + fileSize + ", complete=" + completeSize + ", urlString=" + urlString
				+ ", params=" + params + ", savePath=" + savePath + ", downloadStatus=" + downloadStatus
				+ ", requestType=" + requestType + ", checkKey=" + checkKey + ", isBreakPoints=" + isBreakPoints
				+ ", type=" + type + ", docId=" + docId + "]";
	}
}
