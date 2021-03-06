package com.mn.tiger.widget.swipelistview;

import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;

import com.mn.tiger.widget.adpter.TGViewHolder;

/**
 * 支持侧滑的ViewHolder
 */
public abstract class SwipeListViewHolder<T> extends TGViewHolder<T>
{
	/**
	 * 显示在前面的视图
	 */
	private View frontView;

	/**
	 * 显示在后面的视图
	 */
	private View backView;

	@Override
	public View initView(View convertView, ViewGroup parent, int position)
	{
		View mainView = super.initView(convertView, parent, position);
		// 初始化前视图
		frontView = mainView.findViewById(getFrontViewId());
		// 初始化后视图
		if (getBackViewId() > 0)
		{
			backView = mainView.findViewById(getBackViewId());
		}

		return mainView;
	}

	/**
	 * 获取前视图
	 * 
	 * @return
	 */
	final View getFrontView()
	{
		return frontView;
	}

	/**
	 * 获取后视图
	 * 
	 * @return
	 */
	final View getBackView()
	{
		return backView;
	}

	/**
	 * 获取前视图id
	 * 
	 * @return
	 */
	protected abstract int getFrontViewId();

	/**
	 * 获取后视图id
	 * 
	 * @return
	 */
	protected abstract int getBackViewId();

	@Override
	protected void updateViewDimension(ViewGroup parent, View convertView, T itemData, int position)
	{
		frontView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener()
		{
			@Override
			public boolean onPreDraw()
			{
				backView.getLayoutParams().height = frontView.getMeasuredHeight();
				frontView.getViewTreeObserver().removeOnPreDrawListener(this);
				return false;
			}
		});
		// 设置列表侧边栏宽度
		backView.measure(MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST),
				0);

		((SwipeListView) parent).setSwipeOffsetLeft(parent.getMeasuredWidth()
				- backView.getMeasuredWidth());
	}
}
