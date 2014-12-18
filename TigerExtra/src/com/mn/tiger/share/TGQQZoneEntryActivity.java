package com.mn.tiger.share;

/**
 * QQZone分享启动/回调Activity
 */
public class TGQQZoneEntryActivity extends TGQQEntryActivity
{
	@Override
	public TGQQSharePlugin getPlugin()
	{
		return (TGQQZoneSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_QQ_ZONE);
	}
}
