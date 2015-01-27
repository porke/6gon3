package com.handknittedapps.honeycombmatchthree.logic;

public class Player
{
	public static final int EnergyTypes = 3;
	public static float EnergyPerBlock = 2.0f;
	public static final float MaxEnergy = 100.0f;

	public Player()
	{
		this.score = 0;
	}

	public Player(int score)
	{
		this.score = score;
	}

	public void onScore(int score, float energyMult, MoveDestructionSequences seq)
	{
		//update the score
		this.score += score;

		float[] retEnergies = seq.getEnergies(energyMult);

		for (int j = 0; j < Player.EnergyTypes; ++j)
		{
			this.energies[j] += retEnergies[j];

			if (this.energies[j] > Player.MaxEnergy)
			{
				this.energies[j] = Player.MaxEnergy;
			}
		}
	}

	public void resetEnergy()
	{
		for (int i = 0; i < Player.EnergyTypes; ++i)
		{
			this.energies[i] = 0.0f;
		}
	}

	public int getScore() { return this.score; }
	public void setScore(int score) { this.score = score; }
	public float getEnergy(int type)
	{
		return this.energies[type];
	}

	public void setEnergy(int type, float newEnergy)
	{
		this.energies[type] = newEnergy;
	}

	public void addEnergy(int type, float percentage)
	{
		this.setEnergy(type, this.energies[type] + percentage);
	}

	private int score;
	private float[] energies = new float[Player.EnergyTypes];
}
