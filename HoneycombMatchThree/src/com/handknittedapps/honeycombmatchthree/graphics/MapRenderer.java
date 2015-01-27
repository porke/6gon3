package com.handknittedapps.honeycombmatchthree.graphics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.graphics.events.block.BlockEventType;
import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.logic.DestructionSequence;
import com.handknittedapps.honeycombmatchthree.logic.DestructionType;
import com.handknittedapps.honeycombmatchthree.logic.Map;
import com.handknittedapps.honeycombmatchthree.logic.MoveDestructionSequences;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class MapRenderer implements Observer
{
	private static final int BackgroundXTranslation = 23;
	private static final int BackgroundYTranslation = 58;

	public MapRenderer(int blockColumns, Stage scene, Theme currentTheme)
	{
		this.currentTheme = currentTheme;

		this.blocks = new Group("Blocks");

		// Assume block columns = blockRows
		this.blocks.x = (HoneycombMatchThree.NominalWidth - blockColumns * BlockRenderable.GridSizeHorz) / 2;
		this.blocks.y = (HoneycombMatchThree.NominalHeight - (blockColumns + 0.5f) * BlockRenderable.GridSizeVert);

		this.particleSystems = new Group("ParticleSystems");
		this.particleSystems.x = this.blocks.x;
		this.particleSystems.y = this.blocks.y;

		Sprite background = new Sprite(this.currentTheme.getBackground());
		background.x = this.blocks.x - MapRenderer.BackgroundXTranslation;
		background.y = this.blocks.y - MapRenderer.BackgroundYTranslation;

		this.scoreOverlayManager.x = this.blocks.x;
		this.scoreOverlayManager.y = this.blocks.y;

		scene.addActor(background);
		scene.addActor(this.blocks);
		scene.addActor(this.particleSystems);
		scene.addActor(this.scoreOverlayManager);
		for (int i = 0; i < blockColumns; ++i)
		{
			this.spawningBlocks.add(new LinkedList<BlockRenderable>());
		}
	}

	public BlockRenderable getHitBlock(Point pt)
	{
		Vector2 outVec = new Vector2();
		this.blocks.getStage().toStageCoordinates(pt.x, pt.y, outVec);

		Actor hit = this.blocks.getStage().hit(outVec.x, outVec.y);
		return (hit instanceof BlockRenderable) ? (BlockRenderable) hit : null;
	}

	public void deselectBlock(int id)
	{
		((BlockRenderable) (this.blocks.findActor(String.valueOf(id)))).setSelected(false);
	}

	public boolean isUiLocked()
	{
		//Lock the interface
		for (LinkedList<BlockRenderable> list : this.spawningBlocks)
		{
			if (!list.isEmpty())
			{
				return true;
			}
		}

		List<Actor> actors = this.blocks.getActors();
		for (Actor obs : actors)
		{
			if (((BlockRenderable) obs).isAnyEventEnqueued())
			{
				return true;
			}
		}

		return false;
	}

	public void onFrame(Map map)
	{
		for (int i = 0; i < this.disposableBlocks.size(); ++i)
		{
			BlockRenderable obs = this.disposableBlocks.get(i);
			if (obs.getObserverState() == BlockObserverState.ReadyToBeDisposed)
			{
				this.blocks.removeActor(obs);
				this.disposableBlocks.remove(i);
				--i;
			}
		}

		for (LinkedList<BlockRenderable> list : this.spawningBlocks)
		{
			BlockRenderable obs = list.peek();
			if (obs != null)
			{
				obs.act(Gdx.graphics.getDeltaTime());
				if (obs.getObserverState() == BlockObserverState.Activated)
				{
					this.blocks.addActor(list.removeFirst());
					if (list.peek() != null)
					{
						list.peek().activateEventQueue();
					}
				}
			}
		}

		if (this.updateMap
			&& !this.isUiLocked())
		{
			this.updateMap = map.matchBlocks();
		}
	}

	// Events
	private void onBlockLink(Block block)
	{
		BlockRenderable sprite = this.findBlockObserver(block);
		sprite.onLink(block);
	}

	private void onBlockCreate(Block block)
	{
		BlockRenderable downBlock = (BlockRenderable) this.blocks.findActor(String.valueOf(block.getFirstBlockInColumnId()));
		BlockRenderable obs = new BlockRenderable(block, this.currentTheme, false, downBlock);
		if (obs.getStrengthParticleSystem() != null)
		{
			obs.getStrengthParticleSystem().getSystem().start();
			this.particleSystems.addActor(obs.getStrengthParticleSystem());
		}

		this.blocks.addActor(obs);
		obs.activateEventQueue();
	}

	private void onBlockSpawn(Block block)
	{
		BlockRenderable downBlock = (BlockRenderable) this.blocks.findActor(String.valueOf(block.getFirstBlockInColumnId()));
		int column = block.getPosition().x;
		LinkedList<BlockRenderable> list = this.spawningBlocks.get(column);
		BlockRenderable obs = new BlockRenderable(block, this.currentTheme, true, downBlock);
		list.add(obs);
		list.peek().activateEventQueue();

		if (obs.getStrengthParticleSystem() != null)
		{
			obs.getStrengthParticleSystem().getSystem().start();
			this.particleSystems.addActor(obs.getStrengthParticleSystem());
		}
	}

	private void onBlockDestroy(Block block)
	{
		// Find the block observers that are to be destroyed
		ArrayList<BlockRenderable> disposables = new ArrayList<BlockRenderable>();
		BlockRenderable current = null;
		DestructionSequence destBlocks = block.getDestroyedBlocks();
		for (int i = 0; i < destBlocks.size(); ++i)
		{
			BlockRenderable bo = this.findBlockObserver(destBlocks.getBlock(i));
			disposables.add(bo);

			if (Integer.valueOf(bo.name) == block.getId())
			{
				current = bo;
			}
		}

		// The block must have been in another sequence, skip it
		if (current.getLastEvent() == BlockEventType.Destroy)
		{
			return;
		}

		// If it is a match, play the proper sound
		if (destBlocks.getType() == DestructionType.Match)
		{
			current.onMatch();
		}

		// call the destroy event for the BlockRenderable
		ParticleEffectRenderable effect = current.onDestroy(block, destBlocks.getType());
		if (effect != null)
		{
			this.particleSystems.addActor(effect);
		}

		// Add them to disposable blocks
		this.disposableBlocks.addAll(disposables);
	}

	private void onBlockStrengthChange(Block block)
	{
		BlockRenderable sprite = this.findBlockObserver(block);
		ParticleEffectRenderable shatterPs = sprite.onStrengthChange(block);
		this.particleSystems.addActor(shatterPs);
	}

	//Observer
	@Override
	public void update(Observable o, Object arg)
	{
		BlockEventType evt = (BlockEventType) arg;
		Block block = (Block) o;

		switch(evt)
		{
			case Link:
				this.onBlockLink(block);
				break;
			case Create:
				this.onBlockCreate(block);
				break;
			case Fall:
				this.onBlockFall(block);
				break;
			case Destroy:
				this.onBlockDestroy(block);
				break;
			case Spawn:
				this.onBlockSpawn(block);
				break;
			case StrengthChange:
				this.onBlockStrengthChange(block);
				break;
			case Move:
				this.findBlockObserver(block).onMove(block);
				break;
			case None:
				this.findBlockObserver(block).onClearState();
				break;
			case ColourChange:
				this.findBlockObserver(block).onColourChange(block);
				break;
			case TypeChange:
				this.findBlockObserver(block).onTypeChange(block);
				break;
		}
	}

	private void onBlockFall(Block block)
	{
		BlockRenderable actor = (BlockRenderable) this.findBlockObserver(block);
		actor.onFall(block);
	}

	public void onBlockSequenceDestroyed(MoveDestructionSequences destr)
	{
		// Transform the logical destruction sequences into lists of block sprites
		ArrayList<ArrayList<BlockRenderable>> blocks = new ArrayList<ArrayList<BlockRenderable>>();
		ArrayList<Integer> score = new ArrayList<Integer>();
		for (int i = 0; i < destr.numSequences(); ++i)
		{
			DestructionSequence seq = destr.getDestructionSequence(i);
			ArrayList<BlockRenderable> blockList = new ArrayList<BlockRenderable>();
			blocks.add(blockList);
			score.add(destr.getScore());

			for (int j = 0; j < seq.size(); ++j)
			{
				blockList.add(this.findBlockObserver(seq.getBlock(j)));
			}
		}

		//And call the the overlayManager to display the scores
		this.scoreOverlayManager.onScore(blocks, destr.getScore());
	}

	private BlockRenderable findBlockObserver(Block block)
	{
		// find the block
		BlockRenderable found = (BlockRenderable) this.blocks.findActor(String.valueOf(block.getId()));
		if (found != null)
		{
			return found;
		}

		// the block might be in the spawning queue
		for (int i = 0; i < this.spawningBlocks.size(); ++i)
		{
			LinkedList<BlockRenderable> list = this.spawningBlocks.get(i);
			for (int j = 0; j < list.size(); ++j)
			{
				if (Integer.valueOf(list.get(j).name) == block.getId())
				{
					return list.get(j);
				}
			}
		}

		return null;
	}

	public void enqueueWaitEvent(ArrayList<Block> changedBlocks)
	{
		List<Actor> blocks = this.blocks.getActors();
		for (Actor block : blocks)
		{
			boolean hasChanged = false;
			for (Block cb : changedBlocks)
			{
				if (cb.getId() == ((BlockRenderable) block).getId())
				{
					hasChanged = true;
					break;
				}
			}

			if (!hasChanged
				&& changedBlocks.size() > 0)
			{
				// 	Apply an event delay
				BlockRenderable unchangedBlock = (BlockRenderable) block;
				BlockRenderable observer = this.findBlockObserver(changedBlocks.get(0));
				float waitTime = (observer != null) ? observer.getEventTimeLeft() : 0;
				unchangedBlock.onWait(waitTime);
			}
		}
	}

	public void setMapUpdate(boolean update) { this.updateMap = update; }
	public boolean isMapInNeedOfUpdate() { return this.updateMap; }

	private Group blocks;
	private Group particleSystems;
	private ArrayList<BlockRenderable> disposableBlocks = new ArrayList<BlockRenderable>();
	private ArrayList<LinkedList<BlockRenderable>> spawningBlocks = new ArrayList<LinkedList<BlockRenderable>>();
	private boolean updateMap = false;
	private Theme currentTheme;

	// Score overlays
	private ScoreOverlayManager scoreOverlayManager = new ScoreOverlayManager();
}
