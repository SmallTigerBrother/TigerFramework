package com.mn.tiger.system;

/**
 * 
 * 该类作用及功能说明:版本信息
 * 
 * @date 2014年4月26日
 */
public class Tiger
{
	/**
	 * 当前使用版本
	 */
	public static final VERSION_CODES SDK_CODE = VERSION_CODES.CARROT;
	
	/**
	 * 
	 * 该类作用及功能说明:版本代号
	 * 
	 * @date 2014年4月26日
	 */
	public static enum VERSION_CODES
	{
		PENGUIN("1.5.0", 1), 
		CANDY("2.0.0", 2), 
		PILLOW("2.0.1", 3), 
		CALENDAR("2.1.0", 4), 
		CAKE("2.1.1", 5),
		ZONGZI("2.2.0", 6),
		APRICOT("2.2.1", 7),
		SUGARCANE("2.3.0", 8),
		CARROT("3.0.0", 9);
		
		// release版本号
		public String RELEASE;
		// 版本index
		public int SDK_INT;

		// 构造方法
		private VERSION_CODES(String release, int sdk_int)
		{
			this.RELEASE = release;
			this.SDK_INT = sdk_int;
		}
	}
}
