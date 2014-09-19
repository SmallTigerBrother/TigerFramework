package com.mn.tiger.widget.dialog;

import android.content.Context;

/**
 * 该类作用及功能说明
 * 对话框工厂类
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 */
public class TGDialogFactory
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = TGDialogFactory.class.getSimpleName();

	
	public TGDialogFactory()
	{
		
	}
	
	/**
	 * 该方法的作用:
	 * 创建对话框
	 * @date 2014年2月10日
	 * @param context
	 * @param dialogStyle 对话框风格
	 * @return
	 */
	public IDialog createDialog(Context context, int dialogStyle)
	{
		switch (dialogStyle)
		{
		case IDialog.DIALOG_STYLE_MPDIALOG:
			return new TGDialog(context);
		default:
			return new TGDialog(context);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 创建进度对话框
	 * @date 2014年2月10日
	 * @param context
	 * @param dialogStyle 对话框风格
	 * @param progressStyle 进度条风格
	 * @return
	 */
	public IProgressDialog createProgressDialog(Context context, 
			int dialogStyle, int progressStyle)
	{
		switch (dialogStyle)
		{
			case IDialog.DIALOG_STYLE_MPDIALOG:
				
				switch (progressStyle)
				{
				case IProgressDialog.PROGRESS_STYLE_HORIZONTAL:
					return new TGProgressDialog(context, IProgressDialog.PROGRESS_STYLE_HORIZONTAL);
				case IProgressDialog.PROGRESS_STYLE_SPINNER:
					return new TGProgressDialog(context, IProgressDialog.PROGRESS_STYLE_SPINNER);
				default:
					return null;
				}
				
			default:
				return null;
		}
	}
}
