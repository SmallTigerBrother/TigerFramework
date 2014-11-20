package com.mn.tiger.widget.pulltorefresh.library.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.mn.tiger.R;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase.Orientation;
import com.mn.tiger.widget.xlistview.XListViewHeader;

public class XListHeaderLayout extends LoadingLayout
{
	private XListViewHeader header;
	
	public XListHeaderLayout(Context context, PullToRefreshBase.Mode mode)
	{
		super(context, mode, Orientation.VERTICAL);
		FrameLayout mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mInnerLayout.removeAllViews();
		header = new XListViewHeader(context);
		//覆盖原有的layoutparams
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		header.getChildAt(0).setLayoutParams(lp);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
		mInnerLayout.addView(header, layoutParams);
	}

	@Override
	protected int getDefaultDrawableResId()
	{
		return R.drawable.loading_icon_round;
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
		if(null != header)
		{
			header.setState(XListViewHeader.STATE_NORMAL);
		}
	}

	@Override
	protected void refreshingImpl()
	{
		if(null != header)
		{
			header.setState(XListViewHeader.STATE_REFRESHING);
		}
	}

	@Override
	protected void releaseToRefreshImpl()
	{
		if(null != header)
		{
			header.setState(XListViewHeader.STATE_READY);
		}
	}

	@Override
	protected void resetImpl()
	{
		if(null != header)
		{
			header.setState(XListViewHeader.STATE_READY);
		}
	}
}
