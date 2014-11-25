package com.mn.tiger.demo.template.viewpager;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.adpter.TGFragmentPagerAdapter;

public class ViewPagerWithFragmentActivity extends TGActionBarActivity
{
	@ViewById(id = R.id.view_pager)
	private ViewPager viewPager;
	
	private ArrayList<Fragment> fragments;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_fragment_activity);
		ViewInjector.initInjectedView(this, this);
		
		fragments = new ArrayList<Fragment>();
		fragments.add(new PagerFragment_1());
		fragments.add(new PagerFragment_2());
		
		viewPager.setAdapter(new TGFragmentPagerAdapter(getSupportFragmentManager(), 
				fragments));
		
	}
}
