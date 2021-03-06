package com.mn.tiger.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.widget.TextView;

import com.mn.tiger.log.LogTools;


/**
 *  
 * 该类作用及功能说明: 与String相关的一些转换等操作
 * 
 * @date 2014-2-11
 */
public class StringUtils
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = StringUtils.class.getSimpleName();

	/**
	 * 判断TextView中的字符串是否为null或者""
	 * @param textView
	 * @return
	 */
	public static boolean isTextEmpty(TextView textView)
	{
		if(null == textView)
		{
			throw new IllegalArgumentException("the textview can not be null");
		}
		
		return TextUtils.isEmpty(textView.getText().toString().trim());
	}
	
	/**
	 * 该方法的作用:判断字符串是否为null 或者为""
	 * 
	 * @date 2014-1-23
	 * @param content
	 * @return true表示字符串为null或者""
	 */
	public static boolean isEmptyOrNull(String content)
	{
		if (content == null || content.equals(""))
		{
			return true;
		}
		return false;
	}

	/**
	 * 该方法的作用: 判断字符串是否为IP地址
	 * 
	 * @date 2014-01-23
	 * @param ipString
	 * @return
	 */
	public static boolean isIPAddress(String ipString)
	{
		if (ipString != null)
		{
			// 将字符串通过.分割
			String[] singleArray = ipString.split("\\.");
			if (singleArray == null)
			{
				return false;
			}
			// 遍历分割后的所有字符串，转换成int型再进行判断
			for (String numString : singleArray)
			{
				if (isEmptyOrNull(numString.trim()))
				{
					return false;
				}
				try
				{
					int num = Integer.parseInt(numString.trim());
					if (num < 0 || num > 255)
					{
						return false;
					}
				}
				catch (NumberFormatException e)
				{
					LogTools.e(LOG_TAG,"", e);
					return false;
				}

			}
			return true;
		}
		return false;
	}

	/**
	 * 该方法的作用:通过正则表达式判断是否为email地址
	 * 
	 * @date 2014-1-23
	 * @param emailString
	 * @return
	 */
	public static boolean isEmailAddress(String email)
	{
		boolean tag = true;
		final String pattern1 = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		final Pattern pattern = Pattern.compile(pattern1);
		final Matcher mat = pattern.matcher(email);
		if (!mat.find())
		{
			tag = false;
		}
		return tag;
	}

	/**
	 * 该方法的作用:通过正则表达式判断是否为数字
	 * 
	 * @date 2014-1-23
	 * @param digitString
	 * @return
	 */
	public static boolean isDigit(String digitString)
	{
		if (!isEmptyOrNull(digitString))
		{
			String regex = "[0-9]*";
			return isMatch(regex, digitString);
		}
		return false;
	}

	/**
	 * 该方法的作用: 通过正则表达式判断是否为手机号
	 * 
	 * @date 2014-01-23
	 * @param phoneString
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneString)
	{
		String format = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
		return isMatch(format, phoneString);
	}

	/**
	 * 该方法的作用: 字符串正则校验
	 * 
	 * @date 2014-01-23
	 * @param regex
	 *            正则表达式
	 * @param string
	 *            需要检验的字符串
	 * @return
	 */
	public static boolean isMatch(String regex, String string)
	{

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(string);
		return matcher.matches();
	}

	/**
	 * 该方法的作用: 通过正则表达式判断是否为URL地址
	 * 
	 * @date 2014-01-23
	 * @param strIp
	 * @return
	 */
	public static boolean isUrl(String strIp)
	{
		String strPattern = "^((https?)|(ftp))://(?:(\\s+?)(?::(\\s+?))?@)?([a-zA-Z0-9\\-.]+)"
				+ "(?::(\\d+))?((?:/[a-zA-Z0-9\\-._?,'+\\&%$=~*!():@\\\\]*)+)?$";
		return isMatch(strPattern, strIp);
	}

	/**
	 * 该方法的作用: String 转换成Unicode
	 * 
	 * @date 2014-01-23
	 * @param string
	 *            传入汉字
	 * @return
	 */
	public static String string2Unicode(String string)
	{
		if (!isEmptyOrNull(string))
		{
			char[] charArray = string.toCharArray();
			StringBuffer buffer = new StringBuffer();
			// 遍历数组中所有元素，转换成int型，再拼接
			for (char ch : charArray)
			{
				int code = (int) ch;
				buffer.append(code);
			}
			return buffer.toString();
		}
		return null;
	}

	/**
	 * 该方法的作用: Unicode转换成String
	 * 
	 * @date 2014-01-23
	 * @param string
	 * @return
	 */
	public static String unicode2String(String string)
	{
		if (!isEmptyOrNull(string))
		{
			// 计算共有多少字符
			int end = 0;
			String noSpace = string.trim();
			int count = noSpace.length() / 5;
			StringBuffer buffer = new StringBuffer();

			for (int j = 0; j < count; j++)
			{
				// 截取每个字符的Unicode并转换成char型
				end += 5;
				int uCode = Integer.valueOf(noSpace.substring(j * 5, end));
				buffer.append((char) uCode);

			}
			return buffer.toString();
		}
		return null;
	}

	/**
	 * 该方法的作用:获取url中某参数的值 参数:传入需要获取的参数的名 返回:该参数的值(字符串型) 异常:
	 * 在什么情况下调用:需要获取url中某参数的值
	 * 
	 * @date 2012-12-24
	 * @param url
	 * @param paramName
	 * @return
	 */
	public static String getParamValueOfUrl(String url, String paramName)
	{
		try
		{
			String urls[] = url.split("[?]");
			if (urls.length > 1)
			{
				String param = urls[1];
				String params[] = param.split("[&]");
				for (String string : params)
				{
					String keyAndValue[] = string.split("[=]");
					if (keyAndValue.length > 1)
					{
						String key = keyAndValue[0];
						String value = keyAndValue[1];
						if (key.equalsIgnoreCase(paramName))
						{
							return value;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			return "";
		}
		return "";
	}
}
