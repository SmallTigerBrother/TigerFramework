package com.mn.tiger.widget.pulltorefresh.pullinterface;

import java.util.List;

import android.content.Context;

import com.mn.tiger.widget.pulltorefresh.MPPullToRefreshListView;
import com.mn.tiger.widget.pulltorefresh.library.cache.IPageCache;
import com.mn.tiger.widget.pulltorefresh.library.cache.MPPageCache;

/**
 * 该类作用及功能说明
 * 带缓存的数据控制器
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class MPCacheDataController<T> extends MPPullToRefreshDataController<T>
{
	/**
	 * 分页缓存
	 */
	private IPageCache<T> pageCache = null;
	
	/**
	 * 数据刷新控制器
	 */
	private IPullToRefreshDataController<T> pullToRefreshDataController = null;
	
	public MPCacheDataController(Context context, MPPullToRefreshListView<T> listView)
	{
		super(context, listView);
		
		pageCache = new MPPageCache<T>();
	}
	
	public MPCacheDataController(Context context, 
			IPullToRefreshDataController<T> pullToRefreshDataInterface)
	{
		this(context, pullToRefreshDataInterface.getPullToRefreshListView());
		this.pullToRefreshDataController = pullToRefreshDataInterface;
	}
	
	@Override
	public void refreshListViewReset(List<T> items)
	{
		if(null != pullToRefreshDataController)
		{
			pullToRefreshDataController.refreshListViewReset(items);
		}
		else 
		{
			super.refreshListViewReset(items);
		}
		
		//清空缓存
		pageCache.clearPageCache();
		//保存缓存
		int curPage = getPullToRefreshListView().getPullToRefreshController().getCurrentPage();
		pageCache.addToPageCache(curPage, items);
		pageCache.saveTotalPage(getPullToRefreshListView().getPullToRefreshController().getTotalPage());
	}
	
	@Override
	public void refreshListViewAppend(List<T> items)
	{
		if(null != pullToRefreshDataController)
		{
			pullToRefreshDataController.refreshListViewAppend(items);
		}
		else 
		{
			super.refreshListViewAppend(items);
		}
		
		//保存缓存
		int curPage = getPullToRefreshListView().getPullToRefreshController().getCurrentPage();
		pageCache.addToPageCache(curPage, items);
		pageCache.saveTotalPage(getPullToRefreshListView().getPullToRefreshController().getTotalPage());
	}
	
	/**
	 * 该方法的作用:
	 * 获取分页缓存接口
	 * @author l00220455
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
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param pageCache
	 */
	public void setPageCache(IPageCache<T> pageCache)
	{
		this.pageCache = pageCache;
	}
	
}
