package com.mn.tiger.demo.widget.searchview;


import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ToastUtils;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.TGSearchView.OnQueryTextListener;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase.Mode;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshListView;

/**
 * TGSearchView与PullToRefreshListView配合使用，Adapter注释
 */
public class SearchViewActivity extends TGActionBarActivity implements 
    OnQueryTextListener, OnRefreshListener
{
	@ViewById(id = R.id.searchview)
	private TGSearchView searchView;
	
	@ViewById(id = R.id.search_listview)
	private PullToRefreshListView listView;
	
//	private TGListAdapter<T> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchview_activity);
		
		ViewInjector.initInjectedView(this, this);
		setupViews();
	}
	
	/**
	 * 初始化视图
	 */
	protected void setupViews()
	{
		//初始化seachview
		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint("请输入关键字");
		
		//初始化PullToRefreshListView
		listView.setOnRefreshListener(this);
		listView.setMode(Mode.PULL_FROM_END);
//		listAdapter = new TGListAdapter<T>(this, null, onvertViewLayoutId, viewHolderClass);
//		listView.setAdapter(listAdapter);
	}

	@Override
	public void onQueryTextChange(CharSequence queryText)
	{
		
	}

	@Override
	public void onQueryTextSubmit(CharSequence queryText)
	{
		if(!TextUtils.isEmpty(queryText))
		{
			requestSearch(queryText.toString(), 0);
			return;
		}
		
		ToastUtils.showToast(this, "您输入的内容不能为空");
	}

	@Override
	public void onTextCleaned()
	{
		
	}
	
	private void requestSearch(String queryText, int referId)
	{
		
	}

	@Override
	public void onPullDownToRefresh()
	{
		//TODO 搜索请求,referId，为列表最后一行数据的标识，根据服务自行确定
//		listAdapter.getLastItem();
		requestSearch(searchView.getQueryText(), 0);
	}

	@Override
	public void onPullUpToRefresh()
	{
		//不实现
	}
}
