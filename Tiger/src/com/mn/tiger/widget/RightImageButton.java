package com.mn.tiger.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.mn.tiger.utility.DisplayUtils;

class RightImageButton extends TGImageButton
{
	public RightImageButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void draw(Canvas canvas)
	{
		Drawable bgDrawable = getBackground();
		if(TextUtils.isEmpty(getText()) && null != bgDrawable && 
				bgDrawable instanceof BitmapDrawable)
		{
			Paint paint = getPaint();
			Matrix matrix = new Matrix();
			
			int left = getMeasuredWidth() - bgDrawable.getIntrinsicWidth();
			matrix.setTranslate(left, DisplayUtils.dip2px(getContext(), getPaddingTop()));
			canvas.drawBitmap(((BitmapDrawable)bgDrawable).getBitmap(), 
					matrix, paint);
		}
		else
		{
			super.draw(canvas);
		}
	}
}
