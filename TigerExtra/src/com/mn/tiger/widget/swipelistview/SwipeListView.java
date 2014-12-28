/*
 * Copyright (C) 2013 47 Degrees, LLC
 * http://47deg.com
 * hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mn.tiger.widget.swipelistview;

import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * ListView subclass that provides the swipe functionality
 */
public class SwipeListView extends ListView
{

	/**
	 * log tag
	 */
	public final static String TAG = "SwipeListView";

	/**
	 * whether debug
	 */
	public final static boolean DEBUG = false;

	/**
	 * Used when user want change swipe list mode on some rows
	 */
	public final static int SWIPE_MODE_DEFAULT = -1;

	/**
	 * Disables all swipes
	 */
	public final static int SWIPE_MODE_NONE = 0;

	
	/**
	 * Enables both left and right swipe
	 */
	public final static int SWIPE_MODE_BOTH = 1;

	/**
	 * Enables right swipe
	 */
	public final static int SWIPE_MODE_RIGHT = 2;

	/**
	 * Enables left swipe
	 */
	public final static int SWIPE_MODE_LEFT = 3;

	/**
     * Binds the swipe gesture to reveal a view behind the row (Drawer style)
     */
    public final static int SWIPE_ACTION_REVEAL = 0;
	
	/**
	 * Dismisses the cell when swiped over
	 */
	public final static int SWIPE_ACTION_DISMISS = 1;

	/**
	 * Marks the cell as checked when swiped and release
	 */
	public final static int SWIPE_ACTION_CHOICE = 2;

	/**
	 * No action when swiped
	 */
	public final static int SWIPE_ACTION_NONE = 3;

	/**
	 * Indicates no movement
	 */
	private final static int TOUCH_STATE_REST = 0;

	/**
	 * State scrolling x position
	 */
	private final static int TOUCH_STATE_SCROLLING_X = 1;

	/**
	 * State scrolling y position
	 */
	private final static int TOUCH_STATE_SCROLLING_Y = 2;

	private int touchState = TOUCH_STATE_REST;

	private float lastMotionX;
	private float lastMotionY;
	private int touchSlop;

	/**
	 * Internal listener for common swipe events
	 */
	private SwipeListViewListener swipeListViewListener;

	/**
	 * Internal touch listener
	 */
	private SwipeListViewTouchListener touchListener;
	
	private OnItemClickListener wrapOnItemClickListener;
	
	/**
	 * If you create a View programmatically you need send back and front
	 * identifier
	 * 
	 * @param context
	 *            Context
	 * @param swipeBackView
	 *            Back Identifier
	 * @param swipeFrontView
	 *            Front Identifier
	 */
	public SwipeListView(Context context, int swipeBackView, int swipeFrontView)
	{
		super(context);
		init(null);
	}

	/**
	 * @see android.widget.ListView#ListView(android.content.Context,
	 *      android.util.AttributeSet)
	 */
	public SwipeListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(attrs);
	}

	/**
	 * @see android.widget.ListView#ListView(android.content.Context,
	 *      android.util.AttributeSet, int)
	 */
	public SwipeListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(attrs);
	}

	/**
	 * Init ListView
	 * @param attrs
	 *            AttributeSet
	 */
	private void init(AttributeSet attrs)
	{
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
		
		super.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				SwipeListView.this.closeAnimate(position);
				if(null != wrapOnItemClickListener)
				{
					wrapOnItemClickListener.onItemClick(parent, view, position, id);
				}
			}
		});
		
		touchListener = new SwipeListViewTouchListener(this);
		
		setOnTouchListener(touchListener);
		setOnScrollListener(touchListener.makeScrollListener());
	}
	
	private SwipeListViewTouchListener getSwipeListViewTouchListener()
	{
		return touchListener;
	}
	
	@Override
	public void setOnItemClickListener(android.widget.AdapterView.OnItemClickListener listener)
	{
		this.wrapOnItemClickListener = listener;
	}
	
	public void setSwipeAnimationTime(long swipeAnimationTime)
	{
		getSwipeListViewTouchListener().setAnimationTime(swipeAnimationTime);
	}
	
	public void setSwipeDrawableChecked(int swipeDrawableChecked)
	{
		getSwipeListViewTouchListener().setSwipeDrawableChecked(swipeDrawableChecked);
	}
	
	public void setSwipeDrawableUnchecked(int swipeDrawableUnchecked)
	{
		getSwipeListViewTouchListener().setSwipeDrawableUnchecked(swipeDrawableUnchecked);
	}
	
	public void setSwipeOffsetLeft(float swipeOffsetLeft)
	{
		getSwipeListViewTouchListener().setLeftOffset(swipeOffsetLeft);
	}
	
	public void setSwipeOffsetRight(float swipeOffsetRight)
	{
		getSwipeListViewTouchListener().setRightOffset(swipeOffsetRight);
	}
	
	/**
	 * Recycle cell. This method should be called from getView in Adapter when
	 * use SWIPE_ACTION_CHOICE
	 * 
	 * @param convertView
	 *            parent view
	 * @param position
	 *            position in list
	 */
	@SuppressWarnings("rawtypes")
	public void recycle(View convertView, int position)
	{
		getSwipeListViewTouchListener().reloadChoiceStateInView(
				((SwipeListViewHolder)convertView.getTag()).getFrontView(), position);
		getSwipeListViewTouchListener().reloadSwipeStateInView(
				((SwipeListViewHolder)convertView.getTag()).getFrontView(), position);

		// Clean pressed state (if dismiss is fire from a cell, to this cell,
		// with a press drawable, in a swipelistview
		// when this cell will be recycle it will still have his pressed state.
		// This ensure the pressed state is
		// cleaned.
		for (int j = 0; j < ((ViewGroup) convertView).getChildCount(); ++j)
		{
			View nextChild = ((ViewGroup) convertView).getChildAt(j);
			nextChild.setPressed(false);
		}
	}

	/**
	 * Get if item is selected
	 * 
	 * @param position
	 *            position in list
	 * @return
	 */
	public boolean isChecked(int position)
	{
		return touchListener.isChecked(position);
	}

	/**
	 * Get positions selected
	 * 
	 * @return
	 */
	public List<Integer> getPositionsSelected()
	{
		return touchListener.getPositionsSelected();
	}

	/**
	 * Count selected
	 * 
	 * @return
	 */
	public int getCountSelected()
	{
		return touchListener.getCountSelected();
	}

	/**
	 * Unselected choice state in item
	 */
	public void unselectedChoiceStates()
	{
		touchListener.unselectedChoiceStates();
	}

	/**
	 * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
	 */
	@Override
	public void setAdapter(ListAdapter adapter)
	{
		super.setAdapter(adapter);
		touchListener.resetItems();
		if (null != adapter)
		{
			adapter.registerDataSetObserver(new DataSetObserver()
			{
				@Override
				public void onChanged()
				{
					super.onChanged();
					onListChanged();
					touchListener.resetItems();
				}
			});
		}
	}

	/**
	 * Dismiss item
	 * 
	 * @param position
	 *            Position that you want open
	 */
	public void dismiss(int position)
	{
		int height = touchListener.dismiss(position);
		if (height > 0)
		{
			touchListener.handlerPendingDismisses(height);
		}
		else
		{
			int[] dismissPositions = new int[1];
			dismissPositions[0] = position;
			onDismiss(dismissPositions);
			touchListener.resetPendingDismisses();
		}
	}

	/**
	 * Dismiss items selected
	 */
	public void dismissSelected()
	{
		List<Integer> list = touchListener.getPositionsSelected();
		int[] dismissPositions = new int[list.size()];
		int height = 0;
		for (int i = 0; i < list.size(); i++)
		{
			int position = list.get(i);
			dismissPositions[i] = position;
			int auxHeight = touchListener.dismiss(position);
			if (auxHeight > 0)
			{
				height = auxHeight;
			}
		}
		if (height > 0)
		{
			touchListener.handlerPendingDismisses(height);
		}
		else
		{
			onDismiss(dismissPositions);
			touchListener.resetPendingDismisses();
		}
		touchListener.returnOldActions();
	}

	/**
	 * Open ListView's item
	 * 
	 * @param position
	 *            Position that you want open
	 */
	public void openAnimate(int position)
	{
		touchListener.openAnimate(position);
	}

	/**
	 * Close ListView's item
	 * 
	 * @param position
	 *            Position that you want open
	 */
	public void closeAnimate(int position)
	{
		touchListener.closeAnimate(position);
	}

	/**
	 * Notifies onDismiss
	 * 
	 * @param reverseSortedPositions
	 *            All dismissed positions
	 */
	protected void onDismiss(int[] reverseSortedPositions)
	{
		if (swipeListViewListener != null)
		{
			swipeListViewListener.onDismiss(reverseSortedPositions);
		}
	}

	/**
	 * Start open item
	 * 
	 * @param position
	 *            list item
	 * @param action
	 *            current action
	 * @param right
	 *            to right
	 */
	protected void onStartOpen(int position, int action, boolean right)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onStartOpen(position, action, right);
		}
	}

	/**
	 * Start close item
	 * 
	 * @param position
	 *            list item
	 * @param right
	 */
	protected void onStartClose(int position, boolean right)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onStartClose(position, right);
		}
	}

	/**
	 * Notifies onClickFrontView
	 * 
	 * @param position
	 *            item clicked
	 */
	protected void onClickFrontView(View frontView, int position)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onClickFrontView(frontView, position);
		}
	}

	/**
	 * Notifies onClickBackView
	 * 
	 * @param position
	 *            back item clicked
	 */
	protected void onClickBackView(View backView, int position)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onClickBackView(backView, position);
		}
	}

	/**
	 * Notifies onOpened
	 * 
	 * @param position
	 *            Item opened
	 * @param toRight
	 *            If should be opened toward the right
	 */
	protected void onOpened(int position, boolean toRight)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onOpened(position, toRight);
		}
	}

	/**
	 * Notifies onClosed
	 * 
	 * @param position
	 *            Item closed
	 * @param fromRight
	 *            If open from right
	 */
	protected void onClosed(int position, boolean fromRight)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onClosed(position, fromRight);
		}
	}

	/**
	 * Notifies onChoiceChanged
	 * 
	 * @param position
	 *            position that choice
	 * @param selected
	 *            if item is selected or not
	 */
	protected void onChoiceChanged(int position, boolean selected)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onChoiceChanged(position, selected);
		}
	}

	/**
	 * User start choice items
	 */
	protected void onChoiceStarted()
	{
		if (swipeListViewListener != null)
		{
			swipeListViewListener.onChoiceStarted();
		}
	}

	/**
	 * User end choice items
	 */
	protected void onChoiceEnded()
	{
		if (swipeListViewListener != null)
		{
			swipeListViewListener.onChoiceEnded();
		}
	}

	/**
	 * User is in first item of list
	 */
	protected void onFirstListItem()
	{
		if (swipeListViewListener != null)
		{
			swipeListViewListener.onFirstListItem();
		}
	}

	/**
	 * User is in last item of list
	 */
	protected void onLastListItem()
	{
		if (swipeListViewListener != null)
		{
			swipeListViewListener.onLastListItem();
		}
	}

	/**
	 * Notifies onListChanged
	 */
	protected void onListChanged()
	{
		if (swipeListViewListener != null)
		{
			swipeListViewListener.onListChanged();
		}
	}

	/**
	 * Notifies onMove
	 * 
	 * @param position
	 *            Item moving
	 * @param x
	 *            Current position
	 */
	protected void onMove(int position, float x)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			swipeListViewListener.onMove(position, x);
		}
	}

	protected int changeSwipeMode(int position)
	{
		if (swipeListViewListener != null && position != ListView.INVALID_POSITION)
		{
			return swipeListViewListener.onChangeSwipeMode(position);
		}
		return SWIPE_MODE_DEFAULT;
	}

	/**
	 * Sets the Listener
	 * 
	 * @param swipeListViewListener
	 *            Listener
	 */
	public void setSwipeListViewListener(SwipeListViewListener swipeListViewListener)
	{
		this.swipeListViewListener = swipeListViewListener;
	}

	/**
	 * Resets scrolling
	 */
	public void resetScrolling()
	{
		touchState = TOUCH_STATE_REST;
	}

	/**
	 * Set if all items opened will be closed when the user moves the ListView
	 * 
	 * @param swipeCloseAllItemsWhenMoveList
	 */
	public void setSwipeCloseAllItemsWhenMoveList(boolean swipeCloseAllItemsWhenMoveList)
	{
		getSwipeListViewTouchListener().setSwipeClosesAllItemsWhenListMoves(swipeCloseAllItemsWhenMoveList);
	}

	/**
	 * Sets if the user can open an item with long pressing on cell
	 * 
	 * @param swipeOpenOnLongPress
	 */
	public void setSwipeOpenOnLongPress(boolean swipeOpenOnLongPress)
	{
		getSwipeListViewTouchListener().setSwipeOpenOnLongPress(swipeOpenOnLongPress);
	}

	/**
	 * Set swipe mode
	 * 
	 * @param swipeMode
	 */
	public void setSwipeMode(int swipeMode)
	{
		getSwipeListViewTouchListener().setSwipeMode(swipeMode);
	}

	/**
	 * Return action on left
	 * 
	 * @return Action
	 */
	public int getSwipeActionLeft()
	{
		return getSwipeListViewTouchListener().getSwipeActionLeft();
	}

	/**
	 * Set action on left
	 * 
	 * @param swipeActionLeft
	 *            Action
	 */
	public void setSwipeActionLeft(int swipeActionLeft)
	{
		getSwipeListViewTouchListener().setSwipeActionLeft(swipeActionLeft);
	}

	/**
	 * Return action on right
	 * 
	 * @return Action
	 */
	public int getSwipeActionRight()
	{
		return getSwipeListViewTouchListener().getSwipeActionRight();
	}

	/**
	 * Set action on right
	 * 
	 * @param swipeActionRight
	 *            Action
	 */
	public void setSwipeActionRight(int swipeActionRight)
	{
		getSwipeListViewTouchListener().setSwipeActionRight(swipeActionRight);
	}

	/**
	 * @see android.widget.ListView#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		int action = MotionEventCompat.getActionMasked(ev);
		final float x = ev.getX();
		final float y = ev.getY();

		if (isEnabled() && touchListener.isSwipeEnabled())
		{

			if (touchState == TOUCH_STATE_SCROLLING_X)
			{
				return touchListener.onTouch(this, ev);
			}

			switch (action)
			{
				case MotionEvent.ACTION_MOVE:
					checkInMoving(x, y);
					return touchState == TOUCH_STATE_SCROLLING_Y;
				case MotionEvent.ACTION_DOWN:
					super.onInterceptTouchEvent(ev);
					touchListener.onTouch(this, ev);
					touchState = TOUCH_STATE_REST;
					lastMotionX = x;
					lastMotionY = y;
					return false;
				case MotionEvent.ACTION_CANCEL:
					touchState = TOUCH_STATE_REST;
					break;
				case MotionEvent.ACTION_UP:
					touchListener.onTouch(this, ev);
					return touchState == TOUCH_STATE_SCROLLING_Y;
				default:
					break;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * Check if the user is moving the cell
	 * 
	 * @param x
	 *            Position X
	 * @param y
	 *            Position Y
	 */
	private void checkInMoving(float x, float y)
	{
		final int xDiff = (int) Math.abs(x - lastMotionX);
		final int yDiff = (int) Math.abs(y - lastMotionY);

		final int touchSlop = this.touchSlop;
		boolean xMoved = xDiff > touchSlop;
		boolean yMoved = yDiff > touchSlop;

		if (xMoved)
		{
			touchState = TOUCH_STATE_SCROLLING_X;
			lastMotionX = x;
			lastMotionY = y;
		}

		if (yMoved)
		{
			touchState = TOUCH_STATE_SCROLLING_Y;
			lastMotionX = x;
			lastMotionY = y;
		}
	}

	/**
	 * Close all opened items
	 */
	public void closeOpenedItems()
	{
		touchListener.closeOpenedItems();
	}

}
