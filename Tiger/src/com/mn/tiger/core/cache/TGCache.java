package com.mn.tiger.core.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.mn.tiger.core.parser.json.TGJsonUtils;
import com.mn.tiger.utility.BitmapUtils;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 缓存文件和读取文件工具类
 * 
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-8-14 Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights
 *       reserved.
 */
public class TGCache
{
	public static final String LOG_TAG = TGCache.class.getSimpleName();

	/** 缓存文件类型：图片类型 */
	public static final int IMAGE_TYPE = 0;

	/** 缓存文件类型：普通文件类型 */
	public static final int NORMAL_FILE_TYPE = 1;

	public static final String CACHE_SHAREDPROFERENCES_NAME = "CACHE_SAVE_PATH";

	public static final String CACHE_TIME = "cache_time";

	/** 内存缓存 */
	private static MemoryCache mMemoryCache;

	public static final String SAVE_IMAGE_TYPE = ".image";

	public static final String SAVE_NORMAL_TYPE = ".hw";

	public static final String SAVE_MDM_TYPE = ".mdm";

	/**
	 * 该方法的作用:初始化内存缓存
	 * 
	 * @author yWX158243
	 * @date 2014年3月3日
	 */
	public static void initLruCache(int cacheSize)
	{
		if (mMemoryCache == null)
		{
			synchronized (TGCache.class)
			{
				if (mMemoryCache == null)
				{
					mMemoryCache = MemoryCache.getInstance(cacheSize);
				}
			}
		}
	}

	public static void setDiskCacheSize(int maxSize)
	{

	}

	/**
	 * 
	 * 该方法的作用:保存缓存
	 * 
	 * @author zWX215434
	 * @date 2014年5月13日
	 * @param context
	 *            上下文
	 * @param key
	 *            文件名称(唯一标识，md5 url)缓存文件名称以此命名
	 * @param value
	 *            需要缓存的内容
	 */
	public static void saveCache(Context context, String key, Object value)
	{
		if (TextUtils.isEmpty(key))
		{
			throw new NullPointerException("key can't be empty!");
		}

		if (null == value)
		{
			throw new NullPointerException("value can't be empty!");
		}

		// 图片
		if (value instanceof Bitmap)
		{
			if (null == mMemoryCache)
			{// 未初始化时，使用默认的缓存大小初始化一次
				initLruCache(0);
			}
			if (null != mMemoryCache)
			{
				mMemoryCache.put(key, (Bitmap) value);
				LogTools.d(LOG_TAG, "saveCache IMAGE_TYPE  key =" + key + "  value ="
						+ ((Bitmap) value));
			}
		}
		else
		// 其它类型
		{
			File normal_file = getNormalCacheFile(context, key);
			if (null != normal_file)
			{
				saveAsDiskCache(normal_file.getAbsolutePath(), value);
			}
			else
			{
				LogTools.e(LOG_TAG, "save cache normal_file is null!");
			}
		}
	}

	/**
	 * 该方法的作用:获取默人文件类型的缓存(默认在cache文件夹下)
	 * 
	 * @author yWX158243
	 * @date 2014年3月4日
	 * @param context
	 * @param key
	 *            文件名称(唯一标识，md5 url)缓存文件名称以此命名
	 * @return
	 */
	public static Object getCache(Context context, String key)
	{
		if (null == mMemoryCache)
		{// 未初始化时，使用默认的缓存大小初始化一次
			initLruCache(0);
		}

		File cacheFile = null;
		Object content = null;

		assert null != mMemoryCache;
		content = mMemoryCache.get(key);
		if (null == content)
		{
			cacheFile = getCacheFile(context, key);
			if (cacheFile.exists())
			{
				content = BitmapUtils.decodeBitmap(context, cacheFile);
			}
			else
			{
				cacheFile = getNormalCacheFile(context, key);
				if (cacheFile.exists())
				{
					content = getDiskCache(cacheFile.getAbsolutePath());
				}
			}
			LogTools.d(LOG_TAG, "  getCache  IMAGE_TYPE key =" + key + "  decodeFile  " + content);
			if (content instanceof Bitmap)
			{
				mMemoryCache.put(key, (Bitmap) content);
			}
		}

		return content;
	}

	/**
	 * 该方法的作用:获取硬盘缓存内容
	 * 
	 * @author yWX158243
	 * @date 2014年3月4日
	 * @param fileAbsPath
	 *            文件绝对路径
	 * @return
	 */
	public static Object getDiskCache(String fileAbsPath)
	{
		Object object = null;
		ObjectInputStream objectInputStream = null;
		FileInputStream inputStream = null;
		try
		{
			File file = new File(fileAbsPath);
			if (file == null || !file.exists())
			{
				return null;
			}

			inputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(new BufferedInputStream(inputStream));

			object = objectInputStream.readObject();
		}
		catch (Exception e)
		{
			LogTools.e(e);
		}
		finally
		{
			closeInputStream(inputStream);
			closeInputStream(objectInputStream);
		}

		return object;
	}

	/**
	 * 该方法的作用:缓存到硬盘文件
	 * 
	 * @author yWX158243
	 * @date 2014年3月4日
	 * @param fileAbsPath
	 *            文件绝对路径
	 * @param content
	 *            文件内容
	 */
	private static void saveAsDiskCache(String fileAbsPath, Object content)
	{
		ObjectOutputStream objectoutputStream = null;
		FileOutputStream outStream = null;
		try
		{
			File cacheFile = new File(fileAbsPath);
			if (!cacheFile.exists())
			{
				cacheFile.createNewFile();
			}

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
			closeOutputStream(outStream);
			closeOutputStream(objectoutputStream);
		}
	}

	/**
	 * 
	 * 该方法的作用:获取图片缓存文件
	 * 
	 * @author zWX215434
	 * @date 2014年5月14日
	 * @param context
	 * @param key
	 * @return
	 */
	public static File getCacheFile(Context context, String key)
	{
		File saveFile = new File(getCacheBaseDir(context), key + TGCache.SAVE_IMAGE_TYPE);
		return saveFile;
	}

	/**
	 * 
	 * 该方法的作用:获取普通类型缓存文件
	 * 
	 * @author zWX215434
	 * @date 2014年5月14日
	 * @param context
	 * @param key
	 * @return
	 */
	private static File getNormalCacheFile(Context context, String key)
	{
		File saveFile = new File(getCacheBaseDir(context), key + TGCache.SAVE_NORMAL_TYPE);
		return saveFile;
	}

	/**
	 * 
	 * 该方法的作用:获取缓存的根目录
	 * 
	 * @author zWX215434
	 * @date 2014年5月14日
	 * @param context
	 * @return
	 */
	private static File getCacheBaseDir(Context context)
	{
		File saveFileDir = context.getCacheDir();
		if (!saveFileDir.exists())
		{
			saveFileDir.mkdirs();
		}
		return saveFileDir;
	}

	/**
	 * 
	 * 该方法的作用:MDM加密文件
	 * 
	 * @author zWX215434
	 * @date 2014年5月14日
	 * @param context
	 * @param key
	 * @return
	 */
	private static File getEncryptCacheFile(Context context, String key)
	{
		File encryptCacheFile = new File(getCacheBaseDir(context).getAbsolutePath(), key
				+ TGCache.SAVE_MDM_TYPE);
		return encryptCacheFile;
	}

	/**
	 * 该方法的作用: 保存文件和其保存的时间
	 * 
	 * @author l00220455
	 * @date 2013-8-14
	 * @param content
	 * @param identify
	 *            缓存文件名
	 * @param context
	 */
	public static void saveCache(Object content, String identify, Context context)
	{
		String filename = null;
		if (identify == null || "".equals(identify))
		{
			filename = "cache.hw";
		}
		else
		{
			filename = identify + ".hw";
		}

		writeObject(filename, content, context);
	}

	/**
	 * 该方法的作用:
	 * 
	 * @author l00220455
	 * @date 2013-11-12
	 * @param content
	 * @param identify
	 * @param context
	 * @return
	 */
	public static Object readCache(String identify, Context context)
	{
		String filename = null;
		if (identify == null || "".equals(identify))
		{
			filename = "cache.hw";
		}
		else
		{
			filename = identify + ".hw";
		}

		return readObject(filename, context);
	}

	/**
	 * 该方法的作用: 将object对象写入outFile文件
	 * 
	 * @author l00220455
	 * @date 2013-8-14
	 * @param outFile
	 * @param object
	 * @param context
	 */
	public static void writeObject(String outFile, Object object, Context context)
	{
		File dir = context.getDir("cache", Context.MODE_PRIVATE);
		File file = new File(dir, outFile);
		saveAsDiskCache(file.getAbsolutePath(), object);
	}

	/**
	 * 该方法的作用: 以object的方式读取文件中的内容
	 * 
	 * @author l00220455
	 * @date 2013-8-14
	 * @param filePath
	 * @param context
	 */
	public static Object readObject(String filePath, Context context)
	{
		File dir = context.getDir("cache", Context.MODE_PRIVATE);
		File file = new File(dir, filePath);
		return getDiskCache(file.getAbsolutePath());
	}

	/**
	 * 该方法的作用:关闭输出流
	 * 
	 * @author zWX200279
	 * @date 2014年5月12日
	 * @param stream
	 */
	public static void closeOutputStream(OutputStream stream)
	{
		if (null != stream)
		{
			try
			{
				stream.close();
			}
			catch (Exception e)
			{
				LogTools.e(e);
			}
		}
	}

	/**
	 * 该方法的作用:关闭输入流
	 * 
	 * @author zWX200279
	 * @date 2014年5月12日
	 * @param stream
	 */
	public static void closeInputStream(InputStream stream)
	{
		if (null != stream)
		{
			try
			{
				stream.close();
			}
			catch (Exception e)
			{
				LogTools.e(e);
			}
		}
	}

	/**
	 * 
	 * 该方法的作用:保存加密缓存
	 * 
	 * @author zWX200279
	 * @date 2014年5月14日
	 * @param context
	 * @param fileName
	 *            缓存文件名
	 * @param content
	 *            缓存内容
	 * @param encryption
	 *            加密方法
	 */
	public static void saveEncryptCache(Context context, String fileName, Object content,
			IEncryption encryption)
	{
		if (mMemoryCache == null)
		{// 未初始化时，使用默认的缓存大小初始化一次
			initLruCache(0);
		}
		if (content instanceof Bitmap)
		{
			if (mMemoryCache != null)
			{
				mMemoryCache.put(fileName, (Bitmap) content);
				File file = getEncryptCacheFile(context, fileName);
				encryption.encryptAndSaveBitmap(context, (Bitmap) content, file);
			}
		}
		else
		{
			File normal_file = getEncryptCacheFile(context, fileName);
			if (normal_file != null)
			{
				String contentString = TGJsonUtils.parseObject2Json(content);
				encryption.encryptAndSave(context, contentString, normal_file);;
			}
			else
			{
				LogTools.e(LOG_TAG, "save cache normal_file is null!");
			}
		}

	}

	/**
	 * 
	 * 该方法的作用:获取解密数据
	 * 
	 * @author zWX200279
	 * @date 2014年5月14日
	 * @param context
	 * @param fileName
	 *            文件名
	 * @param mClass
	 *            解析返回Object的类型
	 * @param encryption
	 *            加密方法
	 * @return
	 */
	public static Object getDecryptCache(Context context, String fileName, Class<?> mClass,
			IEncryption encryption)
	{
		if (mMemoryCache == null)
		{// 未初始化时，使用默认的缓存大小初始化一次
			initLruCache(0);
		}
		Object content = null;
		if (mMemoryCache != null)
		{
			File cacheFile = getEncryptCacheFile(context, fileName);

			// 处理第一次访问异常
			if (null == cacheFile)
			{
				return null;
			}

			if (mClass.isAssignableFrom(Bitmap.class))
			{
				content = mMemoryCache.get(fileName);
				LogTools.d(LOG_TAG, "  getCache  IMAGE_TYPE  content " + content);
				// 从磁盘缓存中获取
				if (content == null && isFileExist(encryption, cacheFile))
				{
					content = encryption.decryptBitmap(context, cacheFile);
					// 放入内存缓存
					mMemoryCache.put(fileName, (Bitmap) content);
				}
			}
			else
			{
				if (isFileExist(encryption, cacheFile))
				{
					String contentString = (String) getDiskCache(cacheFile.getAbsolutePath());
					contentString = (String) encryption.decrypt(context, contentString, cacheFile);
					// 对Map采用不同的解密方法
					if (mClass.isAssignableFrom(Map.class))
					{
						content = TGJsonUtils.parseJson2Map(contentString);
					}
					else
					{
						content = TGJsonUtils.parseJson2Object(contentString, mClass);
					}
				}

			}
		}
		return content;
	}

	/**
	 * 该方法的作用:判定缓存文件是否存在
	 * 
	 * @author zWX200279
	 * @date 2014年5月14日
	 * @param encryption
	 *            加密方法
	 * @param cacheFile
	 *            缓存文件
	 */

	private static boolean isFileExist(IEncryption encryption, File cacheFile)
	{
		if (!cacheFile.exists())
		{
			return false;
		}
		return true;
	}
}
