package com.handknittedapps.honeycombmatchthree.views;

import com.badlogic.gdx.scenes.scene2d.ui.FlickScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class VerticalScrollView extends Table
{
	public VerticalScrollView(Table scrollableTable)
	{
		this.scrollPane = new FlickScrollPane(scrollableTable);
		this.scrollPane.setOverscroll(false);
		this.scrollPane.invalidate();

		// First add the help pane
		this.add(this.scrollPane)
			.expand()
			.fill();
		this.row();

		// Then add the slider
		this.horzSlider = new Slider(0, 1, 1, Resources.getSkin());
		this.horzSlider.touchable = false;
		this.add(this.horzSlider)
			.expandX()
			.fillX();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.horzSlider.setValue(this.scrollPane.getScrollPercentY());
	}

	private Slider horzSlider;
	private FlickScrollPane scrollPane;
}
