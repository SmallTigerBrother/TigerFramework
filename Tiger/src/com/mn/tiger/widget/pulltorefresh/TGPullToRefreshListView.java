package com.mn.tiger.widget.pulltorefresh;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

import com.mn.tiger.widget.pulltorefresh.library.adapter.TGPullToRefreshListAdapter;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshDataController;
import com.mn.tiger.widget.pulltorefresh.pullinterface.TGPullToRefreshController;
import com.mn.tiger.widget.pulltorefresh.pullinterface.TGPullToRefreshDataController;

/**
 * 该类作用及功能说明 拖动刷新列表+
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */
public class TGPullToRefreshListView<T> extends AutoPullToRefreshListView
{
	/**
	 * 列表刷新类型
	 */
	private int refreshType = IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND;

	/**
	 * 列表适配器
	 */
	private TGPullToRefreshListAdapter<T> adapter = null;

	/**
	 * 数据刷新控制器
	 */
	private IPullToRefreshDataController<T> pullToRefreshDataController = null;

	public TGPullToRefreshListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		setPullToRefreshController(new TGPullToRefreshController(context, this));
		this.pullToRefreshDataController = new TGPullToRefreshDataController<T>(context, this);
	}

	public TGPullToRefreshListView(Context context)
	{
		super(context);

		setPullToRefreshController(new TGPullToRefreshController(context, this));
		this.pullToRefreshDataController = new TGPullToRefreshDataController<T>(context, this);
	}

	/**
	 * 界面刷新Handler，接收网络访问的结果
	 */
	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler()
	{
		/**
		 * 捕获信息
		 */
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			//获取数据总页数
			int totalPage = getTotalPage(msg);
			getPullToRefreshController().setTotalPage(totalPage);

			//获取数据、列表刷新类型
			@SuppressWarnings("unchecked")
			List<T> resultItems = (List<T>) msg.obj;
			refreshType = msg.arg2;

			if (getRefreshableView().getVisibility() != View.VISIBLE)
			{
				getRefreshableView().setVisibility(View.VISIBLE);
			}

			//刷新列表数据
			refreshListviewData(resultItems);

			//停止列表刷新，拖出视图消失
			TGPullToRefreshListView.this.onRefreshComplete();
		}

		/**
		 * 该方法的作用: 获取总页数
		 * @date 2013-10-26
		 * @param msg
		 *            handler传递过来的消息
		 * @return
		 */
		private int getTotalPage(Message msg)
		{
			int totalPage = msg.arg1;

			if (totalPage == -1)
			{
				throw new RuntimeException("Must Send TotalPage In Message Data");
			}

			return totalPage;
		}

	};

	/**
	 * 该方法的作用:
	 * 刷新列表数据
	 * @date 2014年2月10日
	 * @param resultItems
	 */
	protected void refreshListviewData(List<T> resultItems)
	{
		if (refreshType == IPullToRefreshDataController.REFRESH_LISTVIEW_RESET)
		{
			pullToRefreshDataController.refreshListViewReset(resultItems);
		}
		else
		{
			pullToRefreshDataController.refreshListViewAppend(resultItems);
		}
	}

	/**
	 * 向下拖动回调方法
	 */
	@Override
	protected void onPullDownToRefresh()
	{
		super.onPullDownToRefresh();

		if (isNeedRefresh() && null != adapter && adapter.isBindRequestTask())
		{
			adapter.onPullDownToRefresh(getPullToRefreshController().getCurrentPage());
		}
	};

	/**
	 * 向上拖动回调方法
	 */
	@Override
	protected void onPullUpToRefresh()
	{
		super.onPullUpToRefresh();

		if (isNeedRefresh() && null != adapter && adapter.isBindRequestTask())
		{
			adapter.onPullUpToRefresh(getPullToRefreshController().getCurrentPage());
		}
	}

	/**
	 * 该方法的作用: 获取界面刷新Handler
	 * @date 2013-10-26
	 * @return
	 */
	public Handler getRefreshHandler()
	{
		return handler;
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
			adapter.cancelRequestTask();
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
	 * 该方法的作用:
	 * 设置列表数据刷新控制器
	 * @date 2014年2月10日
	 * @param pullToRefreshDataController
	 */
	public void setPullToRefreshDataController(
			IPullToRefreshDataController<T> pullToRefreshDataController)
	{
		this.pullToRefreshDataController = pullToRefreshDataController;
	}

	/**
	 * 该方法的作用:
	 * 获取列表数据刷新控制器
	 * @date 2014年2月10日
	 * @return
	 */
	public IPullToRefreshDataController<T> getPullToRefreshDataController()
	{
		return pullToRefreshDataController;
	}
}
