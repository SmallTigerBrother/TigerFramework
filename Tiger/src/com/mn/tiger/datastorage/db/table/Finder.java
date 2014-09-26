package com.mn.tiger.datastorage.db.table;

import java.lang.reflect.Field;
import java.util.List;

import android.database.Cursor;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.FinderLazyLoader;
import com.mn.tiger.utility.LogTools;

public class Finder extends Column
{

	public TGDBManager db;

	private final String valueColumnName;
	private final String targetColumnName;

	protected Finder(Class<?> entityType, Field field)
	{
		super(entityType, field);

		com.mn.tiger.datastorage.db.annotation.Finder finder = field
				.getAnnotation(com.mn.tiger.datastorage.db.annotation.Finder.class);
		this.valueColumnName = finder.valueColumn();
		this.targetColumnName = finder.targetColumn();
	}

	public Class<?> getTargetEntityType()
	{
		return ColumnUtils.getFinderTargetEntityType(this);
	}

	public String getTargetColumnName()
	{
		return targetColumnName;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setValue2Entity(Object entity, Cursor cursor, int index)
	{
		Object value = null;
		Class<?> columnType = columnField.getType();
		Object finderValue = TableUtils.getColumnOrId(entity.getClass(), this.valueColumnName)
				.getColumnValue(entity);
		if (columnType.equals(FinderLazyLoader.class))
		{
			value = new FinderLazyLoader(this, finderValue);
		}
		else if (columnType.equals(List.class))
		{
			try
			{
				value = new FinderLazyLoader(this, finderValue).getAllFromDb();
			}
			catch (DbException e)
			{
				LogTools.e(e.getMessage(), e);
			}
		}
		else
		{
			try
			{
				value = new FinderLazyLoader(this, finderValue).getFirstFromDb();
			}
			catch (DbException e)
			{
				LogTools.e(e.getMessage(), e);
			}
		}

		if (setMethod != null)
		{
			try
			{
				setMethod.invoke(entity, value);
			}
			catch (Throwable e)
			{
				LogTools.e(e.getMessage(), e);
			}
		}
		else
		{
			try
			{
				this.columnField.setAccessible(true);
				this.columnField.set(entity, value);
			}
			catch (Throwable e)
			{
				LogTools.e(e.getMessage(), e);
			}
		}
	}

	@Override
	public Object getColumnValue(Object entity)
	{
		return null;
	}

	@Override
	public Object getDefaultValue()
	{
		return null;
	}

	@Override
	public String getColumnDbType()
	{
		return "";
	}
}
