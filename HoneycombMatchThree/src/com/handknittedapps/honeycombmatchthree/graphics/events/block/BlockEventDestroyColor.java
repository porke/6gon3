package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.graphics.ParticleEffectRenderable;

public class BlockEventDestroyColor extends BlockEventDestroyStandard
{
	private static float Duration = 1.0f;

	static
	{
		BlockEventDestroyColor.Duration = FxSettingsRepository.getInstance().getSetting("BlockEventDestroyColor.Duration");
	}

	public BlockEventDestroyColor(boolean activate, BlockRenderable block)
	{
		this(BlockEventDestroyColor.Duration, activate, block);
	}

	public BlockEventDestroyColor(float time, boolean activate, BlockRenderable block)
	{
		super(time, activate, block);
		this.block = block;

		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("graphics/particles/holocaust.ps"), Gdx.files.internal("graphics/particles/"));
		system = new ParticleEffectRenderable(effect);
	}

	@Override
	public void start()
	{
		super.start();
		system.getSystem().setPosition(block.x + BlockRenderable.Size / 2, block.y + BlockRenderable.Size / 2);
		system.getSystem().start();
	}

	@Override
	public boolean update()
	{
		if (!super.update())
		{
			system.getSystem().dispose();
			system.markToRemove(true);
		}

		return isActive;
	}

	@Override
	public void onEnded()
	{
		block.rotation = 0;
		block.scaleX = 0.0f;
		block.scaleY = 0.0f;
	}

	public ParticleEffectRenderable getParticleSystem()
	{
		return system;
	}

	private ParticleEffectRenderable system;
}
