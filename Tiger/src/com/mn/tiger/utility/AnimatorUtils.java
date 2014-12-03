package com.mn.tiger.utility;

import com.nineoldandroids.animation.PropertyValuesHolder;

/**
 * 属性动画工具类
 */
public class AnimatorUtils
{
	/**
	 * X轴缩放属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getScaleXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("scaleX", values);
	}
	
	/**
	 * Y轴缩放属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getScaleYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("scaleY", values);
	}
	
	/**
	 * X轴旋转属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getRotationXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("rotationX", values);
	}
	
	/**
	 * Y轴旋转属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getRotationYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("rotationY", values);
	}
	
	/**
	 * X坐标属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getXPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("x", values);
	}
	
	/**
	 * Y坐标属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getYPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("y", values);
	}
	
	/**
	 * top属性（可改变View高度）
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getTopPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("top", values);
	}
	
	/**
	 * bottom属性（可改变View高度）
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getBottomPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("bottom", values);
	}
	
	/**
	 * left属性（可改变View宽度）
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getLeftPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("left", values);
	}
	
	/**
	 * right属性（可改变View宽度）
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getRightPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("right", values);
	}
	
	/**
	 * 透明度属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getAlphaPropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("alpha", values);
	}
	
	/**
	 * 字体大小属性
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getTextSizePropertyHolder(float... values)
	{
		return PropertyValuesHolder.ofFloat("textSize", values);
	}
	
	/**
	 * 字体颜色属性（不是渐变色）
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getTextColorPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("textColor", values);
	}
	
	/**
	 * 背景颜色属性（不是渐变色）
	 * @param values（第一个值为开始值，第二个值为接收值）
	 * @return
	 */
	public static PropertyValuesHolder getBackgroundColorPropertyHolder(int... values)
	{
		return PropertyValuesHolder.ofInt("backgroundColor", values);
	}
}
