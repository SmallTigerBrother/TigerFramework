package com.mn.tiger.core.cache;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
/**
 * 该类作用及功能说明:数据加解密接口
 * 
 * @date 2014年5月15日
 */
public interface IEncryption
{
	/**
	 * 该方法的作用:加密并保存String数据
	 * 
	 * @date 2014年5月12日
	 * @param content
	 * @param file
	 */
	public void encryptAndSave(Context context,String content, File file);

	/**
	 * 该方法的作用:解密字符串
	 * 
	 * @date 2014年5月12日
	 * @param content
	 * @param file
	 * @return
	 */
	public String decrypt(Context context,String content, File file);

	/**
	 * 该方法的作用:加密并保存bitmap圖片
	 * 
	 * @date 2014年5月12日
	 * @param bitmap
	 * @param file
	 */
	public void encryptAndSaveBitmap(Context context,Bitmap bitmap, File file);

	/**
	 * 该方法的作用: 解密Bitmap图片
	 * 
	 * @date 2014年5月12日
	 * @param file
	 * @return
	 */
	public Object decryptBitmap(Context context,File file);
}
