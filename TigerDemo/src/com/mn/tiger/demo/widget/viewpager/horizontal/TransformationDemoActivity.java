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
import com.mn.tiger.widget.viewpager.transforms.CubeInTransformer;
 
public class TransformationDemoActivity extends TGActionBarActivity
{
	@ViewById(id = R.id.transformation_viewpager)
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.viewpager_transformation_demo);
		ViewInjector.initInjectedView(this, this);
		viewPager.setPageTransformer(true, new CubeInTransformer());
		viewPager.setPageMargin(-100);
		
		ArrayList<View> views = new ArrayList<View>();
		ImageView imageView;
		for (int i = 0; i < 10; i++)
		{
			imageView = new ImageView(this);
			imageView.setBackgroundResource(R.drawable.ic_launcher);
			views.add(imageView);
		}
		
		viewPager.setAdapter(new TGPagerAdapter(views));
	}
}
