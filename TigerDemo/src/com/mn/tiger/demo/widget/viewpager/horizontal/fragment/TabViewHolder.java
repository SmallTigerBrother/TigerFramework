package com.mn.tiger.demo.widget.viewpager.horizontal.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mn.tiger.annonation.ViewById;
import com.mn.tiger.demo.R;
import com.mn.tiger.widget.adpter.TGViewHolder;
import com.mn.tiger.widget.tab.TGTabView.LayoutParams;

public class TabViewHolder extends TGViewHolder<TabModel>
{
	@ViewById(id = R.id.tab_item_image)
	private ImageView imageView;
	
	@ViewById(id = R.id.tab_item_name)
	private TextView textView;
	
	@Override
	public View initView(View convertView)
	{
		View view = super.initView(convertView);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT, 1);
		view.setLayoutParams(layoutParams);
		return view;
	}
	
	@Override
	public void fillData(TabModel itemData, int position)
	{
		imageView.setImageResource(itemData.getImageResId());
		textView.setText(itemData.getTabName());
	}
	
	public ImageView getImageView()
	{
		return imageView;
	}
}
