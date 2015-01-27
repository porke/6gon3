package com.handknittedapps.honeycombmatchthree.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.input.UIInputProcessor;
import com.handknittedapps.honeycombmatchthree.utils.ViewportUtils;
import com.handknittedapps.honeycombmatchthree.views.BaseView;

public abstract class IGameState
{
	protected IGameState()
	{
		this.stage = new Stage(HoneycombMatchThree.NominalWidth, HoneycombMatchThree.NominalHeight, false);
	}

	public final void onEnterBase()
	{
		Rectangle properViewport = this.setupViewport();
		Gdx.input.setInputProcessor(new UIInputProcessor(this.stage, properViewport));

		this.onEnter();
	}

	public final void onExitBase()
	{
		if (!this.isDisposed)
		{
			this.onExit();
			this.stage.dispose();
			this.isDisposed = true;
		}
	}

	/** Calculates and sets the viewport to preserve the aspect ratio.
	 * @return The viewport rectangle. */
	final Rectangle setupViewport()
	{
		// Setup the scaling and cropping positioning
        // calculate new viewport
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(false, HoneycombMatchThree.NominalWidth, HoneycombMatchThree.NominalHeight);

		Vector2 displaySize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Vector2 outSize = new Vector2();
		Vector2 outCrop = new Vector2();
		ViewportUtils.calculateViewport(displaySize, HoneycombMatchThree.NominalAspectRatio, outSize, outCrop);

		Rectangle viewport = new Rectangle(outCrop.x, outCrop.y, outSize.x, outSize.y);
		Gdx.gl.glViewport((int) outCrop.x, (int) outCrop.y, (int) outSize.x, (int) outSize.y);
		camera.update();
		camera.apply((GL10) Gdx.gl);

		this.stage.setCamera(camera);

		return viewport;
	}

	public abstract void onEnter();
	public abstract void onExit();
	public abstract void onPause();
	public abstract void onResume();
	public abstract boolean onFrame();
	public abstract void onBackPressed();

	public boolean isDisposed() {return this.isDisposed; }
	public IGameState getNextState() { return this.nextState; }

	protected Stage stage;
	protected BaseView activeView;
	protected boolean isActive = true;
	protected IGameState nextState;

	private boolean isDisposed = false;
}
