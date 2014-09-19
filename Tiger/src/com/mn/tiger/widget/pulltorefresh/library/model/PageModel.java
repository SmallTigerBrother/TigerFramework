package com.mn.tiger.widget.pulltorefresh.library.model;

import java.util.ArrayList;

/**
 * 列表分页模型
 * @author Dalang
 *
 * @param <T>
 */
public class PageModel<T>
{
	private int totalPage;
	
	private int currentPage;
	
	private ArrayList<T> page;

	public int getTotalPage()
	{
		return totalPage;
	}

	public void setTotalPage(int totalPage)
	{
		this.totalPage = totalPage;
	}

	public int getCurrentPage()
	{
		return currentPage;
	}

	public void setCurrentPage(int currentPage)
	{
		this.currentPage = currentPage;
	}

	public ArrayList<T> getPage()
	{
		return page;
	}

	public void setPage(ArrayList<T> page)
	{
		this.page = page;
	}
}
