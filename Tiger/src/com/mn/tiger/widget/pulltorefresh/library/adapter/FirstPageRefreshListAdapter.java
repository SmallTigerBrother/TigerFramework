package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.widget.pulltorefresh.library.model.PageModel;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshController;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshDataController;

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
	public FirstPageRefreshListAdapter(Context context, BaseAdapter adapter, 
			IHttpErrorHandler httpErrorHandler)
	{
		super(context, adapter);
	}
	
	@Override
	public void onPullDownToRefresh(int currentPage)
	{
		IPullToRefreshController pullToRefreshController = getListView().getPullToRefreshController();
		
		//若当前页码小于起始页码时，执行网络请求，重置当前列表
		if(currentPage < pullToRefreshController.getStartPageNum())
		{
			excuteRequest(getRequestUrl(), getRequestParams(pullToRefreshController.getStartPageNum()), 
					IPullToRefreshDataController.REFRESH_LISTVIEW_RESET);
		}
		else 
		{
			super.onPullDownToRefresh(currentPage);
		}
		
	}
}
