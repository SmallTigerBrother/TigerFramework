package com.mn.tiger.widget.pulltorefresh.library.internal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mn.tiger.utility.CR;

/**
 * 
 * 该类作用及功能说明
 * 
 * @version V2.0
 * @see JDK1.6,android-8
 */

public class MoreLayout extends OriginalLoadingLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private final ProgressBar mFooterProgress;
	private final TextView mFooterText;

	private String mPullLabel;
	private String mRefreshingLabel;
	private String mReleaseLabel;

	/**
	 * 
	 * @date 2012-6-11 构造函数
	 * @param context
	 */
	public MoreLayout(Context context) {
		super(context);
		ViewGroup footer = (ViewGroup) LayoutInflater.from(context).inflate(
				CR.getLayoutId(context, "tiger_more_to_refresh_footer"), this);
		mFooterText = (TextView) footer.findViewById(CR.getIdId(context,
				"tiger_more_to_refresh_text"));
		mFooterProgress = (ProgressBar) footer.findViewById(CR.getIdId(context,
				"tiger_more_to_refresh_progress"));

		mReleaseLabel = context.getString(CR.getStringsId(context,
				"tiger_more_to_refresh_pull_label"));
		mRefreshingLabel = mPullLabel = mReleaseLabel;

		// setTextColor(Color.BLACK);
	}

	/**
	 * 
	 * 该方法的作用:重置 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void reset() {
		mFooterText.setText(mPullLabel);
		mFooterProgress.setVisibility(View.GONE);
	}

	/**
	 * 
	 * 该方法的作用:释放刷新 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void releaseToRefresh() {
		mFooterText.setText(mReleaseLabel);
	}

	/**
	 * 
	 * 该方法的作用:设置拉的过程中文本 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void setPullLabel(String pullLabel) {
		mPullLabel = pullLabel;
	}

	/**
	 * 
	 * 该方法的作用:正在刷新接口 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void refreshing() {
		mFooterText.setText(mRefreshingLabel);
		mFooterProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 该方法的作用:设置刷新中文本 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	/**
	 * 
	 * 该方法的作用:设置释放文本 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void setReleaseLabel(String releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	/**
	 * 
	 * 该方法的作用:下拉刷新 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void pullToRefresh() {
		mFooterText.setText(mPullLabel);
	}

	/**
	 * 
	 * 该方法的作用:设置文本颜色 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void setTextColor(int color) {
		mFooterText.setTextColor(color);
	}

	/**
	 * 该方法的作用:设置第二个文本 在什么情况下调用:
	 * 
	 * @date 2012-6-11
	 */
	public void setDownTextLabel(String textLabel) {
		return;
	}

}