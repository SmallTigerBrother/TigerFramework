package com.mn.tiger.task.invoke;

import java.io.Serializable;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;

public class TGTaskParams implements Serializable, Parcelable
{
	/**
	 * @author l00220455
	 * @date 2014年4月14日
	 */
	private static final long serialVersionUID = 1L;

	private Bundle data = null;
	
	public static final int PARAM_TYPE_MAP = 1;

	public static final int PARAM_TYPE_STRING = 2;

	public static final int PARAM_TYPE_BUNDLE = 3;

	public static final int PARAM_TYPE_UNKNOW = 4;

	private int taskMode = 1;
	
	private int taskType = 0;
	
	private int taskWeight = 0;

	private int taskID;

	private Messenger messenger = null;

	public TGTaskParams()
	{
		this.data = new Bundle();
	}

	protected TGTaskParams(Parcel source)
	{
		taskMode = source.readInt();
		taskType = source.readInt();
		taskWeight = source.readInt();
		taskID = source.readInt();
		data = source.readBundle();
		messenger = source.readParcelable(Messenger.class.getClassLoader());
	}
	
	public void setMapParams(HashMap<String, String> params)
	{
		data.putInt("paramType", TGTaskParams.PARAM_TYPE_MAP);
		data.putSerializable("params", params);
	}
	
	public void setStringParams(String params)
	{
		data.putInt("paramType", TGTaskParams.PARAM_TYPE_STRING);
		data.putString("params", params);
	}
	
	public void setBundleParams(Bundle params)
	{
		data.putInt("paramType", TGTaskParams.PARAM_TYPE_BUNDLE);
		data.putParcelable("params", params);
	}
	
	public void setTaskClsName(String taskClsName)
	{
		data.putString("taskClassName", taskClsName);
	}
	
	public Bundle getData()
	{
		return data;
	}
	
	public Object getParams()
	{
		return data.get("params");
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	public int getTaskMode()
	{
		return taskMode;
	}

	public void setTaskMode(int taskMode)
	{
		this.taskMode = taskMode;
	}

	public int getTaskType()
	{
		return taskType;
	}

	public void setTaskType(int taskType)
	{
		this.taskType = taskType;
	}

	public int getTaskWeight()
	{
		return taskWeight;
	}

	public void setTaskWeight(int taskWeight)
	{
		this.taskWeight = taskWeight;
	}

	public int getTaskID()
	{
		return taskID;
	}

	public void setTaskID(int taskID)
	{
		this.taskID = taskID;
	}

	public Messenger getMessenger()
	{
		return messenger;
	}

	public void setMessenger(Messenger messenger)
	{
		this.messenger = messenger;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeInt(taskMode);
		dest.writeInt(taskType);
		dest.writeInt(taskWeight);
		dest.writeInt(taskID);
		dest.writeBundle(data);
		
		if (messenger != null)
		{
			dest.writeParcelable(messenger, flags);
		}
	}

	public static final Parcelable.Creator<TGTaskParams> CREATOR = new Parcelable.Creator<TGTaskParams>()
	{
		@Override
		public TGTaskParams createFromParcel(Parcel source)
		{
			return new TGTaskParams(source);
		}

		@Override
		public TGTaskParams[] newArray(int size)
		{
			return new TGTaskParams[size];
		}
	};

	@Override
	public String toString()
	{
		return "MPTaskParams [data=" + data + ", taskMode=" + taskMode + ", taskType=" + taskType + ", taskWeight=" + taskWeight + ", taskID=" + taskID
				 + ", messenger=" + messenger + "]";
	}

}
