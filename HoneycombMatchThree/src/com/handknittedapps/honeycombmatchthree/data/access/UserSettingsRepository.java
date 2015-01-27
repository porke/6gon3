package com.handknittedapps.honeycombmatchthree.data.access;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class UserSettingsRepository extends GenericKeyValuePairAccess
{
	private static final String SettingsFile = "UserSettings.xml";

	public UserSettingsRepository()
	{
		super(false, HoneycombMatchThree.name, UserSettingsRepository.SettingsFile);

		if (!load())
		{
			generateDefaults();
		}
	}

	@Override
	protected void generateDefaults()
	{
		setSetting("swarmLogin", "Prompt");
	}
}
