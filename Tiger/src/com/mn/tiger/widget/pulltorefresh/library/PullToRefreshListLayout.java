package com.mn.tiger.widget.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;

import com.mn.tiger.widget.ListLayout;

public class PullToRefreshListLayout extends PullToRefreshBase<ListLayout>
{

	public PullToRefreshListLayout(Context context)
	{
		super(context);
	}

	public PullToRefreshListLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullToRefreshListLayout(Context context, Mode mode)
	{
		super(context, mode);
	}

	public PullToRefreshListLayout(Context context, Mode mode, AnimationStyle style)
	{
		super(context, mode, style);
	}

	@Override
	public PullToRefreshBase.Orientation getPullToRefreshScrollDirection()
	{
		return PullToRefreshBase.Orientation.VERTICAL;
	}

	@Override
	protected ListLayout createRefreshableView(Context context, AttributeSet attrs)
	{
		return new ListLayout(context, attrs);
	}

	@Override
	protected boolean isReadyForPullEnd()
	{
		return isLastItemVisible();
	}

	@Override
	protected boolean isReadyForPullStart()
	{
		return isFirstItemVisible();
	}

	private boolean isFirstItemVisible()
	{
		final Adapter adapter = mRefreshableView.getAdapter();

		if (null == adapter || adapter.isEmpty())
		{
			return true;
		}
		else
		{

			/**
			 * This check should really just be:
			 * mRefreshableView.getFirstVisiblePosition() == 0, but PtRListView
			 * internally use a HeaderView which messes the positions up. For
			 * now we'll just add one to account for it and rely on the inner
			 * condition which checks getTop().
			 */
			if (mRefreshableView.getFirstVisiblePosition() <= 1)
			{
				final View firstVisibleChild = mRefreshableView.getChildAt(0);
				if (firstVisibleChild != null)
				{
					return firstVisibleChild.getTop() >= mRefreshableView.getTop();
				}
			}
		}

		return false;
	}

	private boolean isLastItemVisible()
	{
		final Adapter adapter = mRefreshableView.getAdapter();

		if (null == adapter || adapter.isEmpty())
		{
			return true;
		}
		else
		{
			final int lastItemPosition = mRefreshableView.getCount() - 1;
			final int lastVisiblePosition = mRefreshableView.getLastVisiblePosition();

			/**
			 * This check should really just be: lastVisiblePosition ==
			 * lastItemPosition, but PtRListView internally uses a FooterView
			 * which messes the positions up. For me we'll just subtract one to
			 * account for it and rely on the inner condition which checks
			 * getBottom().
			 */
			if (lastVisiblePosition >= lastItemPosition - 1)
			{
				final int childIndex = lastVisiblePosition
						- mRefreshableView.getFirstVisiblePosition();
				final View lastVisibleChild = mRefreshableView.getChildAt(childIndex);
				if (lastVisibleChild != null)
				{
					return lastVisibleChild.getBottom() <= mRefreshableView.getBottom();
				}
			}
		}

		return false;
	}
}
