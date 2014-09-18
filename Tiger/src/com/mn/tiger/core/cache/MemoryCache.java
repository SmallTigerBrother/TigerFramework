package com.mn.tiger.core.cache;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.graphics.Bitmap;

import com.mn.tiger.utility.LogTools;

/**
 * 
 * 该类作用及功能说明: MemoryCache缓存,主要缓存图片到内存
 * 
 * @author zWX215434
 * @date 2014年3月26日
 */
public class MemoryCache
{
	private static final String LOG_TAG = MemoryCache.class.getSimpleName();
	// 放入缓存时是个同步操作
	// LinkedHashMap构造方法的最后一个参数true代表这个map里的元素将按照最近使用次数由少到多排列，即LRU
	// 这样的好处是如果要将缓存中的元素替换，则先遍历出最近最少使用的元素来替换以提高效率
	private Map<String, Bitmap> cache = Collections
			.synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
	// 缓存中图片所占用的字节，初始0，将通过此变量严格控制缓存所占用的堆内存
	private long size = 0;// current allocated size
	// 缓存只能占用的最大堆内存
	private long limit = 1000000;// max memory in bytes

	public MemoryCache(int cacheSize)
	{
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		if (cacheSize == 0)
		{
			cacheSize = maxMemory / 8;
		}
		setLimit(cacheSize);
	}

	public static MemoryCache getInstance(int cacheSize){
		return new MemoryCache(cacheSize);
	}
	
	/**
	 * 
	 * 该方法的作用: 设置最大堆内存
	 * 
	 * @author zWX215434
	 * @date 2014年3月26日
	 * @param new_limit
	 */
	public void setLimit(long new_limit)
	{
		limit = new_limit;
	}

	/**
	 * 
	 * 该方法的作用:从缓存中读取图片
	 * 
	 * @author zWX215434
	 * @date 2014年3月26日
	 * @param id
	 * @return
	 */
	public Bitmap get(String id)
	{
		try
		{
			if (!cache.containsKey(id))
				return null;
			return cache.get(id);
		}
		catch (NullPointerException ex)
		{
			LogTools.e(LOG_TAG, ex.getMessage(), ex.getCause());
			return null;
		}
	}

	/**
	 * 
	 * 该方法的作用:保存图片到缓存
	 * 
	 * @author zWX215434
	 * @date 2014年3月26日
	 * @param id
	 * @param bitmap
	 */
	public void put(String id, Bitmap bitmap)
	{
		try
		{
			if (cache.containsKey(id))
				size -= getSizeInBytes(cache.get(id));
			cache.put(id, bitmap);
			size += getSizeInBytes(bitmap);
			checkSize();
		}
		catch (Throwable th)
		{
			LogTools.e(LOG_TAG, th.getMessage(), th.fillInStackTrace());
			th.printStackTrace();
		}
	}

	/**
	 * 
	 * 该方法的作用:严格控制堆内存，如果超过将首先替换最近最少使用的那个图片缓存
	 * 
	 * @author zWX215434
	 * @date 2014年3月26日
	 */
	private void checkSize()
	{
		if (size > limit)
		{
			// 先遍历最近最少使用的元素
			Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
			while (iter.hasNext())
			{
				Entry<String, Bitmap> entry = iter.next();
				size -= getSizeInBytes(entry.getValue());
				iter.remove();
				if (size <= limit)
					break;
			}
		}
	}

	public void clear()
	{
		cache.clear();
	}

	/**
	 * 
	 * 该方法的作用:图片占用的内存
	 * 
	 * @author zWX215434
	 * @date 2014年3月26日
	 * @param bitmap
	 * @return
	 */
	long getSizeInBytes(Bitmap bitmap)
	{
		if (bitmap == null)
			return 0;
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
