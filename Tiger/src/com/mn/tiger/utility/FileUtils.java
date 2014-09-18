package com.mn.tiger.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import com.mn.tiger.core.encode.TGEncode;

/**
 * 
 * 该类作用及功能说明 文件和文件夹相关的操作
 * 
 * @author zWX200279
 * @date 2014-2-11
 */
public class FileUtils
{
	/**
	 * 日志标签
	 */
	protected static final String LOG_TAG = FileUtils.class.getSimpleName();

	/**
	 * 该方法的作用:获取SD卡路径
	 * 
	 * @author yWX158243
	 * @date 2014年1月17日
	 * @return
	 */
	public static String getSDCardPath()
	{
		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	}

	/**
	 * 该方法的作用: 判断SD卡是否可用
	 * 
	 * @author l00220455
	 * @date 2013-10-28
	 * @return
	 */
	public static boolean isSDCardAvailable()
	{
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			return true;
		}

		return false;
	}

	/**
	 * 该方法的作用: 获取SD卡剩余空间,单位byte，若SD卡不可用，返回0
	 * 
	 * @author l00220455
	 * @date 2013-10-28
	 * @return
	 */
	public static long getSDFreeSize()
	{
		if (isSDCardAvailable())
		{
			StatFs statFs = new StatFs(getSDCardPath());

			long blockSize = statFs.getBlockSize();

			long freeBlocks = statFs.getAvailableBlocks();
			return freeBlocks * blockSize;
		}

		return 0;
	}

	/**
	 * 该方法的作用: 获取SD卡的总容量，单位byte，若SD卡不可用，返回0
	 * 
	 * @author l00220455
	 * @date 2013-10-28
	 * @return
	 */
	public static long getSDAllSize()
	{
		if (isSDCardAvailable())
		{
			StatFs stat = new StatFs(getSDCardPath());
			// 获取空闲的数据块的数量
			long availableBlocks = (long) stat.getAvailableBlocks() - 4;
			// 获取单个数据块的大小（byte）
			long freeBlocks = stat.getAvailableBlocks();
			return freeBlocks * availableBlocks;
		}
		return 0;
	}

	/**
	 * 该方法的作用:获取指定路径所在空间的剩余可用容量字节数
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param filePath
	 * @return 容量字节 SDCard可用空间，内部存储可用空间
	 */
	public static long getFreeBytes(String filePath)
	{
		// 如果是sd卡的下的路径，则获取sd卡可用容量
		if (filePath.startsWith(getSDCardPath()))
		{
			filePath = getSDCardPath();
		}
		else
		{// 如果是内部存储的路径，则获取内存存储的可用容量
			filePath = Environment.getDataDirectory().getAbsolutePath();
		}
		StatFs stat = new StatFs(filePath);
		long availableBlocks = (long) stat.getAvailableBlocks() - 4;
		return stat.getBlockSize() * availableBlocks;
	}

	/**
	 * 该方法的作用:拷贝文件，通过返回值判断是否拷贝成功
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param sourcePath
	 *            源文件路径
	 * @param targetPath
	 *            目标文件路径
	 * @return
	 */
	public static boolean copyFile(String sourcePath, String targetPath)
	{
		boolean isOK = false;
		if (!StringUtils.isEmptyOrNull(sourcePath) && !StringUtils.isEmptyOrNull(targetPath))
		{
			File sourcefile = new File(sourcePath);
			File targetFile = new File(targetPath);
			if (!sourcefile.exists())
			{
				return false;
			}
			if (sourcefile.isDirectory())
			{
				isOK = copyDirectory(sourcefile, targetFile);
			}
			else if (sourcefile.isFile())
			{
				if (!targetFile.exists())
				{
					createFile(targetPath);
				}
				FileOutputStream outputStream = null;
				FileInputStream inputStream = null;
				try
				{
					inputStream = new FileInputStream(sourcefile);
					outputStream = new FileOutputStream(targetFile);
					byte[] bs = new byte[1024];
					int len;
					while ((len = inputStream.read(bs)) != -1)
					{
						outputStream.write(bs, 0, len);
					}
					isOK = true;
				}
				catch (Exception e)
				{
					LogTools.e(LOG_TAG,"", e);
					isOK = false;
				}
				finally
				{
					if (inputStream != null)
					{
						try
						{
							inputStream.close();
						}
						catch (IOException e)
						{
							LogTools.e(LOG_TAG,"", e);
						}
					}
					if (outputStream != null)
					{
						try
						{
							outputStream.close();
						}
						catch (IOException e)
						{
							LogTools.e(LOG_TAG,"", e);
						}
					}
				}
			}

			return isOK;
		}
		return false;
	}

	/**
	 * 该方法的作用:删除文件
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path)
	{
		if (!StringUtils.isEmptyOrNull(path))
		{
			File file = new File(path);
			if (!file.exists())
			{
				return false;
			}
			
			if(file.isFile())
			{
				try
				{
					file.delete();
				}
				catch (Exception e)
				{
					LogTools.e(LOG_TAG,"", e);
					return false;
				}
			}
			else if(file.isDirectory())
			{
				FileUtils.deleteDirectory(path);
			}
			
			return true;
		}
		return false;
	}

	/**
	 * 剪切文件，将文件拷贝到目标目录，再将源文件删除
	 * 
	 * @param sourcePath
	 * @param targetPath
	 */
	public static boolean cutFile(String sourcePath, String targetPath)
	{
		boolean isSuccessful = copyFile(sourcePath, targetPath);
		if (isSuccessful)
		{
			// 拷贝成功则删除源文件
			return deleteFile(sourcePath);
		}
		return false;
	}

	/**
	 * 该方法的作用: 拷贝目录
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param sourceFile
	 * @param targetFile
	 * @return
	 */
	public static boolean copyDirectory(File sourceFile, File targetFile)
	{
		if (sourceFile == null || targetFile == null)
		{
			return false;
		}
		if (!sourceFile.exists())
		{
			return false;
		}
		if (!targetFile.exists())
		{
			targetFile.mkdirs();
		}
		// 获取目录下所有文件和文件夹的列表
		File[] files = sourceFile.listFiles();
		if (files == null || files.length < 1)
		{
			return false;
		}
		File file = null;
		StringBuffer buffer = new StringBuffer();
		boolean isSuccessful = false;
		// 遍历目录下的所有文件文件夹，分别处理
		for (int i = 0; i < files.length; i++)
		{
			file = files[i];
			buffer.setLength(0);
			buffer.append(targetFile.getAbsolutePath()).append(File.separator)
					.append(file.getName());
			if (file.isFile())
			{
				// 文件直接调用拷贝文件方法
				isSuccessful = copyFile(file.getAbsolutePath(), buffer.toString());
				if (!isSuccessful)
				{
					return false;
				}
			}
			else if (file.isDirectory())
			{
				// 目录再次调用拷贝目录方法
				copyDirectory(file, new File(buffer.toString()));
			}

		}
		return true;
	}

	/**
	 * 该方法的作用:剪切目录，先将目录拷贝完后再删除源目录
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param sourceDirectory
	 * @param targetDirectory
	 * @return
	 */
	public static boolean cutDirectory(String sourceDirectory, String targetDirectory)
	{
		File sourceFile = new File(sourceDirectory);
		File targetFile = new File(targetDirectory);
		boolean isCopySuccessful = copyDirectory(sourceFile, targetFile);
		if (isCopySuccessful)
		{
			return deleteDirectory(sourceDirectory);
		}
		return false;
	}

	/**
	 * 
	 * 该方法的作用:删除目录
	 * 
	 * @author zWX200279
	 * @date 2014-2-12
	 * @param targetDirectory
	 * @return
	 */
	public static boolean deleteDirectory(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			return false;
		}
		File[] files = file.listFiles();
		boolean isSuccessful = false;
		if (files.length == 0)
		{
			file.delete();
			return true;
		}
		// 对所有列表中的路径进行判断是文件还是文件夹
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].isDirectory())
			{
				isSuccessful = deleteDirectory(files[i].getAbsolutePath());
			}
			else if (files[i].isFile())
			{
				isSuccessful = deleteFile(files[i].getAbsolutePath());
			}

			if (!isSuccessful)
			{
				// 如果有删除失败的情况直接跳出循环
				break;
			}
		}
		if (isSuccessful)
		{
			file.delete();
		}
		return isSuccessful;
	}

	/**
	 * 
	 * 该方法的作用:将流写入指定文件
	 * 
	 * @author zWX200279
	 * @date 2014-2-12
	 * @param inputStream
	 * @param path
	 * @return
	 */
	public static boolean streamWriteFile(InputStream inputStream, String path)
	{
		File file = new File(path);
		boolean isSuccessful = true;
		FileOutputStream fileOutputStream = null;
		try
		{
			if (!file.exists())
			{
				File file2 = file.getParentFile();
				file2.mkdirs();
				file.createNewFile();
			}
			fileOutputStream = new FileOutputStream(file);
			byte[] bs = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(bs)) != -1)
			{
				fileOutputStream.write(bs, 0, length);
			}
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG,"", e);
			isSuccessful = false;
		}
		finally
		{
			try
			{
				if (fileOutputStream != null)
				{
					fileOutputStream.close();
				}
			}
			catch (IOException e)
			{
				LogTools.e(LOG_TAG,"", e);
			}
		}
		return isSuccessful;
	}

	/**
	 * 该方法的作用:创建目录
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @param path
	 */
	public static void createDir(String path)
	{
		File file = new File(path);
		if (!file.exists())
		{
			file.mkdirs();
		}
	}

	/**
	 * 该方法的作用:修改文件读写权限
	 * 
	 * @author yWX158243
	 * @date 2013-3-7
	 * @param fileAbsPath
	 * @param mode
	 */
	public static void chmodFile(String fileAbsPath, String mode)
	{
		String cmd = "chmod " + mode + " " + fileAbsPath;
		try
		{
			Runtime.getRuntime().exec(cmd);
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG,"", e);
		}
	}

	/**
	 * 该方法的作用:创建文件，并写入指定内容
	 * 
	 * @author yWX158243
	 * @date 2013-3-7
	 * @param path
	 * @param content
	 */
	public static void createFileThroughContent(String path, String content)
	{
		File myFile = new File(path);
		if (!myFile.exists())
		{
			try
			{
				myFile.createNewFile();
			}
			catch (IOException e)
			{
				LogTools.e(LOG_TAG,"", e);
			}
		}

		if (null != content)
		{
			FileWriter fw = null;
			try
			{
				fw = new FileWriter(myFile);
				fw.write(content);
				fw.flush();
			}
			catch (IOException e)
			{
				LogTools.e(LOG_TAG, e);
			}
			finally
			{
				if (fw != null)
				{
					try
					{
						fw.close();
					}
					catch (IOException e)
					{
						LogTools.e(LOG_TAG,"", e);
					}
				}
			}
		}
	}

	/**
	 * 
	 * 该方法的作用:将object对象写入outFile文件
	 * 
	 * @author zWX200279
	 * @date 2014-2-12
	 * @param outFile
	 * @param object
	 * @param context
	 */
	public static void writeObject(String outFile, Object object, Context context)
	{
		ObjectOutputStream out = null;
		FileOutputStream outStream = null;
		try
		{
			File dir = context.getDir("cache", Context.MODE_PRIVATE);
			outStream = new FileOutputStream(new File(dir, outFile));
			out = new ObjectOutputStream(new BufferedOutputStream(outStream));
			out.writeObject(object);
			out.flush();
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG,"", e);
		}
		finally
		{
			if (outStream != null)
			{
				try
				{
					outStream.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG,"", e);
				}
			}
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG,"", e);
				}
			}
		}
	}

	/**
	 * 
	 * 该方法的作用:从outFile文件读取对象
	 * 
	 * @author zWX200279
	 * @date 2014-2-12
	 * @param filePath
	 * @param context
	 * @return
	 */
	public static Object readObject(String filePath, Context context)
	{
		Object object = null;
		ObjectInputStream in = null;
		FileInputStream inputStream = null;
		try
		{
			File dir = context.getDir("cache", Context.MODE_PRIVATE);
			File f = new File(dir, filePath);
			if (f == null || !f.exists())
			{
				return null;
			}
			inputStream = new FileInputStream(new File(dir, filePath));
			in = new ObjectInputStream(new BufferedInputStream(inputStream));
			object = in.readObject();
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG,"", e);
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG,"", e);
				}
			}
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG, "",e);
				}
			}

		}
		return object;
	}

	/**
	 * 该方法的作用:读取指定路径下的文件内容
	 * 
	 * @author yWX158243
	 * @date 2013-3-7
	 * @param path
	 * @return 文件内容
	 */
	public static String readFile(String path)
	{
		BufferedReader br = null;
		try
		{
			File myFile = new File(path);
			br = new BufferedReader(new FileReader(myFile));
			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null)
			{
				sb.append(line);
				line = br.readLine();
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG,"", e);
		}
		finally
		{
			if (br != null)
			{
				try
				{
					br.close();
				}
				catch (IOException e)
				{
					LogTools.e(LOG_TAG,"", e);
				}
			}
		}
		return null;
	}

	/**
	 * 该方法的作用:创建文件，并修改读写权限
	 * 
	 * @author yWX158243
	 * @date 2013-3-7
	 * @param filePath
	 * @param mode
	 * @return
	 */
	public static File createFile(String filePath, String mode)
	{
		File desFile = null;
		try
		{
			String desDir = filePath.substring(0, filePath.lastIndexOf(File.separator));
			File dir = new File(desDir);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			chmodFile(dir.getAbsolutePath(), mode);
			desFile = new File(filePath);
			if (!desFile.exists())
			{
				desFile.createNewFile();
			}
			chmodFile(desFile.getAbsolutePath(), mode);
		}
		catch (Exception e)
		{
			LogTools.e(LOG_TAG,"", e);
		}
		return desFile;
	}

	/**
	 * 该方法的作用:根据指定路径，创建父目录及文件
	 * 
	 * @author yWX158243
	 * @date 2013-3-6
	 * @param filePath
	 * @return File 如果创建失败的话，返回null
	 */
	public static File createFile(String filePath)
	{
		return createFile(filePath, "755");
	}
	
	/**
	 * 该方法的作用:获取系统存储路径
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @return
	 */
	public static String getRootDirectoryPath()
	{
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * 该方法的作用:获取外部存储路径
	 * 
	 * @author zWX200279
	 * @date 2014-1-23
	 * @return
	 */
	public static String getExternalStorageDirectoryPath()
	{
		return Environment.getExternalStorageDirectory().getPath();
	}
	
	/**
	 * 该方法的作用: 将Url转换成文件名
	 * @author l00220455
	 * @date 2014年1月7日
	 * @param url
	 * @return
	 */
	public static String parseUrl2FileName(String url)
	{
		try
		{
			return TGEncode.getMD5Encrypt(url) + url.substring(url.lastIndexOf("."));
		}
		catch (Exception e)
		{
			LogTools.e(e);
		}
		return "";
	}

	/**
	 * 
	 * 该方法的作用: 根据文件路径获取文件大小
	 * @author pWX197040
	 * @date 2014年7月16日
	 * @param path
	 * @return
	 */
	public static long getFileSize(String path)
	{
		File file = new File(path);
		if (file.exists())
		{
			return file.length();
		}
		
		return -1;
	}
	
	/**
	 * 
	 * 该方法的作用: 根据路径获取文件名
	 * @author pWX197040
	 * @date 2014年7月16日
	 * @param path
	 * @return
	 */
	public static String getFileName(String path)
	{
		File file = new File(path);
		if (file.exists())
		{
			return file.getName();
		}
		
		return "";
	}
	
	/**
	 * 
	 * 该方法的作用: 根据路径获取文件
	 * @author pWX197040
	 * @date 2014年7月16日
	 * @param path
	 * @return
	 */
	public static File getFile(String path)
	{
		if(TextUtils.isEmpty(path))
		{
			return null;
		}
		
		File file = new File(path);
		if (file.exists())
		{
			return file;
		}
		
		return null;
	}
}
