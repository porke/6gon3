package com.handknittedapps.honeycombmatchthree.graphics;

public enum ThemeType
{
	Neutral("Neutral", "White"),
	Industrial("Industrial", "Red"),
	Nature("Nature", "Green"),
	Water("Water", "Blue"),
	Honey("Honey", "Yellow");

	public static ThemeType fromString(String str)
	{
		ThemeType[] themes = ThemeType.values();
		for (ThemeType t : themes)
		{
			if (t.name.equals(str))
			{
				return t;
			}
		}
		return null;
	}

	private ThemeType(String name, String colour)
	{
		this.name = name;
		this.colour = colour;
	}

	public final String name;
	public final String colour;
}
