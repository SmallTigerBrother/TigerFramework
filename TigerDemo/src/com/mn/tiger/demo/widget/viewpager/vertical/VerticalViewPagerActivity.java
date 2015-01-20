package com.mn.tiger.demo.widget.viewpager.vertical;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.viewpager.TGPagerAdapter;
import com.mn.tiger.widget.viewpager.VerticalViewPager;

public class VerticalViewPagerActivity extends TGActionBarActivity implements OnPageChangeListener
{
	@ViewById(id = R.id.view_pager)
	private VerticalViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vertical_viewpager_activity);
		ViewInjector.initInjectedView(this, this);
		
		setupViews();
	}

	private void setupViews()
	{
		ArrayList<View> views = new ArrayList<View>();
		ImageView imageView_1 = new ImageView(this);
		imageView_1.setBackgroundResource(R.drawable.ic_launcher);
		views.add(imageView_1);
		
		ImageView imageView_2 = new ImageView(this);
		imageView_2.setBackgroundResource(R.drawable.tiger_search_close_default);
		views.add(imageView_2);
		
		ImageView imageView_3 = new ImageView(this);
		imageView_3.setBackgroundResource(R.drawable.tiger_search_submit_icon);
		views.add(imageView_3);
		
		viewPager.setAdapter(new TGPagerAdapter(views));
		viewPager.setOnPageChangeListener(this);
	}
	
	@Override
	public void onPageSelected(int page)
	{
		Toast.makeText(this, "这是第" + page + "页", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onPageScrollStateChanged(int state)
	{
	}

	@Override
	public void onPageScrolled(int page, float arg1, int arg2)
	{
	}
}
