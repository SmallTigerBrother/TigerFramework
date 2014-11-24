package com.mn.tiger.datastorage.db.table;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import android.database.Cursor;

import com.mn.tiger.log.LogTools;
import com.mn.tiger.parser.json.TGJsonUtils;

public class ColumnObject extends Column
{
	protected ColumnObject(Class<?> entityType, Field field)
	{
		super(entityType, field);
	}
	
	@Override
	public void setValue2Entity(Object entity, Cursor cursor, int index)
	{
		Object value = null;
		Class<?> columnType = columnField.getType();
		if (columnType.equals(List.class))
		{
			value = TGJsonUtils.parseJson2List(cursor.getString(index), columnType);
		}
		else if(columnType.equals(Map.class))
		{
			value = TGJsonUtils.parseJson2Map(cursor.getString(index));
		}
		else
		{
			value = TGJsonUtils.parseJson2Object(cursor.getString(index), columnType);
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
		return getFieldValue(entity);
	}
	
}
