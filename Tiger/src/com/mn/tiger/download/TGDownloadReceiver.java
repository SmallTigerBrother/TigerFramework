package com.mn.tiger.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.error.TGErrorMsgEnum;
import com.mn.tiger.request.error.TGHttpError;
import com.mn.tiger.request.method.TGHttpMethod;
import com.mn.tiger.request.receiver.DefaultHttpReceiver;
import com.mn.tiger.request.receiver.TGHttpResult;
import com.mn.tiger.task.TGTask.MPTaskState;
import com.mn.tiger.utility.FileUtils;

/**
 * 
 * 该类作用及功能说明: 下载接收类
 * 
 * @date 2014年8月18日
 */
public class TGDownloadReceiver extends DefaultHttpReceiver
{
	// 下载接收监听
	private IDownloadReceiveListener receiveListener;
	
	// 下载任务
	private TGDownloadTask downloadTask;
	
	// 下载参数
	private TGDownloader downloader;
	
	// 已下载长度
	private long completeSize;
	
	public TGDownloadReceiver(Context context, TGDownloader downloader, 
			TGDownloadTask downloadTask, IDownloadReceiveListener receiveListener)
	{
		super(context);
		this.downloader = downloader;
		this.completeSize = downloader.getCompleteSize();
		this.downloadTask = downloadTask;
		this.receiveListener = receiveListener;
	}

	@Override
	public TGHttpResult receiveHttpResult(TGHttpMethod httpMethod, TGHttpResult httpResult)
	{
		// 检测存储路径是否为空
		if (TextUtils.isEmpty(downloader.getSavePath())) {
			downloadFailed(TGErrorMsgEnum.DOWNLOAD_SAVE_PATH_IS_NULL.code,
					TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.DOWNLOAD_SAVE_PATH_IS_NULL));
			return null;
		}
		// 检测请求和接收参数
		if (null == httpMethod || httpResult == null)
		{
			return httpResult;
		}
		// 返回响应码不为200或204，直接报错
		if (httpResult.getResponseCode() != HttpURLConnection.HTTP_OK
				&& httpResult.getResponseCode() != HttpURLConnection.HTTP_PARTIAL)
		{
			// 服务返回异常responseCode
			downloadFailed(httpResult.getResponseCode(), httpResult.getResult().toString());
			return super.receiveHttpResult(httpMethod, httpResult);
		}

		// 文档下载流如果返回的类型为json，则是异常情况
		if (isHasError(httpMethod))
		{   
			// 如果请求正常，但返回错误信息，则处理错误信息，比如1000的错误
			TGHttpResult dealedResult = super.receiveHttpResult(httpMethod, httpResult);

			if (dealedResult != null)
			{ // 其他的错误错误
				int code = dealedResult.getResponseCode();
				String msg = dealedResult.getResult();
				LogTools.e(LOG_TAG, "[Method:receiveHttpResult]  occured error,code:"+code+",msg:"+msg);
				downloadFailed(code, msg);
			}
			else
			{ // httpErrorHandler处理了常规的服务错误后，返回的httpResult为空
				downloadFailed(TGErrorMsgEnum.SERVER_EXCEPTION.code, httpResult.getResult()
						.toString());
			}

			return httpResult;
		}

		return receiveDownloadResult(httpMethod, httpResult);
	}
	
	/**
	 * 该方法的作用:获取header中指定的信息
	 * 
	 * @date 2014年1月7日
	 * @param key
	 * @param headers
	 * @return
	 */
	protected String getHeaderField(String key, Map<String, List<String>> headers)
	{
		if (headers != null)
		{
			if (headers.containsKey(key) && headers.get(key).size() > 0)
			{
				return headers.get(key).get(0);
			}
		}
		return "";
	}

	/**
	 * 该方法的作用:服務是否返回錯誤
	 * 
	 * @date 2014年1月7日
	 * @param httpMethod
	 * @return
	 */
	protected boolean isHasError(TGHttpMethod httpMethod)
	{
		String format = getHeaderField("format", httpMethod.getHeaderFields());
		if (format.equals("json"))
		{
			return true;
		}
		return false;
	}
	
	protected TGHttpResult receiveDownloadResult(TGHttpMethod httpMethod, TGHttpResult httpResult)
	{
		LogTools.p(LOG_TAG,"[Method:receiveDownloadResult]  start..");
		
		if (null == httpMethod)
		{
			return httpResult;
		}
		
		httpResult = dealDownloadResult(httpMethod, httpResult);
		
		LogTools.d(LOG_TAG,"[Method:receiveDownloadResult] saveSSOCookie..");
		return httpResult;
	}
	
	/**
	 * 该方法的作用:处理下载结果
	 * 
	 * @date 2014年4月25日
	 * @param httpMethod
	 * @param httpResult
	 */
	protected TGHttpResult dealDownloadResult(TGHttpMethod httpMethod, TGHttpResult httpResult)
	{
		Map<String, List<String>> headers = httpMethod.getHeaderFields();
		InputStream inputStream = null;
		try
		{
			long serverFileSize = getFileSize(headers);
			String accept_range = getHeaderField("Accept-Ranges", headers);
			String serverCheckString = getServerCheckStr(headers);
			String savePath = getSavePath(headers);
			
			LogTools.p(LOG_TAG,
					"[Method:receiveDownloadResult] download to request serverFileSize:"
							+ serverFileSize + "; serverCheckString: " + serverCheckString);
			// 如果服务端和客户端都有校验值, 校验两边的值是否一致
			if(!equalCheckStr(serverCheckString, downloader.getCheckKey()))
			{
				LogTools.e(LOG_TAG,
						TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.FAILED_CHECK_FILE_MD5));

				downloadFailed(TGErrorMsgEnum.FAILED_CHECK_FILE_MD5.code,
						TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.FAILED_CHECK_FILE_MD5));
				return httpResult;
			}
			
			// 保存空间不足
			if(serverFileSize > 0 && serverFileSize > FileUtils.getFreeBytes(downloader.getSavePath()))
			{
				LogTools.e(LOG_TAG,
						TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.USABLE_SPACE_NOT_ENOUGH));

				downloadFailed(TGErrorMsgEnum.USABLE_SPACE_NOT_ENOUGH.code,
						TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.USABLE_SPACE_NOT_ENOUGH));
				return httpResult;
			}
			
			// 第一次请求，设置文件大小
			if(downloader.getFileSize() <= 0)
			{
				downloader.setFileSize(serverFileSize);
			}
			// 设置文件流校验字符串值和文件存储路径
			downloader.setCheckKey(serverCheckString);
			downloader.setSavePath(savePath);
			// 文件长度未知或者服务端不支持断点下载，使用普通下载
			if(serverFileSize <= 0 || !accept_range.equalsIgnoreCase("bytes"))
			{
				LogTools.p(LOG_TAG, "[Method:dealDownloadResult]  Server is not return the fileSize...; accept_range: " + accept_range);
				
				downloader.setBreakPoints(false);
				
				// 文件刚开始下载，数据库没有记录时，存储数据到数据库
				if(downloader.getCompleteSize() <= 0)
				{
					TGDownloadDBHelper.getInstance(getContext()).saveDownloader(downloader);
				}
				
				inputStream = httpMethod.getInputStream();
				writeToLocalFile(inputStream, savePath);
			}
			// 否则使用断点下载
			else
			{
				// 文件刚开始下载，设置支持断点下载， 存储数据到数据库
				if(!downloader.isBreakPoints() && downloader.getCompleteSize() <= 0)
				{
					downloader.setBreakPoints(true);
					TGDownloadDBHelper.getInstance(getContext()).saveDownloader(downloader);
				}
				
				// 写文件流到本地
				inputStream = httpMethod.getInputStream();
				writeToRandomFile(inputStream, savePath, downloader.getCompleteSize(), downloader.getFileSize());
			}
		}
		catch (IOException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
			downloadFailed(TGHttpError.IOEXCEPTION,
					TGHttpError.getDefaultErrorMsg(getContext(), TGHttpError.IOEXCEPTION));
		}
		catch (NumberFormatException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
			downloadFailed(TGErrorMsgEnum.FAILED_GET_FILE_SIZE.code,
					TGErrorMsgEnum.getErrorMsg(getContext(), TGErrorMsgEnum.FAILED_GET_FILE_SIZE));
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG, e.getMessage(), e);
					downloadFailed(TGHttpError.IOEXCEPTION,
							TGHttpError.getDefaultErrorMsg(getContext(), TGHttpError.IOEXCEPTION));
				}
			}
		}
		return httpResult;
	}
	
	/**
	 * 该方法的作用:写入文件
	 * 
	 * @date 2014年1月14日
	 * @param instream
	 * @param savePath
	 * @throws IOException
	 * @throws NullPointerException
	 */
	protected void writeToLocalFile(InputStream instream, String savePath) throws IOException
	{
		LogTools.p(LOG_TAG, "[Method:writeToLocalFile]");
		if(instream == null)
		{
			LogTools.e(LOG_TAG, "[Method:writeToLocalFile] instream is empty.");
			throw new IOException("Failed to receive stream, instream is null.");
		}
		
		File file = new File(savePath);
		if (!file.exists())
		{ // 文件不存在时，才创建并chmod文件
			file = FileUtils.createFile(file.getAbsolutePath());
		}
		if (null == file)
		{
			throw new IOException("Failed to create file....");
		}
		
		OutputStream outStream = null;
		try
		{
			outStream = new FileOutputStream(file);
			byte[] buffer = new byte[4 * 1024];
			int length = -1;
			while ((length = (instream.read(buffer))) != -1)
			{
				// 当任务被取消、下载出错或者任务被停止时, 停止写文件
				if (downloadTask == null || downloadTask.getTaskState() == MPTaskState.ERROR || 
						downloadTask.getTaskState() == MPTaskState.PAUSE)
				{
					downloadStop(downloader);
					return;
				}
				else
				{
					outStream.write(buffer, 0, length);
					completeSize += length;
					downloader.setCompleteSize(completeSize);
					downloading(downloader);
				}
			}

			downloadFinish(downloader);
		}
		catch(IOException e)
		{
			throw e;
		}
		finally
		{
			if (outStream != null)
			{
				try
				{
					outStream.close();
				}
				catch (IOException e)
				{
					throw e;
				}
			}
		}
	}
	
	/**
	 * 该方法的作用:将请求返回的流写入本地文件
	 * 
	 * @date 2014年1月7日
	 * @param inStream
	 * @param savePath
	 * @param startPos
	 * @param endPos
	 * @throws IOException
	 */
	protected void writeToRandomFile(InputStream inStream, String savePath, long startPos,
			long endPos) throws IOException
	{
		if(inStream == null)
		{
			LogTools.e(LOG_TAG, "[Method:writeToLocalFile] instream is empty.");
			throw new IOException("Failed to receive stream, instream is null.");
		}
		
		RandomAccessFile randomAccessFile = null;
		try
		{
			File file = getFileOfPath(savePath);
			randomAccessFile = new RandomAccessFile(file, "rwd");
			randomAccessFile.seek(startPos);

			LogTools.p(LOG_TAG, "[Method:writeToFile]  seek file position:" + startPos
					+ " start to write...");

			byte[] buffer = new byte[4096];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1)
			{
				// 当任务被取消、下载出错或者任务被停止时, 停止写文件
				if (downloadTask == null || downloadTask.getTaskState() == MPTaskState.ERROR
						|| downloadTask.getTaskState() == MPTaskState.PAUSE)
				{
					downloadStop(downloader);
					return;
				}
				else
				{
					randomAccessFile.write(buffer, 0, length);
					completeSize += length;
					downloader.setCompleteSize(completeSize);
					downloading(downloader);
				}
			}

			downloadFinish(downloader);
		}
		catch (IOException e)
		{
			throw e;
		}
		finally
		{
			if (randomAccessFile != null)
			{
				try
				{
					randomAccessFile.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG, e.getMessage(), e);
					downloadFailed(TGHttpError.IOEXCEPTION,
							TGHttpError.getDefaultErrorMsg(getContext(), TGHttpError.IOEXCEPTION));
				}
			}
		}
	}
	
	/**
	 * 该方法的作用:获取指定路径下的文件，如文件或目录不存在，创建文件目录和文件
	 * 
	 * @date 2013-7-30
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private File getFileOfPath(String path) throws IOException
	{
		File file = new File(path);
		if (!file.exists())
		{
			FileUtils.createFile(path);
			FileUtils.chmodFile(file.getAbsolutePath(), "666");
		}
		return file;
	}
	
	/**
	 * 该方法的作用:获取文件长度
	 * 
	 * @date 2014年1月14日
	 * @param headers
	 * @throws Exception
	 * @return 文件长度
	 */
	protected long getFileSize(Map<String, List<String>> headers) throws NumberFormatException
	{
		// 获取文件大小
		String fileSize = getHeaderField("MEAP_File_Size", headers);
		if (TextUtils.isEmpty(fileSize))
		{
			fileSize = getHeaderField("Content-Length", headers);
		}
		if(!TextUtils.isEmpty(fileSize))
		{
			return Long.valueOf(fileSize);
		}
		else
		{
			return 0;
		}
	}

	/**
	 * 
	 * 该方法的作用: 获取服务响应头中的校验值: 默认为MD5值
	 * @date 2014年7月16日
	 * @param headers
	 * @return
	 */
	protected String getServerCheckStr(Map<String, List<String>> headers)
	{
		if (null == headers)
		{
			LogTools.p(LOG_TAG, "[Method:getServerCheckStr]  headers is null..");
			return "";
		}
		
		String md5String = getHeaderField("MD5String", headers);

		LogTools.d(LOG_TAG, "[Method:getServerCheckStr]  md5String:" + md5String);

		return md5String == null ? "" : md5String;
	}
	
	private void downloading(TGDownloader downloader)
	{
		if (receiveListener != null)
		{
			receiveListener.downloading(downloader);
		}
	}
	
	private void downloadFinish(TGDownloader downloader)
	{
		if (receiveListener != null)
		{
			receiveListener.onFinish(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  receiveListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	private void downloadFailed(int errorCode, String errorMsg)
	{
		if (receiveListener != null)
		{
			downloader.setErrorCode(errorCode);
			downloader.setErrorMsg(errorMsg);
			receiveListener.onFailed(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  receiveListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	private void downloadStop(TGDownloader downloader)
	{
		if (receiveListener != null)
		{
			receiveListener.onStop(downloader);
		}
		else
		{
			LogTools.e(LOG_TAG,
					"[Method:failedDownload]  receiveListener is null,Please set ReceiveListener on Construct..");
		}
	}
	
	/**
	 * 该类作用及功能说明 下载数据接收监听类
	 * 
	 * @date 2014年1月8日
	 */
	public interface IDownloadReceiveListener
	{
		/**
		 * 该方法的作用:正在接收下载流
		 * 
		 * @date 2014年1月22日
		 * @param downloader
		 *            读取的字节数
		 */
		void downloading(TGDownloader downloader);

		/**
		 * 该方法的作用:读取流结束
		 * 
		 * @date 2014年1月22日
		 * @param downloader
		 *            下载线程信息
		 */
		void onFinish(TGDownloader downloader);

		/**
		 * 该方法的作用:下载出错
		 * 
		 * @date 2014年1月22日
		 * @param downloader
		 *            下载线程信息
		 */
		void onFailed(TGDownloader downloader);

		/**
		 * 该方法的作用:下载停止
		 * 
		 * @date 2014年1月22日
		 * @param downloader
		 *            下载线程信息
		 */
		void onStop(TGDownloader downloader);
	}
	
	/**
	 * 
	 * 该方法的作用: 校验下载完成后的文件流MD5值是否和服务端下发的MD5值一致
	 *           返回true, 表示下载文件与服务端文件一致；否则不一致
	 * @date 2014年7月16日
	 * @param serverCheckString
	 * @param localCheckString
	 * @return
	 */
	private boolean equalCheckStr(String serverCheckString, String localCheckString)
	{
		// 服务端没返回校验值时或第一次下载时, 不做校验
		if(TextUtils.isEmpty(serverCheckString) || TextUtils.isEmpty(localCheckString))
		{
			return true;
		}
		
		if (serverCheckString.equals(localCheckString))
		{
			return true;
		}
		else
		{
			LogTools.e(LOG_TAG, "[method:equalCheckStr]: " + "check value is not the same.");
		}

		return false;
	}
	
	/**
	 * 
	 * 该方法的作用: 获取文件保存路径
	 * @date 2014年8月23日
	 * @param headers
	 * @return
	 */
	private String getSavePath(Map<String, List<String>> headers)
	{
		String savePath = downloader.getSavePath();
		if(savePath.endsWith("/"))
		{
			savePath = savePath + getFileName(headers);
		}
		return savePath;
	}
	
	/**
	 * 
	 * 该方法的作用: 获取服务器返回文件名
	 * @date 2014年8月23日
	 * @param headers
	 * @return
	 */
	protected String getFileName(Map<String, List<String>> headers)
	{
		String serverFileName = "";
		if(null != headers)
		{
			// 服务端返回文件名称Content-Disposition
			String contentDisposition = getHeaderField("Content-Disposition", headers);
			serverFileName = contentDisposition.replace("attachment;filename=", "");
			
			LogTools.p(LOG_TAG, "[Method:getFileName]  serverFileName:"+serverFileName);
			if(null == serverFileName)
			{
				serverFileName = "";
			}
		}
		return serverFileName;
	}

	public TGDownloader getDownloader()
	{
		return downloader;
	}

	public void setDownloader(TGDownloader downloader)
	{
		this.downloader = downloader;
	}
}
