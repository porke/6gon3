package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.graphics.ParticleEffectRenderable;

public class BlockEventChangeStrength extends BlockEvent
{
	private static float Duration = 0.25f;

	static
	{
		BlockEventChangeStrength.Duration = FxSettingsRepository.getInstance().getSetting("BlockEventChangeStrength.Duration");
	}

	public BlockEventChangeStrength(BlockRenderable block, int strength)
	{
		super(BlockEventChangeStrength.Duration, false, block);
		this.block = block;

		block.setStrength(strength);

		ParticleEffect effect = new ParticleEffect();
		effect.load(Gdx.files.internal("graphics/particles/strong_break.ps"), Gdx.files.internal("graphics/particles/"));
		this.system = new ParticleEffectRenderable(effect);
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
			this.system.getSystem().dispose();
			this.system.markToRemove(true);
		}

		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.block.setSwitchTexture(null);
		this.block.color.a = 1.0f;
	}

	public ParticleEffectRenderable getParticleSystem()
	{
		return this.system;
	}

	private ParticleEffectRenderable system;
}
