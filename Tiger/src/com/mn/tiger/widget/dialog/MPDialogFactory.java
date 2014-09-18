package com.mn.tiger.widget.dialog;

import android.content.Context;

import com.mn.tiger.system.AppConfiguration;

/**
 * 该类作用及功能说明
 * 对话框工厂类
 * @author l00220455
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2014年2月10日
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public class MPDialogFactory
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = MPDialogFactory.class.getSimpleName();

	
	public MPDialogFactory()
	{
		
	}
	
	/**
	 * 该方法的作用:
	 * 创建对话框
	 * @author l00220455
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
			return new MPDialog(context);
		default:
			return new MPDialog(context);
		}
	}
	
	/**
	 * 该方法的作用:
	 * 创建MJet框架现有的对话框
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param context
	 * @return
	 */
	public IDialog createMJetDialog(Context context)
	{
		return createDialog(context, AppConfiguration.getInstance().getMJetDialogStyle());
	}
	
	/**
	 * 该方法的作用:
	 * 创建进度对话框
	 * @author l00220455
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
					return new MPProgressDialog(context, IProgressDialog.PROGRESS_STYLE_HORIZONTAL);
				case IProgressDialog.PROGRESS_STYLE_SPINNER:
					return new MPProgressDialog(context, IProgressDialog.PROGRESS_STYLE_SPINNER);
				default:
					return null;
				}
				
			default:
				return null;
		}
	}
	
	/**
	 * 该方法的作用:
	 * 创建MJet框架现有的进度对话框
	 * @author l00220455
	 * @date 2014年2月10日
	 * @param context
	 * @param progressStyle
	 * @return
	 */
	public IProgressDialog createMJetProgressDialog(Context context,  int progressStyle)
	{
		return createProgressDialog(context, 
				AppConfiguration.getInstance().getMJetDialogStyle(), progressStyle);
	}
}
