package com.mn.tiger.collection;

import java.lang.reflect.Array;

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
		T[] productInfos = (T[]) Array.newInstance(productInfo.getClass(), 1);
		productInfos[0] = productInfo;
		insertCollection(context, productInfos, callback);
	}
	
	@Override
	public boolean isCollected(Context context, T productInfo)
	{
		return productInfo.isCollected();
	}
}
