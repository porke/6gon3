package com.handknittedapps.honeycombmatchthree.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.handknittedapps.honeycombmatchthree.utils.Point;
import com.handknittedapps.honeycombmatchthree.utils.ViewportUtils;

public class GamestateInputProcessor implements InputProcessor
{
	public GamestateInputProcessor(MouseInputHandler mouseHandler, KeyboardInputHandler keyHandler, Rectangle actualViewport)
	{
		this.mouseHandler = mouseHandler;
		this.keyboardHandler = keyHandler;
		this.actualViewport = actualViewport;
		this.physicalViewport = new Rectangle(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public GamestateInputProcessor(MouseInputHandler handler, Rectangle actualViewport)
	{
		this(handler, new NullKeyboardInputProcessor(), actualViewport);
	}

	public GamestateInputProcessor(KeyboardInputHandler handler, Rectangle actualViewport)
	{
		this(new NullMouseInputProcessor(), handler, actualViewport);
	}

	@Override
	public boolean keyDown(int key)
	{
		this.keyboardHandler.onKeyDown(key);
		return true;
	}

	@Override
	public boolean keyTyped(char key)
	{
		return this.keyboardHandler.onKeyTyped(key);
	}

	@Override
	public boolean keyUp(int key)
	{
		this.keyboardHandler.onKeyUp(key);
		return true;
	}

	@Override
	public boolean scrolled(int amount)
	{
		return false;
	}

	@Override
	// 0 = left button
	// 1 = right button
	// (only PC)
	public boolean touchDown(int x, int y, int arg2, int button)
	{
		this.touchDown = this.unproject(x, y);
		this.mouseHandler.onMouseDown(this.touchDown, (button == 0));
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int ptr)
	{
		// It is possible to use it, however the drag event is emulated
		// by using touchUp and touchDown
		// this.mouseHandler.onDrag(this.touchDown, new Point(x, y));
		return false;
	}

	@Override
	public boolean touchMoved(int x, int y)
	{
		this.mouseHandler.onMouseMove(this.unproject(x, y));
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int btn, int ptr)
	{
		Point unprojectedPoint = this.unproject(x, y);
		this.mouseHandler.onDrag(this.touchDown, unprojectedPoint);
		this.touchDown = null;
		this.mouseHandler.onMouseUp(unprojectedPoint, true);

		return false;
	}

	public void setActualViewport(Rectangle viewport)
	{
		this.actualViewport = viewport;
	}

	private Point unproject(int x, int y)
	{
		Vector2 ret = ViewportUtils.transformToViewport(this.physicalViewport, this.actualViewport, new Vector2(x, y));
		return new Point((int) ret.x, (int) ret.y);
	}

	private Point touchDown;
	private MouseInputHandler mouseHandler;
	private KeyboardInputHandler keyboardHandler;

	private Rectangle actualViewport;
	private Rectangle physicalViewport;
}
