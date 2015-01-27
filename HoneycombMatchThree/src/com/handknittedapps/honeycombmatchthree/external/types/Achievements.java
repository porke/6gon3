package com.handknittedapps.honeycombmatchthree.external.types;

public enum Achievements
{
	CampaignCompleted(16295),
	FiveMissionsCompleted(16297),
	TenMissionsCompleted(16299),
	FifteenMissionCompleted(16301),
	AllRedBonuses(16303),
	AllGreenBonuses(16305),
	AllBlueBonuses(16307),
	AllGameModes(16309),
	ThreeSequences(16389),
	FiveSequencees(16313),
	FiveBlocksInSequence(16315),
	FifthStageInProgressive(16387);

	public final int Id;

	private Achievements(int id)
	{
		this.Id = id;
	}
}
