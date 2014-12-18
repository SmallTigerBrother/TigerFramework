package com.mn.tiger.demo.widget.viewpager.horizontal;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.viewpager.TGPagerAdapter;
import com.mn.tiger.widget.viewpager.TGPagerSlidingTabStrip;

public class PagerSlidingTabStripActivity extends TGActionBarActivity
{
	@ViewById(id = R.id.pager_sliding_tab_strip)
	private TGPagerSlidingTabStrip pagerSlidingTabStrip;
	
	@ViewById(id = R.id.pager_sliding_tab_strip_viewpager)
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pager_sliding_tab_strip_activity);
		ViewInjector.initInjectedView(this, this);
		
		ArrayList<View> views = new ArrayList<View>();
		ImageView imageView;
		for (int i = 0; i < 10; i++)
		{
			imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.ic_launcher);
			views.add(imageView);
		}
		
		viewPager.setAdapter(new StripPagerAdapter(views));
		
		pagerSlidingTabStrip.setViewPager(viewPager);
	}
	
	private class StripPagerAdapter extends TGPagerAdapter
	{
		public StripPagerAdapter(ArrayList<View> views)
		{
			super(views);
		}
		
		@Override
		public CharSequence getPageTitle(int position)
		{
			return "标题" + position;
		}
	}
}
