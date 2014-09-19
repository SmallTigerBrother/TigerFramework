package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.List;

import android.content.Context;

import com.mn.tiger.widget.pulltorefresh.library.cache.IPageCache;
import com.mn.tiger.widget.pulltorefresh.library.cache.TGMemoryPageCache;

public class CacheFreshListAdapter<T> extends TGPullToRefreshListAdapter<T>
{
	/**
	 * 分页缓存
	 */
	private IPageCache<T> pageCache = null;
	
	public CacheFreshListAdapter(Context context, List<T> items)
	{
		super(context, items);
		
		pageCache = new TGMemoryPageCache<T>();
	}

	public CacheFreshListAdapter(Context context, TGPullToRefreshListAdapter<T> adapter)
	{
		super(context, adapter);
		
		pageCache = new TGMemoryPageCache<T>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void resetListData(List<T> items)
	{
		if(null != getDecoratedAdapter())
		{
			((TGPullToRefreshListAdapter<T>)getDecoratedAdapter()).resetListData(items);
		}
		else 
		{
			super.resetListData(items);
		}
		
		//清空缓存
		pageCache.clearPageCache();
		//保存缓存
		pageCache.addToPageCache(getCurrentPage(), items);
		pageCache.saveTotalPage(getTotalPage());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void appendListData(List<T> items)
	{
		if(null != getDecoratedAdapter())
		{
			((TGPullToRefreshListAdapter<T>)getDecoratedAdapter()).appendListData(items);
		}
		else 
		{
			super.appendListData(items);
		}
		
		//保存缓存
		pageCache.addToPageCache(getCurrentPage(), items);
		pageCache.saveTotalPage(getTotalPage());
	}
	
	/**
	 * 该方法的作用:
	 * 获取分页缓存接口
	 * @date 2014年2月10日
	 * @return
	 */
	public IPageCache<T> getPageCache()
	{
		return pageCache;
	}
	
	/**
	 * 该方法的作用:
	 * 设置分页缓存接口
	 * @date 2014年2月10日
	 * @param pageCache
	 */
	public void setPageCache(IPageCache<T> pageCache)
	{
		this.pageCache = pageCache;
	}
}
