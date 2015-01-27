package com.handknittedapps.honeycombmatchthree.graphics;

public enum SpecialCharacters
{
	STAR(200),
	INFINITY(201),
	CAMPAIGN(202),
	SCORE(203),
	EXIT(204),
	CASUAL(205),
	SURVIVAL(206),
	HARDCORE(207),
	TIME_ATTACK(208),
	CLASSIC(209);

	@Override
	public String toString()
	{
		return String.valueOf(this.code);
	}

	private SpecialCharacters(int code)
	{
		this.code = (char) code;
	}

	public char code;
}
