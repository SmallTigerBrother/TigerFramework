package com.mn.tiger.utility;

import android.animation.PropertyValuesHolder;

public class AnimationUtils
{
	public static PropertyValuesHolder getScaleXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("scaleX", values);
	}
	
	public static PropertyValuesHolder getScaleYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("scaleY", values);
	}
	
	public static PropertyValuesHolder getRotationXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("rotationX", values);
	}
	
	public static PropertyValuesHolder getRotationYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("rotationY", values);
	}
	
	public static PropertyValuesHolder getXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("x", values);
	}
	
	public static PropertyValuesHolder getYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("y", values);
	}
	
	public static PropertyValuesHolder getTranslationXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("translationX", values);
	}
	
	public static PropertyValuesHolder getTranslationYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("translationY", values);
	}
	
	public static PropertyValuesHolder getTopPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("top", values);
	}
	
	public static PropertyValuesHolder getBottomPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("bottom", values);
	}
	
	public static PropertyValuesHolder getLeftPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("left", values);
	}
	
	public static PropertyValuesHolder getRightPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("right", values);
	}
	
	public static PropertyValuesHolder getAlphaPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("alpha", values);
	}
	
	public static PropertyValuesHolder getTextSizePropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("textSize", values);
	}
	
	public static PropertyValuesHolder getTextColorPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("textColor", values);
	}
	
	public static PropertyValuesHolder getBackgroundColorPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("backgroundColor", values);
	}
	
	
}
