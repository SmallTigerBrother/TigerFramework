package com.mn.tiger.demo.widget.searchview.actionbar;

import android.os.Bundle;
import android.text.TextUtils;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.DisplayUtils;
import com.mn.tiger.utility.ToastUtils;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.TGNavigationBar;
import com.mn.tiger.widget.TGSearchView;
import com.mn.tiger.widget.TGSearchView.OnQueryTextListener;
import com.mn.tiger.widget.xlistview.XListView;
import com.mn.tiger.widget.xlistview.XListView.IXListViewListener;

public class SearchViewInActionBarActivity extends TGActionBarActivity implements 
    OnQueryTextListener, IXListViewListener
{
	private TGSearchView searchView;
	
	@ViewById(id = R.id.search_xlistview)
	private XListView listView;
	
//	private TGListAdapter<T> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setNavigationBarVisible(true);
		setContentView(R.layout.searchview_in_actionbar_activity);
		
		ViewInjector.initInjectedView(this, this);
		
		setupViews();
	}
	
	private void setupViews()
	{
		// 添加searchview，将searchview直接加入到navigationbar中
		TGNavigationBar navigationBar = getNavigationBar();
		navigationBar.removeAllViews();
		searchView = new TGSearchView(this);
		TGNavigationBar.LayoutParams layoutParams = new TGNavigationBar.LayoutParams(
				TGNavigationBar.LayoutParams.MATCH_PARENT, TGNavigationBar.LayoutParams.MATCH_PARENT);
		layoutParams.bottomMargin = DisplayUtils.dip2px(this, 4);
		layoutParams.topMargin = DisplayUtils.dip2px(this, 4);
		navigationBar.addView(searchView, layoutParams);
		
		searchView.setOnQueryTextListener(this);
		searchView.setQueryHint("请输入关键字");
		
		//初始化XListView
		listView.setPullLoadEnable(true);
		listView.setPullRefreshEnable(true);
		listView.setXListViewListener(this);
//		listAdapter = new TGListAdapter<T>(this, null, onvertViewLayoutId, viewHolderClass);
//		listView.setAdapter(listAdapter);
	}

	@Override
	public void onLoadMore()
	{
		//TODO 搜索请求,referId，为列表最后一行数据的标识，根据服务自行确定
//		listAdapter.getLastItem();
		requestSearch(searchView.getQueryText(), 0);
	}

	@Override
	public void onRefresh()
	{
		
	}
	
	private void requestSearch(String queryText, int referId)
	{
		
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
	
}
