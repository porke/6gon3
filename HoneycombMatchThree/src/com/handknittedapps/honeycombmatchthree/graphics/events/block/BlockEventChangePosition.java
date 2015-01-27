package com.handknittedapps.honeycombmatchthree.graphics.events.block;


import com.badlogic.gdx.scenes.scene2d.Interpolator;
import com.badlogic.gdx.scenes.scene2d.interpolators.AccelerateDecelerateInterpolator;

import com.handknittedapps.honeycombmatchthree.data.access.FxSettingsRepository;
import com.handknittedapps.honeycombmatchthree.graphics.BlockRenderable;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class BlockEventChangePosition extends BlockEvent
{
	private static float MvtTime = 0.2f;			//Movement time per logical block in seconds
	private static float InterpolationFactor = 1.7f;

	static
	{
		BlockEventChangePosition.InterpolationFactor = FxSettingsRepository.getInstance().getSetting("BlockEventChangePosition.InterpolationFactor");
		BlockEventChangePosition.MvtTime = FxSettingsRepository.getInstance().getSetting("BlockEventChangePosition.MvtTime");
	}

	public BlockEventChangePosition(BlockRenderable block, Point src, Point dest)
	{
		super((Math.abs(dest.y - src.y) > Math.abs(dest.x - src.x))
				? BlockEventChangePosition.MvtTime * Math.abs(dest.y - src.y) / BlockRenderable.Size
				: BlockEventChangePosition.MvtTime * Math.abs(dest.x - src.x) / BlockRenderable.Size, false, block);
		this.block = block;
		this.srcPos = new Point(src.x, src.y);
		this.destPos = new Point(dest.x, dest.y);
		this.delta = new Point(dest.x - src.x, dest.y - src.y);
		this.posChangers[0] = AccelerateDecelerateInterpolator.$(BlockEventChangePosition.InterpolationFactor);
		this.posChangers[1] = AccelerateDecelerateInterpolator.$(BlockEventChangePosition.InterpolationFactor);
	}

	@Override
	public boolean update()
	{
		if (super.update())
		{
			this.block.setPosition(new Point(
					(int) (this.srcPos.x + this.delta.x * this.posChangers[0].getInterpolation(this.getTimeElapsedToTime())),
					(int) (this.srcPos.y + this.delta.y * this.posChangers[1].getInterpolation(this.getTimeElapsedToTime()))));
		}

		return this.isActive;
	}

	@Override
	public void onEnded()
	{
		this.block.setPosition(this.destPos);
	}

	private Interpolator[] posChangers = new AccelerateDecelerateInterpolator[2];
	private Point srcPos;
	private Point destPos;
	private Point delta;
}
