package com.handknittedapps.honeycombmatchthree.data.access;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;

public class FxSettingsRepository extends GenericKeyValuePairAccess
{
	private static FxSettingsRepository instance = new FxSettingsRepository();

	public static FxSettingsRepository getInstance()
	{
		return FxSettingsRepository.instance;
	}

	private FxSettingsRepository()
	{
		super(true, HoneycombMatchThree.name, "data/fx_constants.xml");

		if (!this.load())
		{
			this.generateDefaults();
		}
	}

	@Override
	protected void generateDefaults()
	{
		this.keyValuePairs.put("BlockEventChangeColour.InterpolationFactor", 1.5f);
		this.keyValuePairs.put("BlockEventChangePosition.MvtTime", 0.2f);
		this.keyValuePairs.put("BlockEventChangePosition.InterpolationFactor", 1.7f);
		this.keyValuePairs.put("BlockEventChangeStrength.Duration", 0.25f);
		this.keyValuePairs.put("BlockEventChangeStrength.InterpolationFactor", 1.5f);
		this.keyValuePairs.put("BlockEventDestroyBomb.Duration", 0.5f);
		this.keyValuePairs.put("BlockEventDestroyColor.Duration", 1.0f);
		this.keyValuePairs.put("BlockEventDestroyGhost.Duration", 0.5f);
		this.keyValuePairs.put("BlockEventDestroyGhost.InterpolationFactor", 1.5f);
		this.keyValuePairs.put("BlockEventDestroyLine.Duration", 0.5f);
		this.keyValuePairs.put("BlockEventDestroyStandard.Duration", 0.5f);
		this.keyValuePairs.put("BlockEventDestroyStandard.InterpolationFactor", 1.5f);
		this.keyValuePairs.put("BlockEventLinkGhost.FrameDuration", 0.5f / 8.0f);
	}
}
