package com.mn.tiger.demo.widget.viewpager.horizontal.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mn.tiger.app.TGFragment;
import com.mn.tiger.demo.R;

public class PagerFragment_2 extends TGFragment
{
	@Override
	protected View onCreateView(LayoutInflater inflater, Bundle savedInstance)
	{
		View view = inflater.inflate(R.layout.pager_fragment, null);
		view.setBackgroundColor(Color.BLACK);
		return view;
	}

}
