package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;


/**
 * 该类作用及功能说明
 * 首页刷新适配器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 */
public class FirstPageRefreshListAdapter<T> extends TGPullToRefreshListAdapter<T>
{
	public FirstPageRefreshListAdapter(Context context, List<T> items)
	{
		super(context, items);
	}
	
	/**
	 * @date 2014年4月21日
	 * @param context
	 * @param adapter 其他Adapter
	 * @param httpErrorHandler 错误处理接口
	 */
	public FirstPageRefreshListAdapter(Context context, BaseAdapter adapter)
	{
		super(context, adapter);
	}
	
	@Override
	public void onPullDownToRefresh(int currentPage)
	{
		//若当前页码小于起始页码时，执行网络请求，重置当前列表
		if(currentPage < getStartPageNum())
		{
			excuteRequest(getRequestUrl(), getRequestParams(getStartPageNum()), 
					REFRESH_LISTVIEW_RESET);
		}
		else 
		{
			super.onPullDownToRefresh(currentPage);
		}
	}
	
	@Override
	public int countCurPagePullDownAfterPullUp(int currentPage)
	{
		//当前页码小于等于  起始页码-1  时，直接返回当前页码
		currentPage = currentPage - getShowPageMost();
		if(currentPage <= getStartPageNum() - 1)
		{
			return currentPage;
		}
		else
		{
			setCurPullOrientation(PULL_ORIENTATION_DOWN);
		}
		
		return currentPage;
	}
	
	@Override
	public int countCurPagePullDownAfterPullDown(int currentPage)
	{
		currentPage = currentPage - 1;
		return currentPage;
	}
}
