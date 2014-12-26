package com.mn.tiger.demo.widget.viewpager.horizontal.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGFragmentPagerAdapter;

public class ViewPagerWithFragmentActivity extends TGActionBarActivity implements 
    OnTabChangeListener, OnPageChangeListener
{
	@ViewById(id = R.id.view_pager)
	private ViewPager viewPager;
	
	@ViewById(id = R.id.view_pager_tab)
	private TGTabView tabView;
	
	/**
	 * 填充到ViewPager中的Fragment数组
	 */
	private ArrayList<Fragment> fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_fragment_tabview_activity);
		ViewInjector.initInjectedView(this, this);
		
		setupViews();
	}

	private void setupViews()
	{
		//初始化ViewPager
		fragments = new ArrayList<Fragment>();
		fragments.add(new PagerFragment_1());
		fragments.add(new PagerFragment_2());
		fragments.add(new PagerFragment_3());
		
		viewPager.setAdapter(new TGFragmentPagerAdapter(getSupportFragmentManager(), 
				fragments));
		viewPager.setOnPageChangeListener(this);
		
		//初始化TabView
		ArrayList<TabModel> tabModels = new ArrayList<TabModel>();
		TabModel tabModel_1 = new TabModel();
		tabModel_1.setImageResId(R.drawable.tiger_search_submit_icon);
		tabModel_1.setTabName("Fragment_1");
		tabModel_1.setHighlightResId(R.drawable.loading_icon_round);
		tabModels.add(tabModel_1);
		
		TabModel tabModel_2 = new TabModel();
		tabModel_2.setImageResId(R.drawable.tiger_search_close_press);
		tabModel_2.setTabName("Fragment_2");
		tabModel_2.setHighlightResId(R.drawable.tiger_search_submit_icon);
		tabModels.add(tabModel_2);
		
		TabModel tabModel_3 = new TabModel();
		tabModel_3.setImageResId(R.drawable.loading_icon_down);
		tabModel_3.setTabName("Fragment_3");
		tabModel_3.setHighlightResId(R.drawable.ic_launcher);
		tabModels.add(tabModel_3);
		
		tabView.setAdapter(new TGListAdapter<TabModel>(this, tabModels,
				R.layout.fragment_tab_item, TabViewHolder.class));
		tabView.setOnTabChangeListener(this);
		tabView.setSelection(0);
	}
	
	@Override
	public void onTabChanged(TGTabView tabView, int lastTabIndex, int currentTabIndex)
	{
		//还原上一个选中的tab
		resetTab(lastTabIndex);
		//高亮显示当前选中的tab
		highlightTab(currentTabIndex);
		//切换Page
		viewPager.setCurrentItem(currentTabIndex, false);
	}

	/**
	 * 还原tab
	 * @param index
	 */
	private void resetTab(int index)
	{
		//若index > 0，则说明存在已选中的tab
		if(index >= 0)
		{
			TabViewHolder holder = (TabViewHolder) tabView.getTabItem(index).getConvertView().getTag();
			TabModel tabModel = (TabModel) tabView.getAdapter().getItem(index);
			holder.getImageView().setImageResource(tabModel.getImageResId());
		}
	}
	
	/**
	 * 高亮tab
	 * @param index
	 */
	private void highlightTab(int index)
	{
		TabViewHolder holder = (TabViewHolder) tabView.getTabItem(index).getConvertView().getTag();
		TabModel tabModel = (TabModel) tabView.getAdapter().getItem(index);
		holder.getImageView().setImageResource(tabModel.getHighlightResId());
	}
	
	@Override
	public void onPageSelected(int page)
	{
		tabView.setSelection(page);
	}
	
	@Override
	public void onPageScrollStateChanged(int state)
	{
	}

	@Override
	public void onPageScrolled(int page, float arg1, int arg2)
	{
	}
}
