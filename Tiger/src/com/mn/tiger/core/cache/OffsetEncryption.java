package com.mn.tiger.core.cache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明:使用偏移方式加解密
 * 
 * @date 2014年5月15日
 */
public class OffsetEncryption implements IEncryption
{
	/** 图片加密使用的密钥 */
	private int KEY = 0X86;

	@Override
	public void encryptAndSave(Context context, String content, File file)
	{
		char[] contentChars = content.toCharArray();
		for (int i = 0; i < contentChars.length; i++)
		{
			int signChar = contentChars[i];
			// 偏移
			contentChars[i] = (char) (signChar - 3);
		}
		saveAsDiskCache(file, String.valueOf(contentChars));
	}

	@Override
	public String decrypt(Context context, String content, File file)
	{
		char[] contentChars = content.toCharArray();
		for (int i = 0; i < contentChars.length; i++)
		{
			int signChar = contentChars[i];
			// 与加密对应
			contentChars[i] = (char) (signChar + 3);
		}
		return String.valueOf(contentChars);
	}

	@Override
	public void encryptAndSaveBitmap(Context context, Bitmap bitmap, File file)
	{
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
		InputStream inputStream = new ByteArrayInputStream(arrayOutputStream.toByteArray());
		FileOutputStream fileOutputStream = null;
		int read = 0;
		try
		{
			fileOutputStream = new FileOutputStream(file);
			while ((read = inputStream.read()) > -1)
			{
				// 密钥
				fileOutputStream.write(read ^ KEY);
			}
			fileOutputStream.flush();
		}
		catch (IOException e)
		{
			LogTools.e("encryption", e);
		}
		finally
		{
			TGCache.closeOutputStream(fileOutputStream);
			TGCache.closeInputStream(inputStream);
			TGCache.closeOutputStream(arrayOutputStream);
		}
	}

	@Override
	public Object decryptBitmap(Context context, File file)
	{
		Bitmap bitmap = null;
		List<Byte> list = new ArrayList<Byte>();
		FileInputStream fileInputStream = null;
		try
		{
			fileInputStream = new FileInputStream(file);
			int read = 0;
			while ((read = fileInputStream.read()) > -1)
			{
				// 密钥
				read = read ^ KEY;
				list.add((byte) read);
			}
			byte[] arr = new byte[list.size()];
			for (int i = 0; i < arr.length; i++)
			{
				// list转成byte[]
				arr[i] = (Byte) list.get(i);
			}
			bitmap = BitmapFactory.decodeByteArray(arr, 0, arr.length);
		}
		catch (IOException e)
		{
			LogTools.e(e);
		}
		finally
		{
			TGCache.closeInputStream(fileInputStream);
		}
		return bitmap;
	}

	/**
	 * 该方法的作用:保存数据到本地磁盘
	 * 
	 * @date 2014年5月12日
	 * @param fileAbsPath
	 * @param content
	 */
	private void saveAsDiskCache(File cacheFile, Object content)
	{
		ObjectOutputStream objectoutputStream = null;
		FileOutputStream outStream = null;
		try
		{
			outStream = new FileOutputStream(cacheFile);
			objectoutputStream = new ObjectOutputStream(new BufferedOutputStream(outStream));
			objectoutputStream.writeObject(content);
			objectoutputStream.flush();
		}
		catch (Exception e)
		{
			LogTools.e(e);
		}
		finally
		{
			TGCache.closeOutputStream(outStream);
			TGCache.closeOutputStream(objectoutputStream);
		}
	}

}
