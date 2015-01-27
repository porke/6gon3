package com.handknittedapps.honeycombmatchthree.views.play;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.PlayState;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.views.BaseView;

public class EndgameView extends BaseView
{
	public static final String ViewId = "endgame";

	public EndgameView(PlayState controller)
	{
		super(controller, EndgameView.ViewId);
		this.controller = controller;
		Skin skin = Resources.getSkin();

		this.width = HoneycombMatchThree.NominalWidth;
		this.height = HoneycombMatchThree.NominalHeight;
		this.setBackground(skin.getPatch("shadow"));
		this.top().left();

		Image img = new Image(Resources.getEndgameOverlay());
		img.y = (HoneycombMatchThree.NominalHeight - Resources.getEndgameOverlay().getHeight()) / 2 - 12;
		this.addActor(img);

		this.messageLabel = new Label("", skin);
		this.messageLabel.setAlignment(Align.CENTER, Align.CENTER);
		this.add(this.messageLabel)
			.expand()
			.fill()
			.center();
	}

	public void setResult(boolean win)
	{
		String text = win ? "Congratulations, you win!" : "You lose!";
		this.messageLabel.setText(text);
	}

	@Override
	public boolean touchDown(float x, float y, int ptr)
	{
		this.onBackPressed();
		return true;
	}

	@Override
	public void onBackPressed()
	{
		this.controller.onBackPressed();
	}

	private Label messageLabel;
}
