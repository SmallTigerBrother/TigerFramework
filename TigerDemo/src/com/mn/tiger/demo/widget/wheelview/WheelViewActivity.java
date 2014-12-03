package com.mn.tiger.demo.widget.wheelview;

import android.os.Bundle;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ToastUtils;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.wheelview.DateWheel;
import com.mn.tiger.widget.wheelview.DateWheel.OnDateChangedListener;
import com.mn.tiger.widget.wheelview.TimeWheel.OnTimeChangedListener;
import com.mn.tiger.widget.wheelview.TimeWheel;

public class WheelViewActivity extends TGActionBarActivity implements OnDateChangedListener, OnTimeChangedListener
{
	@ViewById(id = R.id.date_wheel)
	private DateWheel dateWheel;
	
	@ViewById(id = R.id.time_wheel)
	private TimeWheel timeWheel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheel_view_demo);
		ViewInjector.initInjectedView(this, this);
		dateWheel.setOnDateChangedListener(this);
		timeWheel.setOnTimeChangedListener(this);
	}

	@Override
	public void onTimeChanged(TimeWheel timeWheel, int hour, int minute, int second)
	{
		ToastUtils.showToast(this, hour + " : " + minute + " : " + second);
	}

	@Override
	public void onDateChanged(DateWheel dateWheel, int year, int month, int dayOfMonth)
	{
		ToastUtils.showToast(this, year + "-" + month + "-" + dayOfMonth);
	}
}
