package com.handknittedapps.honeycombmatchthree.views.campaign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.handknittedapps.honeycombmatchthree.data.model.Mission;
import com.handknittedapps.honeycombmatchthree.graphics.Resources;
import com.handknittedapps.honeycombmatchthree.graphics.SpecialCharacters;
import com.handknittedapps.honeycombmatchthree.logic.modes.CampaignMode;
import com.handknittedapps.honeycombmatchthree.utils.AtomicReference;

public class CampaignMissionTreeTable extends Table
{
	private static int MissionBtnHeight = 64;
	public static int MinMissionBtnWidth = 375;
	private static final int MissionBtnPadding = 8;

	public CampaignMissionTreeTable(CampaignMode campaign, ClickListener missionClickEvent)
	{
		super("mission-tree");
		AtomicReference<Integer> numColumnsRef = new AtomicReference<Integer>();
		AtomicReference<Integer> numRowsRef = new AtomicReference<Integer>();
		ArrayList<MissionButtonEntry> missionButtonEntries = this.createCampaignTreeLoadDesc(numColumnsRef, numRowsRef);
		ArrayList<Mission> availableMissions = campaign.getAvailableMissions(false);
		ArrayList<Mission> completedMissions = campaign.getCompletedMissions();
		int numRows = numRowsRef.get();
		int numColumns = numColumnsRef.get();
		Skin skin = Resources.getSkin();

		for (int row = 0; row < numRows; ++row)
		{
			for (int col = 0; col < numColumns; ++col)
			{
				int objectIndex = Collections.binarySearch(missionButtonEntries, new MissionButtonEntry(row, col, -1));

				if (objectIndex >= 0)
				{
					final Mission mission = campaign.getMission(missionButtonEntries.get(objectIndex).missionId);
					String title = mission.getName();
					Button missionBtn = null;

					String style = mission.getTheme().colour.toLowerCase(Locale.US);
					style += "_campaign";
					boolean completed = false;
					boolean available = availableMissions.contains(mission);

					if (!available)
					{
						completed = completedMissions.contains(mission);
						style += (completed) ? "_border" : "";
					}

					// Create the button
					// If the mission has not been completed yet, hide the text
					missionBtn = new TextButton(completed || available ? title : "",
											skin.getStyle(style, TextButtonStyle.class), String.valueOf(mission.getId()));
					missionBtn.touchable = available || completed;

					if (completed)
					{
						String decoratedTitle = SpecialCharacters.STAR.code + title + SpecialCharacters.STAR.code;
						((TextButton) missionBtn).setText(decoratedTitle);
					}

					missionBtn.setClickListener(missionClickEvent);

					this.add(missionBtn)
					    .minWidth(CampaignMissionTreeTable.MinMissionBtnWidth)
					    .minHeight(CampaignMissionTreeTable.MissionBtnHeight)
					    .maxHeight(CampaignMissionTreeTable.MissionBtnHeight)
					    .pad(CampaignMissionTreeTable.MissionBtnPadding);
				}
				else
				{
					this.add()
						.pad(CampaignMissionTreeTable.MissionBtnPadding);
				}
			}

			this.row();
		}
	}

	private ArrayList<MissionButtonEntry> createCampaignTreeLoadDesc(AtomicReference<Integer> numColumns, AtomicReference<Integer> numRows)
	{
		////////////////////////////////////////
		// Campaign mission tree
		////////////////////////////////////////
		ArrayList<MissionButtonEntry> missionButtonEntries = new ArrayList<MissionButtonEntry>();
		try
		{
			FileHandle campaignTreeFile = Gdx.files.internal("graphics/hud/campaign_tree_format.xml");
			XmlReader read = new XmlReader();
			Element root = read.parse(campaignTreeFile);
			numColumns.set(Integer.valueOf(root.getAttribute("columns")));
			numRows.set(Integer.valueOf(root.getAttribute("rows")));

			int numChildren = root.getChildCount();
			for (int i = 0; i < numChildren; ++i)
			{
				Element child = root.getChild(i);
				int missionId = Integer.valueOf(child.getAttribute("id"));

				// The rows and columns are indexed from 0
				int row = Integer.valueOf(child.getAttribute("row")) - 1;
				int column = Integer.valueOf(child.getAttribute("col")) - 1;
				missionButtonEntries.add(new MissionButtonEntry(row, column, missionId));
			}

			Collections.sort(missionButtonEntries);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return missionButtonEntries;
	}

	private final class MissionButtonEntry implements Comparable<MissionButtonEntry>
	{
		@Override
		public int compareTo(final MissionButtonEntry cmp)
		{
			if (this.row > cmp.row)
			{
				return 1;
			}
			else if (this.row == cmp.row)
			{
				if (this.col > cmp.col)
				{
					return 1;
				}
				else if (this.col == cmp.col)
				{
					return 0;
				}
				else
				{
					return -1;
				}
			}
			else
			{
				return -1;
			}
		}

		private MissionButtonEntry(int row, int col, int missionId)
		{
			this.row = row;
			this.col = col;
			this.missionId = missionId;
		}

		private int row;
		private int col;
		private int missionId;
	}
}
