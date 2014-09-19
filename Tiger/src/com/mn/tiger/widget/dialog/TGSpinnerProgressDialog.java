package com.mn.tiger.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.mn.tiger.utility.CR;
import com.mn.tiger.utility.LogTools;

/**
 * 该类作用及功能说明 带横向进度条的提示框
 * @date 2013-8-26
 */
public class TGSpinnerProgressDialog extends TGDialog implements IProgressDialog
{
	public TGSpinnerProgressDialog(Context context)
	{
		super(context, CR.getStyleId(context, "mjet_progressDialog"));
		setupDialog();
	}
	
	public TGSpinnerProgressDialog(Context context, int theme)
	{
		super(context, theme);
		setupDialog();
	}

	public TGSpinnerProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
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
		View view = inflater.inflate(CR.getLayoutId(getContext(), "mjet_progress_dialog"), null);
		ImageView load_icon = (ImageView) view.findViewById(CR.getIdId(getContext(),
				"mjet_progress_loading_icon"));

		// 获取动画图片
		final AnimationDrawable animationDrawable = (AnimationDrawable) load_icon.getBackground();

		super.setContentView(view);
		super.setCancelable(true);
		super.setCanceledOnTouchOutside(false);

		super.setOnShowListener(new OnShowListener()
		{
			@Override
			public void onShow(DialogInterface dialogInterface)
			{
				// 播放帧动画
				new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(200);
						}
						catch (Exception e)
						{
							LogTools.e(e);
						}
						animationDrawable.start();
					}
				}).start();
			}
		});
		
	}

	
	/**
	 * 该方法的作用:设置进度
	 * @param progress
	 */
	public void setProgress(int progress)
	{
		
	}

	/**
	 * 该方法的作用:获取进度
	 * @date 2013-8-26
	 * @return
	 */
	public int getProgress()
	{
		return 0;
	}

	/**
	 * 该方法的作用:设置进度最大值
	 * @date 2013-8-26
	 * @param max
	 */
	public void setMax(int max)
	{
	}

	/**
	 * 该方法的作用:设置进度文本
	 * @date 2013-8-26
	 * @param text
	 */
	public void setProgressText(String text)
	{
	}

	/**
	 * 该方法的作用:获取进度文本字符串
	 * @date 2013-8-26
	 * @return
	 */
	public String getProgressText()
	{
		return "";
	}

	@Override
	public void setProgressStyle(int style)
	{
		
	}
}
