package com.mn.tiger.collection;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

/**
 * 在线收藏管理类
 */
public abstract class OnLineCollectionManager<T extends ICollectable> implements 
   ICollectionManager<T>
{
	@SuppressWarnings("unchecked")
	@Override
	public void insertCollection(Context context, T productInfo,
			final IModifyCollectionCallback callback)
	{
		List<T> productInfos = new ArrayList<T>();
		productInfos.add(productInfo);
		insertCollection(context, (T[])productInfos.toArray(), callback);
	}
	
	@Override
	public boolean isCollected(Context context, T productInfo)
	{
		return productInfo.isCollected();
	}
	

}
