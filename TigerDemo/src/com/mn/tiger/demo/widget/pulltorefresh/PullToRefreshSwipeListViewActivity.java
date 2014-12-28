package com.mn.tiger.demo.widget.pulltorefresh;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.demo.widget.swipelistview.SwipeListViewActivity.ViewHolder;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGListAdapter;

public class PullToRefreshSwipeListViewActivity extends TGActionBarActivity
{
	@ViewById(id = R.id.pull_to_refresh_swipe_listview)
	private ListView swipeListView;
	
	private TGListAdapter<Integer> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_to_refresh_swipe_list_view_activity);
		ViewInjector.initInjectedView(this, this);
		
		ArrayList<Integer> integers = new ArrayList<Integer>();
		for (int i = 0; i < 30; i++)
		{
			integers.add(i);
		}
		
		listAdapter = new TGListAdapter<Integer>(this, integers,
				-1, ViewHolder.class);
		swipeListView.setAdapter(listAdapter);
	}
	
}
