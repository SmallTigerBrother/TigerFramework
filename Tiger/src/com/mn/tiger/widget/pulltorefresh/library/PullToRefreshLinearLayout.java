package com.mn.tiger.widget.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout>
{

	public PullToRefreshLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public PullToRefreshBase.Orientation getPullToRefreshScrollDirection()
	{
		return PullToRefreshBase.Orientation.VERTICAL;
	}

	@Override
	protected LinearLayout createRefreshableView(Context context, AttributeSet attrs)
	{
		return new LinearLayout(context, attrs);
	}

	@Override
	protected boolean isReadyForPullEnd()
	{
		return true;
	}

	@Override
	protected boolean isReadyForPullStart()
	{
		return true;
	}

}
