package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

import com.mn.tiger.request.async.TGAsyncTask;
import com.mn.tiger.request.error.IHttpErrorHandler;
import com.mn.tiger.utility.Commons;
import com.mn.tiger.widget.adpter.MPListAdapter;
import com.mn.tiger.widget.pulltorefresh.AutoPullToRefreshListView.OnRefreshListenerPlus;
import com.mn.tiger.widget.pulltorefresh.MPPullToRefreshListView;
import com.mn.tiger.widget.pulltorefresh.library.request.MPPullToRefreshTask;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshDataController;
import com.mn.tiger.widget.pulltorefresh.pullinterface.MPCacheDataController;

/**
 * 该类作用及功能说明
 * 拖动刷新列表适配器
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-10-26
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class MPPullToRefreshListAdapter<T> extends MPListAdapter<T> implements OnRefreshListenerPlus
{
	/**
	 * 拖动刷新列表
	 */
	private MPPullToRefreshListView<T> listView = null;
	
	/**
	 * 请求Url
	 */
	private String requestUrl = null;
	
	/**
	 * 请求任务
	 */
	private MPPullToRefreshTask<List<T>> requestTask = null;
	
	/**
	 * 错误处理接口
	 */
	private IHttpErrorHandler httpErrorHandler;
	
	/**
	 * 请求类型
	 */
	private int requestType = TGAsyncTask.REQUEST_GET;
	
	/**
	 * 是否绑定服务
	 */
	private boolean bindRequestTask = false;
	
	/**
	 * 请求参数
	 */
	private Object requestParams = new HashMap<String, String>();
	
	/**
	 * 当前页码的请求参数Key值
	 */
	private String currentPageKey = null;
	
	/**
	 * @author l00220455
	 * @date 2013-10-26
	 * 构造函数
	 * @param context
	 * @param items 列表数据
	 * @param httpErrorHandler 错误处理接口
	 */
	public MPPullToRefreshListAdapter(Context context, List<T> items, 
			IHttpErrorHandler httpErrorHandler) 
	{
		super(context, items);
		
		this.httpErrorHandler = httpErrorHandler;
		
		requestTask = initRequestTask(httpErrorHandler);
	}
	
	/**
	 * @author l00220455
	 * @date 2014年4月21日
	 * @param context
	 * @param adapter 其他Adapter
	 * @param httpErrorHandler 错误处理接口
	 */
	public MPPullToRefreshListAdapter(Context context, BaseAdapter adapter, 
			IHttpErrorHandler httpErrorHandler)
	{
		super(context, adapter);
		
		this.httpErrorHandler = httpErrorHandler;
		
		requestTask = initRequestTask(httpErrorHandler);
	}
	
	@Override
	public Object getItem(int position) 
	{
		return super.getItem(position - 1);
	}
	
	/**
	 * 该方法的作用:
	 * 初始化请求任务，绑定服务时，必须重载该方法，必须返回一个new的对象
	 * @author l00220455
	 * @date 2013-10-26
	 * @param httpErrorHandler 错误处理接口
	 */
	protected MPPullToRefreshTask<List<T>> initRequestTask(IHttpErrorHandler httpErrorHandler)
	{
		return null;
	}
	
	/**
	 * 该方法的作用:
	 * 获取网络请求参数，绑定服务时，必须重载该方法
	 * @author l00220455
	 * @date 2013-11-12
	 * @param currentPage
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Object getRequestParams(int currentPage)
	{
		if(null != currentPageKey && null != requestParams && requestParams instanceof HashMap<?, ?>)
		{
			((HashMap<String, String>)requestParams).put(currentPageKey, currentPage + "");
		}
		return requestParams;
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求参数
	 * @author l00220455
	 * @date 2013-11-15
	 * @param params
	 */
	public void setRequestParams(Object params)
	{
		this.requestParams = params;
	}
	
	/**
	 * 该方法的作用:
	 * 绑定拖动刷新列表
	 * @author l00220455
	 * @date 2013-10-26
	 * @param listView
	 */
	public void bindListView(MPPullToRefreshListView<T> listView) 
	{
		this.listView = listView;
		if(null != requestTask && null != this.listView)
		{
			requestTask.setHandler(this.listView.getRefreshHandler());
		}
	}

	/**
	 * 该方法的作用:
	 * 获取绑定的拖动刷新列表
	 * @author l00220455
	 * @date 2013-10-26
	 * @return
	 */
	public MPPullToRefreshListView<T> getListView() 
	{
		return listView;
	}

	/**
	 * 该方法的作用:
	 * 设置请求Url
	 * @author l00220455
	 * @date 2013-10-26
	 * @param requestUrl
	 */
	public void setRequestUrl(String requestUrl) 
	{
		this.requestUrl = requestUrl;
		
		if(null != requestTask)
		{
			requestTask.setRequestUrl(requestUrl);
		}
	}

	/**
	 * 该方法的作用:
	 * 获取请求Url
	 * @author l00220455
	 * @date 2013-10-26
	 * @return
	 */
	public String getRequestUrl() 
	{
		return requestUrl;
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求类型
	 * @author l00220455
	 * @date 2013-10-26
	 * @param requestType
	 */
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
		
		if(null != requestTask)
		{
			requestTask.setRequestType(requestType);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求类型
	 * @author l00220455
	 * @date 2013-10-26
	 * @return
	 */
	public int getRequestType()
	{
		return requestType;
	}
	
	/**
	 * 该方法的作用:
	 * 执行请求任务
	 * @author l00220455
	 * @date 2013-10-26
	 * @param params
	 */
	public void excuteRequestTask(Object params, int listViewRefreshType)
	{
		Commons.cancelAsyncTask(requestTask);
		
		requestTask = initRequestTask(httpErrorHandler);
		
		if(null != requestTask && null != params)
		{
			requestTask.setListViewRefreshType(listViewRefreshType);
			requestTask.execute(params);
		}
		else 
		{
			if(bindRequestTask && null == requestTask)
			{
				throw new RuntimeException("The bind requestTask should not be null");
			}
			
			if(null == params)
			{
				throw new RuntimeException("The params should not be null");
			}
		}
	}
	
	/**
	 * 该方法的作用:
	 * 执行网络请求任务
	 * @author l00220455
	 * @date 2013-10-26
	 * @param requestTask
	 * @param params
	 */
	public void excuteRequestTask(MPPullToRefreshTask<ArrayList<T>> requestTask, 
			HashMap<String, String> params, int listViewRefreshType)
	{
		Commons.cancelAsyncTask(requestTask);
		
		if(null != requestTask)
		{
			requestTask.setListViewRefreshType(listViewRefreshType);
			requestTask.setHandler(getListviewRefreshHandler());
			requestTask.execute(params);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取请求任务
	 * @author l00220455
	 * @date 2013-10-26
	 * @return
	 */
	public MPPullToRefreshTask<List<T>> getRequestTask()
	{
		return requestTask;
	}
	
	/**
	 * 该方法的作用:
	 * 取消请求任务
	 * @author l00220455
	 * @date 2013-11-8
	 */
	public void cancelRequestTask()
	{
		if(null != requestTask)
		{
			Commons.cancelAsyncTask(requestTask);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 获取列表刷新Handler
	 * @author l00220455
	 * @date 2013-10-28
	 * @return
	 */
	public Handler getListviewRefreshHandler()
	{
		Handler handler = null;
		if(null != listView)
		{
			handler = listView.getRefreshHandler();
		}
		
		return handler;
	}
	
	/**
	 * 向下拖动回调方法
	 */
	@Override
	public void onPullDownToRefresh(int currentPage) 
	{
		IPullToRefreshDataController<T> dataController = listView.getPullToRefreshDataController();
		
		if(null != listView && dataController instanceof MPCacheDataController)
		{
			MPCacheDataController<T> cacheDataController = (MPCacheDataController<T>)dataController;
			Handler handler = listView.getRefreshHandler();
			
			List<T> cacheList = cacheDataController.getPageCache().getPageCache(currentPage);
			if(null != cacheList)
			{
				Message msg = getDeliveryMessage((List<T>)cacheList,
						cacheDataController.getPageCache().getTotalPage(), 
						IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
				handler.sendMessage(msg);
			}
			else 
			{
				excuteRequestTask(getRequestParams(currentPage), 
						IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
			}
		}
		else 
		{
			excuteRequestTask(getRequestParams(currentPage), 
					IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
		}
	}
	
	/**
	 * 向上拖动回调方法
	 */
	@Override
	public void onPullUpToRefresh(int currentPage) 
	{
		IPullToRefreshDataController<T> dataController = listView.getPullToRefreshDataController();
		if(null != listView && dataController instanceof MPCacheDataController)
		{
			MPCacheDataController<T> cacheDataController = (MPCacheDataController<T>)dataController;
			Handler handler = listView.getRefreshHandler();
			
			List<T> cacheList = cacheDataController.getPageCache().getPageCache(currentPage);
			if(null != cacheList)
			{
				Message msg = getDeliveryMessage((List<T>)cacheList,
						cacheDataController.getPageCache().getTotalPage(),
						IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
				handler.sendMessage(msg);
			}
			else 
			{
				excuteRequestTask(getRequestParams(currentPage), 
						IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
			}
		}
		else
		{
			excuteRequestTask(getRequestParams(currentPage), 
					IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
		}
		
	}
	
	/**
	 * 该方法的作用:
	 * 获取数据传递Message
	 * @author l00220455
	 * @date 2013-11-11
	 * @param pageItems
	 * @param currentPage
	 * @param totalPage
	 * @return
	 */
	private Message getDeliveryMessage(List<T> pageItems, int totalPage, int listViewRefreshType)
	{
		Message message = new Message();
		message.obj = pageItems;
		message.arg1 = totalPage;
		message.arg2 = listViewRefreshType;
		
		return message;
	}

	/**
	 * 该方法的作用:
	 * 设置是否绑定服务
	 * @author l00220455
	 * @date 2013-11-11
	 * @param bindServie
	 */
	public void setBindRequestTask(boolean bindServie) 
	{
		this.bindRequestTask = bindServie;
	}
	
	/**
	 * 该方法的作用:
	 * 是否绑定请求任务
	 * @author l00220455
	 * @date 2013-11-11
	 * @return
	 */
	public boolean isBindRequestTask() 
	{
		return bindRequestTask;
	}
	
	/**
	 * 该方法的作用:
	 * 设置当前页码请求参数的key值
	 * @author l00220455
	 * @date 2014年1月27日
	 * @param currentPageKey
	 */
	public void setCurrentPageKey(String currentPageKey)
	{
		this.currentPageKey = currentPageKey;
	}
	
	/**
	 * 该方法的作用:
	 * 获取当前页码请求参数的key值
	 * @author l00220455
	 * @date 2014年1月27日
	 * @return
	 */
	protected String getCurrentPageKey()
	{
		return currentPageKey;
	}
}
