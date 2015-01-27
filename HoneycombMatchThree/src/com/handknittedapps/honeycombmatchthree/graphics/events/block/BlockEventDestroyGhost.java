package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateDecelerateInterpolator;

import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;

public class BlockEventDestroyGhost extends BlockEvent
{
	private static float Duration = 0.5f;
	private static float InterpolationFactor = 1.5f;
	
	static
	{
		BlockEventDestroyGhost.Duration = FxSettingsRepository.getInstance().getSetting("BlockEventDestroyGhost.Duration");
		BlockEventDestroyGhost.InterpolationFactor = FxSettingsRepository.getInstance().getSetting("BlockEventDestroyGhost.InterpolationFactor");
	}
	
	public BlockEventDestroyGhost(boolean activate, BlockRenderable block)
	{
		this(BlockEventDestroyGhost.Duration, activate, block);
	}

	public BlockEventDestroyGhost(float time, boolean activate, BlockRenderable block)
	{
		super(time, activate, block);
		interpolator = AccelerateDecelerateInterpolator.$(BlockEventDestroyGhost.InterpolationFactor);
		this.block = block;
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			block.color.a = 1.0f - interpolator.getInterpolation(this.getTimeElapsedToTime());
		}

		return isActive;
	}

	@Override
	public void onEnded()
	{
		block.color.a = 0.0f;
	}

	private Interpolator interpolator;
}
