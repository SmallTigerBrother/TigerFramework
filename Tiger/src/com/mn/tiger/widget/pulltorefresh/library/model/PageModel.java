package com.mn.tiger.widget.pulltorefresh.library.model;

import java.util.ArrayList;

public class PageModel<T>
{
	private int listViewRefreshTpye;
	
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

	public int getListViewRefreshTpye()
	{
		return listViewRefreshTpye;
	}

	public void setListViewRefreshTpye(int listViewRefreshTpye)
	{
		this.listViewRefreshTpye = listViewRefreshTpye;
	}
}
