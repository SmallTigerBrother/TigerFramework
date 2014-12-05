package com.mn.tiger.share;


public class TGQQZoneEntryActivity extends TGQQEntryActivity
{
	@Override
	public TGQQSharePlugin getPlugin()
	{
		return (TGQQZoneSharePlugin) TGSharePluginManager.getInstance().getPlugin(
				TGSharePluginManager.TAG_QQ_ZONE);
	}
}
