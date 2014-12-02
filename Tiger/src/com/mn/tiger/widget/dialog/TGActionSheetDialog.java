package com.mn.tiger.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mn.tiger.R;
import com.mn.tiger.utility.DisplayUtils;

/**
 * 底部浮出的Sheet对话框（仿IOS）
 */
public class TGActionSheetDialog extends Dialog implements View.OnClickListener
{
	/**
	 * 取消按钮的id
	 */
	public static final int CANCEL_BUTTN_ID = 123456;
	
	/**
	 * 自定义按钮区的layout
	 */
	private LinearLayout btnPanelLayout;
	
	/**
	 * 自带取消按钮
	 */
	private View cancelBtn;
	
	/**
	 * 按钮点击事件监听器
	 */
	private OnSheetClickListener onSheetClickListener;
	
	public TGActionSheetDialog(Context context)
	{
		this(context, R.style.DialogTheme_Sheet);
	}
	
	public TGActionSheetDialog(Context context, int theme)
	{
		super(context, theme);
		setContentView(R.layout.dialog_sheet);
		//设置对话框宽度、高度
		this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		//设置视图显示在底部
		this.getWindow().setGravity(Gravity.BOTTOM);
		
		btnPanelLayout = (LinearLayout) this.findViewById(R.id.sheet_btn_panel);
		//添加取消按钮
		cancelBtn = createCancelButton();
		cancelBtn.setId(CANCEL_BUTTN_ID);
		cancelBtn.setOnClickListener(this);
		LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.sheet_main);
		mainLayout.addView(cancelBtn);
	}
	
	/**
	 * 添加自定义按钮
	 * @param id 自定义按钮的id
	 * @param btnText 自定义按钮的文本
	 */
	public final void addButton(int id, String btnText)
	{
		View button = createSheetButton(btnText);
		button.setId(id);
		button.setOnClickListener(this);
		if(null != btnPanelLayout)
		{
			btnPanelLayout.addView(button);
		}
		else
		{
			throw new NullPointerException("The ParentView is NULL, you may use a custom contentView");
		}
	}
	
	/**
	 * 创建一个按钮
	 * @param btnText 按钮文本
	 * @return
	 */
	protected View createSheetButton(String btnText)
	{
		Button button = new Button(getContext());
		button.setGravity(Gravity.CENTER);
		button.setText(btnText);
		button.setBackgroundColor(0xfff6f6f6);
		button.setTextSize(18);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 8);
		layoutParams.leftMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.rightMargin = DisplayUtils.dip2px(getContext(), 16);
		button.setLayoutParams(layoutParams);
		return button;
	}
	
	/**
	 * 创建取消按钮
	 * @return
	 */
	protected View createCancelButton()
	{
		Button button = new Button(getContext());
		button.setGravity(Gravity.CENTER);
		button.setText(R.string.dialog_sheet_cancel);
		button.setBackgroundColor(0xfff6f6f6);
		button.setTextSize(18);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.topMargin = DisplayUtils.dip2px(getContext(), 8);
		layoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.leftMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.rightMargin = DisplayUtils.dip2px(getContext(), 16);
		button.setLayoutParams(layoutParams);
		return button;
	}
	
	/**
	 * 设置背景颜色
	 * @param color
	 */
	public void setBackgroundColor(int color)
	{
		this.getWindow().getDecorView().setBackgroundColor(color);
	}
	
	/**
	 * 设置背景资源文件
	 * @param resid
	 */
	public void setBackgroundResource(int resid)
	{
		this.getWindow().getDecorView().setBackgroundResource(resid);
	}
	
	/**
	 * 设置按钮点击事件监听器
	 * @param listener
	 */
	public void setOnSheetClickListener(OnSheetClickListener listener)
	{
		this.onSheetClickListener = listener;
	}
	
	/**
	 * 设置取消按钮可见性
	 * @param visibility
	 */
	public void setCancelButtonVisibility(int visibility)
	{
		cancelBtn.setVisibility(visibility);
	}
	
	@Override
	public void onClick(View v)
	{
		if(null != onSheetClickListener)
		{
			onSheetClickListener.OnSheetClick(v);
		}
	}
	
	/**
	 * 按钮点击事件监听器
	 */
	public static interface OnSheetClickListener
	{
		/**
		 * 按钮点击事件回调方法
		 * @param view 点击按钮视图
		 */
		void OnSheetClick(View view);
	}
	
}
