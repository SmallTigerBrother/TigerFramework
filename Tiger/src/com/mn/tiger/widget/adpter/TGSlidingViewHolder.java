package com.mn.tiger.widget.adpter;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.mn.tiger.widget.slidingmenu.CustomViewAbove;
import com.mn.tiger.widget.slidingmenu.SlidingMenu;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideMode;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideTouchMode;

/**
 * 支持侧滑的ViewHolder
 * @param <T>
 */
public abstract class TGSlidingViewHolder<T> extends TGViewHolder<T>
{
	/**
	 * 执行侧滑的SlidingMenu
	 */
	private SlidingMenu slidingMenu;
	
	/**
	 * 正常显示的主视图
	 */
	private View contentView;
	
	/**
	 * 滑出菜单
	 */
	private View menuView;
	
	@Override
	public final View initView(View convertView)
	{
		//重写初始化列表行方法，将SlidingMenu作为父视图
		slidingMenu = new SlidingMenu(getActivity());
		slidingMenu.setMode(SlideMode.RIGHT);
		slidingMenu.setTouchModeAbove(SlideTouchMode.TOUCHMODE_FULLSCREEN);
		
		slidingMenu.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		
		//添加主视图
		contentView = initContentView();
		slidingMenu.setContent(contentView);
		
		//添加菜单
		menuView = initMenu();
		slidingMenu.setMenu(menuView);
		
		return slidingMenu;
	}
	
	/**
	 * 初始化主视图
	 * @return
	 */
	public abstract View initContentView();
	
	/**
	 * 初始化菜单
	 * @return
	 */
	public abstract View initMenu();
	
	@Override
	public void updateViewDimension(T itemData, int position, ViewGroup parent)
	{
		contentView.measure(0, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
		menuView.measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST), 0);
		
		//设置SlidingMenu宽高
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, contentView.getMeasuredHeight());
		slidingMenu.setLayoutParams(layoutParams);
		
		//自适应menu宽度
		slidingMenu.setBehindOffset(parent.getMeasuredWidth() - 
				menuView.getMeasuredWidth());
		
		//还原列表行展开状态
		slidingMenu.showContentRightNow();
	}
	
	/**
	 * 滚动事件监听器，滚动时自动收起menu
	 * @return
	 */
	public static OnScrollListener newSlidingOnScrollListener()
	{
		return new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount)
			{
				//列表滚动时，还原已被滑出的菜单
				int childCount = view.getChildCount();
				View child;
				for(int i = 0; i < childCount; i++)
				{
					child = view.getChildAt(i);
					if(child instanceof SlidingMenu)
					{
						((SlidingMenu)child).showContent();
					}
				}
			}
		};
	}
	
	/**
	 * 列表行长按事件监听器，长按时展开菜单
	 * @return
	 */
	public static OnItemLongClickListener newSlidingOnItemLongClickListener()
	{
		return new OnItemLongClickListener()
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
			{
				if(view instanceof CustomViewAbove)
				{
					//长按时展开菜单
					((CustomViewAbove)view).setCurrentItem(0);;
				}
				return false;
			}
		};
	}
}