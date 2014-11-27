package com.mn.tiger.widget.viewpager;

/**
 * 滚动接口
 */
public interface IScrollable
{
	/**
	 * 是否可以向左滑动
	 * @return
	 */
	boolean canScrollLeft();
	
	/**
	 * 是否可以向右滑动
	 * @return
	 */
	boolean canScrollRight();
	
	/**
	 * 是否可以向上滑动
	 * @return
	 */
	boolean canScrollUp();
	
	/**
	 * 是否可以向下滑动
	 * @return
	 */
	boolean canScrollDown();
}
