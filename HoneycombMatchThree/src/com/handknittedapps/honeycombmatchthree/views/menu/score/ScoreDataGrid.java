package com.handknittedapps.honeycombmatchthree.views.menu.score;

import com.badlogic.gdx.scenes.scene2d.ui.Align;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.handknittedapps.honeycombmatchthree.data.model.GameStats;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;

public class ScoreDataGrid extends Table
{
	public static final int Padding = 10;

	public ScoreDataGrid(GameStats score)
	{
		Skin skin = Resources.getSkin();

		// Create total time
		String totalTm = this.formatDuration(score.getTotalTimeSpent());

		Label totalTimeLbl = new Label("Time spent", skin.getStyle(LabelStyle.class));
		this.totalTime = new Label(totalTm, skin.getStyle(LabelStyle.class));
		this.totalTime.setAlignment(Align.RIGHT);
		this.add(totalTimeLbl).expandX().fillX().pad(0, ScoreDataGrid.Padding, 0, 0);
		this.add(this.totalTime).expandX().fillX().pad(0, 0, 0, ScoreDataGrid.Padding);
		this.row();

		// Create total score
		String totalSc = String.valueOf(score.getTotalScore());
		Label totalScoreLbl = new Label("Score gained", skin.getStyle(LabelStyle.class));
		this.totalScore = new Label(totalSc, skin.getStyle(LabelStyle.class));
		this.totalScore.setAlignment(Align.RIGHT);
		this.add(totalScoreLbl).expandX().fillX().pad(0, ScoreDataGrid.Padding, 0, 0);
		this.add(this.totalScore).expandX().fillX().pad(0, 0, 0, ScoreDataGrid.Padding);
		this.row();

		// Create total moves
		String totalMov = String.valueOf(score.getTotalMoves());
		Label totalMovesLbl = new Label("Moves made", skin.getStyle(LabelStyle.class));
		this.totalMoves = new Label(totalMov, skin.getStyle(LabelStyle.class));
		this.totalMoves.setAlignment(Align.RIGHT);
		this.add(totalMovesLbl).expandX().fillX().pad(0, ScoreDataGrid.Padding, 0, 0);
		this.add(this.totalMoves).expandX().fillX().pad(0, 0, 0, ScoreDataGrid.Padding);
		this.row();
	}

	private String formatDuration(int seconds)
	{
		if (seconds > 3600)
		{
			return String.format("%d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
		}
		else
		{
			return String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
		}
	}

	public void updateScoreData(GameStats score)
	{
		this.totalMoves.setText(String.valueOf(score.getTotalMoves()));
		this.totalScore.setText(String.valueOf(score.getTotalScore()));
		this.totalTime.setText(this.formatDuration(score.getTotalTimeSpent()));
	}

	private Label totalScore;
	private Label totalMoves;
	public Label totalTime;
}
