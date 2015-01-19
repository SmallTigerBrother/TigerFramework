package com.mn.tiger.demo.widget.viewpager.horizontal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.DisplayUtils;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGAutoFlipViewPager;

public class DotIndicatorBannerView<T> extends RelativeLayout implements OnTabChangeListener, OnPageChangeListener
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

	private ArrayList<T> dataList;
	
	private IImageViewHolder<T> imageViewHolder;

	public DotIndicatorBannerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	private void initView()
	{
		bannerViewPager = new TGAutoFlipViewPager(getContext());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		bannerViewPager.setLayoutParams(layoutParams);
		this.addView(bannerViewPager);

		tabView = new TGTabView(getContext());
		RelativeLayout.LayoutParams tabLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		tabLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tabLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		tabLayoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 8);
		tabView.setLayoutParams(tabLayoutParams);
		this.addView(tabView);

		dataList = new ArrayList<T>();
	}

	@SuppressWarnings("unchecked")
	public void setData(List<T> data)
	{
		dataList.clear();
		dataList.addAll(data);

		// 初始化indicator
		tabView.setAdapter(new TGListAdapter<T>((Activity) getContext(), (List<T>) dataList, -1,
				(Class<? extends TGViewHolder<T>>) TGTabIndicatorHolder.class));
		tabView.setOnTabChangeListener(this);
		// 必须在setOnTabChangeListener后调用
		tabView.setSelection(0);

		// 初始化banner
		bannerViewPager.setAdapter(new TGAutoFlipViewPager.CirclePagerAdapter<T>((Activity) getContext(), dataList, null)
		{
			@Override
			protected View initPageView(int viewType) throws InstantiationException, IllegalAccessException
			{
				return new ImageView(getActivity());
			}
			
			@Override
			protected void fillPageData(int position, int viewType, List<T> pageData, View viewOfPage)
			{
				if (null != pageData && pageData.size() > position && null != imageViewHolder)
				{
					imageViewHolder.fillData((ImageView) viewOfPage, pageData.get(position), position, viewType);
				}
			}
		});
		bannerViewPager.setOnPageChangeListener(this);
	}

	public void startScroll()
	{
		// 开始滚动
		bannerViewPager.startScroll();
	}
	
	public void setImageViewHolder(IImageViewHolder<T> imageViewHolder)
	{
		this.imageViewHolder = imageViewHolder;
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
		// 切换indicator
		tabView.setSelection(page % 4);
	}

	@Override
	public void onTabChanged(TGTabView tabView, int lastSelectedIndex, int curSelectedIndex)
	{
		// 还原上一个tab（判断上一次是否有选中过，若无选中项，lastSelectedIndex == -1）
		if (lastSelectedIndex >= 0)
		{
			tabView.getTabItem(lastSelectedIndex).getConvertView()
					.setBackgroundResource(R.drawable.banner_indicator_dot_default);
		}
		// 选中当前的tab
		tabView.getTabItem(curSelectedIndex).getConvertView()
				.setBackgroundResource(R.drawable.banner_indicator_dot_selected);
	}

	/**
	 * indicatorHolder
	 */
	public static class TGTabIndicatorHolder<T> extends TGViewHolder<T>
	{
		/**
		 * indicatorView
		 */
		private ImageView imageView;

		@Override
		public View initView(View convertView)
		{
			// 初始化indicator
			imageView = new ImageView(getActivity());
			TGTabView.LayoutParams layoutParams = new TGTabView.LayoutParams(DisplayUtils.dip2px(getActivity(), 4),
					DisplayUtils.dip2px(getActivity(), 4));
			layoutParams.leftMargin = DisplayUtils.dip2px(getActivity(), 4);
			layoutParams.bottomMargin = DisplayUtils.dip2px(getActivity(), 4);
			imageView.setLayoutParams(layoutParams);
			return imageView;
		}

		@Override
		public void fillData(Object itemData, int position)
		{
			imageView.setBackgroundResource(R.drawable.banner_indicator_dot_default);
		}
	}

	public static interface IImageViewHolder<T>
	{
		public void fillData(ImageView imageView, T itemData, int position, int viewType);
	}
}
