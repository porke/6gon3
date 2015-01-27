package com.handknittedapps.honeycombmatchthree.logic.moves;

public enum MoveType
{
	DefaultSwap(0, "DefaultSwap", 0, 0),
	RemoteSwap(1, "RemoteSwap", 1, 0),
	RotateSwap(2, "RotateSwap", 1, 1),
	ColourChange(3, "ColourChange", 1, 2),
	BombChange(4, "BombChange", 2, 0),
	IncomeBoost(5, "IncomeBoost", 2, 1),
	DeleteLine(6, "DeleteLine", 2, 2),
	ClearColour(7, "ClearColour", 3, 0),
	Fear(8, "Fear", 3, 1),
	Conversion(9, "Conversion", 3, 2);

	public static final int NumEnergyTypes = 3;

	public static MoveType fromString(String str)
	{
		MoveType[] moves = MoveType.values();
		for (MoveType m : moves)
		{
			if (m.name.equals(str))
			{
				return m;
			}
		}
		return null;
	}

	private MoveType(int val, String name, int tier, int type)
	{
		this.value = val;
		this.name = name;
		this.tier = tier;
		this.type = type;
		switch(tier)
		{
		case 1:
			this.cost = 0.2f;
			break;
		case 2:
			this.cost = 0.6f;
			break;
		case 3:
			this.cost = 0.9f;
			break;
		default:
			this.cost = 0.0f;
		}
	}

	public static MoveType get(int colour, int tier)
	{
		for (MoveType mt : MoveType.values())
		{
			if (mt.tier == tier && mt.type == colour)
			{
				return mt;
			}
		}
		return null;
	}

	public String getColour()
	{
		switch(this.type)
		{
		case 0:
			return "Red";
		case 1:
			return "Green";
		case 2:
			return "Blue";
		default:
			return "";
		}
	}

	public final int value;
	public final String name;
	public final int tier;
	public final int type;		//0 - R, 1 - G, 2 - B
	public final float cost;
}
