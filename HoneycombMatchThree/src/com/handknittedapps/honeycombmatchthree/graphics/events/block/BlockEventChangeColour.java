package com.handknittedapps.honeycombmatchthree.graphics.events.block;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateDecelerateInterpolator;

import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.logic.BlockColour;
import com.handknittedapps.honeycombmatchthree.logic.BlockType;

public class BlockEventChangeColour extends BlockEvent
{
	private static float InterpolationFactor = 1.5f;

	static
	{
		BlockEventChangeColour.InterpolationFactor = FxSettingsRepository.getInstance().getSetting("BlockEventChangeColour.InterpolationFactor");
	}

	public BlockEventChangeColour(float time, BlockRenderable block, BlockColour colour, BlockType type)
	{
		super(time, false, block);
		this.interpolator = AccelerateDecelerateInterpolator.$(BlockEventChangeColour.InterpolationFactor);
		block.color.a = 0.0f;
		this.switchColor = new Color(block.color);
		block.setColour(colour.value, type.ordinal());
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			this.block.color.a = this.interpolator.getInterpolation(this.getTimeElapsedToTime());
			this.switchColor.a = 1.0f - this.block.color.a;
			this.block.setSwitchColor(this.switchColor);
		}
		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.block.setSwitchTexture(null);
		this.block.color.a = 1.0f;
		this.block.setSwitchColor(new Color(1.0f, 1.0f, 1.0f, 0.0f));
	}

	private Color switchColor;
	private Interpolator interpolator;}
