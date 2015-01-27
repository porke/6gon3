package com.handknittedapps.honeycombmatchthree.views.campaign;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.handknittedapps.honeycombmatchthree.HoneycombMatchThree;
import com.handknittedapps.honeycombmatchthree.control.CampaignState;
import com.handknittedapps.honeycombmatchthree.control.IGameState;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.hud.HudTurnBasedParticleEffect;
import com.handknittedapps.honeycombmatchthree.utils.Point;
import com.handknittedapps.honeycombmatchthree.views.BackgroundView;

public class EndCampaignView extends BackgroundView
{
	private static final String EndgameMessage = "Congratulations, you've won the game! " +
			"However, this was the easy part. " +
			"There are also infinite modes, that you might want to try.";

	public EndCampaignView(IGameState controller)
	{
		super(controller, "End campaign", Resources.getBackground());

		Label lblGratz = new Label(EndCampaignView.EndgameMessage, this.skin.getStyle(LabelStyle.class));
		lblGratz.setAlignment(Align.CENTER);
		lblGratz.setWrap(true);
		this.add(lblGratz).expand().fill();
		this.row();

		// Create the particle system
		this.particleEffect = new HudTurnBasedParticleEffect("endgame", 1, new Point(100, 100));
		this.addActor(this.particleEffect);

		if (HoneycombMatchThree.IsDesktopVersion)
		{
			Button btnReturn = new ImageButton(this.skin.getStyle("return-button", ImageButtonStyle.class));
			btnReturn.setClickListener(this.backClick);

			this.add(btnReturn)
				.bottom()
				.left();
		}
	}

	@Override
	public void onBackPressed()
	{
		this.particleEffect.dispose();
		this.particleEffect = null;

		((CampaignState) this.controller).showCampaignView();
	}

	@Override
	public void act(float delta)
	{
		super.act(delta);
		this.particleEffect.act(delta);
	}

	private ClickListener backClick = new ClickListener()
	{
		@Override
		public void click(Actor actor, float x, float y)
		{
			EndCampaignView.this.onBackPressed();
		}
	};

	private HudTurnBasedParticleEffect particleEffect;
}
