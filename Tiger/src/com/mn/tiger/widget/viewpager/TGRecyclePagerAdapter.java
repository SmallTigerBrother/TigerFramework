package com.mn.tiger.widget.viewpager;

import java.util.ArrayList;

import com.mn.tiger.log.Logger;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

/**
 * 支持视图重用的PagerAdapter
 * @param <T> 每页的数据类型
 */
public class TGRecyclePagerAdapter<T> extends PagerAdapter
{
	private static final Logger LOG = Logger.getLogger(TGRecyclePagerAdapter.class);
	
	/**
	 * 忽略类型
	 */
	static final int IGNORE_ITEM_VIEW_TYPE = AdapterView.ITEM_VIEW_TYPE_IGNORE;

	private Activity activity;

	/**
	 * 保存重用视图的数组
	 */
	private RecyleArray recyleArray = null;
	
	/**
	 * 分页数据
	 */
	private ArrayList<T> pagerData;
	
	/**
	 * 分页视图ViewHolder类
	 */
	private Class<? extends TGPagerViewHolder<T>> viewHolderClazz;
	
	/**
	 * 默认的ViewHolder，用来获取ViewType
	 */
	private TGPagerViewHolder<T> pagerViewHolder;

	public TGRecyclePagerAdapter(Activity activity, ArrayList<T> pagerData, 
			Class<? extends TGPagerViewHolder<T>> viewHolderClazz)
	{
		this.activity = activity;
		this.pagerData = pagerData;
		this.viewHolderClazz = viewHolderClazz;
		recyleArray = new RecyleArray();
		try
		{
			pagerViewHolder = viewHolderClazz.newInstance();
		}
		catch (Exception e)
		{
			LOG.e(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Object instantiateItem(ViewGroup container, int position)
	{
		//查找ViewPager中是否已存在子视图
		View view = recyleArray.getScrapView(pagerViewHolder.getItemViewType(position));
		try
		{
			TGPagerViewHolder<T> viewHolder;
			if(null == view)
			{
				//初始化viewholder
				viewHolder = viewHolderClazz.newInstance();
				viewHolder.setActivity(activity);
				viewHolder.setPagerAdapter(this);
				//初始化子视图
				view = viewHolder.initPage(pagerViewHolder.getItemViewType(position));
				view.setTag(viewHolder);
			}
			//填充数据
			viewHolder = (TGPagerViewHolder<T>) view.getTag();
			if(null != pagerData)
			{
				viewHolder.fillData(pagerData.get(position), position);
			}
			else
			{
				viewHolder.fillData(null, position);
			}
			container.addView(view);
		}
		catch (Exception e)
		{
			LOG.e(e);
		}
		
		return view;
	}

	@Override
	public final void destroyItem(ViewGroup container, int position, Object object)
	{
		View view = (View) object;
		
		//从container中移除
		container.removeView(view);
		
		//加入到已遗弃视图数组中
		recyleArray.addScrapView(view, pagerViewHolder.getItemViewType(position));
	}

	@Override
	public final boolean isViewFromObject(View view, Object object)
	{
		return view == object;
	}

	protected Activity getActivity()
	{
		return activity;
	}
	
	@Override
	public int getCount()
	{
		if(null != pagerData)
		{
			return pagerData.size();
		}
		return 0;
	}
	
	/**
	 * 保存重用视图的数组
	 */
	private class RecyleArray
	{
		/**
		 * 保存多种ViewType重用视图的数组
		 */
		private SparseArray<ArrayList<View>> allScrapViewArrays;
		
		public RecyleArray()
		{
			allScrapViewArrays = new SparseArray<ArrayList<View>>();
		}
		
		/**
		 * 添加弃用的视图
		 * @param view
		 * @param viewType
		 */
		public void addScrapView(View view, int viewType)
		{
			//根据ViewType获取对应的数组
			ArrayList<View> scrapArray = allScrapViewArrays.get(viewType);
			//初始化对应类型的view数组
			if(null == scrapArray)
			{
				scrapArray = new ArrayList<View>();
				allScrapViewArrays.put(viewType, scrapArray);
			}
			scrapArray.add(view);
		}
		
		/**
		 * 获取弃用的视图
		 * @param viewType
		 * @return
		 */
		public View getScrapView(int viewType)
		{
			//根据viewType获取view列表
			ArrayList<View> viewArray = getScrapViewArrayByType(viewType);
			//查找该类型下的未使用的子视图
			if(viewArray.size() == 0)
			{
				return null;
			}
			else
			{
				View view;
				for(int i = 0; i < viewArray.size(); i++)
				{
					//若view不为null，返回
					view = viewArray.get(i);
					if(null != view)
					{
						viewArray.remove(view);
						return view;
					}
					else
					{
						//清楚null对象
						viewArray.remove(i);
					}
				}
				
				return null;
			}
		}
		
		/**
		 * 根据ViewType获取弃用View的数组
		 */
		private ArrayList<View> getScrapViewArrayByType(int viewType)
		{
			ArrayList<View> scrapArray = allScrapViewArrays.get(viewType);
			if(null == scrapArray)
			{
				scrapArray = new ArrayList<View>();
				allScrapViewArrays.put(viewType, scrapArray);
			}
			return scrapArray;
		}
	}
}
