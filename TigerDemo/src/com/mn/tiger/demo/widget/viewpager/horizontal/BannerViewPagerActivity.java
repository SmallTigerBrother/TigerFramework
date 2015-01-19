package com.mn.tiger.demo.widget.viewpager.horizontal;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.DisplayUtils;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGAutoFlipViewPager;
import com.mn.tiger.widget.viewpager.TGPagerViewHolder;

public class BannerViewPagerActivity extends TGActionBarActivity implements 
    OnTabChangeListener,OnPageChangeListener
{
	/**
	 * banner视图
	 */
	@ViewById(id = R.id.banner_demo)
	private TGAutoFlipViewPager bannerViewPager;
	
	/**
	 * indicator视图
	 */
	@ViewById(id = R.id.banner_indicator)
	private TGTabView tabView;
	
	private ArrayList<Boolean> selectedStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.banner_viewpager_activity);
		ViewInjector.initInjectedView(this, this);
		setupViews();
	}
	
	/**
	 * 初始化视图
	 */
	private void setupViews()
	{
		//初始化indicator
		selectedStatus = new ArrayList<Boolean>();
		selectedStatus.add(false);
		selectedStatus.add(false);
		selectedStatus.add(false);
		selectedStatus.add(false);
		tabView.setAdapter(new TGListAdapter<Boolean>(this, selectedStatus, -1, 
				TGTabIndicatorHolder.class));
		tabView.setOnTabChangeListener(this);
		//必须在setOnTabChangeListener后调用
		tabView.setSelection(0);
		
		//初始化banner
		ArrayList<Boolean> pagerData = new ArrayList<Boolean>();
		for (int i = 0; i < 5; i++)
		{
			pagerData.add(false);
		}
		bannerViewPager.setAdapter(new TGAutoFlipViewPager.CirclePagerAdapter<Boolean>(
				this, pagerData, BannerViewHolder.class));
		
		bannerViewPager.setOnPageChangeListener(this);
		
		//开始滚动
		bannerViewPager.startScroll();
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}
	
	@Override
	public void onPageSelected(int page)
	{
		//切换indicator
		tabView.setSelection(page % 4);
	}
	
	@Override
	public void onTabChanged(TGTabView tabView, int lastSelectedIndex, int curSelectedIndex)
	{
		//还原上一个tab（判断上一次是否有选中过，若无选中项，lastSelectedIndex == -1）
		if(lastSelectedIndex >= 0)
		{
			tabView.getTabItem(lastSelectedIndex).getConvertView().setBackgroundResource(
					R.drawable.banner_indicator_dot_default);
		}
		//选中当前的tab
		tabView.getTabItem(curSelectedIndex).getConvertView().setBackgroundResource(
				R.drawable.banner_indicator_dot_selected);
	}
	
	
	/**
	 * indicatorHolder
	 */
	public static class TGTabIndicatorHolder extends TGViewHolder<Boolean>
	{
		/**
		 * indicatorView
		 */
		private ImageView imageView;
		
		@Override
		public View initView(View convertView)
		{
			//初始化indicator
			imageView = new ImageView(getActivity());
			TGTabView.LayoutParams layoutParams = new TGTabView.LayoutParams(
					DisplayUtils.dip2px(getActivity(), 4),
					DisplayUtils.dip2px(getActivity(), 4));
			layoutParams.leftMargin = DisplayUtils.dip2px(getActivity(), 4);
			layoutParams.bottomMargin = DisplayUtils.dip2px(getActivity(), 4);
			imageView.setLayoutParams(layoutParams);
			return imageView;
		}
		
		@Override
		public void fillData(Boolean itemData, int position)
		{
			imageView.setBackgroundResource(R.drawable.banner_indicator_dot_default);
		}
	}
	
	public static class BannerViewHolder extends TGPagerViewHolder<Boolean>
	{
		/**
		 * indicatorView
		 */
		private ImageView imageView;
		
		@Override
		public View initPage(int viewType)
		{
			imageView = new ImageView(getActivity());
			return imageView;
		}

		@Override
		public void fillData(Boolean itemData, int position, int viewType)
		{
			imageView.setBackgroundResource(R.drawable.ic_launcher);
		}
		
	}
	
}
