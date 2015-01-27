package com.handknittedapps.honeycombmatchthree.logic;


import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.handknittedapps.honeycombmatchthree.control.PlayState;
import com.handknittedapps.honeycombmatchthree.data.access.GameSettingsRepository;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.external.AchievementHandler;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.graphics.MapRenderer;
import com.handknittedapps.honeycombmatchthree.graphics.Theme;
import com.handknittedapps.honeycombmatchthree.graphics.ThemeType;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventType;
import com.handknittedapps.honeycombmatchthree.input.MouseInputHandler;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.moves.FearMove;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;
import com.handknittedapps.honeycombmatchthree.logic.subsystems.AchievementSystem;
import com.handknittedapps.honeycombmatchthree.logic.subsystems.HUD;
import com.handknittedapps.honeycombmatchthree.logic.subsystems.StatsSubsystem;
import com.handknittedapps.honeycombmatchthree.utils.Pair;
import com.handknittedapps.honeycombmatchthree.utils.Point;
import com.handknittedapps.honeycombmatchthree.views.play.EndgameView;

public class Gamestate implements Disposable, Observer, MouseInputHandler
{
	private static final int MapSize = 6;

	public Gamestate(PlayState controller, ThemeType themeType)
	{
		// Insert the constant values to the proper classes
		GameSettingsRepository settings = new GameSettingsRepository();
		Player.EnergyPerBlock = settings.getSetting("EnergyPerBlock");
		MoveDestructionSequences.ScorePerBlock = settings.getSetting("ScorePerBlock");
		MoveDestructionSequences.ScorePerAdditionalBlocksFactor = settings.getSetting("ScorePerAdditionalBlocksFactor");
		FearMove.DurationInTurns = settings.getSetting("FearDurationInTurns");

		this.energyBoostFactor = settings.getSetting("EnergyBoostFactor");
		this.energyBoostDuration = settings.getSetting("EnergyBoostDurationInSeconds");

		this.controller = controller;

		// Load the assets
		this.currentTheme = Theme.create(themeType);
	}

	public void startGame(PlayMode playMode, Stage scene, AchievementHandler achievementHandler, boolean isDesktop)
	{
		this.dragModeEnabled = !isDesktop;
		this.availableMoves = playMode.getUnlockedBonuses();
		this.gameMode = playMode.getGameMode();

		// Setup the probabilities
		GameSettingsRepository settings = new GameSettingsRepository();
		for (BlockType type : playMode.getUnlockedBlocks())
		{
			switch (type)
			{
				case Ghost:
					Map.GhostBlockChance = settings.getSetting("GhostBlockSpawnChance");
					break;
				case Strong:
					Map.StrongBlockChance = settings.getSetting("StrongBlockSpawnChance");
					break;
				case Bomb:
					Map.BombBlockChance = settings.getSetting("BombBlockSpawnChance");
					break;
				case Immobile:
					Map.ImmobileBlockChance = settings.getSetting("ImmobileBlockSpawnChance");
					break;
				case Normal:
					break;
			}
		}

		// Create the map and the map renderer
		this.map = new Map(playMode, Gamestate.MapSize);
		this.mapRenderer = new MapRenderer(Gamestate.MapSize, scene, this.currentTheme);

		this.map.createRandomMap();
		this.map.setObservers(this, this.mapRenderer);

		// Setup the chain of responsibility
		this.hud = new HUD(this, this.gameMode, playMode.getUnlockedBonuses(), scene);
		this.achievementSystem = new AchievementSystem(achievementHandler, null);
		this.hud.setNext(this.achievementSystem);
		this.statsManager = new StatsSubsystem(null, this.gameMode.getModeType(), playMode.getPlayModeType());
		this.achievementSystem.setNext(this.statsManager);

		this.hud.onGameStart(playMode.getPlayModeType(), this.gameMode.getModeType(), playMode.getStageId());

		// Perform a first time update
		this.hud.onScore(this.gameMode, new MoveDestructionSequences(DestructionType.Match));
	}

	public void resumeGame()
	{
		this.hasStarted = true;
	}

	private void checkMapLockLoseCondition()
	{
		// Update the map if there are more combos and the animations have finished
		if (!this.mapRenderer.isMapInNeedOfUpdate())
		{
			// Check if the player can use a bonus to change the state of the map
			boolean hasEnoughEnergy = false;
			for (MoveType mt : this.availableMoves)
			{
				hasEnoughEnergy |= (this.gameMode.getPlayer().getEnergy(mt.type) > mt.cost);
			}

			// Check if there are no moves left
			if (!hasEnoughEnergy
				&& !this.map.isValidMovePossible())
			{
				// if not, end the game
				if (!hasEnoughEnergy)
				{
					this.gameMode.mapLockLose();
				}
			}
		}
	}

	public void onFrame()
	{
		this.checkMapLockLoseCondition();

		// On frame update
		this.mapRenderer.onFrame(this.map);

		if (!this.gameMode.hasEnded()
			&& this.hasStarted)
		{
			this.gameMode.onFrame();
			this.hud.onFrame(this.gameMode);
		}
		else if (!this.hud.isWinProgressBarEnqueued())
		{
			// If this score ends the game, play a proper sound
			if (!this.hasGameEndBeenProcessed
				&& this.gameMode.hasEnded())
			{
				this.processEndgame();
			}
		}
	}

	public void processEndgame()
	{
		if (!this.hasGameEndBeenProcessed)
		{
			this.hasGameEndBeenProcessed = true;
			this.controller.setActiveView(EndgameView.ViewId);
			this.hud.onEndgame(this.gameMode);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable map, Object arg)
	{
		// Post-score message
		// A notification about score
		if (arg instanceof MoveDestructionSequences)
		{
			this.onScore((MoveDestructionSequences) arg);
		}
		// This is a post-move, pre-destroy notification
		else if (arg instanceof Pair)
		{
			Pair<ArrayList<Block>, BlockEventType> evtArg = (Pair<ArrayList<Block>, BlockEventType>) arg;

			if (evtArg.getSecond() == BlockEventType.Move)
			{
				this.onMove(evtArg.getFirst());
			}
			else if (evtArg.getSecond() == BlockEventType.Destroy)
			{
				this.mapRenderer.enqueueWaitEvent(evtArg.getFirst());
			}
		}
		// This is a post-move, post-destroy notification about resetting the move
		else
		{
			this.hud.deactivateButtons(this.gameMode.getPlayer());
		}
	}

	private void onScore(MoveDestructionSequences destr)
	{
		if (destr.numSequences() > 0)
		{
			destr.setSimCombo(this.map.getSequenceCombo());
			this.gameMode.onScore(destr);

			this.hud.onScore(this.gameMode, destr);
			this.mapRenderer.setMapUpdate(true);
			this.mapRenderer.onBlockSequenceDestroyed(destr);
		}
	}

	private void onMove(ArrayList<Block> blocksAffected)
	{
		// The map renderer is to enqueue a wait event for all the blocks
		// which are not on the list
		// typeof(arg) = ArrayList<Block>
		this.mapRenderer.enqueueWaitEvent(blocksAffected);

		// If the move cost energy, inform the HUD about the change
		if (this.map.getCurrentMoveMode() != null)
		{
			int moveColour = this.map.getCurrentMoveMode().type;
			this.gameMode.getPlayer().addEnergy(moveColour, -this.map.getCurrentMoveMode().cost);

			Block targetBlock = null;
			if (blocksAffected.size() > 0)
			{
				targetBlock = blocksAffected.get(0);
			}

			this.hud.onBonusUsed(this.gameMode.getPlayer(), this.map.getCurrentMoveMode(), targetBlock);
		}
	}

	@Override
	public void onMouseMove(Point delta)
	{
		// Unused
	}

	@Override
	public void onMouseUp(Point pt, boolean left)
	{
		this.hud.onTouchUp(pt);
	}

	@Override
	public void onMouseDown(Point pt, boolean left)
	{
		this.hud.onTouchDown(pt, this.gameMode.getPlayer());

		if (	!this.dragModeEnabled
			&&  !this.mapRenderer.isUiLocked())
		{
			BlockRenderable currentBlock = this.mapRenderer.getHitBlock(pt);

			// A block is already selected
			// perform the appropriate move
			if (this.selectedBlockId != -1
				&& currentBlock != null)
			{
				int moveDir = -1;
				GraphVertex<Block> selectedBlock = this.map.findBlockById(this.selectedBlockId);
				for (int dir = 0; dir < this.map.getFieldType().NumDirections; ++dir)
				{
					GraphVertex<Block> neighbour = selectedBlock.getNeighbour(dir);
					if (neighbour != null
						&& neighbour.getData().getId() == currentBlock.getId())
					{
						moveDir = dir;
						break;
					}
				}

				this.performMove(this.selectedBlockId, moveDir);

				this.mapRenderer.deselectBlock(this.selectedBlockId);
				this.selectedBlockId = -1;
			}
			// A block and an appropriate move is selected
			else if (currentBlock != null
					&& (this.map.getCurrentMoveMode() == MoveType.BombChange
					|| this.map.getCurrentMoveMode() == MoveType.ClearColour))
			{
				this.selectedBlockId = currentBlock.getId();
				this.performMove(this.selectedBlockId, -1);
				this.mapRenderer.deselectBlock(this.selectedBlockId);
				this.selectedBlockId = -1;
			}
			// A block has been hit and no blocks are selected
			// select the current one
			else if (currentBlock != null)
			{
				// Check if the corresponding block is not immobile
				Block block = this.map.findBlockById(currentBlock.getId()).getData();

				if (block.getType() != BlockType.Immobile)
				{
					this.selectedBlockId = currentBlock.getId();
					currentBlock.setSelected(true);
				}
			}
			// No blocks have been hit, and a block is selected
			// deselect it
			else if (this.selectedBlockId != -1)
			{
				this.mapRenderer.deselectBlock(this.selectedBlockId);
				this.selectedBlockId = -1;
			}
		}
	}

	public boolean onBonusUsed(MoveType mt)
	{
		if (this.map.getCurrentMoveMode() == mt)
		{
			this.map.setMoveType(MoveType.DefaultSwap);
			return false;
		}

		// This bonus is used instantly, so subtract the energy
		if (mt == MoveType.IncomeBoost)
		{
			this.gameMode.enableEnergyMultiplication(this.energyBoostFactor, this.energyBoostDuration);
			this.gameMode.getPlayer().addEnergy(mt.type, -mt.cost);
		}
		else
		{
			this.map.setMoveType(mt);
		}

		return true;
	}

	@Override
	public void onDrag(Point src, Point dest)
	{
		if (this.mapRenderer.isUiLocked()) return;
		if (this.gameMode.hasEnded()) return;
		if (!this.dragModeEnabled) return;
		if (src == null) return;

		BlockRenderable srcBlock = this.mapRenderer.getHitBlock(src);
		if (srcBlock != null)
		{
			// Instead of selecting the block in dest point
			// calculate the direction
			Vector2 dir = (new Vector2(dest.x - src.x, dest.y - src.y)).nor();
			Vector2 srcVec = new Vector2(0.0f, -1.0f);	// -1 is the UP vector, as the axes are upside down

			// It might just happen that the drag is a click
			// so initialize the default value to -1
			int moveDir = -1;
			if (dir.len() > Double.MIN_VALUE)
			{
				double angle = Math.acos(srcVec.dot(dir));
				// acos returns only values [0; Pi]
				if (dir.x < 0.0f)
				{
					angle = (2 * Math.PI - angle);
				}

				// digitize the angle to a direction
				final double directionDiscr =  2 * Math.PI / this.map.getFieldType().NumDirections;
				angle += directionDiscr / 2;

				if(angle > 2 * Math.PI)
				{
					angle -= 2 * Math.PI;
				}

				moveDir = (int) (angle / directionDiscr);
			}

			this.performMove(srcBlock.getId(), moveDir);
		}
	}

	@Override
	public void dispose()
	{
		// Dispose of all the remaining resources
		this.currentTheme.dispose();
	}

	public boolean isEndgame()
	{
		return this.gameMode.hasEnded();
	}

	public GameStats getScore()
	{
		return this.statsManager.getScore();
	}

	public boolean hasWon()
	{
		return this.gameMode.hasWon();
	}

	public float getEnergyBoostDuration()
	{
		return this.energyBoostDuration;
	}

	private void performMove(int blockId, int dir)
	{
		if (this.map.performMove(blockId, dir))
		{
			this.gameMode.onMove();
			this.hud.onMove(this.gameMode);

			if (!this.hasStarted)
			{
				this.hasStarted = true;
			}
		}
		else
		{
			this.hud.deactivateButtons(this.gameMode.getPlayer());
		}
	}

	// Input layer data
	private int selectedBlockId = -1;
	private boolean dragModeEnabled;
	private PlayState controller;

	// Graphics layer data
	private MapRenderer mapRenderer;
	private Theme currentTheme;

	// Logic layer data
	private Map map;
	private GameMode gameMode;
	private MoveType[] availableMoves;
	private boolean hasGameEndBeenProcessed = false;
	private boolean hasStarted = false;
	private float energyBoostFactor;
	private float energyBoostDuration;

	// Other subsystems
	// The hierarchy in the responsibility chain is as follows:
	// hud -> achievementSystem -> statsManager
	private HUD hud;
	private StatsSubsystem statsManager;
	private AchievementSystem achievementSystem;
}
