package com.mn.tiger.widget.pulltorefresh.library.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mn.tiger.R;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.mn.tiger.widget.xlistview.XListViewFooter;


public class XListFooterLayout extends LoadingLayout
{
	private XListViewFooter footer;
	
	public XListFooterLayout(Context context, PullToRefreshBase.Mode mode)
	{
		super(context, mode, Orientation.VERTICAL);
		FrameLayout mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mInnerLayout.removeAllViews();
		footer = new XListViewFooter(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		mInnerLayout.addView(footer, layoutParams);
	}

	@Override
	protected int getDefaultDrawableResId()
	{
		return 0;
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable)
	{
		
	}

	@Override
	protected void onPullImpl(float scaleOfLayout)
	{
		
	}

	@Override
	protected void pullToRefreshImpl()
	{
		if(null != footer)
		{
			footer.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	@Override
	protected void refreshingImpl()
	{
		if(null != footer)
		{
			footer.setState(XListViewFooter.STATE_LOADING);
		}
	}

	@Override
	protected void releaseToRefreshImpl()
	{
		if(null != footer)
		{
			footer.setState(XListViewFooter.STATE_READY);
		}
	}

	@Override
	protected void resetImpl()
	{
		if(null != footer)
		{
			footer.setState(XListViewFooter.STATE_READY);
		}
	}
}
