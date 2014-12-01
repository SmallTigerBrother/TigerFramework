package com.mn.tiger.demo.widget.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.dialog.TGActionSheetDialog;
import com.mn.tiger.widget.dialog.TGActionSheetDialog.OnSheetClickListener;

public class DialogDemoActivity extends TGActionBarActivity implements 
    OnClickListener, OnSheetClickListener
{
	@ViewById(id = R.id.dialog_sheet)
	private Button actionSheetButton;
	
	private TGActionSheetDialog actionSheetDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_demo);
		ViewInjector.initInjectedView(this, this);
		actionSheetButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.dialog_sheet:
				actionSheetDialog = new TGActionSheetDialog(this);
				actionSheetDialog.addButton(1, "按钮1");
				actionSheetDialog.addButton(2, "按钮2");
				actionSheetDialog.addButton(3, "按钮3");
				actionSheetDialog.setOnSheetClickListener(this);
				
				actionSheetDialog.show();
				
				break;

			default:
				break;
		}
	}
	
	@Override
	public void OnSheetClick(View view)
	{
		switch (view.getId())
		{
			case 1:
				
				break;
			case 2:
				
				break;

			case 3:
				break;
				
			case TGActionSheetDialog.CANCEL_BUTTN_ID:
				actionSheetDialog.dismiss();
				break;
				
			default:
				break;
		}
	}
}
