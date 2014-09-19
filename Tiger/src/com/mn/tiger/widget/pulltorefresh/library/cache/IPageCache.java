package com.mn.tiger.widget.pulltorefresh.library.cache;

import java.util.List;


/**
 * 该类作用及功能说明
 * 分页缓存接口
 * @version V2.0
 * @see JDK1.6,android-8
 * @date 2013-11-12
 * Copyright Huawei Technologies Co., Ltd. 1998-2011. All rights reserved.
 */
public interface IPageCache<T> 
{
	/**
	 * 该方法的作用:
	 * 添加分页缓存
	 * @date 2013-11-12
	 * @param page
	 * @param pageList
	 */
	void addToPageCache(int page, List<T> pageList);
	
	/**
	 * 该方法的作用:
	 * 删除某一页缓存
	 * @date 2013-11-12
	 * @param page
	 */
	void removePageCache(int page);
	
	/**
	 * 该方法的作用:
	 * 清空分页缓存
	 * @date 2013-11-12
	 */
	void clearPageCache();
	
	/**
	 * 该方法的作用:
	 * 根据页码获取某页的缓存
	 * @date 2013-11-12
	 * @param page
	 * @return
	 */
	List<T> getPageCache(int page);
	
	/**
	 * 该方法的作用:
	 * 设置数据提供端的总页数
	 * @date 2013-11-12
	 * @param totalPage
	 */
	void saveTotalPage(int totalPage);
	
	/**
	 * 该方法的作用:
	 * 获取数据提供端的总页数
	 * @date 2013-11-12
	 * @return
	 */
	int getTotalPage();
	
	/**
	 * 该方法的作用:
	 * 获取当前缓存的总页数
	 * @date 2013-11-15
	 * @return
	 */
	int getCurrentCacheTotalPage();
}
