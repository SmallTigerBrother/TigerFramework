package com.mn.tiger.widget.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mn.tiger.utility.CR;

/**
 * 该类作用及功能说明 带横向进度条的提示框
 * @date 2013-8-26
 */
public class TGHorizontalProgressDialog extends TGDialog implements IProgressDialog
{
	/** 
	 * 进度条 
	 */
	private ProgressBar progressBar;

	/**
	 *  显示进度百分比的文本 
	 */
	private TextView progressText;
	
	public TGHorizontalProgressDialog(Context context)
	{
		super(context);
		setupDialog();
	}
	
	public TGHorizontalProgressDialog(Context context, int theme)
	{
		super(context, theme);
		setupDialog();
	}

	public TGHorizontalProgressDialog(Context context, boolean cancelable, 
			OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setupDialog();
	}

	/**
	 * 该方法的作用:初始化视图
	 * @date 2013-8-26
	 */
	private void setupDialog()
	{
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		View view = (View) inflater.inflate(
				CR.getLayoutId(getContext(), "tiger_horizontal_progress_dialog"), null);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		super.setBodyContentView(view, params);

		progressBar = (ProgressBar) view.findViewById(
				CR.getIdId(getContext(), "tiger_progress_bar"));
		progressText = (TextView) view.findViewById(
				CR.getIdId(getContext(), "tiger_progress_text"));
		
	}

	/**
	 * 该方法的作用:设置进度
	 * @date 2013-8-26
	 * @param progress
	 */
	public void setProgress(int progress)
	{
		if (progressBar != null)
		{
			progressBar.setProgress(progress);

			// 默认显示进度百分比，进度最大值为100
			this.setProgressText(progress + "%");
			this.setMax(100);
		}
	}

	/**
	 * 该方法的作用:获取进度
	 * @date 2013-8-26
	 * @return
	 */
	public int getProgress()
	{
		if (progressBar != null)
		{
			return progressBar.getProgress();
		}
		return 0;
	}

	/**
	 * 该方法的作用:设置进度最大值
	 * @date 2013-8-26
	 * @param max
	 */
	public void setMax(int max)
	{
		if (progressBar != null)
		{
			progressBar.setMax(max);
		}
	}

	/**
	 * 该方法的作用:设置进度文本
	 * @date 2013-8-26
	 * @param text
	 */
	public void setProgressText(String text)
	{
		if (progressText != null)
		{
			progressText.setText(text);
		}
	}

	/**
	 * 该方法的作用:获取进度文本字符串
	 * @date 2013-8-26
	 * @return
	 */
	public String getProgressText()
	{
		if (progressText != null)
		{
			return progressText.getText().toString();
		}
		return "";
	}

	@Override
	public void setProgressStyle(int style)
	{
	}
}
