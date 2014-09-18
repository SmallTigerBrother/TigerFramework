package com.mn.tiger.widget.pulltorefresh.library.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.widget.pulltorefresh.library.PullToRefreshBase;

/**
 * 
 * 
 * 该类作用及功能说明
 * 
 * @author nKF50342
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2012-6-11 Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights
 *       reserved.
 */
public class LoadingLayout extends OriginalLoadingLayout {

	static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;

	private final ImageView mHeaderImage;
	private final ProgressBar mHeaderProgress;
	private final TextView mHeaderText;
	private final TextView mHeaderTextDown;

	private String mPullLabel;
	private String mRefreshingLabel;
	private String mReleaseLabel;

	private final Animation mRotateAnimation, mResetRotateAnimation;

	/**
	 * 
	 * @author nKF50342
	 * @date 2012-6-11 构造函数
	 * @param context
	 * @param mode
	 * @param releaseLabel
	 * @param pullLabel
	 * @param refreshingLabel
	 * @param attrs
	 */
	public LoadingLayout(Context context, final int mode, String releaseLabel,
			String pullLabel, String refreshingLabel, TypedArray attrs) {
		super(context);
		ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(
				CR.getLayoutId(context, "mjet_pull_to_refresh_header"), this);
		mHeaderText = (TextView) header.findViewById(CR.getIdId(context,
				"mjet_pull_to_refresh_text"));
		mHeaderTextDown = (TextView) header.findViewById(CR.getIdId(context,
				"mjet_pull_to_refresh_text_down"));
		mHeaderImage = (ImageView) header.findViewById(CR.getIdId(context,
				"mjet_pull_to_refresh_image"));
		mHeaderProgress = (ProgressBar) header.findViewById(CR.getIdId(context,
				"mjet_pull_to_refresh_progress"));

		final Interpolator interpolator = new LinearInterpolator();
		mRotateAnimation = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(interpolator);
		mRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mResetRotateAnimation.setInterpolator(interpolator);
		mResetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);

		mReleaseLabel = releaseLabel;
		mPullLabel = pullLabel;
		mRefreshingLabel = refreshingLabel;

		switch (mode) {
		case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
			mHeaderImage.setImageResource(CR.getDrawableId(context,
					"mjet_pulltorefresh_up_arrow"));
			break;
		case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
		default:
			mHeaderImage.setImageResource(CR.getDrawableId(context,
					"mjet_pulltorefresh_down_arrow"));
			break;
		}

		// TODO 注销该代码
		// if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
		// attrs.getColor(R.styleable.PullToRefresh_ptrHeaderTextColor,
		// Color.BLACK);
		// //setTextColor(color);
		// }
		// setTextColor(Color.BLACK);
	}

	/**
	 * 
	 * 该方法的作用:重置 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void reset() {
		mHeaderText.setText(mPullLabel);
		mHeaderImage.setVisibility(View.VISIBLE);
		mHeaderProgress.setVisibility(View.GONE);
	}

	/**
	 * 
	 * 该方法的作用:释放刷新 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void releaseToRefresh() {
		mHeaderText.setText(mReleaseLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	/**
	 * 
	 * 该方法的作用:设置拉的过程中文本 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void setPullLabel(String pullLabel) {
		mPullLabel = pullLabel;
		mHeaderText.setText(mPullLabel);
	}

	/**
	 * 
	 * 该方法的作用:正在刷新接口 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void refreshing() {
		mHeaderText.setText(mRefreshingLabel);
		mHeaderTextDown.setText(refreshingText);
		mHeaderImage.clearAnimation();
		mHeaderImage.setVisibility(View.INVISIBLE);
		mHeaderProgress.setVisibility(View.VISIBLE);
	}

	/**
	 * 
	 * 该方法的作用:设置刷新中文本 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	/**
	 * 
	 * 该方法的作用:设置释放文本 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void setReleaseLabel(String releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	/**
	 * 
	 * 该方法的作用:下拉刷新 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void pullToRefresh() {
		mHeaderText.setText(mPullLabel);
		mHeaderImage.clearAnimation();
		mHeaderImage.startAnimation(mResetRotateAnimation);
	}

	/**
	 * 
	 * 该方法的作用:设置文本颜色 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void setTextColor(int color) {
		mHeaderText.setTextColor(color);
		mHeaderTextDown.setTextColor(color);
	}

	String refreshingText = null;

	/**
	 * 
	 * 该方法的作用:设置第二个文本 在什么情况下调用:
	 * 
	 * @author nKF50342
	 * @date 2012-6-11
	 */
	public void setDownTextLabel(String str) {
		mHeaderTextDown.setText(str);
		refreshingText = str;
		mHeaderTextDown.setVisibility(View.VISIBLE);
	}

}
