package com.mn.tiger.widget.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.pulltorefresh.library.internal.EmptyViewMethodAccessor;
import com.mn.tiger.widget.pulltorefresh.library.internal.LoadingLayout;
import com.mn.tiger.widget.pulltorefresh.library.internal.MoreLayout;
import com.mn.tiger.widget.pulltorefresh.library.internal.OriginalLoadingLayout;

/**
 * 
 * 
 * 该类作用及功能说明:listview的封装
 * 
 * @author nKF50342
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-6-19 Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights
 *       reserved.
 */
public class PullToRefreshListView extends
		PullToRefreshAdapterViewBase<ListView> {

	private LoadingLayout mHeaderLoadingView;
	private MoreLayout mFooterLoadingView;

	private FrameLayout mLvFooterLoadingFrame;
	private boolean mAddedLvFooter = false;

	/**
	 * 
	 * 
	 * 该类作用及功能说明:listview为空时显示的view
	 * 
	 * @author nKF50342
	 * @version V2.0
	 * @see JDK1.6,android-8
	 * @date 2012-6-19 Copyright Huawei Technologies Co., Ltd. 1998-2011. All
	 *       rights reserved.
	 */
	class InternalListView extends ListView implements EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setAdapter(ListAdapter adapter) {
			// Add the Footer View at the last possible moment
			if (!mAddedLvFooter && null != mLvFooterLoadingFrame) {
				addFooterView(mLvFooterLoadingFrame, null, false);
				mAddedLvFooter = true;
			}

			super.setAdapter(adapter);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	/**
	 * 
	 * @author nKF50342
	 * @date 2012-6-19 构造函数
	 * @param context
	 */
	public PullToRefreshListView(Context context) {
		super(context);
		setDisableScrollingWhileRefreshing(false);
	}

	/**
	 * 
	 * @author nKF50342
	 * @date 2012-6-19 构造函数
	 * @param context
	 * @param mode
	 */
	public PullToRefreshListView(Context context, int mode) {
		super(context, mode);
		setDisableScrollingWhileRefreshing(false);
	}

	/**
	 * 
	 * @author nKF50342
	 * @date 2012-6-19 构造函数
	 * @param context
	 * @param attrs
	 */
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDisableScrollingWhileRefreshing(false);
	}

	/**
	 * 菜单上下文
	 */
	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}

	public void setReleaseLabel(String releaseLabel) {
		super.setReleaseLabel(releaseLabel);

		if (null != mHeaderLoadingView) {
			mHeaderLoadingView.setReleaseLabel(releaseLabel);
		}
		if (null != mFooterLoadingView) {
			mFooterLoadingView.setReleaseLabel(releaseLabel);
		}
	}

	public void setPullLabel(String pullLabel) {
		super.setPullLabel(pullLabel);

		if (null != mHeaderLoadingView) {
			mHeaderLoadingView.setPullLabel(pullLabel);
		}
		if (null != mFooterLoadingView) {
			mFooterLoadingView.setPullLabel(pullLabel);
		}
	}

	public void setRefreshingLabel(String refreshingLabel) {
		super.setRefreshingLabel(refreshingLabel);

		if (null != mHeaderLoadingView) {
			mHeaderLoadingView.setRefreshingLabel(refreshingLabel);
		}
		if (null != mFooterLoadingView) {
			mFooterLoadingView.setRefreshingLabel(refreshingLabel);
		}
	}

	/**
	 * 
	 * 该方法的作用:创建listview 参数: 返回值: 异常: 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-19
	 * @return
	 */
	@Override
	protected final ListView createRefreshableView(Context context,
			AttributeSet attrs) {
		ListView lv = new InternalListView(context, attrs);

		final int mode = getMode();

		// Loading View Strings
		String pullLabel = context.getString(CR.getStringsId(context,
				"mjet_pull_to_refresh_pull_label"));
		String refreshingLabel = context.getString(CR.getStringsId(context,
				"mjet_pull_to_refresh_refreshing_label"));
		String releaseLabel = context.getString(CR.getStringsId(context,
				"mjet_pull_to_refresh_release_label"));

		// Get Styles from attrs
		int[] attrIds = new int[] {
				CR.getAttrId(context, "mjet_ptrAdapterViewBackground"),
				CR.getAttrId(context, "mjet_ptrHeaderBackground"),
				CR.getAttrId(context, "mjet_ptrHeaderTextColor"),
				CR.getAttrId(context, "mjet_ptrMode") };
		TypedArray typedArray = context.obtainStyledAttributes(attrs, attrIds);

		// Add Loading Views
		if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
			FrameLayout frame = new FrameLayout(context);
			mHeaderLoadingView = new LoadingLayout(context,
					MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel,
					refreshingLabel, typedArray);
			frame.addView(mHeaderLoadingView,
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			mHeaderLoadingView.setVisibility(View.GONE);
			lv.addHeaderView(frame, null, false);
		}
		if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
			mLvFooterLoadingFrame = new FrameLayout(context);
			mFooterLoadingView = new MoreLayout(context);
			mLvFooterLoadingFrame.addView(mFooterLoadingView,
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.WRAP_CONTENT);
			mFooterLoadingView.setVisibility(View.GONE);
		}

		typedArray.recycle();

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}

	/**
	 * 
	 * 该方法的作用:设置listview的刷新状态 参数: 返回值: 异常: 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-19
	 * @return
	 */
	@Override
	protected void setRefreshingInternal(boolean doScroll) {

		// If we're empty, then the header/footer views won't show so we use the
		// normal method
		ListAdapter adapter = mRefreshableView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			super.setRefreshingInternal(doScroll);
			return;
		}

		super.setRefreshingInternal(false);

		final OriginalLoadingLayout originalLoadingLayout, listViewLoadingLayout;
		final int selection, scrollToY;

		switch (getCurrentMode()) {
		case MODE_PULL_UP_TO_REFRESH:
			originalLoadingLayout = getFooterLayout();
			listViewLoadingLayout = mFooterLoadingView;
			selection = mRefreshableView.getCount() - 1;
			scrollToY = getScrollY() - getHeaderHeight();
			break;
		case MODE_PULL_DOWN_TO_REFRESH:
		default:
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;
			selection = 0;
			scrollToY = getScrollY() + getHeaderHeight();
			break;
		}

		if (doScroll) {
			// We scroll slightly so that the ListView's header/footer is at the
			// same Y position as our normal header/footer
			setHeaderScroll(scrollToY);
		}

		// Hide our original Loading View
		originalLoadingLayout.setVisibility(View.INVISIBLE);

		// Show the ListView Loading View and set it to refresh
		listViewLoadingLayout.setVisibility(View.VISIBLE);
		listViewLoadingLayout.refreshing();

		if (doScroll) {
			// Make sure the ListView is scrolled to show the loading
			// header/footer
			mRefreshableView.setSelection(selection);

			// Smooth scroll as normal
			smoothScrollTo(0);
		}
	}

	/**
	 * 
	 * 该方法的作用:重置头 参数: 返回值: 异常: 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-19
	 * @return
	 */
	@Override
	protected void resetHeader() {

		// If we're empty, then the header/footer views won't show so we use the
		// normal method
		ListAdapter adapter = mRefreshableView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			super.resetHeader();
			return;
		}

		OriginalLoadingLayout originalLoadingLayout;
		OriginalLoadingLayout listViewLoadingLayout;

		int scrollToHeight = getHeaderHeight();
		int selection;

		switch (getCurrentMode()) {
		case MODE_PULL_UP_TO_REFRESH:
			originalLoadingLayout = getFooterLayout();
			listViewLoadingLayout = mFooterLoadingView;

			selection = mRefreshableView.getCount() - 1;
			break;
		case MODE_PULL_DOWN_TO_REFRESH:
		default:
			originalLoadingLayout = getHeaderLayout();
			listViewLoadingLayout = mHeaderLoadingView;
			scrollToHeight *= -1;
			selection = 0;
			break;
		}

		// Set our Original View to Visible
		originalLoadingLayout.setVisibility(View.VISIBLE);

		// Scroll so our View is at the same Y as the ListView header/footer,
		// but only scroll if we've pulled to refresh
		if (getState() != MANUAL_REFRESHING) {
			mRefreshableView.setSelection(selection);
			setHeaderScroll(scrollToHeight);
		}

		// Hide the ListView Header/Footer
		listViewLoadingLayout.setVisibility(View.GONE);

		super.resetHeader();
	}

	/**
	 * 
	 * 该方法的作用:根据这个值用于判断是否为头部滑动 参数: 返回值: 异常: 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-19
	 * @return
	 */
	protected int getNumberInternalHeaderViews() {
		return null != mHeaderLoadingView ? 1 : 0;
	}

	/**
	 * 
	 * 该方法的作用:根据这个值用于判断是否为尾部滑动 参数: 返回值: 异常: 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-19
	 * @return
	 */
	protected int getNumberInternalFooterViews() {
		return null != mFooterLoadingView ? 1 : 0;
	}

}
