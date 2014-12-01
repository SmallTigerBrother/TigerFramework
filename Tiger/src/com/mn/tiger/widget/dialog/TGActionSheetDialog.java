package com.mn.tiger.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mn.tiger.R;
import com.mn.tiger.utility.DisplayUtils;

/**
 * 底部浮出的Sheet对话框（仿IOS）
 */
public class TGActionSheetDialog extends Dialog implements View.OnClickListener
{
	public static final int CANCEL_BUTTN_ID = 123456;
	
	private LinearLayout btnPanelLayout;
	
	private View cancelBtn;
	
	private OnSheetClickListener onSheetClickListener;
	
	public TGActionSheetDialog(Context context)
	{
		this(context, R.style.DialogTheme_Sheet);
	}
	
	public TGActionSheetDialog(Context context, int theme)
	{
		super(context, theme);
		setContentView(R.layout.dialog_sheet);
		btnPanelLayout = (LinearLayout) this.findViewById(R.id.sheet_btn_panel);
		cancelBtn = createCancelButton();
		LinearLayout mainLayout = (LinearLayout) this.findViewById(R.id.sheet_main);
		mainLayout.addView(cancelBtn);
	}
	
	public TGActionSheetDialog(Context context, boolean cancelable,
			IDialog.OnCancelListener cancelListener)
	{
		this(context, R.style.DialogTheme_Sheet);
		super.setCancelable(cancelable);
		super.setOnCancelListener(cancelListener);
	}
	
	public final void addButton(int id, String btnText)
	{
		View button = createSheetButton(btnText);
		button.setId(id);
		if(null != btnPanelLayout)
		{
			btnPanelLayout.addView(button);
		}
		else
		{
			throw new NullPointerException("The ParentView is NULL, you may use a custom contentView");
		}
	}
	
	protected View createSheetButton(String btnText)
	{
		Button button = new Button(getContext(), null, R.style.dialog_sheet_text_style);
		button.setGravity(Gravity.CENTER);
		button.setId(CANCEL_BUTTN_ID);
		button.setText(btnText);
		LinearLayout.LayoutParams layoutParams = DisplayUtils.newLayoutParamsMW(
				LinearLayout.LayoutParams.class);
		layoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 8);
		layoutParams.leftMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.rightMargin = DisplayUtils.dip2px(getContext(), 16);
		button.setLayoutParams(layoutParams);
		return button;
	}
	
	protected View createCancelButton()
	{
		Button button = new Button(getContext(), null, R.style.dialog_sheet_text_style);
		button.setGravity(Gravity.CENTER);
		button.setText(R.string.dialog_sheet_cancel);
		LinearLayout.LayoutParams layoutParams = DisplayUtils.newLayoutParamsMW(
				LinearLayout.LayoutParams.class);
		layoutParams.topMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.bottomMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.leftMargin = DisplayUtils.dip2px(getContext(), 16);
		layoutParams.rightMargin = DisplayUtils.dip2px(getContext(), 16);
		button.setLayoutParams(layoutParams);
		return button;
	}
	
	public void setOnSheetClickListener(OnSheetClickListener listener)
	{
		this.onSheetClickListener = listener;
	}
	
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
	
	public static interface OnSheetClickListener
	{
		void OnSheetClick(View view);
	}
	
}
