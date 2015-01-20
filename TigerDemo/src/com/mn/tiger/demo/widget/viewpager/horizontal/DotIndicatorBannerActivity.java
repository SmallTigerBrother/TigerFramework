package com.mn.tiger.demo.widget.viewpager.horizontal;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ImageView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.app.TGActionBarActivity;
import com.mn.tiger.demo.R;
import com.mn.tiger.utility.ViewInjector;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView;
import com.mn.tiger.widget.viewpager.DotIndicatorBannerPagerView.IImageViewHolder;

public class DotIndicatorBannerActivity extends TGActionBarActivity
{
	@ViewById(id = R.id.test_dot_view)
	private DotIndicatorBannerPagerView<Boolean> dotIndicatorBannerView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dot_view_layout);
		ViewInjector.initInjectedView(this, this);
		
		List<Boolean> dataList = new ArrayList<Boolean>();
		for(int i = 0;i < 4; i ++)
		{
			dataList.add(false);
		}
		
		dotIndicatorBannerView.setImageViewHolder(new IImageViewHolder<Boolean>()
		{
			@Override
			public void fillData(ImageView imageView, Boolean itemData, int position, int viewType)
			{
				imageView.setBackgroundResource(R.drawable.ic_launcher);
			}
		});
		
		dotIndicatorBannerView.setData(dataList);
		dotIndicatorBannerView.startScroll();
	}
}
