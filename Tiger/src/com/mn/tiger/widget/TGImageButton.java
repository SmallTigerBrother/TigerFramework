package com.mn.tiger.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class TGImageButton extends TextView
{
	private float textSize = 18f;
	private int textColor = Color.BLACK;

	public TGImageButton(Context context)
	{
		this(context, null);
	}

	public TGImageButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
	}

	private void initView()
	{
		this.setFocusable(true);
		this.setClickable(true);
		this.setTextSize(textSize);
		this.setTextColor(textColor);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		Drawable bgDrawable = getBackground();
		
		if(TextUtils.isEmpty(getText()) && null != bgDrawable && 
				bgDrawable instanceof BitmapDrawable)
		{
			int viewHeight = MeasureSpec.getSize(heightMeasureSpec);
			double ratio = bgDrawable.getIntrinsicWidth() * 1f / bgDrawable.getIntrinsicHeight() * 1f;
			setMeasuredDimension((int) (viewHeight * ratio + getPaddingLeft() + getPaddingRight()), 
					viewHeight);
		}
		else
		{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
	
	public void setImageDrawable(Drawable drawable)
	{
		this.setBackground(drawable);
	}

	public void setImageResource(int resId)
	{
		this.setBackgroundResource(resId);
	}
}
