package com.mn.tiger.demo;


import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.activity.AniminationDemoActivity;
import com.mn.tiger.demo.activity.NavigationBarActivity;
import com.mn.tiger.demo.activity.SlidingActivity;
import com.mn.tiger.demo.authorize.AuthorizeDemoActivity;
import com.mn.tiger.demo.datastorage.DataStorageDemoActivity;
import com.mn.tiger.demo.download.DownloadDemoActivity;
import com.mn.tiger.demo.share.ShareDemoActivity;
import com.mn.tiger.demo.widget.GridListViewActivity;
import com.mn.tiger.demo.widget.dialog.DialogDemoActivity;
import com.mn.tiger.demo.widget.pulltorefresh.PullToRefreshSwipeListViewActivity;
import com.mn.tiger.demo.widget.searchview.SearchViewActivity;
import com.mn.tiger.demo.widget.searchview.actionbar.SearchViewInActionBarActivity;
import com.mn.tiger.demo.widget.viewpager.horizontal.DotIndicatorBannerActivity;
import com.mn.tiger.demo.widget.viewpager.horizontal.HorizontalViewPagerActivity;
import com.mn.tiger.demo.widget.viewpager.horizontal.PagerSlidingTabStripActivity;
import com.mn.tiger.demo.widget.viewpager.horizontal.RecycleViewPagerActivity;
import com.mn.tiger.demo.widget.viewpager.horizontal.TransformationDemoActivity;
import com.mn.tiger.demo.widget.viewpager.horizontal.fragment.ViewPagerWithFragmentActivity;
import com.mn.tiger.demo.widget.viewpager.vertical.VerticalViewPagerActivity;
import com.mn.tiger.demo.widget.wheelview.WheelViewActivity;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;

public class MainActivity extends TGActionBarActivity implements OnItemClickListener
{
	@ViewById(id = R.id.demo_list)
	private ListView demoListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		ViewInjector.initInjectedView(this, this);
		
		ArrayList<DemoModel> demoModels = new ArrayList<MainActivity.DemoModel>();
		demoModels.add(DemoModel.NavigationBarDemo);
		demoModels.add(DemoModel.SildingMenuDemo);
		demoModels.add(DemoModel.AnimationDemo);
		demoModels.add(DemoModel.HorizontalViewPagerWithFragment);
		demoModels.add(DemoModel.HorizontalViewPager);
		demoModels.add(DemoModel.VerticalViewPager);
		demoModels.add(DemoModel.BannerViewPager);
		demoModels.add(DemoModel.TranformationViewPager);
		demoModels.add(DemoModel.RecycledViewPager);
		demoModels.add(DemoModel.PagerSlidingTabStrip);
		demoModels.add(DemoModel.SearchView);
		demoModels.add(DemoModel.SearchViewInActionBar);
		demoModels.add(DemoModel.DataStorageDemo);
		demoModels.add(DemoModel.DownloadDemo);
		demoModels.add(DemoModel.RequestDemo);
		demoModels.add(DemoModel.DialogDemo);
		demoModels.add(DemoModel.WheelViewDemo);
		demoModels.add(DemoModel.AuthorizeDemo);
		demoModels.add(DemoModel.ShareDemo);
		demoModels.add(DemoModel.PullToRefreshSwipeListView);
		demoModels.add(DemoModel.GridListView);
		
		demoListView.setAdapter(new TGListAdapter<DemoModel>(this, demoModels, -1, 
				ViewHolder.class));
		demoListView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, 
			int position, long id)
	{
		Intent intent = new Intent();
		DemoModel itemData = (DemoModel) parent.getAdapter().getItem(position);
		switch (itemData)
		{
			case NavigationBarDemo:
				intent.setClass(this, NavigationBarActivity.class);
				break;

			case SildingMenuDemo:
				intent.setClass(this, SlidingActivity.class);
				break;
				
			case AnimationDemo:
				intent.setClass(this, AniminationDemoActivity.class);
				break;

			case HorizontalViewPagerWithFragment:
				intent.setClass(this, ViewPagerWithFragmentActivity.class);
				break;
				
			case VerticalViewPager:
				intent.setClass(this, VerticalViewPagerActivity.class);
				break;
				
			case HorizontalViewPager:
				intent.setClass(this, HorizontalViewPagerActivity.class);
				break;
				
			case BannerViewPager:
				intent.setClass(this, DotIndicatorBannerActivity.class);
				break;
				
			case TranformationViewPager:
				intent.setClass(this, TransformationDemoActivity.class);
				break;
				
			case RecycledViewPager:
				intent.setClass(this, RecycleViewPagerActivity.class);
				break;
				
			case PagerSlidingTabStrip:
				intent.setClass(this, PagerSlidingTabStripActivity.class);
				break;
				
			case SearchView:
				intent.setClass(this, SearchViewActivity.class);
				break;
				
			case SearchViewInActionBar:
				intent.setClass(this, SearchViewInActionBarActivity.class);
				break;
				
			case DataStorageDemo:
				intent.setClass(this, DataStorageDemoActivity.class);
				break;
				
			case DownloadDemo:
				intent.setClass(this, DownloadDemoActivity.class);
				break;
				
			case DialogDemo:
				intent.setClass(this, DialogDemoActivity.class);
				break;
				
			case WheelViewDemo:
				intent.setClass(this, WheelViewActivity.class);
				break;
				
			case AuthorizeDemo:
				intent.setClass(this, AuthorizeDemoActivity.class);
				break;
				
			case ShareDemo:
				intent.setClass(this, ShareDemoActivity.class);
				break;
				
			case PullToRefreshSwipeListView:
				intent.setClass(this, PullToRefreshSwipeListViewActivity.class);
				break;
				
			case GridListView:
				intent.setClass(this, GridListViewActivity.class);
				break;
				
			default:
				break;
		}
		this.startActivity(intent);
	}
	
	public static class ViewHolder extends TGViewHolder<DemoModel>
	{
		private TextView textView;
		
		@Override
		public View initView(View convertView, ViewGroup parent)
		{
			textView = new TextView(getActivity());
			textView.setTextSize(24);
			textView.setPadding(10, 16, 10, 16);
			return textView;
		}
		
		@Override
		public void fillData(ViewGroup parent, View convertView, DemoModel itemData, int position)
		{
			textView.setText(itemData.demoType);
		}
	}
	
	public static enum DemoModel
	{
		NavigationBarDemo("部分自定义 NavigationBar"),
		SildingMenuDemo("SldingMenu + NavigationBar"),
		
		AnimationDemo("AnimationOfActivity"),
		
		SearchView("SearchView"),
		
		SearchViewInActionBar("SearchViewInActionBar"),
		
		HorizontalViewPager("HorizontalViewPager"),
		
		VerticalViewPager("VerticalViewPager"),
		
		HorizontalViewPagerWithFragment("HorizontalViewPager + Fragment + TabView"),
		
		BannerViewPager("BannerViewPager + Indicator"),
		
		TranformationViewPager("TranformationViewPagerDemo"),
		
		RecycledViewPager("RecycledViewPagerDemo"),
		
		PagerSlidingTabStrip("PagerSlidingTabStripDemo"),
		
		DataStorageDemo("DataBaseDemo"),
		
		DownloadDemo("DownloadDemo"),
		
		RequestDemo("RequestDemo"),
		
		DialogDemo("DialogDemo"),
		
		WheelViewDemo("WheelViewDemo"),
		
		AuthorizeDemo("AuthorzieDemo"),
		
		ShareDemo("ShareDemo"),
		
		PullToRefreshSwipeListView("PullToRefreshSwipeListViewDemo"),
		
		GridListView("GridListView");
		
		private DemoModel(String demoType)
		{
			this.demoType = demoType;
		}
		
		public final String demoType;
	}
}
