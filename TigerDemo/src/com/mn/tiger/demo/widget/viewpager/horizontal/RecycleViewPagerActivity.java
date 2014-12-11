package com.mn.tiger.demo.widget.viewpager.horizontal;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.viewpager.TGPagerViewHolder;
import com.mn.tiger.widget.viewpager.TGRecyclePagerAdapter;
 
public class RecycleViewPagerActivity extends TGActionBarActivity
{
	@ViewById(id = R.id.transformation_viewpager)
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.viewpager_transformation_demo);
		ViewInjector.initInjectedView(this, this);
		
		ArrayList<Integer> pagerData = new ArrayList<Integer>();
		for(int i = 0; i < 20; i++)
		{
			pagerData.add(i);
		}
		
		viewPager.setAdapter(new TGRecyclePagerAdapter<Integer>(this, pagerData, 
				RecyclePagerViewHolder.class));
	}
	
	public static class RecyclePagerViewHolder extends TGPagerViewHolder<Integer>
	{
		private TextView textView;
		
		private ImageView imageView;
		
		@Override
		public View initPage(int viewType)
		{
			switch (viewType)
			{
				case 0:
					textView = new TextView(getActivity());
					return textView;
					
				case 1:
					imageView = new ImageView(getActivity());
					return imageView;

				default:
					break;
			}
			return null;
		}

		@Override
		public void fillData(Integer itemData, int position, int viewType)
		{
			switch (viewType)
			{
				case 0:
					textView.setText(textView.toString() + "———— position == " + position);
					break;
				case 1:
					imageView.setBackgroundResource(R.drawable.ic_launcher);
					break;

				default:
					break;
			}
		}
		
		@Override
		public int getItemViewType(ArrayList<Integer> pagerData, int position)
		{
			//计算不同页的类型，若超过两种类型，必须自定义类型
			int data = pagerData.get(position);
			if(data % 2 == 0)
			{
				return 1;
			}
			
			return 0;
		}
	}
	
}
