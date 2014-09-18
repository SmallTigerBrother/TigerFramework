package com.mn.tiger.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * 该类作用及功能说明 带横向进度条的提示框
 * @author yWX158243
 * @date 2013-8-26
 */
public class MPProgressDialog implements IProgressDialog
{
	/**
	 * 日志标签
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();
	
	/**
	 * 进度框
	 */
	private IProgressDialog progressDialog;

	public MPProgressDialog(Context context, int style)
	{
		if(style == IProgressDialog.PROGRESS_STYLE_HORIZONTAL)
		{
			progressDialog = new MPHorizontalProgressDialog(context);
		}
		else 
		{
			progressDialog = new MPSpinnerProgressDialog(context);
		}
	}
	
	public MPProgressDialog(Context context, int theme, int style)
	{
		if(style == IProgressDialog.PROGRESS_STYLE_HORIZONTAL)
		{
			progressDialog = new MPHorizontalProgressDialog(context, theme);
		}
		else 
		{
			progressDialog = new MPSpinnerProgressDialog(context, theme);
		}
	}

	public MPProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener, int style)
	{
		if(style == IProgressDialog.PROGRESS_STYLE_HORIZONTAL)
		{
			progressDialog = new MPHorizontalProgressDialog(context);
		}
		else 
		{
			progressDialog = new MPSpinnerProgressDialog(context);
		}
		
		progressDialog.setCancelable(cancelable);
		progressDialog.setOnCancelListener(cancelListener);
	}

	/**
	 * 该方法的作用:设置进度
	 * @author yWX158243
	 * @date 2013-8-26
	 * @param progress
	 */
	public void setProgress(int progress)
	{
		progressDialog.setProgress(progress);
	}

	/**
	 * 该方法的作用:获取进度
	 * @author yWX158243
	 * @date 2013-8-26
	 * @return
	 */
	public int getProgress()
	{
		return progressDialog.getProgress();
	}

	/**
	 * 该方法的作用:设置进度最大值
	 * @author yWX158243
	 * @date 2013-8-26
	 * @param max
	 */
	public void setMax(int max)
	{
		progressDialog.setMax(max);
	}

	/**
	 * 该方法的作用:设置进度文本
	 * @author yWX158243
	 * @date 2013-8-26
	 * @param text
	 */
	public void setProgressText(String text)
	{
		progressDialog.setProgressText(text);
	}

	/**
	 * 该方法的作用:获取进度文本字符串
	 * @author yWX158243
	 * @date 2013-8-26
	 * @return
	 */
	public String getProgressText()
	{
		return progressDialog.getProgressText();
	}

	@Override
	public void setBackgroundDrawable(Drawable drawable)
	{
		progressDialog.setBackgroundDrawable(drawable);
	}

	@Override
	public void setOnCancelListener(OnCancelListener listener)
	{
		progressDialog.setOnCancelListener(listener);
	}

	@Override
	public void setOnDismissListener(OnDismissListener listener)
	{
		progressDialog.setOnDismissListener(listener);
	}

	@Override
	public void setOnKeyListener(OnKeyListener listener)
	{
		progressDialog.setOnKeyListener(listener);
	}

	@Override
	public void setOnShowListener(OnShowListener listener)
	{
		progressDialog.setOnShowListener(listener);
	}

	@Override
	public void setLeftButton(CharSequence text, OnClickListener listener)
	{
		progressDialog.setLeftButton(text, listener);
	}

	@Override
	public void setMiddleButton(CharSequence text, OnClickListener listener)
	{
		progressDialog.setMiddleButton(text, listener);
	}

	@Override
	public void setRightButton(CharSequence text, OnClickListener listener)
	{
		progressDialog.setRightButton(text, listener);
	}

	@Override
	public void setTitleContentView(View contentView,
			android.view.ViewGroup.LayoutParams layoutParams)
	{
		progressDialog.setTitleContentView(contentView, layoutParams);
	}

	@Override
	public void setTitleVisibility(int visibility)
	{
		progressDialog.setTitleVisibility(visibility);
	}

	@Override
	public void setTitleText(CharSequence title)
	{
		progressDialog.setTitleText(title);
	}

	@Override
	public void setTitleTextColor(int color)
	{
		progressDialog.setTitleTextColor(color);
	}

	@Override
	public void setBodyContentView(View contentView,
			android.view.ViewGroup.LayoutParams layoutParams)
	{
		progressDialog.setBodyContentView(contentView, layoutParams);
	}

	@Override
	public void setBodyVisibility(int visibility)
	{
		progressDialog.setBodyVisibility(visibility);
	}

	@Override
	public void setBodyText(CharSequence text)
	{
		progressDialog.setBodyText(text);
	}

	@Override
	public TextView getBodyTextView()
	{
		return progressDialog.getBodyTextView();
	}

	@Override
	public void setBodyTextColor(int color)
	{
		progressDialog.setBodyTextColor(color);
	}

	@Override
	public void setBottomContentView(View contentView,
			android.view.ViewGroup.LayoutParams layoutParams)
	{
		progressDialog.setBottomContentView(contentView, layoutParams);
	}

	@Override
	public void setBottomVisibility(int visibility)
	{
		progressDialog.setBottomVisibility(visibility);
	}

	@Override
	public void show()
	{
		progressDialog.show();
	}

	@Override
	public void setCancelable(boolean cancelable)
	{
		progressDialog.setCancelable(cancelable);
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancelable)
	{
		progressDialog.setCanceledOnTouchOutside(cancelable);
	}

	@Override
	public void cancel()
	{
		progressDialog.cancel();
	}

	@Override
	public void dismiss()
	{
		progressDialog.dismiss();
	}

	@Override
	public void setProgressStyle(int style)
	{
		progressDialog.setProgressStyle(style);
	}

	@Override
	public boolean isShowing()
	{
		return progressDialog.isShowing();
	}
	
	@Override
	public Window getWindow()
	{
		return progressDialog.getWindow();
	}
}
