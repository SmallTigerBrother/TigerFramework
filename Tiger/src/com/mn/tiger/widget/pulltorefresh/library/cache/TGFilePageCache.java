package com.mn.tiger.widget.pulltorefresh.library.cache;

import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mn.tiger.core.cache.TGCache;


public class TGFilePageCache<T> implements IPageCache<T> 
{
	private String identity = "filePageCache";
	
	private Context context;
	
	public TGFilePageCache(Context context, String identity)
	{
		this.context = context;
		this.identity = identity;
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		if(null == cacheMap)
		{
			TGCache.saveCache(new HashMap<String, Object>(), identity, context);
		}
	}
	
	@Override
	public void addToPageCache(int page, List<T> pageList) 
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		cacheMap.put("" + page, pageList);
		
		TGCache.saveCache(cacheMap, identity, context);
	}

	@Override
	public void removePageCache(int page)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		cacheMap.remove("" + page);
		
		TGCache.saveCache(cacheMap, identity, context);
	}

	@Override
	public void clearPageCache() 
	{
		TGCache.saveCache(new HashMap<String, Object>(), identity, context);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getPageCache(int page) 
	{
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		return (List<T>) cacheMap.get("" + page);
	}

	@Override
	public void saveTotalPage(int totalPage) 
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		cacheMap.put("totalPage", Integer.valueOf(totalPage));
		
		TGCache.saveCache(cacheMap, identity, context);
	}

	@Override
	public int getTotalPage() 
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		return (Integer) cacheMap.get("totalPage");
	}
	
	@Override
	public int getCurrentCacheTotalPage() 
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> cacheMap = (HashMap<String, Object>) TGCache.readCache(
				identity, context);
		return cacheMap.size();
	}
}
