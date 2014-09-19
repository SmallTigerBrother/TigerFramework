package com.mn.tiger.widget.pulltorefresh.library.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;

import com.mn.tiger.request.TGHttpRequest;
import com.mn.tiger.request.async.TGHttpAsyncRequester;
import com.mn.tiger.request.async.TGHttpAsyncRequester.OnCancelListener;
import com.mn.tiger.request.async.TGHttpAsyncRequester.RequestListener;
import com.mn.tiger.utility.Commons;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.pulltorefresh.AutoPullToRefreshListView.OnRefreshListenerPlus;
import com.mn.tiger.widget.pulltorefresh.TGPullToRefreshListView;
import com.mn.tiger.widget.pulltorefresh.library.model.PageModel;
import com.mn.tiger.widget.pulltorefresh.pullinterface.IPullToRefreshDataController;
import com.mn.tiger.widget.pulltorefresh.pullinterface.TGCacheDataController;
import com.squareup.otto.Bus;

/**
 * 该类作用及功能说明
 * 拖动刷新列表适配器
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-10-26
 */
public class TGPullToRefreshListAdapter<T extends Object> extends TGListAdapter<T> implements OnRefreshListenerPlus, 
    RequestListener<PageModel<T>>, OnCancelListener
{
	/**
	 * 拖动刷新列表
	 */
	private TGPullToRefreshListView<T> listView = null;
	
	/**
	 * 请求Url
	 */
	private String requestUrl = null;
	
	/**
	 * 请求任务
	 */
	private TGHttpAsyncRequester<PageModel<T>> requester = null;
	
	/**
	 * 请求类型
	 */
	private int requestType = TGHttpRequest.REQUEST_GET;
	
	/**
	 * 是否绑定服务
	 */
	private boolean bindRequestTask = false;
	
	/**
	 * 请求参数
	 */
	private Object requestParams = new HashMap<String, String>();
	
	private String parserClsName = "";
	
	/**
	 * 当前页码的请求参数Key值
	 */
	private String currentPageKey = null;
	
	private Bus eventBus;
	
	private int listViewRefreshType = IPullToRefreshDataController.REFRESH_LISTVIEW_RESET;
	
	/**
	 * @date 2013-10-26
	 * 构造函数
	 * @param context
	 * @param items 列表数据
	 * @param httpErrorHandler 错误处理接口
	 */
	public TGPullToRefreshListAdapter(Context context, List<T> items) 
	{
		super(context, items);
		eventBus = new Bus();
	}
	
	/**
	 * @date 2014年4月21日
	 * @param context
	 * @param adapter 其他Adapter
	 * @param httpErrorHandler 错误处理接口
	 */
	public TGPullToRefreshListAdapter(Context context, BaseAdapter adapter)
	{
		super(context, adapter);
		eventBus = new Bus();
	}
	
	/**
	 * 该方法的作用:
	 * 获取网络请求参数，绑定服务时，必须重载该方法
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
	 * @date 2013-10-26
	 * @param listView
	 */
	public void bindListView(TGPullToRefreshListView<T> listView) 
	{
		this.listView = listView;
	}

	/**
	 * 该方法的作用:
	 * 获取绑定的拖动刷新列表
	 * @date 2013-10-26
	 * @return
	 */
	public TGPullToRefreshListView<T> getListView() 
	{
		return listView;
	}
	
	/**
	 * 该方法的作用:
	 * 设置请求类型
	 * @date 2013-10-26
	 * @param requestType
	 */
	public void setRequestType(int requestType)
	{
		this.requestType = requestType;
	}
	
	/**
	 * 该方法的作用:
	 * 执行请求任务
	 * @date 2013-10-26
	 * @param params
	 */
	protected void excuteRequest(String requestUrl, Object params, int listViewRefreshType)
	{
		Commons.cancelAsyncTask(requester);
		
		this.listViewRefreshType = listViewRefreshType;
		
		if(null != requester && null != params)
		{
			switch (requestType)
			{
				case TGHttpRequest.REQUEST_GET:
					
					requester.get(requestUrl, PageModel.class.getName(), params, this);
					break;

				case TGHttpRequest.REQUEST_POST:

					requester.post(requestUrl, PageModel.class.getName(), params, this);
					break;

				case TGHttpRequest.REQUEST_PUT:
					requester.put(requestUrl, PageModel.class.getName(), params, this);

					break;

				case TGHttpRequest.REQUEST_DELETE:
					requester.delete(requestUrl, PageModel.class.getName(), params, this);
					
					break;

				default:
					break;
			}
		}
		else 
		{
			if(bindRequestTask && null == requester)
			{
				throw new RuntimeException("The bind requestTask should not be null");
			}
		}
	}
	
	@Override
	public void onRequestStart()
	{
		//TODO 显示对话框
	}

	@Override
	public void onRequestSuccess(PageModel<T> result)
	{
		//消失对话框
		//TODO 刷新列表
		//停止拖动
		result.setListViewRefreshTpye(listViewRefreshType);
		eventBus.post(result);
	}

	@Override
	public void onRequestError(int code, String message)
	{
		//TODO 消失对话框
		//TODO 停止拖动
		//TODO 提示异常
		eventBus.post(message);
	}
	
	@Override
	public void onRequestCancel()
	{
		//TODO 停止拖动
		eventBus.post(null);
	}
	
	/**
	 * 该方法的作用:
	 * 取消请求任务
	 * @date 2013-11-8
	 */
	public void cancelRequester()
	{
		Commons.cancelAsyncTask(requester);
	}
	
	/**
	 * 该方法的作用:
	 * 获取列表刷新Handler
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
		
		if(null != listView && dataController instanceof TGCacheDataController)
		{
			TGCacheDataController<T> cacheDataController = (TGCacheDataController<T>)dataController;
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
				excuteRequest(requestUrl, getRequestParams(currentPage),
						IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
			}
		}
		else 
		{
			excuteRequest(requestUrl, getRequestParams(currentPage), 
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
		if(null != listView && dataController instanceof TGCacheDataController)
		{
			TGCacheDataController<T> cacheDataController = (TGCacheDataController<T>)dataController;
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
				excuteRequest(requestUrl, getRequestParams(currentPage),
						IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
			}
		}
		else
		{
			excuteRequest(requestUrl, getRequestParams(currentPage), 
					IPullToRefreshDataController.REFRESH_LISTVIEW_APPEND);
		}
		
	}
	
	/**
	 * 该方法的作用:
	 * 获取数据传递Message
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
	 * @date 2014年1月27日
	 * @return
	 */
	protected String getCurrentPageKey()
	{
		return currentPageKey;
	}
	
	public Bus getEventBus()
	{
		return eventBus;
	}

	public String getParserClsName()
	{
		return parserClsName;
	}

	public void setParserClsName(String parserClsName)
	{
		this.parserClsName = parserClsName;
	}
	
	public String getRequestUrl()
	{
		return requestUrl;
	}
}
