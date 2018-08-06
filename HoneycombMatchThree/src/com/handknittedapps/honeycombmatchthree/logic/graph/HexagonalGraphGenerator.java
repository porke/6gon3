package com.handknittedapps.honeycombmatchthree.logic.graph;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class HexagonalGraphGenerator implements GraphGenerator<Block>
{
	private static final int NumDirections = 6;

	@Override
	public void generateGraph(Graph<Block> graph, int sideSize)
	{
		
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
