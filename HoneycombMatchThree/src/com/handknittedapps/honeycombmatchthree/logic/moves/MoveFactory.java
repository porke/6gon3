package com.handknittedapps.honeycombmatchthree.logic.moves;

public class MoveFactory
{
	private MoveFactory()
	{
		// Locked class
	}

	public static Move createMove(MoveType type)
	{
		switch (type)
		{
			case DefaultSwap:
				return new SwapMove();
			case RemoteSwap:
				return new RemoteSwap();
			case RotateSwap:
				return new RotateMove();
			case ColourChange:
				return new ColourChangeMove();
			case BombChange:
				return new ConvertToBombMove();
			case DeleteLine:
				return new DeleteLineMove();
			case ClearColour:
				return new ClearColourMove();
			case Fear:
				return new FearMove();
			case Conversion:
				return new ConversionMove();
			case IncomeBoost:
				return new NullMove();
			default:
				throw new IllegalArgumentException("Invalid move type.");
		}
	}
}