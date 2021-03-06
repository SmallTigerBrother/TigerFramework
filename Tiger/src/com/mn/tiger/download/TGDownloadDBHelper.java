package com.mn.tiger.download;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.mn.tiger.datastorage.TGDBManager;
import com.mn.tiger.datastorage.db.exception.DbException;
import com.mn.tiger.datastorage.db.sqlite.Selector;
import com.mn.tiger.datastorage.db.sqlite.WhereBuilder;
import com.mn.tiger.datastorage.db.upgrade.AbsDbUpgrade;
import com.mn.tiger.log.LogTools;
import com.mn.tiger.utility.Constant;

/**
 * 该类作用及功能说明:数据库操作类
 * 
 * @date 2014年6月5日
 */
public class TGDownloadDBHelper
{
	/**
	 * 日志标识
	 */
	protected final String LOG_TAG = this.getClass().getSimpleName();

	private static TGDownloadDBHelper instance;

	private TGDBManager dbManager;

	private static String database_name = "tiger_download.db";

	private static int database_version = 1;

	/** 文件下载url列名 */
	private final String LOAD_INFO_URL_COLUMN_NAME = "url";

	/** 下载任务请求的参数 */
	private final String LOAD_INFO_PARAM_COLUMN_NAME = "params";
	
	private final String LOAD_INFO_SAVE_PATH = "savePath";

	/** 文件下载类型 */
	private final String LOAD_INFO_COLUMN_TYPE = "downloadType";

	public static TGDownloadDBHelper getInstance(Context context)
	{
		if (instance == null)
		{
			instance = new TGDownloadDBHelper(context);
		}
		return instance;
	}

	private TGDownloadDBHelper(Context context)
	{
		dbManager = getDB(context);
	}

	private TGDBManager getDB(Context context)
	{
		TGDBManager db = TGDBManager.create(context, context.getApplicationInfo().dataDir
				+ File.separator + Constant.STORE_DATABASE_PATH, database_name, database_version,
				new AbsDbUpgrade()
				{
					@Override
					public void upgradeSuccess()
					{
						LogTools.p(TAG, "tiger_download upgrade success");
					}

					@Override
					public void upgradeFail()
					{
						LogTools.e(TAG, "tiger_download upgrade success");
					}

					@Override
					public void upgradeNeedless()
					{
						LogTools.p(TAG, "tiger_download upgrade need less.");
					}

				});
		db.configAllowTransaction(true);
		db.configDebug(false);
		return db;
	}

	/**
	 * 得到下载具体信息
	 * 
	 * @throws DbException
	 */
	public synchronized TGDownloader getDownloader(String urlstr, HashMap<String, String> params, String savePath)
	{
		TGDownloader downloader = null;
		try
		{
			downloader = dbManager.findFirst(
					TGDownloader.class,
					WhereBuilder.b(LOAD_INFO_URL_COLUMN_NAME, "=", urlstr).and(
							LOAD_INFO_PARAM_COLUMN_NAME, "=", TGDownloader.getParamsString(params)).and(
									LOAD_INFO_SAVE_PATH, "=", savePath));
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}

		return downloader;
	}

	/**
	 * 得到所有下载具体信息
	 * 
	 * @throws DbException
	 */
	public synchronized List<TGDownloader> getAllDownloader()
	{
		List<TGDownloader> downloaderList = null;
		try
		{
			downloaderList = dbManager.findAll(TGDownloader.class);
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}

		return downloaderList;
	}

	/**
	 * 根据下载类型得到下载具体信息
	 * 
	 * @throws DbException
	 */
	public synchronized List<TGDownloader> getDownloader(String downloadType)
	{
		List<TGDownloader> downloaderList = null;
		try
		{
			downloaderList = dbManager.findAll(TGDownloader.class,
					WhereBuilder.b(LOAD_INFO_COLUMN_TYPE, "=", downloadType));
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}

		return downloaderList;
	}

	/**
	 * 根据sql得到下载具体信息
	 * 
	 * @throws DbException
	 */
	public synchronized List<TGDownloader> getDownloaderBySql(Selector selector)
	{
		List<TGDownloader> downloaderList = null;
		try
		{
			downloaderList = dbManager.findAll(selector);
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}

		return downloaderList;
	}

	/**
	 * 查看Downloader表中是否有数据
	 * 
	 * @throws DbException
	 */
	public synchronized boolean isHasDownloaders(String urlstr, HashMap<String, String> params)
	{
		long count = 0;
		try
		{
			count = dbManager.count(
					TGDownloader.class,
					WhereBuilder.b(LOAD_INFO_URL_COLUMN_NAME, "=", urlstr).and(
							LOAD_INFO_PARAM_COLUMN_NAME, "=", TGDownloader.getParamsString(params)));
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}
		return count > 0;
	}

	/**
	 * 该方法的作用:保存文件下载信息(有记录则更新记录)
	 * 
	 * @date 2014年1月8日
	 * @param info
	 * @throws DbException
	 */
	public synchronized void saveDownloader(TGDownloader info)
	{
		try
		{
			dbManager.saveOrUpdate(info);
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}
	}

	/**
	 * 该方法的作用:更新文件下载状态
	 * 
	 * @date 2014年1月6日
	 * @param info
	 * @throws DbException
	 */
	public void updateDownloader(TGDownloader info)
	{
		if (info == null)
		{
			return;
		}

		try
		{
			dbManager.update(info);
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}
	}

	/**
	 * 下载完成后删除数据库中的数据
	 * 
	 * @throws DbException
	 */
	public synchronized void deleteDownloader(TGDownloader info)
	{
		try
		{
			dbManager.delete(info);
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}
	}

	public List<TGDownloader> findAllDownloader()
	{
		try
		{
			return dbManager.findAll(TGDownloader.class);
		}
		catch (DbException e)
		{
			LogTools.e(LOG_TAG, e.getMessage(), e);
		}

		return new ArrayList<TGDownloader>();
	}
}