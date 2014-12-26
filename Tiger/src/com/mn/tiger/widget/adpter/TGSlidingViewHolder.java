package com.mn.tiger.widget.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.slidingmenu.SlidingMenu;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideMode;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.SlideTouchMode;
import com.mn.tiger.widget.slidingmenu.SlidingMenu.onTapListener;

/**
 * 支持侧滑的ViewHolder
 * @param <T>
 */
public abstract class TGSlidingViewHolder<T> extends TGViewHolder<T> implements 
    OnClickListener,onTapListener
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
	
	/**
	 * 列表行position
	 */
	private int position;
	
	/**
	 * 列表行id
	 */
	private long id;
	
	@Override
	public final View initView(View convertView)
	{
		//重写初始化列表行方法，将SlidingMenu作为父视图
		slidingMenu = new SlidingMenu(getActivity());
		slidingMenu.setMode(SlideMode.RIGHT);
		slidingMenu.setTouchModeAbove(SlideTouchMode.TOUCHMODE_FULLSCREEN);
		
		slidingMenu.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		
		//添加主视图
		if(getLayoutId() > 0)
		{
			contentView = initContentView(LayoutInflater.from(getActivity()).inflate(getLayoutId(), null));
		}
		else
		{
			contentView = initContentView(null);
		}
		
		slidingMenu.setContent(contentView);
		
		//添加菜单
		menuView = initMenu();
		slidingMenu.setMenu(menuView);
		
		//设置点击事件（伪装onItemClick事件）
		slidingMenu.setOnClickListener(this);
		//设置轻击事件（用于还原menu状态）
		slidingMenu.setOnTapListener(this);
		
		ViewInjector.initInjectedView(this, slidingMenu);
		
		return slidingMenu;
	}
	
	/**
	 * 初始化主视图
	 * @return
	 */
	public View initContentView(View contentView)
	{
		return contentView;
	}
	
	/**
	 * 初始化菜单
	 * @return
	 */
	public abstract View initMenu();
	
	@Override
	public void updateViewDimension(T itemData, final int position, final ViewGroup parent)
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
		
		this.position = position;
		this.id = getAdapter().getItemId(position);
	}
	
	@Override
	public void onClick(View v)
	{
		//自身处理onItemClick事件，执行统一操作
		onItemTap((AdapterView<?>) slidingMenu.getParent(), slidingMenu.getContent(), 
						position, id);
		
		//伪装成onItemClick事件
		OnItemClickListener onItemClickListener =
				((AbsListView)slidingMenu.getParent()).getOnItemClickListener();
		onItemClickListener.onItemClick((AdapterView<?>) slidingMenu.getParent(), slidingMenu.getContent(), 
				position, id);
	}
	
	@Override
	public boolean onTap(View view)
	{
		onItemTap((AdapterView<?>) slidingMenu.getParent(), slidingMenu, position, id);
		return false;
	}
	
	/**
	 * 自身处理onItemClick事件，默认会收起其他列表行的menu，如果想保留其他列表行的menu，请重写该方法
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	public void onItemTap(AdapterView<?> parent, View view, int position, long id)
	{
		int childCount = parent.getChildCount();
		SlidingMenu slidingMenu;
		for(int i = 0; i < childCount; i++)
		{
			slidingMenu = (SlidingMenu)parent.getChildAt(i);
			if(slidingMenu.isMenuShowing())
			{
				slidingMenu.showContent();
			}
		}
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
				if(view instanceof SlidingMenu)
				{
					//长按时展开菜单
					((SlidingMenu)view).showMenu();
				}
				return false;
			}
		};
	}
}