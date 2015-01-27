package com.handknittedapps.honeycombmatchthree.logic;

public enum BlockType
{
	Normal,
	Ghost,
	Bomb,
	Immobile,
	Strong;

	public static BlockType fromString(String str)
	{
		BlockType[] vals = BlockType.values();
		for (BlockType v : vals)
		{
			if (v.name().equals(str))
			{
				return v;
			}
		}
		return null;
	}
}
