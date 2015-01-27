package com.handknittedapps.honeycombmatchthree.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.handknittedapps.honeycombmatchthree.utils.Point;
import com.handknittedapps.honeycombmatchthree.utils.ViewportUtils;

public class UIInputProcessor extends InputAdapter
{
	public UIInputProcessor(Stage stage, Rectangle viewport)
	{
		this.stage = stage;
		this.actualViewport = viewport;
		this.physicalViewport = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public boolean touchDown(int x, int y, int ptr, int button)
	{
		Point pt = this.unproject(x, y);
		return this.stage.touchDown(pt.x, pt.y, ptr, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int ptr)
	{
		Point pt = this.unproject(x, y);
		return this.stage.touchDragged(pt.x, pt.y, ptr);
	}

	@Override
	public boolean touchMoved(int x, int y)
	{
		Point pt = this.unproject(x, y);
		return this.stage.touchMoved(pt.x, pt.y);
	}

	@Override
	public boolean touchUp(int x, int y, int btn, int ptr)
	{
		Point pt = this.unproject(x, y);
		return this.stage.touchUp(pt.x, pt.y, ptr, btn);
	}

	private Point unproject(int x, int y)
	{
		Vector2 ret = ViewportUtils.transformToViewport(this.physicalViewport, this.actualViewport, new Vector2(x, y));
		return new Point((int) ret.x, (int) ret.y);
	}

	private Stage stage;
	private Rectangle actualViewport;
	private Rectangle physicalViewport;
}
