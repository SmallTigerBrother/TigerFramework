package com.mn.tiger.request.sync.method;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.request.sync.apache.MultipartEntity;
import com.mn.tiger.utility.Commons;

/**
 * 网络请求参数
 */
public class TGHttpParams extends ConcurrentHashMap<String, HashMap<String, String>>
{
	private static final String LOG_TAG = TGHttpParams.class.getSimpleName();

	private static final long serialVersionUID = 1L;

	private final static char[] MULTIPART_CHARS = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();

	private static String ENCODING = "UTF-8";

	/**
	 * 参数边界
	 */
	private String boundary = null;

	private ByteArrayOutputStream out;

	/**
	 * 是否已设置最后的边界
	 */
	boolean isSetLast = false;

	/**
	 * 是否已设置第一个边界
	 */
	boolean isSetFirst = false;

	/**
	 * 设置字符串参数
	 * 
	 * @param params
	 */
	public void setStringParams(HashMap<String, String> params)
	{
		this.put("string_param", params);
	}

	/**
	 * 获取字符串参数
	 * 
	 * @return
	 */
	public HashMap<String, String> getStringParams()
	{
		return this.get("string_param");
	}

	/**
	 * 设置文件参数
	 * 
	 * @param params
	 */
	public void setFileParams(HashMap<String, String> params)
	{
		this.put("file_param", params);
	}

	/**
	 * 获取文件参数
	 * 
	 * @return
	 */
	public HashMap<String, String> getFileParams()
	{
		return this.get("file_param");
	}

	/**
	 * 初始化边界
	 */
	public String initBoundary()
	{
		if (TextUtils.isEmpty(boundary))
		{
			final StringBuffer buf = new StringBuffer();
			final Random rand = new Random();
			for (int i = 0; i < 30; i++)
			{
				buf.append(MULTIPART_CHARS[rand.nextInt(MULTIPART_CHARS.length)]);
			}
			this.boundary = buf.toString();
		}

		return this.boundary;
	}

	/**
	 * 将字符串参数合并成键值对
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String mergeStringParams2KeyValuePair() throws UnsupportedEncodingException
	{
		StringBuffer urlBuffer = new StringBuffer();
		Map<String, String> StrParams = getStringParams();

		if (null != StrParams)
		{
			Object[] paraKeys = StrParams.keySet().toArray();
			int parameterLength = paraKeys.length;

			for (int i = 0; i < parameterLength; i++)
			{
				urlBuffer.append(URLEncoder.encode((String) paraKeys[i], "UTF-8"));
				urlBuffer.append("=");
				urlBuffer.append(URLEncoder.encode(StrParams.get(paraKeys[i]), "utf-8"));
				if (i != (parameterLength - 1))
				{
					urlBuffer.append("&");
				}
			}
		}

		return urlBuffer.toString();
	}

	/**
	 * 将参数转换成byte数组
	 * 
	 * @return
	 */
	public byte[] toByteArray()
	{
		out = new ByteArrayOutputStream();
		initBoundary();

		HashMap<String, String> strParams = getStringParams();
		HashMap<String, String> fileParams = getFileParams();
		if (null != strParams)
		{
			for (HashMap.Entry<String, String> entry : strParams.entrySet())
			{
				addPart(entry.getKey(), entry.getValue());
			}
		}

		if (null != fileParams)
		{
			try
			{
				int lastIndex = fileParams.entrySet().size() - 1;
				int currentIndex = 0;
				FileInputStream inputStream;
				for (HashMap.Entry<String, String> entry : fileParams.entrySet())
				{
					boolean isLast = (currentIndex == lastIndex);
					inputStream = new FileInputStream(entry.getValue());
					addPart(entry.getKey(), entry.getValue(), inputStream, "application/octet-stream", isLast);
					currentIndex++;
				}
			}
			catch (Exception e)
			{
				LogTools.e(LOG_TAG, e);
			}
		}

		writeLastBoundaryIfNeeds();

		final byte[] result = out.toByteArray();

		Commons.closeOutputStream(out);
		out = null;
		return result;
	}

	/**
	 * 添加字符串参数键值对
	 * 
	 * @param key
	 * @param value
	 */
	private void addPart(final String key, final String value)
	{
		writeFirstBoundaryIfNeeds();
		try
		{
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
			out.write(value.getBytes());
			out.write(("\r\n--" + boundary + "\r\n").getBytes());
		}
		catch (final IOException e)
		{
			LogTools.e(LOG_TAG, e);
		}
	}

	/**
	 * 添加文件参数键值对
	 * 
	 * @param key
	 * @param fileName
	 * @param inputStream
	 * @param type
	 * @param isLast
	 */
	private void addPart(final String key, final String fileName, final InputStream inputStream, String type,
			final boolean isLast)
	{
		writeFirstBoundaryIfNeeds();
		try
		{
			type = "Content-Type: " + type + "\r\n";
			out.write(("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + fileName + "\"\r\n")
					.getBytes());
			out.write(type.getBytes());
			out.write("Content-Transfer-Encoding: binary\r\n\r\n".getBytes());

			final byte[] buf = new byte[4096];
			int length = 0;
			while ((length = inputStream.read(buf)) != -1)
			{
				out.write(buf, 0, length);
			}

			if (!isLast)
			{
				out.write(("\r\n--" + boundary + "\r\n").getBytes());
			}
			out.flush();
		}
		catch (final IOException e)
		{
			LogTools.e(LOG_TAG, e);
		}
		finally
		{
			Commons.closeInputStream(inputStream);
		}
	}

	/**
	 * 写入第一个Boundary
	 */
	private void writeFirstBoundaryIfNeeds()
	{
		if (!isSetFirst)
		{
			try
			{
				out.write(("--" + boundary + "\r\n").getBytes());
			}
			catch (final IOException e)
			{
				LogTools.e(LOG_TAG, e);
			}
		}

		isSetFirst = true;
	}

	/**
	 * 写入最后一个Boundary
	 */
	private void writeLastBoundaryIfNeeds()
	{
		if (isSetLast)
		{
			return;
		}

		try
		{
			out.write(("\r\n--" + boundary + "--\r\n").getBytes());
		}
		catch (final IOException e)
		{
			LogTools.e(LOG_TAG, e);
		}

		isSetLast = true;
	}

	/**
	 * Returns an HttpEntity containing all request parameters
	 */
	public HttpEntity getEntity()
	{
		HttpEntity entity = null;

		HashMap<String, String> stringParams = getStringParams();
		HashMap<String, String> fileParams = getFileParams();

		if (null != fileParams && !fileParams.isEmpty())
		{
			MultipartEntity multipartEntity = new MultipartEntity();

			// Add string params
			for (ConcurrentHashMap.Entry<String, String> entry : stringParams.entrySet())
			{
				multipartEntity.addPart(entry.getKey(), entry.getValue());
			}

			// Add file params
			int currentIndex = 0;
			int lastIndex = fileParams.entrySet().size() - 1;
			try
			{
				for (HashMap.Entry<String, String> entry : fileParams.entrySet())
				{
					String filePath = entry.getValue();
					InputStream inputStream = new FileInputStream(filePath);
					boolean isLast = currentIndex == lastIndex;
					multipartEntity.addPart(entry.getKey(), filePath, inputStream, isLast);
					currentIndex++;
				}
			}
			catch (Exception e)
			{
				LogTools.d(LOG_TAG, e.getMessage(), e);
			}
			
			entity = multipartEntity;
		}
		else
		{
			try
			{
				entity = new UrlEncodedFormEntity(getParamsList(), ENCODING);
			}
			catch (UnsupportedEncodingException e)
			{
				LogTools.d(LOG_TAG, e.getMessage(), e);
			}
		}

		return entity;
	}

	protected List<BasicNameValuePair> getParamsList()
	{
		List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();

		for (ConcurrentHashMap.Entry<String, String> entry : getStringParams().entrySet())
		{
			lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}

		return lparams;
	}
}
