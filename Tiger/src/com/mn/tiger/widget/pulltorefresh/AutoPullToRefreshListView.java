package com.mn.tiger.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

import com.mn.tiger.utility.NetworkUtils;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshListView;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshController;

/**
 * 该类作用及功能说明
 * 自动拖动刷新列表
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 */
public class AutoPullToRefreshListView extends PullToRefreshListView
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 拖动刷新时间监听器
	 */
	private OnRefreshListenerPlus onRefreshListenerPlus = null;
	
	/**
	 * 上下拖动时是否需要刷新界面
	 */
	private boolean isNeedRefresh = false;
	
	/**
	 * 拖动刷新控制器
	 */
	private IPullToRefreshController pullToRefreshController = null;
	
	public AutoPullToRefreshListView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);	
	}
	
	public AutoPullToRefreshListView(Context context)
	{
		super(context);
	}
	
	/**
	 * 向下拖动时回调方法
	 */
	@Override
 	protected void onPullDownToRefresh() 
	{
		super.onPullDownToRefresh();
		
		//检查网络是否可用
		if(!NetworkUtils.isConnectivityAvailable(getContext()))
		{
			pullToRefreshController.onPullDownToRefreshWithoutNetwork();
			return;
		}
		
		//通过计算页码判断是否需要调用刷新接口
		isNeedRefresh = pullToRefreshController.checkNeedRefreshPullDown();
		
		if(isNeedRefresh && null != onRefreshListenerPlus)
		{
			onRefreshListenerPlus.onPullDownToRefresh(pullToRefreshController.getCurrentPage());
		}
		else 
		{
			this.onRefreshComplete();
		}
	}
	
	/**
	 * 向上拖动时回调方法
	 */
	@Override
	protected void onPullUpToRefresh() 
	{
		super.onPullUpToRefresh();
		
		//检查网络是否可用
		if(!NetworkUtils.isConnectivityAvailable(getContext()))
		{
			pullToRefreshController.onPullUpToRefreshWithoutNetwork();
			return;
		}
		
		isNeedRefresh = pullToRefreshController.checkNeedRefreshPullUp();
		
		if(isNeedRefresh && null != onRefreshListenerPlus)
		{
			onRefreshListenerPlus.onPullUpToRefresh(pullToRefreshController.getCurrentPage());
		}
		else 
		{
			this.onRefreshComplete();
		}
	}
	
	/**
	 * 该方法的作用:
	 * 是否需要刷新列表
	 * @date 2014年2月10日
	 * @return
	 */
	public boolean isNeedRefresh()
	{
		return isNeedRefresh;
	}
	
	/**
	 * 该方法的作用:
	 * 设置拖动刷新事件监听器
	 * @date 2013-10-28
	 * @param onRefreshListener2
	 */
	
	public final void setOnRefreshListener(OnRefreshListenerPlus listener) 
	{
		onRefreshListenerPlus = listener;
	}
	
	/**
	 * 该方法的作用:
	 * 设置CacheColorHint
	 * @date 2013-10-28
	 * @param color
	 */
	public void setCacheColorHint(int color)
	{
		getRefreshableView().setCacheColorHint(color);
	}
	
	/**
	 * 该方法的作用:
	 * 设置拖动刷新控制器
	 * @date 2013年12月6日
	 * @return
	 */
	public IPullToRefreshController getPullToRefreshController()
	{
		return pullToRefreshController;
	}
	
	/**
	 * 该方法的作用:
	 * 获取拖动刷新控制器
	 * @date 2013年12月6日
	 * @param pullToRefreshController
	 */
	public void setPullToRefreshController(IPullToRefreshController pullToRefreshController)
	{
		this.pullToRefreshController = pullToRefreshController;
	}
	
	/**
	 * 该类作用及功能说明
	 * 列表拖动刷新事件监听器
	 * @version V2.0
	 * @see JDK1.6,android-8
	 * @date 2013-11-1
	 */
	public interface OnRefreshListenerPlus
	{
		/**
		 * 该方法的作用:
		 * 向下拖动刷新事件回调方法
		 * @date 2013-11-1
		 * @param currentPage 即将加载的页面的页码
		 */
		public void onPullDownToRefresh(int currentPage);
		
		/**
		 * 该方法的作用:
		 * 向上拖动刷新事件回调方法
		 * @date 2013-11-1
		 * @param currentPage 即将加载的页面的页码
		 */
		public void onPullUpToRefresh(int currentPage);
	}
}
