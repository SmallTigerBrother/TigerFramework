package com.mn.tiger.demo.widget.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.dialog.TGActionSheetDialog;
import com.mn.tiger.widget.dialog.TGDateWheelDialog;
import com.mn.tiger.widget.dialog.TGActionSheetDialog.OnSheetClickListener;
import com.mn.tiger.widget.dialog.TGDateWheelDialog.OnDateSetListener;
import com.mn.tiger.widget.wheelview.DateWheel;

public class DialogDemoActivity extends TGActionBarActivity implements 
    OnClickListener, OnSheetClickListener, OnDateSetListener
{
	@ViewById(id = R.id.dialog_sheet)
	private Button actionSheetButton;
	
	@ViewById(id = R.id.dialog_date_wheel)
	private Button dateWheelButton;
	
	private TGActionSheetDialog actionSheetDialog;
	
	private TGDateWheelDialog dateWheelDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_demo);
		ViewInjector.initInjectedView(this, this);
		actionSheetButton.setOnClickListener(this);
		dateWheelButton.setOnClickListener(this);
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
				actionSheetDialog.setBackgroundColor(Color.YELLOW);
				
				actionSheetDialog.show();
				
				break;
				
			case R.id.dialog_date_wheel:
				dateWheelDialog = new TGDateWheelDialog(this, this, 2014, 12, 2);
				
				dateWheelDialog.show();
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
	
	@Override
	public void onDateSet(DateWheel dateWheel, int year, int monthOfYear, int dayOfMonth)
	{
		
	}
}
