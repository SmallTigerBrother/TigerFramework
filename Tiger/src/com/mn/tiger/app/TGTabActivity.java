package com.mn.tiger.app;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.mn.tiger.R;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGFragmentPagerAdapter;

public abstract class TGTabActivity<T> extends TGActionBarActivity implements 
    OnPageChangeListener, OnTabChangeListener
{
	private TGTabView tabView;
	
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setNavigationBarVisible(false);
		setContentView(R.layout.tiger_tab_activity);
		
		tabView = (TGTabView) findViewById(R.id.tiger_tab_bar);
		viewPager = (ViewPager) findViewById(R.id.tiger_view_pager);
		
		
		TGFragmentPagerAdapter pagerAdapter = new TGFragmentPagerAdapter(
				getSupportFragmentManager(), initFragments());
		viewPager.setAdapter(pagerAdapter);
		
		tabView.setOnTabChangeListener(this);
		viewPager.setOnPageChangeListener(this);
	}
	
	protected abstract List<Fragment> initFragments();

	protected abstract void initTabView(TGTabView taView);
	
	public TGTabView getTabView()
	{
		return tabView;
	}
	
	public ViewPager getViewPager()
	{
		return viewPager;
	}
	
	@Override
	public void onPageSelected(int page)
	{
		//页码切换时，修改tab选中项
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

	@Override
	public void onTabChanged(TGTabView tabView, int lastTabIndex, int currentTabIndex)
	{
		//重置上一个选中的tab，如果从来没有设置过选中项，lastTabIndex== -1
		if(lastTabIndex >= 0)
		{
			resetLastTab(tabView, lastTabIndex);
		}
		
		//高亮显示当前Tab
		highLightCurrentTab(tabView, currentTabIndex);
		
		//设置ViewPager显示的页面
		viewPager.setCurrentItem(currentTabIndex, false);
	}
	
	protected abstract void resetLastTab(TGTabView tabView, int lastTabIndex);
	
	protected abstract void highLightCurrentTab(TGTabView tabView, int currentTabIndex);
}
