package com.mn.tiger.demo.activity;


import android.os.Bundle;

import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;

public class NavigationBarActivity extends TGActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigationbar);
		setNavigationBarVisible(true);
	}
	
}
