package com.mn.tiger.demo.template.viewpager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mn.tiger.app.TGFragment;
import com.mn.tiger.demo.R;

public class PagerFragment_1 extends TGFragment
{
	@Override
	protected View onCreateView(LayoutInflater inflater, Bundle savedInstance)
	{
		View view = inflater.inflate(R.layout.pager_fragment, null);
		view.setBackgroundColor(Color.BLUE);
		return view;
	}

}
