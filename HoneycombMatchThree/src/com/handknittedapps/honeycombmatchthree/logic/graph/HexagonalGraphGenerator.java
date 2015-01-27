package com.handknittedapps.honeycombmatchthree.logic.graph;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class HexagonalGraphGenerator implements GraphGenerator<Block>
{
	private static final int NumDirections = 6;

	@Override
	public void generateGraph(Graph<Block> graph, int sideSize)
	{
		this.gridSize = sideSize;
		this.graph = graph;

		int numTiles = this.gridSize * this.gridSize;
		for (int i = 0; i < numTiles; ++i)
		{
			GraphVertex<Block> curr = this.graph.getVertex(i);
			Point blockPos = curr.getData().getPosition();

			//First row
			if (blockPos.y == 0)
			{
				//Bottom
				if (blockPos.x == 0)
				{
					this.addLink(i, 0);
					this.addLink(i, 1);
				}
				//Top
				else if (blockPos.x == this.gridSize - 1)
				{
					this.addLink(i, 0);
					this.addLink(i, 4);
					this.addLink(i, 5);
				}
				//The rest apart from the top and bottom elements
				else
				{
					if (blockPos.x % 2 == 1)
					{
						this.addLink(i, 2);
						this.addLink(i, 4);
					}
					this.addLink(i, 0);
					this.addLink(i, 1);
					this.addLink(i, 5);
				}
			}
			// Top row
			else if (blockPos.y == this.gridSize - 1)
			{
				//First element
				if (blockPos.x == 0)
				{
					this.addLink(i, 1);
					this.addLink(i, 2);
					this.addLink(i, 3);
				}
				//Last element
				else if (blockPos.x == this.gridSize - 1)
				{
					if (blockPos.x % 2 == 0)
					{
						this.addLink(i, 5);
					}

					this.addLink(i, 3);
					this.addLink(i, 4);
				}
				//The rest apart from the first and last element
				else
				{
					if (blockPos.x % 2 == 0)
					{
						this.addLink(i, 1);
						this.addLink(i, 5);
					}
					this.addLink(i, 2);
					this.addLink(i, 3);
					this.addLink(i, 4);
				}
			}
			//The middle elements
			else
			{
				//First column
				if (blockPos.x == 0)
				{
					this.addLink(i, 0);
					this.addLink(i, 1);
					this.addLink(i, 2);
					this.addLink(i, 3);
				}
				//Last column
				else if (blockPos.x == this.gridSize - 1)
				{
					this.addLink(i, 0);
					this.addLink(i, 3);
					this.addLink(i, 4);
					this.addLink(i, 5);
				}
				//All other fields
				else
				{
					for (int d = 0; d < HexagonalGraphGenerator.NumDirections; ++d)
					{
						this.addLink(i, d);
					}
				}
			}
		}
	}

	private void addLink(int currIdx, int direction)
	{
		GraphVertex<Block> toAdd = null;
		GraphVertex<Block> curr = this.graph.getVertex(currIdx);

		if (direction == 0)
		{
			toAdd = this.graph.getVertex(currIdx + this.gridSize);
		}
		else if (direction == 3)
		{
			toAdd = this.graph.getVertex(currIdx - this.gridSize);
		}

		if (curr.getData().getPosition().x % 2 == 0)
		{
			switch(direction)
			{
			case 1:
				toAdd = this.graph.getVertex(currIdx + 1);
				break;
			case 2:
				toAdd = this.graph.getVertex(currIdx - this.gridSize + 1);
				break;
			case 4:
				toAdd = this.graph.getVertex(currIdx - this.gridSize - 1);
				break;
			case 5:
				toAdd = this.graph.getVertex(currIdx - 1);
				break;
			}
		}
		else
		{
			switch(direction)
			{
				case 1:
					toAdd = this.graph.getVertex(currIdx + this.gridSize + 1);
					break;
				case 2:
					toAdd = this.graph.getVertex(currIdx + 1);
					break;
				case 4:
					toAdd = this.graph.getVertex(currIdx - 1);
					break;
				case 5:
					toAdd = this.graph.getVertex(currIdx + this.gridSize - 1);
					break;
			}
		}

		curr.addEdge(direction, toAdd);
	}

	private Graph<Block> graph;
	private int gridSize;
}
