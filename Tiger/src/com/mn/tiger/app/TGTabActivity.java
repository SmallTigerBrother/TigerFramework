package com.mn.tiger.app;

import java.util.Arrays;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mn.tiger.R;
import com.mn.tiger.widget.adpter.TGListAdapter;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.mn.tiger.widget.tab.TGTabView;
import com.mn.tiger.widget.tab.TGTabView.LayoutParams;
import com.mn.tiger.widget.tab.TGTabView.OnTabChangeListener;
import com.mn.tiger.widget.viewpager.TGFragmentPagerAdapter;

/**
 * 带底部Tab的FragmentActivity
 * @author Dalang
 */
public abstract class TGTabActivity extends TGActionBarActivity implements 
    OnPageChangeListener, OnTabChangeListener
{
	/**
	 * 底部Tab
	 */
	private TGTabView tabView;
	
	/**
	 * ViewPager
	 */
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setNavigationBarVisible(false);
		setContentView(R.layout.tiger_tab_activity);
		
		tabView = (TGTabView) findViewById(R.id.tiger_tab_bar);
		viewPager = (ViewPager) findViewById(R.id.tiger_view_pager);
		
		tabView.setOnTabChangeListener(this);
	}
	
	/**
	 * 设置Tab和Fragment
	 * @param tabModels 所有的tab
	 * @param fragments 所有的fragment
	 */
	public void setTabs(TabModel[] tabModels, Fragment[] fragments)
	{
		TGFragmentPagerAdapter pagerAdapter = new TGFragmentPagerAdapter(
				getSupportFragmentManager(), Arrays.asList(fragments));
		viewPager.setAdapter(pagerAdapter);
		viewPager.setOnPageChangeListener(this);
		
		tabView.setAdapter(new TGListAdapter<TabModel>(this, Arrays.asList(tabModels),
				R.layout.tiger_fragment_tab_item, TabViewHolder.class));
		tabView.setSelection(0);
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

	/**
	 * 重置上一个tab的资源
	 * @param tabView
	 * @param lastTabIndex 上一个tab的索引
	 */
	protected void resetLastTab(TGTabView tabView, int lastTabIndex)
	{
		TabViewHolder holder = (TabViewHolder) tabView.getTabItem(lastTabIndex).getConvertView().getTag();
		TabModel tabModel = (TabModel) tabView.getAdapter().getItem(lastTabIndex);
		holder.getImageView().setImageResource(tabModel.getDefaultResId());
		holder.getTextView().setTextColor(tabModel.getDefaultTextColor());
		holder.getTextView().setTextSize(tabModel.getDefaultTextSize());
	}

	/**
	 * 高亮当前选中的的tab
	 * @param tabView
	 * @param currentTabIndex 当前tab的索引
	 */
	protected void highLightCurrentTab(TGTabView tabView, int currentTabIndex)
	{
		TabViewHolder holder = (TabViewHolder) tabView.getTabItem(currentTabIndex).getConvertView().getTag();
		TabModel tabModel = (TabModel) tabView.getAdapter().getItem(currentTabIndex);
		holder.getImageView().setImageResource(tabModel.getHighlightResId());
		holder.getTextView().setTextColor(tabModel.getHighlightTextColor());
		holder.getTextView().setTextSize(tabModel.getHighlightTextSize());
	}

	/**
	 * 获取tabView
	 * @return
	 */
	protected TGTabView getTabView()
	{
		return tabView;
	}
	
	/**
	 * 获取ViewPager
	 * @return
	 */
	protected ViewPager getViewPager()
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
	
	/**
	 * 自定义的TabViewHolder
	 * @author Dalang
	 */
	public static class TabViewHolder extends TGViewHolder<TabModel>
	{
		private ImageView imageView;
		
		private TextView textView;
		
		@Override
		public View initView(View convertView, ViewGroup parent)
		{
			View view = super.initView(convertView, parent);
			//设置tab均分
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 
					LayoutParams.WRAP_CONTENT, 1);
			view.setLayoutParams(layoutParams);
			
			imageView = (ImageView) view.findViewById(R.id.tab_item_image);
			textView = (TextView) view.findViewById(R.id.tab_item_name);
			
			return view;
		}
		
		@Override
		public void fillData(ViewGroup parent, View convertView, TabModel itemData, int position)
		{
			imageView.setImageResource(itemData.getDefaultResId());
			textView.setText(itemData.getTabName());
		}
		
		public ImageView getImageView()
		{
			return imageView;
		}
		
		public TextView getTextView()
		{
			return textView;
		}
	}
	
	/**
	 * 底部Tab数据模型
	 * @author Dalang
	 */
	protected static class TabModel
	{
		/**
		 * 默认图片资源
		 */
		private int defaultResId;
		
		/**
		 * 高亮图片资源
		 */
		private int highlightResId;
		
		/**
		 * tab名称
		 */
		private String tabName;
		
		/**
		 * 默认的文本颜色
		 */
		private int defaultTextColor = Color.BLACK;
		
		/**
		 * 选中时高亮显示的文本颜色
		 */
		private int highlightTextColor = Color.BLACK;
		
		/**
		 * 默认的文字大小
		 */
		private float defaultTextSize = 16f;
		
		/**
		 * 选中时的文字大小
		 */
		private float highlightTextSize = 16f;
		
		public TabModel()
		{
		}

		public int getDefaultResId()
		{
			return defaultResId;
		}

		public void setDefaultResId(int defaultResId)
		{
			this.defaultResId = defaultResId;
		}

		public String getTabName()
		{
			return tabName;
		}

		public void setTabName(String tabName)
		{
			this.tabName = tabName;
		}

		public int getHighlightResId()
		{
			return highlightResId;
		}

		public void setHighlightResId(int highlightResId)
		{
			this.highlightResId = highlightResId;
		}

		public int getDefaultTextColor()
		{
			return defaultTextColor;
		}

		public void setDefaultTextColor(int defaultTextColor)
		{
			this.defaultTextColor = defaultTextColor;
		}

		public int getHighlightTextColor()
		{
			return highlightTextColor;
		}

		public void setHighlightTextColor(int highlightTextColor)
		{
			this.highlightTextColor = highlightTextColor;
		}

		public float getDefaultTextSize()
		{
			return defaultTextSize;
		}

		public void setDefaultTextSize(float defaultTextSize)
		{
			this.defaultTextSize = defaultTextSize;
		}

		public float getHighlightTextSize()
		{
			return highlightTextSize;
		}

		public void setHighlightTextSize(float highlightTextSize)
		{
			this.highlightTextSize = highlightTextSize;
		}
	}
}
