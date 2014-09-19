package com.mn.tiger.widget.tab;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * 自定义Tab视图
 */
public class TGTabView extends LinearLayout
{

	/**
	 * 传入的item是否均分MpTabView的整个宽度
	 */
	private boolean isAverage = false;
	/**
	 * 在允许均分时每个item占的weight
	 */
	private static final int WEIGHT = 1;
	/**
	 * 所有TabItem选项
	 */
	private ArrayList<TabItem> tabItems;

	/**
	 * Tab切换事件监听器
	 */
	private OnTabChangeListener onTabChangeListener;

	/**
	 * 当前选中的Tab项的索引值
	 */
	private int currentTabIndex = -1;

	/**
	 * 适配器
	 */
	private BaseAdapter adapter;

	/**
	 * 数据观察者对象，用于更新Tabview
	 */
	private DataSetObserver dataSetObserver = null;

	public TGTabView(Context context)
	{
		super(context);

		tabItems = new ArrayList<TGTabView.TabItem>();
	}

	public TGTabView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		tabItems = new ArrayList<TGTabView.TabItem>();
	}

	/**
	 * 获取当前选中的Tab的索引值
	 * 
	 * @return
	 */
	public int getCurrentTabIndex()
	{
		return currentTabIndex;
	}

	/**
	 * 获取TabItem
	 * 
	 * @param index
	 *            TabItem的索引值
	 * @return
	 */
	public TabItem getTabItem(int index)
	{
		return tabItems.get(index);
	}

	/**
	 * 添加TabItem
	 * 
	 * @param tabItem
	 */
	public void addTabItem(TabItem tabItem)
	{
		tabItems.add(tabItem.getIndex(), tabItem);
		View itemView = tabItem.getContentView();
		if (isAverage)
		{
			//对每个itemView加weight属性均分
			LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT, WEIGHT);
			itemView.setLayoutParams(params);
		}
		this.addView(itemView);
	}

	/**
	 * 该方法的作用:设置是否均分MPTabview的宽度
	 * 
	 * @date 2014-3-14
	 * @param isAverage
	 */
	public void setIsAverageWidth(boolean isAverage)
	{
		this.isAverage = isAverage;
		if (adapter != null)
		{
			setAdapter(adapter);
		}
	}

	/**
	 * 设置Tab切换事件监听器
	 * 
	 * @param onTabChangeListener
	 */
	public void setOnTabChangeListener(OnTabChangeListener onTabChangeListener)
	{
		this.onTabChangeListener = onTabChangeListener;
		// 设置各个Tab的点击事件
		setTabListeners();
	}

	/**
	 * 设置所有Tab项的事件监听器
	 */
	private void setTabListeners()
	{
		// 设置各个Tab的点击事件
		for (TabItem tabItem : tabItems)
		{
			final int tabIndex = tabItem.getIndex();
			tabItem.getContentView().setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View view)
				{
					setSelection(tabIndex);
				}
			});
		}
	}

	/**
	 * 设置选中项
	 * 
	 * @param index
	 */
	public void setSelection(int index)
	{
		if (index < 0)
		{
			return;
		}

		if (currentTabIndex == index)
		{
			return;
		}

		// 执行Tab切换事件
		if (null != onTabChangeListener)
		{
			int lastTabIndex = currentTabIndex;
			currentTabIndex = index;
			onTabChangeListener.onTabChanged(this, lastTabIndex, currentTabIndex);
		}
	}

	/**
	 * 设置适配器
	 * 
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter)
	{
		// 添加TabItem
		this.removeAllViews();
		this.adapter = adapter;
		this.tabItems.clear();
		for (int i = 0; i < adapter.getCount(); i++)
		{
			TabItem tabItem = new TabItem();
			tabItem.setIndex(i);
			tabItem.setContentView(adapter.getView(i, null, this));
			this.addTabItem(tabItem);
		}

		// 注册数据观察者
		if (null != dataSetObserver)
		{
			adapter.unregisterDataSetObserver(dataSetObserver);
		}
		dataSetObserver = new TabViewDatasetObserver();
		adapter.registerDataSetObserver(dataSetObserver);

		// 设置选中项
		if (currentTabIndex != -1)
		{
			setSelection(currentTabIndex);
		}
	}

	/**
	 * Tab选中项
	 */
	public static class TabItem
	{
		/**
		 * 内容视图
		 */
		private View contentView;

		/**
		 * 索引值
		 */
		private int index;

		/**
		 * 获取内容视图
		 * 
		 * @return 内容视图
		 */
		public View getContentView()
		{
			return contentView;
		}

		/**
		 * 设置内容视图
		 * 
		 * @param contentView
		 *            内容视图
		 */
		public void setContentView(View contentView)
		{
			this.contentView = contentView;
		}

		/**
		 * 获取索引值
		 * 
		 * @return 索引值
		 */
		public int getIndex()
		{
			return index;
		}

		/**
		 * 设置索引值
		 * 
		 * @param index
		 *            索引值
		 */
		public void setIndex(int index)
		{
			this.index = index;
		}
	}

	/**
	 * 数据观察者类
	 */
	private class TabViewDatasetObserver extends DataSetObserver
	{
		@Override
		public void onChanged()
		{
			setAdapter(adapter);
		}

		@Override
		public void onInvalidated()
		{
			super.onInvalidated();
		}
	}

	/**
	 * Tab切换事件监听器接口
	 */
	public static interface OnTabChangeListener
	{
		void onTabChanged(TGTabView tabView, int lastTabIndex, int currentTabIndex);
	}

	/**
	 * 该方法的作用:获取Tabview中的所有的Item的view
	 * 
	 * @date 2014-3-10
	 * @return
	 */
	public ArrayList<TabItem> getTabItems()
	{
		return tabItems;
	}
}
