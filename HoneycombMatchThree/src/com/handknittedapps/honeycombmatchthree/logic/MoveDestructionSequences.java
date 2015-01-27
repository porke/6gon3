package com.handknittedapps.honeycombmatchthree.logic;

import java.util.ArrayList;

public class MoveDestructionSequences
{
	public static final boolean DebugOutput = false;
	public static float ScorePerAdditionalBlocksFactor;
	public static float ScorePerBlock;

	@SuppressWarnings("unused")
	private MoveDestructionSequences()
	{
		// Intentionally empty constructor
	}

	public MoveDestructionSequences(DestructionType dt)
	{
		this.type = dt;
	}

	public void add(DestructionSequence seq)
	{
		this.blockSequences.add(seq);
	}

	public void clear()
	{
		for (DestructionSequence seq : this.blockSequences)
		{
			seq.clear();
		}

		this.blockSequences.clear();
	}

	public int numParralel()
	{
		int numParallel = 0;
		for (DestructionSequence seq : this.blockSequences)
		{
			if (seq.getType() == DestructionType.Match)
			{
				++numParallel;
			}
		}

		return numParallel;
	}

	public int getScore()
	{
		if (this.type == DestructionType.Match)
		{
			int ret = 0;

			if (MoveDestructionSequences.DebugOutput)
			{
				System.out.print("Sequence: " + this.blockSequences.size() + ", sizes: ");
			}

			for (DestructionSequence seq : this.blockSequences)
			{
				int seqScore = 0;
				if (seq.getType() == DestructionType.Match)
				{
					seqScore = (int) (MoveDestructionSequences.ScorePerBlock * seq.size());
				}
				else if (seq.getType() == DestructionType.Bomb)
				{
					seqScore = 1;
				}

				if (seq.isGhost())
				{
					seqScore *= 2;
				}

				ret += seqScore * Math.pow(MoveDestructionSequences.ScorePerAdditionalBlocksFactor, (seqScore - Block.Sequence));

				if (MoveDestructionSequences.DebugOutput)
				{
					System.out.print(seqScore + " ");

					for (int b = 0; b < seq.size(); ++b)
					{
						System.out.print(seq.getBlock(b).getColour() + " ");
					}
				}
			}

			ret *= this.blockSequences.size();

			if (MoveDestructionSequences.DebugOutput)
			{
				System.out.println(", combo: " + (this.simCombo + 1) + ", Earned " + (ret * (this.simCombo + 1)) + " points!");
			}

			return ret * (this.simCombo + 1);
		}
		else
		{
			return (int) (this.blockSequences.get(0).size() * MoveDestructionSequences.ScorePerBlock);
		}
	}

	public float[] getEnergies(float energyMult)
	{
		float[] energies = new float[Player.EnergyTypes];

		//and the energies
		for (int i = 0; i < this.numSequences(); ++i)
		{
			BlockColour type = this.getDestructionSequence(i).getColour();
			final float seqSize = this.getDestructionSequence(i).size();
			switch(type)
			{
				case Blue:
					energies[type.value] += energyMult * Player.EnergyPerBlock * seqSize / Player.MaxEnergy;
					break;
				case Red:
					energies[type.value] += energyMult * Player.EnergyPerBlock * seqSize / Player.MaxEnergy;
					break;
				case Green:
					energies[type.value] += energyMult * Player.EnergyPerBlock * seqSize / Player.MaxEnergy;
					break;
				case Yellow:
					energies[BlockColour.Green.value] += energyMult * Player.EnergyPerBlock / 2.0f * seqSize / Player.MaxEnergy;
					energies[BlockColour.Red.value] += energyMult * Player.EnergyPerBlock / 2.0f * seqSize / Player.MaxEnergy;
					break;
				case Magenta:
					energies[BlockColour.Red.value] += energyMult * Player.EnergyPerBlock / 2.0f * seqSize / Player.MaxEnergy;
					energies[BlockColour.Blue.value] += energyMult * Player.EnergyPerBlock / 2.0f * seqSize / Player.MaxEnergy;
					break;
				case Cyan:
					energies[BlockColour.Blue.value] += energyMult * Player.EnergyPerBlock / 2.0f * seqSize / Player.MaxEnergy;
					energies[BlockColour.Green.value] += energyMult * Player.EnergyPerBlock / 2.0f * seqSize / Player.MaxEnergy;
					break;
				case Null:
				case Orange:
					break;
			}
		}

		return energies;
	}
	// Currently removes incorrect sequences
	// - sequences consisting mixed non triplets of ghosts and normal blocks
	public void validateSequences()
	{
		for (int i = 0; i < this.blockSequences.size(); ++i)
		{
			if (!this.blockSequences.get(i).isValid())
			{
				if (MoveDestructionSequences.DebugOutput)
				{
					DestructionSequence ds = this.blockSequences.get(i);
					System.out.print("Sequence removed: ");
					for (int g = 0; g < ds.size(); ++g)
					{
						System.out.print(ds.getBlock(g).getType() + "( "
								+ ds.getBlock(g).getPosition().toString() + ") ");
					}
					System.out.println();
				}
				this.blockSequences.remove(i);
				--i;
			}
		}
	}
	public void setSimCombo(int combo) { this.simCombo = combo; }
	public int getCurrentCombo() { return this.simCombo; }
	public boolean isGhost(int seqNum)
	{
		return this.blockSequences.get(seqNum).isGhost();
	}
	public DestructionSequence getDestructionSequence(int index) { return this.blockSequences.get(index); }
	public int numSequences() { return this.blockSequences.size(); }

	private ArrayList<DestructionSequence> blockSequences = new ArrayList<DestructionSequence>();
	private int simCombo;
	private DestructionType type;
}
