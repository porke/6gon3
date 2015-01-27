package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateDecelerateInterpolator;

import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;

public class BlockEventDestroyStandard extends BlockEvent
{
	private static float Duration = 0.5f;
	private static float InterpolationFactor = 1.5f;

	static
	{
		BlockEventDestroyStandard.Duration = FxSettingsRepository.getInstance().getSetting("BlockEventDestroyStandard.Duration");
		BlockEventDestroyStandard.InterpolationFactor = FxSettingsRepository.getInstance().getSetting("BlockEventDestroyStandard.InterpolationFactor");
	}

	public BlockEventDestroyStandard(boolean activate, BlockRenderable block)
	{
		this(BlockEventDestroyStandard.Duration, activate, block);
	}

	public BlockEventDestroyStandard(float time, boolean activate, BlockRenderable block)
	{
		super(time, activate, block);
		this.interpolator = AccelerateDecelerateInterpolator.$(BlockEventDestroyStandard.InterpolationFactor);
		this.block = block;
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			float scale = 1.0f - this.interpolator.getInterpolation(this.getTimeElapsedToTime());
			this.block.scaleX = scale;
			this.block.scaleY = scale;
			this.block.rotation = 360 * this.interpolator.getInterpolation(this.getTimeElapsedToTime());
		}
		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.block.rotation = 0;
		this.block.scaleX = 0.0f;
		this.block.scaleY = 0.0f;
	}

	private Interpolator interpolator;
}
