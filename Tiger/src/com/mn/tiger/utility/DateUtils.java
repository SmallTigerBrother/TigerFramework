package com.mn.tiger.utility;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * 该类作用及功能说明:与日期相关的操作
 * 
 * @author zWX200279
 * @date 2014-2-11
 */
public class DateUtils
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = DateUtils.class.getSimpleName();

	/**
	 * 该方法的作用:通过传入的format格式将日期转换为字符串
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param date
	 * @param format
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String date2String(Date date, String format)
	{
		if (date != null && format != null)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			return dateFormat.format(date);
		}
		return null;
	}

	/**
	 * 该方法的作用:通过传入的format格式 将日期转换为字符串
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param dateString
	 * @param format
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static Date string2Date(String dateString, String format)
	{
		if (!StringUtils.isEmptyOrNull(dateString) && !StringUtils.isEmptyOrNull(format))
		{
			Date date = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			try
			{
				date = dateFormat.parse(dateString);
			}
			catch (ParseException e)
			{
				LogTools.e(LOG_TAG,"", e);
			}
			return date;
		}
		return null;
	}

	/**
	 * 该方法的作用:使用compareTo方法比较日期大小
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param date1
	 * @param date2
	 * @return 返回 1 date1>date2 -1 date1<date2 0 date1=date2 3 传入的格式有误
	 */
	public static int compareDate(Date date1, Date date2)
	{
		if (date1 != null && date2 != null)
		{
			return date1.compareTo(date2);
		}
		return 3;
	}

	/**
	 * 该方法的作用:该方法的作用:使用compareTo方法比较日期大小
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param string1
	 * @param format1
	 * @param string2
	 * @param format2
	 * @return 返回 1 date1>date2 -1 date1<date2 0 date1=date2 3 传入的格式有误
	 */
	public static int compareDate1(String string1, String format1, String string2, String format2)
	{
		Date date1 = string2Date(string1, format1);
		Date date2 = string2Date(string2, format2);
		if (date1 == null || date2 == null)
		{
			return 3;
		}
		int i = date1.compareTo(date2);
		if (i > 0)
		{
			return 1;
		}
		else if (i < 0)
		{
			return -1;
		}
		return i;
	}

	/**
	 * 该方法的作用:获取星期几
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param date
	 * @return -1为获取失败 0为周日 1为周一 ....6为周六
	 */
	public static int getWeekofDate(Date date)
	{
		if (date != null)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
			if (day < 0)
			{
				day = 0;
			}
			return day;
		}
		return -1;
	}

	/**
	 * 该方法的作用:将传入的string和format得到Date对象，再获取星期几
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param dateString
	 * @param format
	 * @return -1为获取失败 0为周日 1为周一 ....6为周六
	 */
	public static int getWeekofString(String dateString, String format)
	{
		if (!StringUtils.isEmptyOrNull(dateString) && !StringUtils.isEmptyOrNull(format))
		{
			Date date = string2Date(dateString, format);
			if (date == null)
			{
				return -1;
			}
			int day = getWeekofDate(date);
			return day;
		}
		return -1;
	}
}
