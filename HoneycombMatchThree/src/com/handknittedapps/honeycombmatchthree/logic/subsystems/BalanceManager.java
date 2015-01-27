package com.handknittedapps.honeycombmatchthree.logic.subsystems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;


public class BalanceManager extends GameSubsystem
{
	public BalanceManager()
	{
		this(null);
	}

	public BalanceManager(GameSubsystem next)
	{
		super(next);
		Gdx.files.external(HoneycombMatchThree.name + "/stats.txt").delete();
	}

	private void postData()
	{
		int durationInSeconds = (int) ((System.nanoTime() - this.gameStartTime) / 1000000000);

		FileHandle file = Gdx.files.external(HoneycombMatchThree.name + "/stats.txt");

		Date date = new Date();
		SimpleDateFormat dateFmt = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		file.writeString("Game date: " + dateFmt.format(date) + "\n", true);
		file.writeString("score=" + String.valueOf(this.score) + "\n", true);
		file.writeString("duration=" + String.valueOf(durationInSeconds) + "\n", true);
		file.writeString("play_mode=" + this.gameMode + "\n", true);
		file.writeString("moves=" + String.valueOf(this.moves) + "\n", true);
		file.writeString("map_mode=" + this.mapMode + "\n", true);
		file.writeString("red_energy=" + String.valueOf(this.energyGained[0]) + "\n", true);
		file.writeString("blue_energy=" + String.valueOf(this.energyGained[2]) + "\n", true);
		file.writeString("green_energy=" + String.valueOf(this.energyGained[1]) + "\n", true);
		file.writeString("win_lose_exit=" + String.valueOf(this.winLoseQuit) + "\n", true);
		file.writeString("stage=" + (this.mission.length() > 0 ? this.mission : " ") + "\n", true);
		file.writeString("parallel_seq=" + String.valueOf(this.numParallelCombos) + "\n", true);
		file.writeString("combo_seq=" + String.valueOf(this.numSequentialCombos) + "\n\n", true);
	}

	@Override
	public void onGameStart(PlayModeType pmt, GameModeType gmt, Object data)
	{
		this.gameStartTime = System.nanoTime();
		this.gameMode = pmt.name();
		this.mapMode = gmt.toString();
		this.mission = String.valueOf(data);

		if (this.next != null)
		{
			this.next.onGameStart(pmt, gmt, data);
		}
	}

	@Override
	public void onEndgame(GameMode gm)
	{
		// Fill the rest of the data and post it
		if (gm.hasEnded())
		{
			this.winLoseQuit = (gm.hasWon()) ? 1 : 0;
		}
		else
		{
			this.winLoseQuit = -1;
		}

		this.postData();

		if (this.next != null)
		{
			this.next.onEndgame(gm);
		}
	}

	@Override
	public void onBonusUsed(Player player, MoveType mt, Block targetBlock)
	{
		if (this.bonusesUsed.containsKey(mt.name))
		{
			this.bonusesUsed.put(mt, this.bonusesUsed.get(mt) + 1);
		}
		else
		{
			this.bonusesUsed.put(mt, 0);
		}

		// Propagate the request
		if (this.next != null)
		{
			this.next.onBonusUsed(player, mt, targetBlock);
		}
	}

	@Override
	public void onMove(GameMode gm)
	{
		// Propagate the request
		if (this.next != null)
		{
			this.next.onMove(gm);
		}
	}

	@Override
	public void onScore(GameMode gm, MoveDestructionSequences mts)
	{
		this.score += mts.getScore();

		float[] retEnergy = mts.getEnergies(gm.getEnergyMultiplier());
		for (int i = 0; i < MoveType.NumEnergyTypes; ++i)
		{
			this.energyGained[i] += retEnergy[i];
		}

		if (mts.getCurrentCombo() > 0)
		{
			++this.numSequentialCombos;
		}
		else
		{
			++this.moves;
		}

		// 0 parallel occurs when the player uses the
		// ClearLine or ClearColour bonus
		if (mts.numParralel() > 0)
		{
			// 1 parallel is a normal match 3 (4 or 5)
			this.numParallelCombos += mts.numParralel() - 1;
		}

		// Propagate the request
		if (this.next != null)
		{
			this.next.onScore(gm, mts);
		}
	}


	private long gameStartTime;
	private int score;
	private int[] energyGained = new int[MoveType.NumEnergyTypes];
	private String gameMode;
	private String mapMode;
	private int winLoseQuit;			// 1 - win, 0 - lose, -1 quit
	private String mission;				// Stage for progressive or NULL for casual
	private int numParallelCombos;
	private int numSequentialCombos;
	private int moves;
	private HashMap<MoveType, Integer> bonusesUsed = new HashMap<MoveType, Integer>();
}
