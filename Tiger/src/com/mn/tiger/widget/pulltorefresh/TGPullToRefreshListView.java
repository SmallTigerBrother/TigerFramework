package com.mn.tiger.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView.OnItemClickListener;

import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshListView;
import com.mn.tiger.widget.pulltorefresh.library.adapter.TGPullToRefreshListAdapter;

/**
 * 该类作用及功能说明 拖动刷新列表+
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGPullToRefreshListView<T> extends PullToRefreshListView
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
	 * 列表适配器
	 */
	private TGPullToRefreshListAdapter<T> adapter = null;

	public TGPullToRefreshListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TGPullToRefreshListView(Context context)
	{
		super(context);
	}

	/**
	 * 向下拖动回调方法
	 */
	@Override
	protected void onPullDownToRefresh()
	{
		super.onPullDownToRefresh();
		
		//通过计算页码判断是否需要调用刷新接口
		if (adapter.checkNeedRefreshPullDown() && null != onRefreshListenerPlus)
		{
			onRefreshListenerPlus.onPullDownToRefresh(adapter.getCurrentPage());
		}
		else
		{
			this.onRefreshComplete();
		}
	};

	/**
	 * 向上拖动回调方法
	 */
	@Override
	protected void onPullUpToRefresh()
	{
		super.onPullUpToRefresh();

		if(adapter.checkNeedRefreshPullUp() && null != onRefreshListenerPlus)
		{
			onRefreshListenerPlus.onPullUpToRefresh(adapter.getCurrentPage());
		}
		else 
		{
			this.onRefreshComplete();
		}
	}

	/**
	 * 界面移除时，取消网络请求
	 */
	@Override
	protected void onDetachedFromWindow()
	{
		super.onDetachedFromWindow();

		if (null != adapter)
		{
			adapter.cancelRequester();
		}
	}

	/**
	 * 该方法的作用: 设置列表适配器
	 * @date 2013-10-26
	 * @param adapter
	 */
	public void setAdapter(TGPullToRefreshListAdapter<T> adapter)
	{
		this.adapter = adapter;

		getRefreshableView().setAdapter(adapter);

		adapter.bindListView(this);
	}
	
	/**
	 * 该方法的作用:
	 * 获取列表适配器
	 * @date 2014年3月13日
	 * @return
	 */
	public TGPullToRefreshListAdapter<T> getAdapter()
	{
		return adapter;
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
	 * 设置拖动刷新事件监听器
	 * @date 2013-10-28
	 * @param onRefreshListener2
	 */
	
	public void setOnRefreshListener(OnRefreshListenerPlus listener) 
	{
		this.onRefreshListenerPlus = listener;
	}
	
	/**
	 * 该方法的作用: 设置列表行点击事件
	 * 
	 * @date 2013-11-12
	 * @param onItemClickListener
	 */
	public void setOnItemClickListener(OnItemClickListener onItemClickListener)
	{
		getRefreshableView().setOnItemClickListener(onItemClickListener);
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
