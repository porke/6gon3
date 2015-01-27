package com.handknittedapps.honeycombmatchthree.logic.subsystems;


import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.graphics.hud.Button;
import com.handknittedapps.honeycombmatchthree.graphics.hud.Displayable;
import com.handknittedapps.honeycombmatchthree.graphics.hud.HudBonusPushedParticleEffect;
import com.handknittedapps.honeycombmatchthree.graphics.hud.HudTimeBasedParticleEffect;
import com.handknittedapps.honeycombmatchthree.graphics.hud.HudTurnBasedParticleEffect;
import com.handknittedapps.honeycombmatchthree.graphics.hud.ProgressBar;
import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.Gamestate;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.logic.Player;
import com.handknittedapps.honeycombmatchthree.logic.modes.PlayModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameMode;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.GameModeType;
import com.handknittedapps.honeycombmatchthree.logic.modes.core.HardcoreMode;
import com.handknittedapps.honeycombmatchthree.logic.moves.FearMove;
import com.handknittedapps.honeycombmatchthree.logic.moves.MoveType;
import com.handknittedapps.honeycombmatchthree.utils.ActionListener;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class HUD extends GameSubsystem
{
	private enum ProgressBarType
	{
		RedEnergy1,
		GreenEnergy1,
		BlueEnergy1,
		RedEnergy2,
		GreenEnergy2,
		BlueEnergy2,
		RedEnergy3,
		GreenEnergy3,
		BlueEnergy3,
		Time,
		Mana,
		Win;

		public static ProgressBarType matchToTierAndColour(int colour, int tier)
		{
			if (colour == 0)
			{
				return ProgressBarType.matchToTierAndColour("R", tier);
			}
			else if (colour == 1)
			{
				return ProgressBarType.matchToTierAndColour("G", tier);
			}
			else if (colour == 2)
			{
				return ProgressBarType.matchToTierAndColour("B", tier);
			}

			return null;
		}

		public static ProgressBarType matchToTierAndColour(String colour, int tier)
		{
			if (colour.equals("R"))
			{
				switch (tier)
				{
					case 1:
						return RedEnergy1;
					case 2:
						return RedEnergy2;
					case 3:
						return RedEnergy3;
				}
			}
			else if (colour.equals("G"))
			{
				switch (tier)
				{
					case 1:
						return GreenEnergy1;
					case 2:
						return GreenEnergy2;
					case 3:
						return GreenEnergy3;
				}
			}
			else if (colour.equals("B"))
			{
				switch (tier)
				{
					case 1:
						return BlueEnergy1;
					case 2:
						return BlueEnergy2;
					case 3:
						return BlueEnergy3;
				}
			}
			return null;
		}
	};

	private static Point AdvertPosition = new Point(0, 96);

	public HUD(Gamestate gs, GameMode mode, MoveType[] unlockedMoves, Stage scene)
	{
		super(null);
		this.mapMovesToTierLimits(unlockedMoves);

		// Load the action button icons
		String prefix = "graphics/hud/bonus/";
		MoveType[] moveTypes = MoveType.values();
		for (MoveType mt : moveTypes)
		{
			if (mt == MoveType.DefaultSwap)
			{
				continue;
			}

			Texture[] textures = new Texture[4];
			String colour = "";
			if (mt.type == 0)
			{
				colour = "Red";
			}
			else if (mt.type == 1)
			{
				colour = "Green";
			}
			else if (mt.type == 2)
			{
				colour = "Blue";
			}

			textures[0] = new Texture(prefix + colour + mt.name + "Inactive.png");
			textures[1] = new Texture(prefix + colour + mt.name + "Normal.png");
			textures[2] = new Texture(prefix + colour + mt.name + "Active.png");
			textures[3] = new Texture(prefix + colour + "Empty.png");
			this.actionButtonIcons.put(mt.name, textures);
		}

		this.stage = scene;

		// Create the action button group and add it
		this.actionButtons = new Group();
		this.actionButtons.x = HUD.AdvertPosition.x;
		this.actionButtons.y = HUD.AdvertPosition.y;
		this.stage.addActor(this.actionButtons);

		// Create the control group
		this.controlGroup = new Group("Controls");
		this.controlGroup.x = HUD.AdvertPosition.x;
		this.controlGroup.y = HUD.AdvertPosition.y;
		this.stage.addActor(this.controlGroup);

		// Create the particle systems group
		this.particleSystems = new Group("PE");
		this.particleSystems.x = HUD.AdvertPosition.x;
		this.particleSystems.y = HUD.AdvertPosition.y;
		this.stage.addActor(this.particleSystems);

		// Create the bonus particle systems group
		this.bonusParticleSystems = new Group("BPE");
		this.bonusParticleSystems.x = HUD.AdvertPosition.x;
		this.bonusParticleSystems.y = HUD.AdvertPosition.y;
		this.stage.addActor(this.bonusParticleSystems);

		// Create the button events
		this.createEvents(gs, mode);
	}

	private void limitActionButtons()
	{
		for (Actor a : this.actionButtons.getActors())
		{
			// We have a button
			Displayable ctrl = (Displayable) a;
			String d = a.name;
			if (d.startsWith("btn"))
			{
				int tier = Integer.valueOf(d.substring(4, 5));
				String colour = d.substring(3, 4);
				int limit = 4;

				if (colour.equals("R"))
				{
					limit = this.bonusTierLimits[0];
				}
				else if (colour.equals("G"))
				{
					limit = this.bonusTierLimits[1];
				}
				else if (colour.equals("B"))
				{
					limit = this.bonusTierLimits[2];
				}
				if (tier > limit)
				{
					ctrl.setActive(0);
					((Button) ctrl).setActive(3);
				}
			}
		}
	}

	private void createEnergyProgressBars()
	{
		for (Actor a : this.actionButtons.getActors())
		{
			Displayable ctrl = (Displayable) a;
			String d = a.name;
			if (d.startsWith("btn"))
			{
				int tier = Integer.valueOf(d.substring(4, 5));
				String colour = d.substring(3, 4);

				Texture progressBarTex = ((Button) ctrl).textures[1];
				ProgressBar pbr = new ProgressBar.Builder("pbr" + colour + tier, progressBarTex)
												 .position(new Point((int) ctrl.x, (int) ctrl.y))
												 .size(new Point((int) ctrl.width, (int) ctrl.height))
												 .normalizedMappingMin(ctrl.width / (float)progressBarTex.getWidth())
												 .normalizedMappingMax(ctrl.height / (float)progressBarTex.getWidth())
												 .yAxis(true)
												 .inverted(false)
												 .build();

				this.progressBars.put(ProgressBarType.matchToTierAndColour(colour, tier),pbr);
				this.controlGroup.addActor(pbr);
			}
		}
	}

	private void createActionButtons()
	{
		FileHandle input = Gdx.files.internal("graphics/hud/hud.txt");
		Scanner scan = new Scanner(input.read());
		while (scan.hasNext())
		{
			String name = scan.next();
			String type = scan.next();
			Point position = new Point(scan.nextInt(), scan.nextInt());
			Point size = new Point(scan.nextInt(), scan.nextInt());
			float inputRadius;
			if (type.equals("Button"))
			{
				String actionId = scan.next();
				inputRadius = scan.nextFloat();

				Button button = new Button.Builder(name, this.actionButtonIcons.get(actionId))
										  .inputRadius(inputRadius)
										  .position(position)
										  .size(size)
										  .active(0)
										  .build();
				this.actionButtons.addActor(button);
			}
		}

		scan.close();
	}

	@Override
	public void onMove(GameMode gm)
	{
		if (gm.getModeType() == GameModeType.Survival)
		{
			((ProgressBar) this.controlGroup.findActor("pbrMana")).addProgressUpdateEvent(gm.getLoseConditionLeft());
		}
		else if (gm.getModeType() == GameModeType.Hardcore)
		{
			((ProgressBar) this.controlGroup.findActor("pbrMana")).addProgressUpdateEvent(((HardcoreMode) gm).getLoseConditionLeftMana());
			((ProgressBar) this.controlGroup.findActor("pbrGlobal")).addProgressUpdateEvent(gm.getProgress());
		}

		// Update the turn data for the particle systems
		List<Actor> effects = this.particleSystems.getActors();
		for (Actor act : effects)
		{
			if (act instanceof HudTurnBasedParticleEffect)
			{
				HudTurnBasedParticleEffect ps = ((HudTurnBasedParticleEffect) act);
				ps.setDurationInTurns(ps.getDurationInTurns() - 1);
			}
		}

		if (this.next != null)
		{
			this.next.onMove(gm);
		}
	}

	public boolean onTouchDown(Point pt, Player player)
	{
		Vector2 local = new Vector2();
		this.stage.toStageCoordinates(pt.x, pt.y, local);
		this.actionButtons.toLocalCoordinates(local);
		Actor button = this.actionButtons.hit(local.x, local.y);

		if (button instanceof Displayable)
		{
			this.evaluateActionButtons(player, button);

			if (button.touchable)
			{
				this.buttonCallbacks.get(button.name).actionPerformed();
			}
		}

		return this.stage.touchDown(pt.x, pt.y, 0, 0);
	}

	public boolean onTouchUp(Point pt)
	{
		return this.stage.touchUp(pt.x, pt.y, 0, 0);
	}

	private void updateEnergyProgressBars(Player player)
	{
		// The orbs are filled only if at least one colour bonus
		// is unlocked (red, green or blue)
		if (this.bonusTierLimits[0] > 0)
		{
			float[] energyValues = new float[] {
					player.getEnergy(0) / MoveType.RemoteSwap.cost,
					player.getEnergy(0) / MoveType.BombChange.cost,
					player.getEnergy(0) / MoveType.ClearColour.cost };

			if (this.bonusTierLimits[0] > 0)
			{
				this.progressBars.get(ProgressBarType.RedEnergy1).addProgressUpdateEvent(energyValues[0] > 1.0f ? 1.0f : energyValues[0]);
			}
			if (this.bonusTierLimits[0] > 1)
			{
				this.progressBars.get(ProgressBarType.RedEnergy2).addProgressUpdateEvent(energyValues[1] > 1.0f ? 1.0f : energyValues[1]);
			}
			if (this.bonusTierLimits[0] > 2)
			{
				this.progressBars.get(ProgressBarType.RedEnergy3).addProgressUpdateEvent(energyValues[2] > 1.0f ? 1.0f : energyValues[2]);
			}
		}
		if (this.bonusTierLimits[1] > 0)
		{
			float[] energyValues = new float[] {
					player.getEnergy(1) / MoveType.RotateSwap.cost,
					player.getEnergy(1) / MoveType.IncomeBoost.cost,
					player.getEnergy(1) / MoveType.Fear.cost };

			if (this.bonusTierLimits[1] > 0)
			{
				this.progressBars.get(ProgressBarType.GreenEnergy1).addProgressUpdateEvent(energyValues[0] > 1.0f ? 1.0f : energyValues[0]);
			}

			if (this.bonusTierLimits[1] > 1)
			{
				this.progressBars.get(ProgressBarType.GreenEnergy2).addProgressUpdateEvent(energyValues[1] > 1.0f ? 1.0f : energyValues[1]);
			}

			if (this.bonusTierLimits[1] > 2)
			{
				this.progressBars.get(ProgressBarType.GreenEnergy3).addProgressUpdateEvent(energyValues[2] > 1.0f ? 1.0f : energyValues[2]);
			}
		}
		if (this.bonusTierLimits[2] > 0)
		{
			float[] energyValues = new float[] {
					player.getEnergy(2) / MoveType.ColourChange.cost,
					player.getEnergy(2) / MoveType.DeleteLine.cost,
					player.getEnergy(2) / MoveType.Conversion.cost };

			if (this.bonusTierLimits[2] > 0)
			{
				this.progressBars.get(ProgressBarType.BlueEnergy1).addProgressUpdateEvent(energyValues[0] > 1.0f ? 1.0f : energyValues[0]);
			}
			if (this.bonusTierLimits[2] > 1)
			{
				this.progressBars.get(ProgressBarType.BlueEnergy2).addProgressUpdateEvent(energyValues[1] > 1.0f ? 1.0f : energyValues[1]);
			}

			if (this.bonusTierLimits[2] > 2)
			{
				this.progressBars.get(ProgressBarType.BlueEnergy3).addProgressUpdateEvent(energyValues[2] > 1.0f ? 1.0f : energyValues[2]);
			}
		}
	}

	public void onFrame(GameMode gm)
	{
		if (gm.getModeType() == GameModeType.Classic)
		{
			ProgressBar scoreProgressBar = this.progressBars.get(ProgressBarType.Win);

			if (!scoreProgressBar.isEnqueued())
			{
				scoreProgressBar.updateProgress(gm.getProgress());
			}
		}

		// update the time progress bar for appropriate modes
		else if (gm.getModeType() != GameModeType.Casual
			&& gm.getModeType() != GameModeType.Survival)
		{
			ProgressBar timeProgressBar = this.progressBars.get(ProgressBarType.Time);
			if(!timeProgressBar.isEnqueued())
			{
				timeProgressBar.updateProgress(gm.getLoseConditionLeft());
			}
		}
	}

	private void evaluateActionButtons(Player player, Actor b)
	{
		// For the colours
		for (int i = 0; i < MoveType.NumEnergyTypes; ++i)
		{
			char[] colours = {'R', 'G', 'B'};

			// For the bonus tiers
			for (int j = 0; j < MoveType.NumEnergyTypes; ++j)
			{
				String id = "btn" + colours[i] + (j + 1);
				Button target = (Button) this.actionButtons.findActor(id);

				MoveType mt = MoveType.get(i, j + 1);
				boolean unlocked = (mt.tier <= this.bonusTierLimits[mt.type]);
				boolean costOK = (player.getEnergy(i) > MoveType.get(i, j + 1).cost);
				target.touchable = costOK && unlocked;

				// Yes I need to check if the references point to the same object
				if (	target == b
					&&  target.touchable)
				{
					this.setBonusProgressBarState(mt, true);
				}
				else
				{
					this.setBonusProgressBarState(mt, false);
				}
			}
		}
	}

	private void mapMovesToTierLimits(MoveType[] unlockedMoves)
	{
		if (unlockedMoves != null)
		{
			this.bonusTierLimits[0] = 0;
			this.bonusTierLimits[1] = 0;
			this.bonusTierLimits[2] = 0;
			for (MoveType m : unlockedMoves)
			{
				if (this.bonusTierLimits[m.type] < m.tier)
				{
					this.bonusTierLimits[m.type] = m.tier;
				}
			}
		}
	}

	private void createEvents(final Gamestate gs, final GameMode gm)
	{
		this.buttonCallbacks.put("btnR1", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.RemoteSwap);
				HUD.this.setBonusProgressBarState(MoveType.RemoteSwap, enable);
			}
		});

		this.buttonCallbacks.put("btnR2", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.BombChange);
				HUD.this.setBonusProgressBarState(MoveType.BombChange, enable);
			}
		});

		this.buttonCallbacks.put("btnR3", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.ClearColour);
				HUD.this.setBonusProgressBarState(MoveType.ClearColour, enable);
			}
		});

		this.buttonCallbacks.put("btnB1", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.ColourChange);
				HUD.this.setBonusProgressBarState(MoveType.ColourChange, enable);
			}
		});

		this.buttonCallbacks.put("btnB2", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.DeleteLine);
				HUD.this.setBonusProgressBarState(MoveType.DeleteLine, enable);
			}
		});

		this.buttonCallbacks.put("btnB3", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.Conversion);
				HUD.this.setBonusProgressBarState(MoveType.Conversion, enable);
			}
		});

		this.buttonCallbacks.put("btnG1", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.RotateSwap);
				HUD.this.setBonusProgressBarState(MoveType.RotateSwap, enable);
			}
		});

		this.buttonCallbacks.put("btnG2", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				gs.onBonusUsed(MoveType.IncomeBoost);

				// This is the boost bonus (immediate activation)
				// And immediate energy subtraction
				HUD.this.updateEnergyProgressBars(gm.getPlayer());
				HUD.this.evaluateActionButtons(gm.getPlayer(), null);

				float duration = gs.getEnergyBoostDuration();
				HUD.this.particleSystems.addActor(new HudTimeBasedParticleEffect("income_boost", duration, new Point(HoneycombMatchThree.NominalWidth - 50, 130)));
				HUD.this.particleSystems.addActor(new HudTimeBasedParticleEffect("income_boost", duration, new Point(HoneycombMatchThree.NominalWidth - 105, 130)));
				HUD.this.particleSystems.addActor(new HudTimeBasedParticleEffect("income_boost", duration, new Point(HoneycombMatchThree.NominalWidth - 160, 130)));
			}
		});

		this.buttonCallbacks.put("btnG3", new ActionListener()
		{
			@Override
			public void actionPerformed()
			{
				boolean enable = gs.onBonusUsed(MoveType.Fear);
				HUD.this.setBonusProgressBarState(MoveType.Fear, enable);
			}
		});
	}

	public boolean isWinProgressBarEnqueued()
	{
		return this.progressBars.get(ProgressBarType.Win).isEnqueued();
	}

	private void setBonusProgressBarState(MoveType moveType, boolean active)
	{
		ProgressBarType type = ProgressBarType.matchToTierAndColour(moveType.type, moveType.tier);
		ProgressBar progressBar = this.progressBars.get(type);
		progressBar.setActive(active ? 2 : 1);

		HudBonusPushedParticleEffect systemActor = (HudBonusPushedParticleEffect) this.bonusParticleSystems.findActor(moveType.toString());
		if (active
			&& systemActor == null)
		{
			Point position = new Point((int) progressBar.x + (int) progressBar.width / 2, (int) progressBar.y + (int) progressBar.height / 2);
			HudBonusPushedParticleEffect particleEffect = new HudBonusPushedParticleEffect(moveType.toString(), position);

			this.bonusParticleSystems.addActor(particleEffect);
		}
		else if (!active
				&& systemActor != null)
		{
			this.bonusParticleSystems.removeActor(systemActor);
		}
	}

	public void deactivateButtons(Player player)
	{
		this.evaluateActionButtons(player, null);
	}

	private void constructManaProgressBar(Point progressBarTexCoords,
										  Point backgroundTexCoords,
										  Point progressBarSize,
										  Point backgroundBarSize)
	{
		int progressBarXpos = 0;
		int backgroundBarXpos = 0;

		// Add a mana progress bar and the background
		Texture manaTexture = new Texture("graphics/hud/HealthProgressBarFull.png");
		ProgressBar mana = new ProgressBar.Builder("pbrMana", manaTexture)
										  .position(new Point(progressBarXpos, 68))
										  .size(progressBarSize)
										  .normalizedMappingMin(progressBarTexCoords.y / (float)manaTexture.getWidth())
										  .normalizedMappingMax(progressBarTexCoords.x / (float)manaTexture.getWidth())
										  .yAxis(false)
										  .inverted(false)
 										  .build();
		this.progressBars.put(ProgressBarType.Mana, mana);

		Texture emptyManaTexture = new Texture("graphics/hud/HealthProgressBarEmpty.png");
		ProgressBar loseBackground = new ProgressBar.Builder("pbrLoseBackground", emptyManaTexture)
													 .position(new Point(backgroundBarXpos, 68))
													 .size(backgroundBarSize)
													 .normalizedMappingMin(backgroundTexCoords.y / (float)emptyManaTexture.getWidth())
													 .normalizedMappingMax(backgroundTexCoords.x / (float)emptyManaTexture.getWidth())
													 .yAxis(false)
													 .inverted(false)
													 .currProgress(1.0f)
													 .build();

		this.controlGroup.addActor(loseBackground);
		this.controlGroup.addActor(this.progressBars.get(ProgressBarType.Mana));

		// Add some eyecandy
		mana.addProgressUpdateEvent(1.0f);
	}

	private void constructWinProgressBar(Point progressBarTexCoords,
										 Point backgroundTexCoords,
										 Point progressBarSize,
										 Point backgroundBarSize)
	{
		int progressBarXpos = 0;
		int backgroundBarXpos = 0;

		// Yes, not dividing the y coordinate IS intentional!
		Texture winTexture = new Texture("graphics/hud/VictoryProgressBarFull.png");
		ProgressBar winProgressBar = new ProgressBar.Builder("pbrGlobal", winTexture)
													.position(new Point(progressBarXpos, 128))
													.size(progressBarSize)
													.normalizedMappingMin(progressBarTexCoords.y / (float)winTexture.getWidth())
													.normalizedMappingMax(progressBarTexCoords.x / (float)winTexture.getWidth())
													.yAxis(false)
													.inverted(false)
													.build();
		this.progressBars.put(ProgressBarType.Win, winProgressBar);

		Texture winBackgroundTex = new Texture("graphics/hud/VictoryProgressBarEmpty.png");
		ProgressBar winBackground = new ProgressBar.Builder("pbrWinBackground", winBackgroundTex)
													.position(new Point(backgroundBarXpos, 128))
													.size(backgroundBarSize)
													.normalizedMappingMin(backgroundTexCoords.y / (float)winBackgroundTex.getWidth())
													.normalizedMappingMax(backgroundTexCoords.x / (float)winBackgroundTex.getWidth())
												   .yAxis(false)
												   .inverted(false)
												   .currProgress(1.0f)
												   .build();

		this.controlGroup.addActor(winBackground);
		this.controlGroup.addActor(this.progressBars.get(ProgressBarType.Win));
	}

	private void constructTimeProgressBar(Point progressBarTexCoords,
			 							  Point backgroundTexCoords,
			 							  Point progressBarSize,
			 							  Point backgroundBarSize)
	{
		int progressBarXpos = 0;
		int backgroundBarXpos = 0;

		Texture timeTexture = new Texture("graphics/hud/TimeProgressBarFull.png");
		ProgressBar timeProgressBar = new ProgressBar.Builder("pbrTime", timeTexture)
													 .position(new Point(progressBarXpos, 4))
													 .size(progressBarSize)
													 .normalizedMappingMin(progressBarTexCoords.y / (float)timeTexture.getWidth())
													 .normalizedMappingMax(progressBarTexCoords.x / (float)timeTexture.getWidth())
													 .yAxis(false)
													 .inverted(false)
													 .build();
		this.progressBars.put(ProgressBarType.Time, timeProgressBar);

		Texture timeBackgroundTex = new Texture("graphics/hud/TimeProgressBarEmpty.png");
		ProgressBar loseBackground = new ProgressBar.Builder("pbrLoseBackground", timeBackgroundTex)
													.position(new Point(backgroundBarXpos, 4))
													.size(backgroundBarSize)
													.normalizedMappingMin(backgroundTexCoords.y / (float)timeBackgroundTex.getWidth())
													.normalizedMappingMax(backgroundTexCoords.x / (float)timeBackgroundTex.getWidth())
													.yAxis(false)
													.inverted(false)
													.currProgress(1.0f)
													.build();
		this.controlGroup.addActor(loseBackground);
		this.controlGroup.addActor(this.progressBars.get(ProgressBarType.Time));

		// Add some eyecandy
		timeProgressBar.addProgressUpdateEvent(1.0f);
	}

	@Override
	public void onGameStart(PlayModeType pmt, GameModeType gmt, Object data)
	{
		// Global progress
		// x = Progress bar length
		// y = offset in texels from the beginning
		Point progressBarTexCoords = new Point(224, 32);
		Point backgroundTexCoords = new Point(256, 0);
		Point progressBarSize = new Point(224, 32);
		Point backgroundBarSize = new Point(256, 32);

		// Create the win progress bar & background
		this.constructWinProgressBar(progressBarTexCoords, backgroundTexCoords, progressBarSize, backgroundBarSize);

		// turn unnecessary controls off
		if (gmt == GameModeType.Survival)
		{
			// Add a mana progress bar and the background
			this.constructManaProgressBar(progressBarTexCoords, backgroundTexCoords, progressBarSize, backgroundBarSize);
		}
		else if (gmt == GameModeType.Hardcore)
		{
			// Add a mana progress bar and the background
			this.constructManaProgressBar(progressBarTexCoords, backgroundTexCoords, progressBarSize, backgroundBarSize);

			// Add a time progress bar and the background
			this.constructTimeProgressBar(progressBarTexCoords, backgroundTexCoords, progressBarSize, backgroundBarSize);
		}
		else if (gmt != GameModeType.Casual
			  && gmt != GameModeType.Classic)
		{
			// Add a time progress bar and the background
			this.constructTimeProgressBar(progressBarTexCoords, backgroundTexCoords, progressBarSize, backgroundBarSize);
		}

		// Load the controls
		this.createActionButtons();
		this.createEnergyProgressBars();

		// Limit the action buttons
		this.limitActionButtons();

		if (this.next != null)
		{
			this.next.onGameStart(pmt, gmt, data);
		}
	}

	@Override
	public void onEndgame(GameMode gm)
	{
		if (this.next != null)
		{
			this.next.onEndgame(gm);
		}
	}

	@Override
	public void onBonusUsed(Player player, MoveType mt, Block targetBlock)
	{
		this.updateEnergyProgressBars(player);
		this.evaluateActionButtons(player, null);

		if (mt == MoveType.Fear)
		{
			Actor prevFearParticleSystem = this.stage.findActor("fear");
			if (prevFearParticleSystem != null)
			{
				this.stage.removeActor(prevFearParticleSystem);
			}

			HudTurnBasedParticleEffect particleEffect = new HudTurnBasedParticleEffect("fear", FearMove.DurationInTurns, new Point(HoneycombMatchThree.NominalWidth - 170, 136));
			ParticleEffect effect = particleEffect.getSystem();
			effect.setFlip(false, true);
			Array<ParticleEmitter> emitters = effect.getEmitters();
			final String colourName = targetBlock.getColour().toString().toLowerCase(Locale.US);
			for (ParticleEmitter emitter : emitters)
			{
				if (!emitter.getName().equals(colourName))
				{
					emitter.setMaxParticleCount(0);
				}
			}

			this.particleSystems.addActor(particleEffect);
		}

		if (this.next != null)
		{
			this.next.onBonusUsed(player, mt, targetBlock);
		}
	}

	@Override
	public void onScore(GameMode gm, MoveDestructionSequences mts)
	{
		if (this.progressBars.get(ProgressBarType.Win).getCurrProgress() < 1)
		{
			this.progressBars.get(ProgressBarType.Win).addProgressUpdateEvent(gm.getProgress());
		}

		if (!gm.hasEnded())
		{
			StringBuilder sb = new StringBuilder();
			sb.append(gm.getPlayer().getScore());

			this.updateEnergyProgressBars(gm.getPlayer());

			if (gm.getModeType() == GameModeType.Hardcore)
			{
				ProgressBar mana = ((ProgressBar) this.controlGroup.findActor("pbrMana"));
				mana.addProgressUpdateEvent(((HardcoreMode) gm).getLoseConditionLeftMana());
			}

			this.evaluateActionButtons(gm.getPlayer(), null);
		}

		if (this.next != null)
		{
			this.next.onScore(gm, mts);
		}
	}

	private HashMap<String, ActionListener> buttonCallbacks = new HashMap<String, ActionListener>();
	private HashMap<String, Texture[]> actionButtonIcons = new HashMap<String, Texture[]>();
	private HashMap<ProgressBarType, ProgressBar> progressBars = new HashMap<ProgressBarType, ProgressBar>();
	private Group actionButtons;
	private Group controlGroup;
	private Group particleSystems;
	private Group bonusParticleSystems;
	private Stage stage;

	private int[] bonusTierLimits = new int[] {0, 0, 0};
}
