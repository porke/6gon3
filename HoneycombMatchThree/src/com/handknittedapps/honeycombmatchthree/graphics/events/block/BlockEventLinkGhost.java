package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.badlogic.gdx.graphics.g2d.Animation;

import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class BlockEventLinkGhost extends BlockEvent
{
	private static float FrameDuration;

	static
	{
		BlockEventLinkGhost.FrameDuration = FxSettingsRepository.getInstance().getSetting("BlockEventLinkGhost.FrameDuration");
	}

	public BlockEventLinkGhost(boolean activate, BlockRenderable block)
	{
		super(BlockEventLinkGhost.FrameDuration * Resources.getGhostBlinkAnimation().size(), activate, block);
		this.block = block;
		this.animation = new Animation(BlockEventLinkGhost.FrameDuration, Resources.getGhostBlinkAnimation());
		block.setAnimationFrame(this.animation.getKeyFrame(0, false));
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			this.block.setAnimationFrame(this.animation.getKeyFrame(this.timeElapsed, false));
		}

		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.block.setAnimationFrame(null);
	}

	private Animation animation;
}
