package com.mn.tiger.utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import com.mn.tiger.log.LogTools;

public class BitmapUtils
{
	/**
	 * 垂直排布
	 */
	public static int IMAGE_MERGE_TYPE_VETTICAL = 0;
	/**
	 * 水平排布
	 */
	public static int IMAGE_MERGE_TYPE_HORIZONTAL = 1;
	/**
	 * 图片间的间隙距离
	 */
	private static int IMAGE_SPACE = 3;
	/**
	 * 相对源图片的偏移
	 */
	private static int IMAGE_OFFSET = 6;

	/**
	 * 根据文件路径获取指定宽高的Bitmap
	 * 
	 * @param filename
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromFile(String filename, int reqWidth, int reqHeight)
	{

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 *  根据指定的安宽高计算取样率
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
			int reqHeight)
	{
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth)
		{

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee a final image
			// with both dimensions larger than or equal to the requested height
			// and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap)
			{
				inSampleSize++;
			}
		}
		return inSampleSize;
	}

	/**
	 * 该方法的作用:混合兩張圖片
	 * 
	 * @date 2014年3月24日
	 * @param src
	 * @param src1
	 * @param flag
	 * @return
	 */
	public static Bitmap mergeBitmap(Bitmap src1, Bitmap src2, int flag)
	{
		if (src1 == null)
		{
			return null;
		}
		int width1 = src1.getWidth();
		int height1 = src1.getHeight();
		int width2 = src2.getWidth();
		int height2 = src2.getHeight();

		int desw = 0;
		int desh = 0;
		// 判断是否是水平排布，计算要使用的高和宽
		if (flag == IMAGE_MERGE_TYPE_HORIZONTAL)
		{
			desw = width1 + width2;
			desh = Math.max(height1, height2);
		}
		else
		{
			desw = Math.max(width1, width2);
			desh = height1 + height2;
		}

		Bitmap newb = Bitmap.createBitmap(desw, desh, Config.ARGB_8888);
		Canvas canvas = new Canvas(newb);
		canvas.drawBitmap(src1, 0, 0, null);

		if (flag == IMAGE_MERGE_TYPE_HORIZONTAL)
		{
			canvas.drawBitmap(src2, width1, 0, null);
		}
		else
		{
			canvas.drawBitmap(src2, 0, height1, null);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newb;
	}

	/**
	 * 该方法的作用:切割图片
	 * 
	 * @date 2014年3月24日
	 * @param bitmap
	 * @param xPiece
	 *            垂直切割多少份
	 * @param yPiece
	 *            水平切割多少份
	 * @return
	 */
	public static ArrayList<Bitmap> split(Bitmap bitmap, int xPiece, int yPiece)
	{
		ArrayList<Bitmap> pieces = new ArrayList<Bitmap>();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 计算切割后每张图片的宽度
		int pieceWidth = width / xPiece;
		// 计算切割后每张图片的高度
		int pieceHeight = height / yPiece;
		// 循环切割
		for (int i = 0; i < yPiece; i++)
		{
			for (int j = 0; j < xPiece; j++)
			{
				int xValue = j * pieceWidth;
				int yValue = i * pieceHeight;
				Bitmap bitmap_piece = Bitmap.createBitmap(bitmap, xValue, yValue, pieceWidth,
						pieceHeight);
				pieces.add(bitmap_piece);
			}
		}
		return pieces;
	}

	/**
	 * 该方法的作用:将图片置灰
	 * 
	 * @date 2014年3月24日
	 * @param img
	 * @return
	 */
	public static Bitmap convertGreyImg(Bitmap img)
	{
		int width = img.getWidth();
		int height = img.getHeight();

		int[] pixels = new int[width * height];

		img.getPixels(pixels, 0, width, 0, 0, width, height);
		int alpha = 0xFF << 24;
		// 将所有的像素点颜色设置成灰色
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int grey = pixels[width * i + j];

				int red = ((grey & 0x00FF0000) >> 16);
				int green = ((grey & 0x0000FF00) >> 8);
				int blue = (grey & 0x000000FF);

				grey = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
				grey = alpha | (grey << 16) | (grey << 8) | grey;
				pixels[width * i + j] = grey;
			}
		}
		// 创建一个指定宽高的Bitmap
		Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);
		// 将上面得到的像素点集合设置到Bitmap上
		result.setPixels(pixels, 0, width, 0, 0, width, height);
		return result;
	}

	/**
	 * 该方法的作用:缩小图片
	 * 
	 * @date 2014年3月24日
	 * @param bgimage
	 * @param newWidth
	 *            希望得到的Bitmap的宽度
	 * @param newHeight
	 *            希望得到的Bitmap的高度
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bgimage, int newWidth, int newHeight)
	{
		int width = bgimage.getWidth();
		int height = bgimage.getHeight();
		Matrix matrix = new Matrix();
		// 计算宽度的缩放比例
		float scaleWidth = ((float) newWidth) / width;
		// 计算高度的缩放比例
		float scaleHeight = ((float) newHeight) / height;
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, width, height, matrix, true);
		return bitmap;
	}

	/**
	 * 
	 * 该方法的作用:压缩图片质量
	 * 
	 * @date 2013-4-16
	 * @param fromFile
	 *            要压缩的图片路径
	 * @param toFile
	 *            压缩后的保存路径
	 * @param quality
	 *            图片质量(0~100)
	 */
	public static Bitmap compressBitmap(String fromFile, String toFile, int quality)
	{
		Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
		return compressBitmap(bitmap, toFile, quality);
	}

	/**
	 * 
	 * 该方法的作用:压缩图片质量
	 * 
	 * @date 2013-4-16
	 * @param inputstream
	 * @param toFile
	 * @param quality
	 * @return
	 */
	public static Bitmap compressBitmap(InputStream inputstream, String toFile, int quality)
	{
		Bitmap bitmap = BitmapFactory.decodeStream(inputstream);
		return compressBitmap(bitmap, toFile, quality);
	}

	/**
	 * 
	 * 该方法的作用:压缩图片质量
	 * 
	 * @date 2013-4-16
	 * @param bitmap
	 * @param toFile
	 * @param quality
	 * @return
	 */
	public static Bitmap compressBitmap(Bitmap bitmap, String path, int quality)
	{
		Bitmap newBM = null;
		FileOutputStream out = null;
		try
		{
			File myCaptureFile = new File(path);
			if (!myCaptureFile.exists())
			{
				File dir = myCaptureFile.getParentFile();
				if (!dir.exists())
				{
					dir.mkdirs();
				}
				myCaptureFile.createNewFile();
			}
			out = new FileOutputStream(myCaptureFile);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out))
			{
				out.flush();
				out.close();
			}
			// Release memory resources
			if (!bitmap.isRecycled())
			{
				bitmap.recycle();
			}
			if (!bitmap.isRecycled())
			{
				bitmap.recycle();
			}
			newBM = BitmapFactory.decodeFile(path);
		}
		catch (Exception e)
		{
			LogTools.e(e);
		}
		finally
		{
			try
			{
				if (out != null)
				{
					out.close();
				}
			}
			catch (Exception e)
			{
				LogTools.e(e);
			}
		}
		return newBM;
	}

	/**
	 * 该方法的作用:设置圆角
	 * 
	 * @date 2014年3月25日
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
	{

		Bitmap output = Bitmap
				.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 该方法的作用:快照截图
	 * 
	 * @date 2014年3月25日
	 * @param v
	 * @return
	 */
	public static Bitmap getViewBitmap(View v)
	{

		v.clearFocus();
		v.setPressed(false);

		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0)
		{
			v.destroyDrawingCache();
		}
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null)
		{
			return null;
		}
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		// Restore the view
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		return bitmap;
	}

	/**
	 * 该方法的作用: 解析图片资源，最大缩放比例为32
	 * 
	 * @date 2013-10-16
	 * @param context
	 * @param imageFile
	 * @return
	 */
	public static Bitmap decodeBitmap(Context context, File imageFile)
	{
		return decodeBitmap(context, imageFile, 32);
	}

	/**
	 * 该方法的作用: 解析图片资源
	 * 
	 * @date 2013-10-16
	 * @param context
	 * @param imageFile
	 * @param maxInSampleSize
	 *            最大缩放比例
	 * @return
	 */
	public static Bitmap decodeBitmap(Context context, File imageFile, int maxInSampleSize)
	{
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inDither = true;
		options.inTempStorage = new byte[12 * 1024];
		options.inSampleSize = 1;

		// 设置最大缩放比例
		if (maxInSampleSize < 1)
		{
			maxInSampleSize = 64;
		}

		while (null == bitmap)
		{
			bitmap = getScaledBitmap(context, imageFile, options);

			options.inSampleSize = options.inSampleSize * 2;

			if (options.inSampleSize > maxInSampleSize * 2)
			{
				return null;
			}
		}
		return bitmap;
	}

	/**
	 * 该方法的作用: 获取缩放的图片
	 * 
	 * @date 2013-10-16
	 * @param context
	 * @param imageFile
	 * @param options
	 *            图片解析配置信息
	 * @return
	 */
	public static Bitmap getScaledBitmap(Context context, File imageFile, Options options)
	{
		Bitmap bitmap = null;
		// 获取资源图片
		FileInputStream fileInputStream = null;
		InputStream inputStream = null;
		try
		{
			fileInputStream = new FileInputStream(imageFile);
			inputStream = new BufferedInputStream(fileInputStream);
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		}
		catch (OutOfMemoryError err)
		{
			LogTools.e("inSampleSize" + "-----" + options.inSampleSize + "-------" + err);
			return null;
		}
		catch (FileNotFoundException e)
		{
			LogTools.e(e);
		}
		finally
		{
			closeInputStream(inputStream);
			closeInputStream(fileInputStream);
		}

		return bitmap;
	}

	/**
	 * 该方法的作用:关闭流
	 * 
	 * @date 2014年3月24日
	 * @param stream
	 */
	private static void closeInputStream(InputStream stream)
	{
		if (null != stream)
		{
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				LogTools.e(e);
			}
		}
	}

	/**
	 * 该方法的作用:九宫格图片合成
	 * 
	 * @date 2014年3月21日
	 * @param bitmaps
	 *            需要混合的图片集
	 * @param width
	 *            希望得到的Bitmap的宽度
	 * @param height
	 *            希望得到的Bitmap的高度
	 * @param backgroundColor
	 *            希望得到的Bitmap的背景色
	 * @return
	 */
	public static Bitmap sudokuImage(List<Bitmap> bitmaps, int width, int height,
			int backgroundColor)
	{
		// 创建一个Bitmap
		Bitmap sourceBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// 将Bitmap放入canvas中
		Canvas canvas = new Canvas(sourceBitmap);
		// 判断用户是否设置了背景颜色
		if (backgroundColor == 0)
		{
			canvas.drawColor(Color.GRAY);
		}
		else
		{
			canvas.drawColor(backgroundColor);
		}
		int row = 0;
		// 判断图片合成的规则，小于5个采用2*2的规则，大于5的采用3*3的规则
		if (bitmaps.size() < 5)
		{
			row = 2;
		}
		else
		{
			row = 3;
		}
		// 计算每个小图片的宽度
		int iconWidth = (width - IMAGE_OFFSET * 2 - (row - 1) * IMAGE_SPACE) / row;
		// 计算每个小图片的高度
		int iconHeight = (height - IMAGE_OFFSET * 2 - (row - 1) * IMAGE_SPACE) / row;
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bitmap = null;
		int left = 0;
		int top = 0;
		int line = 0;
		// 将图片绘制在canvas上
		for (int i = 0; i < bitmaps.size(); i++)
		{
			// 判断当前图片应该属于第几行
			if ((row == 2 && i < 2) || (row == 3 && i < 3))
			{
				line = 1;
			}
			else if ((row == 2) || (row == 3 && i < 6))
			{
				line = 2;
			}
			else
			{
				line = 3;
			}
			// 缩小图片
			bitmap = scaleBitmap(bitmaps.get(i), iconWidth, iconHeight);
			// 计算当前图片绘制的距离左边的位置
			left = IMAGE_OFFSET + (i % row) * IMAGE_SPACE + ((i % row) * iconWidth);
			// 计算当前图片绘制的距离上面的位置
			top = IMAGE_OFFSET + iconHeight * (line - 1) + IMAGE_SPACE * (line - 1);
			canvas.drawBitmap(bitmap, left, top, paint);
			bitmap.recycle();
			bitmap = null;
		}
		return sourceBitmap;
	}
}
