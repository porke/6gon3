package com.handknittedapps.honeycombmatchthree.graphics;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEvent;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventChangeColour;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventChangePosition;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventChangeState;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventChangeStrength;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventDestroyBomb;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventDestroyColor;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventDestroyGhost;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventDestroyLine;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventDestroyStandard;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventInsertParallel;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventLinkGhost;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventType;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockWaiterEvent;
import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;
import com.handknittedapps.honeycombmatchthree.logic.DestructionType;
import com.handknittedapps.honeycombmatchthree.utils.Point;


public class BlockRenderable extends Actor
{
	public static final int Size = 79;
	public static final int GridSizeHorz = 71;
	public static final int GridSizeVert = 82;
	public static final float OddBlockTranslation = BlockRenderable.GridSizeVert * 1.73f * 0.5f * 0.55f;
	public static final float ConversionDuration = 0.25f;
	private static final float HitRadius = 48.0f;

	public BlockRenderable(Block block, Theme theme, boolean spawn, BlockRenderable downBlock)
	{
		super(String.valueOf(block.getId()));
		this.blockTheme = theme;
		this.onCreate(block, spawn, downBlock);
	}

	public void activateEventQueue()
	{
		if (!this.eventQueue.isEmpty()
			&& !this.eventQueue.peek().isActive())
		{
			this.eventQueue.peek().start();
		}
	}

	public void onLink(Block block)
	{
		this.eventQueue.add(new BlockEventInsertParallel(0, false, this, new BlockEventLinkGhost(false, this)));
		this.activateEventQueue();
	}

	public void onMatch()
	{
		this.activateEventQueue();
	}

	/***
	 * @return A handle to a particle system if one is created (certain destruction types).
	 * */
	public ParticleEffectRenderable onDestroy(Block block, DestructionType destructionType)
	{
		ParticleEffectRenderable particleEffect = null;
		switch (block.getType())
		{
			case Normal:
			case Immobile:
			{
				if (destructionType == DestructionType.Colour)
				{
					BlockEventDestroyColor evt = new BlockEventDestroyColor(false, this);
					particleEffect = evt.getParticleSystem();
					this.eventQueue.add(evt);
				}
				else if (destructionType == DestructionType.Line)
				{
					BlockEventDestroyLine evt = new BlockEventDestroyLine(false, this);
					particleEffect = evt.getParticleSystem();
					this.eventQueue.add(evt);
				}
				else
				{
					this.eventQueue.add(new BlockEventDestroyStandard(false, this));
				}
				break;
			}
			case Ghost:
				this.eventQueue.add(new BlockEventDestroyGhost(false, this));
				break;
			case Bomb:
				BlockEventDestroyBomb evt = new BlockEventDestroyBomb(false, this);
				particleEffect = evt.getParticleSystem();

				this.eventQueue.add(evt);
				break;
			case Strong:
				break;
		}

		this.eventQueue.add(new BlockEventChangeState(0.0f, this, BlockObserverState.ReadyToBeDisposed));
		this.activateEventQueue();

		if (block.getNumSequences() == 0)
		{
			this.lastEvent = BlockEventType.Destroy;
		}

		return particleEffect;
	}

	public void onMove(Block block)
	{
		this.eventQueue.add(new BlockEventChangePosition(this, this.logicPosToHexPixel(block.getPrevPosition()), this.logicPosToHexPixel(block.getPosition())));
		this.activateEventQueue();

		this.lastEvent = BlockEventType.Move;
	}

	public void onFall(Block block)
	{
		this.eventQueue.add(new BlockEventChangePosition(this, this.logicPosToHexPixel(block.getPrevPosition()), this.logicPosToHexPixel(block.getPosition())));
		this.activateEventQueue();

		this.lastEvent = BlockEventType.Fall;
	}

	public void onClearState()
	{
		this.lastEvent = BlockEventType.None;
	}

	public void onColourChange(Block block)
	{
		this.eventQueue.add(new BlockEventChangeColour(BlockRenderable.ConversionDuration, this, block.getColour(), block.getType()));
		this.activateEventQueue();
	}

	public void onTypeChange(Block block)
	{
		this.eventQueue.add(new BlockEventChangeColour(BlockRenderable.ConversionDuration, this, block.getColour(), block.getType()));
		this.activateEventQueue();
	}

	public void onWait(float time)
	{
		this.eventQueue.add(new BlockWaiterEvent(time, false));
		this.activateEventQueue();
	}

	public void insertEventToParallelQueue(BlockEvent evt)
	{
		this.parallelEventQueue.add(evt);
		evt.start();
	}

	/***
	 * @return A handle to a particle system.
	 * */
	public ParticleEffectRenderable onStrengthChange(Block block)
	{
		// Add the break effect for the strong blocks
		BlockEventChangeStrength changeStrengthEvent = new BlockEventChangeStrength(this, block.getStrength());
		BlockEventInsertParallel particleEvent = new BlockEventInsertParallel(0, false, this, changeStrengthEvent);
		this.eventQueue.add(particleEvent);
		this.activateEventQueue();

		return changeStrengthEvent.getParticleSystem();
	}

	private void onCreate(Block block, boolean spawn, BlockRenderable downBlock)
	{
		Point position = this.logicPosToHexPixel(block.getPosition());
		this.x = position.x;
		this.y = position.y;

		int colour = block.getColour().ordinal();
		int type = block.getType().ordinal();
		this.blockTexture = this.getTextureFromColourAndType(colour, type);
		if (block.getStrength() > 2)
		{
			this.strengthParticleSystem = new ParticleEffectRenderable(new ParticleEffect());
			this.loadParticleSystemForStrength(block.getStrength());
		}

		float evtTime = (downBlock != null) ? downBlock.getEventTimeLeft() : 0.0f;
		this.observerState = BlockObserverState.WaitingToActivate;
		this.eventQueue.add(new BlockEventChangeState(evtTime, this, BlockObserverState.Activated));
		this.lastEvent = (spawn) ? BlockEventType.Spawn : BlockEventType.Create;
	}

	private Point logicPosToHexPixel(Point logicPos)
	{
		Point pos = new Point(0, 0);
		pos.x = (BlockRenderable.GridSizeHorz * logicPos.x);
		pos.y = (int) (BlockRenderable.GridSizeVert * logicPos.y + (logicPos.x % 2) * BlockRenderable.OddBlockTranslation);
		return pos;
	}

	public int getId()
	{
		return Integer.valueOf(this.name);
	}

	public BlockObserverState getObserverState()
	{
		return this.observerState;
	}

	public BlockEventType getLastEvent()
	{
		return this.lastEvent;
	}

	public float getEventTimeLeft()
	{
		float ret = 0.0f;
		for (int i = 0; i < this.eventQueue.size(); ++i)
		{
			ret += this.eventQueue.get(i).getTimeLeft();
		}
		return ret;
	}

	public boolean isAnyEventEnqueued()
	{
		return !this.eventQueue.isEmpty();
	}

	public void setPosition(Point pos)
	{
		this.x = pos.x;
		this.y = pos.y;

		if (this.strengthParticleSystem != null)
		{
			this.strengthParticleSystem.getSystem().setPosition(pos.x + BlockRenderable.Size / 2, pos.y + BlockRenderable.Size / 2);
		}
	}

	public void setObserverState(BlockObserverState state)
	{
		this.observerState = state;
	}

	public void setStrength(int value)
	{
		if (value > 1)
		{
			this.loadParticleSystemForStrength(value);
		}
		else if (this.strengthParticleSystem != null)
		{
			this.strengthParticleSystem.getSystem().dispose();
			this.strengthParticleSystem.markToRemove(true);
			this.strengthParticleSystem = null;
		}
	}

	private void loadParticleSystemForStrength(int strength)
	{
		ParticleEffect sys = this.strengthParticleSystem.getSystem();
		sys.load(Gdx.files.internal("graphics/particles/strong" + String.valueOf(strength - 1) + ".ps"), Gdx.files.internal("graphics/particles/"));
		sys.setPosition(this.x + BlockRenderable.Size / 2, this.y + BlockRenderable.Size / 2);
	}

	public void setColour(int colour, int type)
	{
		this.blockSwitchTexture = this.blockTexture;
		this.blockTexture = this.getTextureFromColourAndType(colour, type);
	}

	private TextureRegion getTextureFromColourAndType(int colour, int type)
	{
		TextureRegion reg = null;
		switch (BlockType.values()[type])
		{
			case Normal:
			case Strong:
				reg = this.blockTheme.getBlockTexture(colour);
				break;
			case Ghost:
				reg = Resources.getGhostBlock(colour);
				break;
			case Bomb:
				reg = Resources.getBombBlock(colour);
				break;
			case Immobile:
				reg = Resources.getImmobileBlock(0);
				break;
			default:
				throw new IllegalArgumentException("Invalid block type.");
		}

		return reg;
	}

	@Override
	public Actor hit(float x, float y)
	{
		Point center = new Point(BlockRenderable.GridSizeHorz / 2, BlockRenderable.GridSizeVert / 2);
		float distance = Math.abs(center.x - x) + Math.abs(center.y - y);

		return (distance < BlockRenderable.HitRadius ? this : null);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		// Draw the primary texture
		this.drawTextureRegion(batch, this.blockTexture, this.color);

		// If the block is being converted also draw the secondary texture
		if (this.blockSwitchTexture != null)
		{
			this.drawTextureRegion(batch, this.blockSwitchTexture, this.switchColor);
		}

		// Draw the selection marker
		if (this.isSelected)
		{
			this.drawTextureRegion(batch, Resources.getHoverMarker(), Color.WHITE);
		}

		if (this.ghostAnimationFrame != null)
		{
			this.drawTextureRegion(batch, this.ghostAnimationFrame, Color.WHITE);
		}

		batch.setColor(Color.WHITE);
	}

	private void drawTextureRegion(SpriteBatch batch, TextureRegion tex, Color color)
	{
		batch.setColor(color);
		batch.draw(tex,
					this.x,
					this.y,
					BlockRenderable.Size / 2,
					BlockRenderable.Size / 2,
					BlockRenderable.Size,
					BlockRenderable.Size,
					this.scaleX,
					this.scaleY,
					this.rotation);
	}

	public ParticleEffectRenderable getStrengthParticleSystem()
	{
		return this.strengthParticleSystem;
	}

	public void addEvent(BlockEvent evt)
	{
		this.eventQueue.add(evt);
	}

	public void setSwitchTexture(TextureRegion tex)
	{
		this.blockSwitchTexture = tex;
	}

	public void setSwitchColor(Color col)
	{
		this.switchColor = col;
	}

	public void setSelected(boolean selected)
	{
		this.isSelected = selected;
	}

	public void setAnimationFrame(TextureRegion frame)
	{
		this.ghostAnimationFrame = frame;
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);

		if (!this.eventQueue.isEmpty())
		{
			if (!this.eventQueue.peek().update())
			{
				this.eventQueue.poll();

				if (!this.eventQueue.isEmpty())
				{
					this.eventQueue.peek().start();
				}
			}
		}

		// Process parallel events
		for (int i = 0; i < this.parallelEventQueue.size(); ++i)
		{
			if (!this.parallelEventQueue.get(i).update())
			{
				this.parallelEventQueue.remove(i);
				--i;
			}
		}
	}

	@Override
	public boolean touchDown(float arg0, float arg1, int arg2)
	{
		// Empty
		return false;
	}

	@Override
	public void touchDragged(float arg0, float arg1, int arg2)
	{
		// Empty
	}

	@Override
	public void touchUp(float arg0, float arg1, int arg2)
	{
		// Empty
	}

	private Color switchColor = Color.WHITE;
	private boolean isSelected = false;
	private Theme blockTheme;
	private TextureRegion ghostAnimationFrame;
	private TextureRegion blockTexture;
	private TextureRegion blockSwitchTexture;
	private BlockObserverState observerState;
	private BlockEventType lastEvent = BlockEventType.None;
	private LinkedList<BlockEvent> eventQueue = new LinkedList<BlockEvent>();
	private ArrayList<BlockEvent> parallelEventQueue = new ArrayList<BlockEvent>();
	private ParticleEffectRenderable strengthParticleSystem;
}
