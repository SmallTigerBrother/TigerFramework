package com.mn.tiger.task.result;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * 该类作用及功能说明: 任务结果
 * 
 * @date 2014年7月28日
 */
public class TGTaskResult implements Serializable, Parcelable
{	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DATA_TYPE_UNKNOWN = 0x00000000;

	public static final int DATA_TYPE_INT = 0x00000001;

	public static final int DATA_TYPE_STRING = 0x00000002;

	public static final int DATA_TYPE_SERIALIZEABLE = 0x00000003;

	public static final int DATA_TYPE_PARCELABLE = 0x00000004;

	private int dataType = DATA_TYPE_UNKNOWN;
	
	private int taskID = -1;

	private Object result = null;

	public TGTaskResult()
	{
		
	}

	public TGTaskResult(Parcel source)
	{
		setTaskID(source.readInt());
		dataType = source.readInt();
		switch (dataType)
		{
			case DATA_TYPE_INT:
				setResult(source.readInt());
				break;
			case DATA_TYPE_STRING:
				setResult(source.readString());
				break;
			case DATA_TYPE_PARCELABLE:
				setResult(source.readParcelable(TGTaskResult.class.getClassLoader()));
				break;
			case DATA_TYPE_SERIALIZEABLE:
				setResult(source.readSerializable());
				break;

			default:
				break;
		}
	}
	
	public void setResult(Object result)
	{
		if (null != result)
		{
			if (result instanceof Integer)
			{
				dataType = DATA_TYPE_INT;
			}
			else if (result instanceof String)
			{
				dataType = DATA_TYPE_STRING;
			}
			else if (result instanceof Parcelable)
			{
				dataType = DATA_TYPE_PARCELABLE;
			}
			else if (result instanceof Serializable)
			{
				dataType = DATA_TYPE_SERIALIZEABLE;
			}

			this.result = result;
		}
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(getTaskID());
		Object result = getResult();
		dest.writeInt(dataType);
		if (null != result)
		{
			switch (dataType)
			{
				case DATA_TYPE_INT:
					dest.writeInt((Integer) result);
					break;
				case DATA_TYPE_STRING:
					dest.writeString((String) result);
					break;
				case DATA_TYPE_PARCELABLE:
					dest.writeParcelable((Parcelable) result, flags);
					break;
				case DATA_TYPE_SERIALIZEABLE:
					dest.writeSerializable((Serializable) result);
					break;

				default:
					break;
			}
		}
	}

	public static final Parcelable.Creator<TGTaskResult> CREATOR = new Parcelable.Creator<TGTaskResult>()
	{
		@Override
		public TGTaskResult createFromParcel(Parcel source)
		{
			return new TGTaskResult(source);
		}

		@Override
		public TGTaskResult[] newArray(int size)
		{
			return new TGTaskResult[size];
		}
	};

	@Override
	public String toString()
	{
		return "MPTaskResult [taskID=" + getTaskID() + ", result=" + getResult()
				+ ", dataType=" + dataType + "]";
	}
	
	public Object getResult()
	{
		return result;
	}
	
	public int getTaskID()
	{
		return taskID;
	}

	public void setTaskID(int taskID)
	{
		this.taskID = taskID;
	}
}