package com.handknittedapps.honeycombmatchthree.views.menu.score;


import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.views.BaseView;

public class GlobalCampaignScores extends Table
{
	public static final int Padding = 10;

	public GlobalCampaignScores(ClickListener scoreHandler, ClickListener timeHandler, ClickListener moveHandler)
	{
		super("campaign-global-scores");
		Skin skin = Resources.getSkin();

		Table header = new Table();
		header.setBackground(skin.getPatch("GreenCase"));

		Label caption = new Label("Global highscores", skin);
		caption.setAlignment(Align.CENTER);
		header.add(caption);
		this.add(header)
			.expandX()
			.fillX();
		this.row();


		Table buttons = new Table();
		TextButton globalScore = new TextButton("Score", skin);
		globalScore.setClickListener(scoreHandler);
		buttons.add(globalScore)
			   .expandX()
			   .fillX()
			   .height(BaseView.MainButtonHeight);

		TextButton globalTime = new TextButton("Time", skin);
		globalTime.setClickListener(timeHandler);
		buttons.add(globalTime)
			   .expandX()
			   .fillX()
			   .height(BaseView.MainButtonHeight);

		TextButton globalMoves = new TextButton("Moves", skin);
		globalMoves.setClickListener(moveHandler);
		buttons.add(globalMoves)
			   .expandX()
			   .fillX()
			   .height(BaseView.MainButtonHeight);

		this.add(buttons)
			.height(BaseView.MainButtonHeight);

	}
}
