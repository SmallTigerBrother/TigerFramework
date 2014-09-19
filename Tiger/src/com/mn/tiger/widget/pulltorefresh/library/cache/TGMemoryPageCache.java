package com.mn.tiger.widget.pulltorefresh.library.cache;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 该类作用及功能说明
 * 分页缓存（内存缓存）
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-11-12
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class TGMemoryPageCache<T> implements IPageCache<T>
{
	/**
	 * 存储数据的Map
	 */
	private LinkedHashMap<Integer, List<T>> cacheMap = null;
	
	/**
	 * 总页数
	 */
	private int totalPage = 0;
	
	/**
	 * @date 2013-11-12
	 * 构造函数
	 */
	public TGMemoryPageCache()
	{
		cacheMap = new LinkedHashMap<Integer, List<T>>();
	}
	
	@Override
	public void addToPageCache(int page, List<T> pageList) 
	{
		cacheMap.put(page, pageList);
	}

	@Override
	public void removePageCache(int page) 
	{
		cacheMap.remove(page);
	}

	@Override
	public void clearPageCache()
	{
		cacheMap.clear();
	}

	@Override
	public List<T> getPageCache(int page) 
	{
		return cacheMap.get(page);
	}

	@Override
	public void saveTotalPage(int totalPage) 
	{
		this.totalPage = totalPage;
	}

	@Override
	public int getTotalPage() 
	{
		return totalPage;
	}

	@Override
	public int getCurrentCacheTotalPage() 
	{
		return cacheMap.size();
	}
	
	
}
