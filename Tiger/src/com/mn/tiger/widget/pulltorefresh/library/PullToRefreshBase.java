package com.mn.tiger.widget.pulltorefresh.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.LogTools;
import com.mn.tiger.widget.pulltorefresh.library.internal.LoadingLayout;
import com.mn.tiger.widget.pulltorefresh.library.internal.MoreLayout;
import com.mn.tiger.widget.pulltorefresh.library.internal.OriginalLoadingLayout;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout {

	/**
	 * 该类作用及功能说明:用于滑动效果
	 * @version V2.0
	 * @see JDK1.6,android-8
	 * @date 2012-6-19 Copyright Huawei Technologies Co., Ltd. 1998-2011. All
	 *       rights reserved.
	 */
	final class SmoothScrollRunnable implements Runnable {

		static final int ANIMATION_DURATION_MS = 190;
		static final int ANIMATION_FPS = 1000 / 60;

		private final Interpolator mInterpolator;
		private final int mScrollToY;
		private final int mScrollFromY;
		private final Handler mHandler;

		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		private int mCurrentY = -1;

		/**
		 * 
		 * @date 2012-6-19 构造函数
		 * @param handler
		 * @param fromY
		 * @param toY
		 */
		public SmoothScrollRunnable(Handler handler, int fromY, int toY) {
			mHandler = handler;
			mScrollFromY = fromY;
			mScrollToY = toY;
			mInterpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		public void run() {

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
						/ ANIMATION_DURATION_MS;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY)
						* mInterpolator
								.getInterpolation(normalizedTime / 1000f));
				mCurrentY = mScrollFromY - deltaY;
				setHeaderScroll(mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				mHandler.postDelayed(this, ANIMATION_FPS);
			}
		}

		public void stop() {
			mContinueRunning = false;
			mHandler.removeCallbacks(this);
		}
	}

	// ===========================================================
	// Constants
	// ===========================================================

	static final boolean DEBUG = false;
	static final String LOG_TAG = "PullToRefresh";

	static final float FRICTION = 2.0f;

	static final int PULL_TO_REFRESH = 0x0;
	static final int RELEASE_TO_REFRESH = 0x1;
	static final int REFRESHING = 0x2;
	static final int MANUAL_REFRESHING = 0x3;

	public static final int MODE_PULL_DOWN_TO_REFRESH = 0x1;
	public static final int MODE_PULL_UP_TO_REFRESH = 0x2;
	public static final int MODE_BOTH = 0x3;

	// ===========================================================
	// Fields
	// ===========================================================

	private int mTouchSlop;

	private float mInitialMotionY;
	private float mLastMotionX;
	private float mLastMotionY;
	private boolean mIsBeingDragged = false;

	private int mState = PULL_TO_REFRESH;
	private int mMode = MODE_BOTH;
	private int mCurrentMode;

	private boolean mDisableScrollingWhileRefreshing = false;

	T mRefreshableView;
	private boolean mIsPullToRefreshEnabled = true;

	private LoadingLayout mHeaderLayout;
	private OriginalLoadingLayout mFooterLayout;
	private int mHeaderHeight, mFooterHeight;

	private final Handler mHandler = new Handler();

	private OnRefreshListener mOnRefreshListener;
	private OnRefreshListener2 mOnRefreshListener2;

	private SmoothScrollRunnable mCurrentSmoothScrollRunnable;

	// ===========================================================
	// Constructors
	// ===========================================================
	private boolean isPullUpEnable = true;// 是否显示向上拉更多
	private boolean isPullDownEnable = true;// 是否显示向下拉刷新

	private boolean isScroll = false;

	/**
	 * 控制参数，设定是否在该类中执行拖动后的刷新功能，默认值为执行刷新
	 */
	protected boolean isRefreshInBase = true;

	public void setPullDownEnable(boolean enable) {
		this.isPullDownEnable = enable;

	}

	public void setScrollEnable(boolean enable) {
		this.isScroll = enable;
	}

	public void setPullUpEnable(boolean enable) {
		this.isPullUpEnable = enable;
	}

	/**
	 * 
	 * @date 2012-6-19 构造函数
	 * @param context
	 */
	public PullToRefreshBase(Context context) {
		super(context);
		init(context, null);
	}

	/**
	 * 
	 * @date 2012-6-19 构造函数
	 * @param context
	 * @param mode
	 */
	public PullToRefreshBase(Context context, int mode) {
		super(context);
		mMode = mode;
		init(context, null);
	}

	/**
	 * 
	 * @date 2012-6-19 构造函数
	 * @param context
	 * @param attrs
	 */
	public PullToRefreshBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Deprecated. Use {@link #getRefreshableView()} from now on.
	 * 
	 * @deprecated
	 * @return The Refreshable View which is currently wrapped
	 */
	public final T getAdapterView() {
		return mRefreshableView;
	}

	/**
	 * Get the Wrapped Refreshable View. Anything returned here has already been
	 * added to the content view.
	 * 
	 * @return The View which is currently wrapped
	 */
	public final T getRefreshableView() {
		return mRefreshableView;
	}

	/**
	 * Whether Pull-to-Refresh is enabled
	 * 
	 * @return enabled
	 */
	public final boolean isPullToRefreshEnabled() {
		return mIsPullToRefreshEnabled;
	}

	/**
	 * Get the mode that this view is currently in. This is only really useful
	 * when using <code>MODE_BOTH</code>.
	 * 
	 * @return int of either <code>MODE_PULL_DOWN_TO_REFRESH</code> or
	 *         <code>MODE_PULL_UP_TO_REFRESH</code>
	 */
	public final int getCurrentMode() {
		return mCurrentMode;
	}

	/**
	 * Get the mode that this view has been set to. If this returns
	 * <code>MODE_BOTH</code>, you can use <code>getCurrentMode()</code> to
	 * check which mode the view is currently in
	 * 
	 * @return int of either <code>MODE_PULL_DOWN_TO_REFRESH</code>,
	 *         <code>MODE_PULL_UP_TO_REFRESH</code> or <code>MODE_BOTH</code>
	 */
	public final int getMode() {
		return mMode;
	}

	public final void setMode(int mode) {
		this.mMode = mode;
	}

	/**
	 * Returns whether the widget has disabled scrolling on the Refreshable View
	 * while refreshing.
	 * 
	 * @return true if the widget has disabled scrolling while refreshing
	 */
	public final boolean isDisableScrollingWhileRefreshing() {
		return mDisableScrollingWhileRefreshing;
	}

	/**
	 * Returns whether the Widget is currently in the Refreshing mState
	 * 
	 * @return true if the Widget is currently refreshing
	 */
	public final boolean isRefreshing() {
		return mState == REFRESHING || mState == MANUAL_REFRESHING;
	}

	/**
	 * By default the Widget disabled scrolling on the Refreshable View while
	 * refreshing. This method can change this behaviour.
	 * 
	 * @param disableScrollingWhileRefreshing
	 *            - true if you want to disable scrolling while refreshing
	 */
	public final void setDisableScrollingWhileRefreshing(
			boolean disableScrollingWhileRefreshing) {
		mDisableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
	}

	/**
	 * Mark the current Refresh as complete. Will Reset the UI and hide the
	 * Refreshing View
	 */
	public final void onRefreshComplete() {
		if (mState != PULL_TO_REFRESH) {
			resetHeader();
		}
	}

	/**
	 * Set OnRefreshListener for the Widget
	 * 
	 * @param listener
	 *            - Listener to be used when the Widget is set to Refresh
	 */
	public final void setOnRefreshListener(OnRefreshListener listener) {
		mOnRefreshListener = listener;
	}

	/**
	 * Set OnRefreshListener for the Widget
	 * 
	 * @param listener
	 *            - Listener to be used when the Widget is set to Refresh
	 */
	public final void setOnRefreshListener(OnRefreshListener2 listener) {
		mOnRefreshListener2 = listener;
	}

	public final OnRefreshListener2 getOnRefreshListener2() {
		return mOnRefreshListener2;
	}

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * 
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		mIsPullToRefreshEnabled = enable;
	}

	/**
	 * Set Text to show when the Widget is being pulled, and will refresh when
	 * released
	 * 
	 * @param releaseLabel
	 *            - String to display
	 */
	public void setReleaseLabel(String releaseLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setReleaseLabel(releaseLabel);
		}
		if (null != mFooterLayout) {
			mFooterLayout.setReleaseLabel(releaseLabel);
		}
	}

	public void setDownTextLabel(String str) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setDownTextLabel(str);
		}

	}

	/**
	 * Set Text to show when the Widget is being Pulled
	 * 
	 * @param pullLabel
	 *            - String to display
	 */
	public void setPullLabel(String pullLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setPullLabel(pullLabel);
		}
		if (null != mFooterLayout) {
			mFooterLayout.setPullLabel(pullLabel);
		}
	}

	/**
	 * Set Text to show when the Widget is refreshing
	 * 
	 * @param refreshingLabel
	 *            - String to display
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setRefreshingLabel(refreshingLabel);
		}
		if (null != mFooterLayout) {
			mFooterLayout.setRefreshingLabel(refreshingLabel);
		}
	}

	public final void setRefreshing() {
		setRefreshing(true);
	}

	/**
	 * Sets the Widget to be in the refresh mState. The UI will be updated to
	 * show the 'Refreshing' view.
	 * 
	 * @param doScroll
	 *            - true if you want to force a scroll to the Refreshing view.
	 */
	public final void setRefreshing(boolean doScroll) {
		if (!isRefreshing()) {
			setRefreshingInternal(doScroll);
			mState = MANUAL_REFRESHING;
		}
	}

	/**
	 * Sets the Widget to be in the refresh mState. The UI will be updated to
	 * show the 'Refreshing' view.
	 * 
	 * @param doScroll
	 *            - true if you want to force a scroll to the Refreshing view.
	 */
	public final void setRefreshing(int currentMode) {
		if (!isRefreshing()) {
			this.mCurrentMode = currentMode;
			setRefreshingInternal(true);
			mState = MANUAL_REFRESHING;
		}
	}

	public final boolean hasPullFromTop() {
		return mCurrentMode != MODE_PULL_UP_TO_REFRESH;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	/**
	 * 
	 * 该方法的作用:用于处理上拉下拉滑动事件
	 * 
	 * @date 2012-6-19
	 * @param context
	 * @param attrs
	 */
	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		if (!mIsPullToRefreshEnabled) {
			return false;
		}
		if (isRefreshing() && isScroll) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				mInitialMotionY = event.getY();
				return true;
			}
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				mLastMotionY = event.getY();
				pullEvent();
				return true;
			}
		}

		if (isRefreshing() && mDisableScrollingWhileRefreshing) {
			return true;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getEdgeFlags() != 0) {
			return false;
		}

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE: {
			if (mIsBeingDragged) {
				mLastMotionY = event.getY();
				pullEvent();
				return true;
			}
			break;
		}

		case MotionEvent.ACTION_DOWN: {
			if (isReadyForPull()) {
				mLastMotionY = mInitialMotionY = event.getY();
				return true;
			}
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if (mIsBeingDragged) {
				mIsBeingDragged = false;

				if (mState == RELEASE_TO_REFRESH) {

					if (null != mOnRefreshListener) {
						setRefreshingInternal(true);
						mOnRefreshListener.onRefresh();
						return true;

					} else if (null != mOnRefreshListener2) {
						setRefreshingInternal(true);
						if (mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {

							if (isRefreshInBase) {
								mOnRefreshListener2.onPullDownToRefresh();
							}
						} else if (mCurrentMode == MODE_PULL_UP_TO_REFRESH) {

							if (isRefreshInBase) {
								mOnRefreshListener2.onPullUpToRefresh();
							}
						}
						return true;
					} else {
						setRefreshingInternal(true);
						if (mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {
							onPullDownToRefresh();
						} else if (mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
							onPullUpToRefresh();
						}
					}

					return true;
				}

				smoothScrollTo(0);
				return true;
			}
			break;
		}
		default:
			break;
		}

		return false;
	}

	protected void onPullDownToRefresh() {

	}

	protected void onPullUpToRefresh() {

	}

	/**
	 * 
	 * 该方法的作用:用于分配所有滑动事件 在什么情况下调用:
	 * 
	 * @date 2012-6-19
	 * @param context
	 * @param attrs
	 */
	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {

		if (!mIsPullToRefreshEnabled) {
			return false;
		}

		if (isRefreshing() && isScroll) {// 跟随webview一起滑动
			if (isReadyForPullDown()
					&& mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {
				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& getScrollY() != 0) {// 按下
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					final float dy = event.getY() - mLastMotionY;
					if (getScrollY() != 0) {// 跟随滚动
						return true;
					} else if (dy > 0 && Math.abs(getScrollY()) < mHeaderHeight) {
						return true;
					}
				}
			} else if (isReadyForPullUp()
					&& mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& getScrollY() != 0) {// 按下
					return true;
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					final float dy = event.getY() - mLastMotionY;
					if (getScrollY() != 0) {// 跟随滚动
						return true;
					} else if (dy < 0 && getScrollY() < mFooterHeight) {
						return true;
					}
				}
			}

		}
		if (isRefreshing() && mDisableScrollingWhileRefreshing) {
			return true;
		}

		final int action = event.getAction();

		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			mIsBeingDragged = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
			return true;
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			if (isReadyForPull()) {

				final float y = event.getY();
				final float dy = y - mLastMotionY;
				final float yDiff = Math.abs(dy);
				final float xDiff = Math.abs(event.getX() - mLastMotionX);

				if (yDiff > mTouchSlop && yDiff > xDiff) {
					if ((mMode == MODE_PULL_DOWN_TO_REFRESH || mMode == MODE_BOTH)
							&& dy >= 0.0001f
							&& isReadyForPullDown()
							&& !isRefreshing() && isPullDownEnable) {
						mLastMotionY = y;
						mIsBeingDragged = true;
						if (mMode == MODE_BOTH) {
							mCurrentMode = MODE_PULL_DOWN_TO_REFRESH;
						}
					} else if ((mMode == MODE_PULL_UP_TO_REFRESH || mMode == MODE_BOTH)
							&& dy <= 0.0001f
							&& isReadyForPullUp()
							&& !isRefreshing() && isPullUpEnable) {
						mLastMotionY = y;
						mIsBeingDragged = true;
						if (mMode == MODE_BOTH) {
							mCurrentMode = MODE_PULL_UP_TO_REFRESH;
						}
					}
				}
			}
			break;
		}
		case MotionEvent.ACTION_DOWN: {
			if (isReadyForPull()) {
				mLastMotionY = mInitialMotionY = event.getY();
				mLastMotionX = event.getX();
				mIsBeingDragged = false;
			}
			break;
		}
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
		default:
			break;
		}

		return mIsBeingDragged;
	}

	protected void addRefreshableView(Context context, T refreshableView) {
		addView(refreshableView, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f));
	}

	/**
	 * This is implemented by derived classes to return the created View. If you
	 * need to use a custom View (such as a custom ListView), override this
	 * method and return an instance of your custom class.
	 * 
	 * Be sure to set the ID of the view in this method, especially if you're
	 * using a ListActivity or ListFragment.
	 * 
	 * @param context
	 *            Context to create view with
	 * @param attrs
	 *            AttributeSet from wrapped class. Means that anything you
	 *            include in the XML layout declaration will be routed to the
	 *            created View
	 * @return New instance of the Refreshable View
	 */
	protected abstract T createRefreshableView(Context context,
			AttributeSet attrs);

	public final OriginalLoadingLayout getFooterLayout() {
		return mFooterLayout;
	}

	public final LoadingLayout getHeaderLayout() {
		return mHeaderLayout;
	}

	protected final int getHeaderHeight() {
		return mHeaderHeight;
	}

	protected final int getState() {
		return mState;
	}

	/**
	 * Implemented by derived class to return whether the View is in a mState
	 * where the user can Pull to Refresh by scrolling down.
	 * 
	 * @return true if the View is currently the correct mState (for example,
	 *         top of a ListView)
	 */
	protected abstract boolean isReadyForPullDown();

	/**
	 * Implemented by derived class to return whether the View is in a mState
	 * where the user can Pull to Refresh by scrolling up.
	 * 
	 * @return true if the View is currently in the correct mState (for example,
	 *         bottom of a ListView)
	 */
	protected abstract boolean isReadyForPullUp();

	// ===========================================================
	// Methods
	// ===========================================================

	protected void resetHeader() {
		mState = PULL_TO_REFRESH;
		mIsBeingDragged = false;

		if (null != mHeaderLayout) {
			mHeaderLayout.reset();
		}
		if (null != mFooterLayout) {
			mFooterLayout.reset();
		}

		smoothScrollTo(0);
	}

	protected void setRefreshingInternal(boolean doScroll) {
		mState = REFRESHING;

		if (null != mHeaderLayout) {
			mHeaderLayout.refreshing();
		}
		if (null != mFooterLayout) {
			mFooterLayout.refreshing();
		}

		if (doScroll) {
			smoothScrollTo(mCurrentMode == MODE_PULL_DOWN_TO_REFRESH ? -mHeaderHeight
					: mHeaderHeight);
		}
	}

	protected final void setHeaderScroll(int y) {
		scrollTo(0, y);
	}

	protected final void smoothScrollTo(int y) {
		if (null != mCurrentSmoothScrollRunnable) {
			mCurrentSmoothScrollRunnable.stop();
		}

		if (getScrollY() != y) {
			mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(mHandler,
					getScrollY(), y);
			mHandler.post(mCurrentSmoothScrollRunnable);
		}
	}

	/**
	 * 
	 * 该方法的作用:初始化各种值 在什么情况下调用:
	 * 
	 * @date 2012-6-19
	 * @param context
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {

		setOrientation(LinearLayout.VERTICAL);

		mTouchSlop = ViewConfiguration.getTouchSlop();

		int[] attrIds = new int[] {
				CR.getAttrId(context, "mjet_ptrAdapterViewBackground"),
				CR.getAttrId(context, "mjet_ptrHeaderBackground"),
				CR.getAttrId(context, "mjet_ptrHeaderTextColor"),
				CR.getAttrId(context, "mjet_ptrMode") };
		// Styleables from XML,强制通过代码添加
		TypedArray typedArray = context.obtainStyledAttributes(attrs, attrIds);
		if (typedArray.hasValue(CR.getStyleableId(context,
				"mjet_PullToRefresh_ptrMode"))) {
			mMode = typedArray.getInteger(
					CR.getStyleableId(context, "mjet_PullToRefresh_ptrMode"),
					MODE_PULL_DOWN_TO_REFRESH);
		}

		// Refreshable View
		// By passing the attrs, we can add ListView/GridView params via XML
		mRefreshableView = createRefreshableView(context, attrs);
		addRefreshableView(context, mRefreshableView);

		// Loading View Strings
		String pullLabel = context.getString(CR.getStringsId(context,
				"mjet_pull_to_refresh_pull_label"));
		String refreshingLabel = context.getString(CR.getStringsId(context,
				"mjet_pull_to_refresh_refreshing_label"));
		String releaseLabel = context.getString(CR.getStringsId(context,
				"mjet_pull_to_refresh_release_label"));

		// Add Loading Views
		if (mMode == MODE_PULL_DOWN_TO_REFRESH || mMode == MODE_BOTH) {
			mHeaderLayout = new LoadingLayout(context,
					MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel,
					refreshingLabel, typedArray);
			addView(mHeaderLayout, 0, new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(mHeaderLayout);
			mHeaderHeight = mHeaderLayout.getMeasuredHeight();
		}
		if (mMode == MODE_PULL_UP_TO_REFRESH || mMode == MODE_BOTH) {
			mFooterLayout = new MoreLayout(context);
			addView(mFooterLayout, new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(mFooterLayout);
			LogTools.d("mEHaderHeight.." + mHeaderHeight);
			// if(mHeaderHeight==0){
			// mHeaderHeight = mFooterLayout.getMeasuredHeight();
			// }
			mFooterHeight = mFooterLayout.getMeasuredHeight();
			LogTools.d("mEHaderHeight.." + mHeaderHeight + ":" + mFooterHeight);
		}

		// Styleables from XML
		if (typedArray.hasValue(CR.getStyleableId(context,
				"mjet_PullToRefresh_ptrHeaderBackground"))) {
			setBackgroundResource(typedArray.getResourceId(CR.getStyleableId(
					context, "mjet_PullToRefresh_ptrHeaderBackground"),
					Color.WHITE));
		}
		if (typedArray.hasValue(CR.getStyleableId(context,
				"mjet_PullToRefresh_ptrAdapterViewBackground"))) {
			mRefreshableView.setBackgroundResource(typedArray.getResourceId(CR
					.getStyleableId(context,
							"mjet_PullToRefresh_ptrAdapterViewBackground"),
					Color.WHITE));
		}
		typedArray.recycle();
		typedArray = null;

		// Hide Loading Views
		switch (mMode) {
		case MODE_BOTH:
			setPadding(0, -mHeaderHeight, 0, -mFooterHeight);
			break;
		case MODE_PULL_UP_TO_REFRESH:
			setPadding(0, 0, 0, -mFooterHeight);
			break;
		case MODE_PULL_DOWN_TO_REFRESH:
		default:
			setPadding(0, -mHeaderHeight, 0, 0);
			break;
		}

		// If we're not using MODE_BOTH, then just set mCurrentMode to current
		// mMode
		if (mMode != MODE_BOTH) {
			mCurrentMode = mMode;
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * Actions a Pull Event
	 * 
	 * @return true if the Event has been handled, false if there has been no
	 *         change
	 */
	private boolean pullEvent() {
		if (isRefreshing() && isScroll) {
			if (isReadyForPullDown()
					&& mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {
				int z = Math.round((mLastMotionY - mInitialMotionY) / FRICTION);
				if (z >= 0) {
					if (getScrollY() < -mHeaderHeight) {
						setHeaderScroll(-mHeaderHeight);
						return true;
					} else if (getScrollY() == -mHeaderHeight) {
						return true;
					}
					setHeaderScroll(Math.max(-mHeaderHeight, getScrollY() - z));
				} else {
					setHeaderScroll(Math.min(0, getScrollY() - z));
				}
				return true;
			} else if (isReadyForPullUp()
					&& mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
				int z = Math.round((mLastMotionY - mInitialMotionY) / FRICTION);
				if (z <= 0) {
					if (getScrollY() > mFooterHeight) {
						setHeaderScroll(mFooterHeight);
						return true;
					} else if (getScrollY() == mFooterHeight) {
						return true;
					}
					setHeaderScroll(Math.min(mFooterHeight, getScrollY() - z));
				} else {
					setHeaderScroll(Math.max(0, getScrollY() - z));
				}
				return true;
			}

		}

		final int newHeight;
		final int oldHeight = getScrollY();

		if (mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
			newHeight = Math.round(Math.max(mInitialMotionY - mLastMotionY, 0)
					/ FRICTION);
		} else {
			newHeight = Math.round(Math.min(mInitialMotionY - mLastMotionY, 0)
					/ FRICTION);
		}
		/*
		 * switch (mCurrentMode) { case MODE_PULL_UP_TO_REFRESH: newHeight =
		 * Math.round(Math.max(mInitialMotionY - mLastMotionY, 0) / FRICTION);
		 * break; case MODE_PULL_DOWN_TO_REFRESH: default: newHeight =
		 * Math.round(Math.min(mInitialMotionY - mLastMotionY, 0) / FRICTION);
		 * break; }
		 */

		setHeaderScroll(newHeight);
		if (newHeight != 0) {
			boolean isHeight = false;

			if (mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
				if (mState == PULL_TO_REFRESH) {
					isHeight = (mFooterHeight < Math.abs(newHeight));
				} else {
					isHeight = (mFooterHeight >= Math.abs(newHeight));
				}
			} else if (mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {
				if (mState == PULL_TO_REFRESH) {
					isHeight = (mHeaderHeight < Math.abs(newHeight));
				} else {
					isHeight = (mHeaderHeight >= Math.abs(newHeight));
				}
			}
			/*
			 * switch (mCurrentMode) { case MODE_PULL_UP_TO_REFRESH: if(mState
			 * == PULL_TO_REFRESH){ isHeight=(mFooterHeight <
			 * Math.abs(newHeight)); }else{ isHeight=(mFooterHeight >=
			 * Math.abs(newHeight)); } break; case MODE_PULL_DOWN_TO_REFRESH:
			 * if(mState == PULL_TO_REFRESH){ isHeight=(mHeaderHeight <
			 * Math.abs(newHeight)); }else{ isHeight=(mHeaderHeight >=
			 * Math.abs(newHeight)); } //isHeight=(mHeaderHeight <
			 * Math.abs(newHeight)); break; }
			 */
			if (mState == PULL_TO_REFRESH && isHeight) {
				mState = RELEASE_TO_REFRESH;

				if (mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
					mFooterLayout.releaseToRefresh();
				} else if (mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {
					mHeaderLayout.releaseToRefresh();
				}
				/*
				 * switch (mCurrentMode) { case MODE_PULL_UP_TO_REFRESH:
				 * mFooterLayout.releaseToRefresh(); break; case
				 * MODE_PULL_DOWN_TO_REFRESH: mHeaderLayout.releaseToRefresh();
				 * break; }
				 */

				return true;

			} else if (mState == RELEASE_TO_REFRESH && isHeight) {
				mState = PULL_TO_REFRESH;

				if (mCurrentMode == MODE_PULL_UP_TO_REFRESH) {
					mFooterLayout.pullToRefresh();
				} else if (mCurrentMode == MODE_PULL_DOWN_TO_REFRESH) {
					mHeaderLayout.pullToRefresh();
				}
				/*
				 * switch (mCurrentMode) { case MODE_PULL_UP_TO_REFRESH:
				 * mFooterLayout.pullToRefresh(); break; case
				 * MODE_PULL_DOWN_TO_REFRESH: mHeaderLayout.pullToRefresh();
				 * break; }
				 */

				return true;
			}
		}

		return oldHeight != newHeight;
	}

	private boolean isReadyForPull() {
		switch (mMode) {
		case MODE_PULL_DOWN_TO_REFRESH:
			return isReadyForPullDown();
		case MODE_PULL_UP_TO_REFRESH:
			return isReadyForPullUp();
		case MODE_BOTH:
			return isReadyForPullUp() || isReadyForPullDown();
		default:
			break;
		}
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/**
	 * Simple Listener to listen for any callbacks to Refresh.
	 * 
	 */
	public static interface OnRefreshListener {

		/**
		 * onRefresh will be called for both Pull Down from top, and Pull Up
		 * from Bottom
		 */
		public void onRefresh();

	}

	/**
	 * An advanced version of the Listener to listen for callbacks to Refresh.
	 * This listener is different as it allows you to differentiate between Pull
	 * Ups, and Pull Downs.
	 * 
	 */
	public static interface OnRefreshListener2 {

		/**
		 * onPullDownToRefresh will be called only when the user has Pulled Down
		 * from the top, and released.
		 */
		public void onPullDownToRefresh();

		/**
		 * onPullUpToRefresh will be called only when the user has Pulled Up
		 * from the bottom, and released.
		 */
		public void onPullUpToRefresh();

	}

	public static interface OnLastItemVisibleListener {

		public void onLastItemVisible();

	}

	@Override
	public void setLongClickable(boolean longClickable) {
		getRefreshableView().setLongClickable(longClickable);
	}
}
