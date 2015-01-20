package com.mn.tiger.widget.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mn.tiger.utility.DisplayUtils;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGAutoFlipViewPager.CirclePagerAdapter;

/**
 * 带底部圆点导航的自动滚动Banner
 * @param <T>
 */
public class DotIndicatorBannerPagerView<T> extends RelativeLayout implements OnTabChangeListener, OnPageChangeListener
{
	/**
	 * banner视图
	 */
	private TGAutoFlipViewPager bannerViewPager;

	/**
	 * indicator视图
	 */
	private TGTabView tabView;

	/**
	 * Banner中填充的数据
	 */
	private ArrayList<T> dataList;
	
	/**
	 * 图片填充接口
	 */
	private IImageViewHolder<T> imageViewHolder;
	
	/**
	 * dot默认图标资源
	 */
	private Drawable dotDefaultRes;
	
	/**
	 * dot选中资源
	 */
	private Drawable dotSelectedRes;
	
	private IndicatorShowMode indicatorShowMode = IndicatorShowMode.SHOW_AUTO;

	public DotIndicatorBannerPagerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	/**
	 * 初始化
	 */
	private void initView()
	{
		//初始化dot资源
		initDotViewBackground();
		
		//添加BannerViewPager
		bannerViewPager = new TGAutoFlipViewPager(getContext());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		bannerViewPager.setLayoutParams(layoutParams);
		this.addView(bannerViewPager);

		//添加TabView
		tabView = new TGTabView(getContext());
		RelativeLayout.LayoutParams tabLayoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		tabLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		tabLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		tabLayoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 8);
		tabView.setLayoutParams(tabLayoutParams);
		this.addView(tabView);

		//初始化数据List
		dataList = new ArrayList<T>();
	}

	/**
	 * 填充数据
	 * @param data 分页图片数据
	 */
	public void setData(List<T> data)
	{
		dataList.clear();
		dataList.addAll(data);

		// 初始化indicator
		tabView.setAdapter(new DotTabAdapter((Activity) getContext(),dataList));
		tabView.setOnTabChangeListener(this);
		// 必须在setOnTabChangeListener后调用
		tabView.setSelection(0);

		// 初始化banner
		bannerViewPager.setAdapter(new BannerPagerAdapter((Activity) getContext(), dataList));
		bannerViewPager.setOnPageChangeListener(this);
		
		//设置导航圆点显示模式
		switch (indicatorShowMode)
		{
			case HIDE_ALWAYS:
				tabView.setVisibility(View.GONE);
				break;
				
			case SHOW_AUTO:
				if(dataList.size() == 1)
				{
					tabView.setVisibility(View.GONE);
				}
				else
				{
					tabView.setVisibility(View.VISIBLE);
				}
				break;

			default:
				tabView.setVisibility(View.VISIBLE);
				break;
		}
	}

	/**
	 * 开始滚动
	 */
	public void startScroll()
	{
		// 开始滚动
		bannerViewPager.startScroll();
	}
	
	/**
	 * 停止滚动
	 */
	public void stopScroll()
	{
		bannerViewPager.stopScroll();
	}
	
	/**
	 * 设置图片资源填充接口
	 * @param imageViewHolder
	 */
	public void setImageViewHolder(IImageViewHolder<T> imageViewHolder)
	{
		this.imageViewHolder = imageViewHolder;
	}
	
	/**
	 * 设置背景资源
	 * @param defaultRes 默认资源
	 * @param selectedRes 选中时显示的资源
	 */
	public void setDotViewBackground(Drawable defaultRes, Drawable selectedRes)
	{
		this.dotDefaultRes = defaultRes;
		this.dotSelectedRes = selectedRes;
	}
	
	/**
	 * 初始化圆点视图
	 */
	private void initDotViewBackground()
	{
		GradientDrawable dotDefaultShapeDrawable = new GradientDrawable();
		dotDefaultShapeDrawable.setShape(GradientDrawable.OVAL);
		dotDefaultShapeDrawable.setColor(0xb0000000);
		dotDefaultShapeDrawable.setSize(DisplayUtils.dip2px(getContext(), 6), 
				DisplayUtils.dip2px(getContext(), 6));
		
		this.dotDefaultRes = dotDefaultShapeDrawable;
		
		GradientDrawable dotSelectedshapeDrawable = new GradientDrawable();
		dotSelectedshapeDrawable.setShape(GradientDrawable.OVAL);
		dotSelectedshapeDrawable.setSize(DisplayUtils.dip2px(getContext(), 6), 
				DisplayUtils.dip2px(getContext(), 6));
		dotSelectedshapeDrawable.setColor(0xff000000);
		
		this.dotSelectedRes = dotSelectedshapeDrawable;
	}
	
	/**
	 * 设置导航圆点显示模式
	 * @param mode
	 */
	public void setIndicatorShowMode(IndicatorShowMode mode)
	{
		this.indicatorShowMode = mode;
	}
	
	@Override
	protected void onDetachedFromWindow()
	{
		//停止滚动
		bannerViewPager.stopScroll();
		super.onDetachedFromWindow();
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
		tabView.setSelection(page % dataList.size());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onTabChanged(TGTabView tabView, int lastSelectedIndex, int curSelectedIndex)
	{
		// 还原上一个tab（判断上一次是否有选中过，若无选中项，lastSelectedIndex == -1）
		if (lastSelectedIndex >= 0)
		{
			tabView.getTabItem(lastSelectedIndex).getConvertView()
					.setBackgroundDrawable(dotDefaultRes);
		}
		// 选中当前的tab
		tabView.getTabItem(curSelectedIndex).getConvertView()
				.setBackgroundDrawable(dotSelectedRes);
	}

	/**
	 * 底部圆点适配器
	 */
	private class DotTabAdapter extends TGListAdapter<T>
	{
		public DotTabAdapter(Activity activity, List<T> items)
		{
			super(activity, items, -1, null);
		}
		
		@Override
		protected View initView()
		{
			// 初始化indicator
			ImageView imageView = new ImageView(getActivity());
			TGTabView.LayoutParams layoutParams = new TGTabView.LayoutParams(TGTabView.LayoutParams.WRAP_CONTENT, TGTabView.LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = DisplayUtils.dip2px(getActivity(), 2);
			layoutParams.rightMargin = DisplayUtils.dip2px(getActivity(), 2);
			imageView.setLayoutParams(layoutParams);
			return imageView;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void fillData(int position, View convertView, ViewGroup parent)
		{
			convertView.setBackgroundDrawable(dotDefaultRes);
		}
	}
	
	/**
	 * Banner适配器
	 */
	private class BannerPagerAdapter extends CirclePagerAdapter<T>
	{
		public BannerPagerAdapter(Activity activity, List<T> pagerData)
		{
			super(activity, pagerData, null);
		}
		
		@Override
		public int getCount()
		{
			if(dataList.size() == 1)
			{
				return 1;
			}
			else
			{
				return super.getCount();
			}
		}
		
		@Override
		protected View initPageView(int viewType) throws InstantiationException,
				IllegalAccessException
		{
			return new ImageView(getActivity());
		}
		
		@Override
		protected void fillPageData(int position, int viewType, List<T> pageData, View viewOfPage)
		{
			if (null != pageData && pageData.size() > position && null != imageViewHolder)
			{
				imageViewHolder.fillData((ImageView) viewOfPage, pageData.get(position),
						position, viewType);
			}
		}
	}
	
	/**
	 * ImageView填充接口类
	 * @param <T>
	 */
	public static interface IImageViewHolder<T>
	{
		public void fillData(ImageView imageView, T itemData, int position, int viewType);
	}
	
	/**
	 * 底部导航圆点显示模式
	 */
	public static enum IndicatorShowMode
	{
		/**
		 * 一直显示
		 */
		SHOW_ALWAYS,
		/**
		 * 自动控制，当只有一页时，自动隐藏
		 */
		SHOW_AUTO,
		/**
		 * 一直不显示
		 */
		HIDE_ALWAYS;
	}
}