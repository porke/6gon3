package com.handknittedapps.honeycombmatchthree.logic.graph;

import com.handknittedapps.honeycombmatchthree.logic.Block;
import com.handknittedapps.honeycombmatchthree.utils.Point;

public class QuadGraphGenerator implements GraphGenerator<Block>
{
	@Override
	public void generateGraph(Graph<Block> graph, int sideSize)
	{
		int numTiles = sideSize * sideSize;
		for (int i = 0; i < numTiles; ++i)
		{
			GraphVertex<Block> vertex = graph.getVertex(i);
			Point blockPos = vertex.getData().getPosition();

			// Top row
			if (blockPos.y == 0)
			{
				// First column
				if (blockPos.x == 0)
				{
					vertex.addEdge(1, graph.getVertex(i + 1));
					vertex.addEdge(2, graph.getVertex(i + sideSize));
				}
				// Last column
				else if (blockPos.x == sideSize - 1)
				{
					vertex.addEdge(2, graph.getVertex(i + sideSize));
					vertex.addEdge(3, graph.getVertex(i - 1));
				}
				// Middle columns
				else
				{
					vertex.addEdge(1, graph.getVertex(i + 1));
					vertex.addEdge(2, graph.getVertex(i + sideSize));
					vertex.addEdge(3, graph.getVertex(i - 1));
				}
			}
			// Middle rows
			else if (blockPos.y != sideSize - 1)
			{
				// First column
				if (blockPos.x == 0)
				{
					vertex.addEdge(0, graph.getVertex(i - sideSize));
					vertex.addEdge(1, graph.getVertex(i + 1));
					vertex.addEdge(2, graph.getVertex(i + sideSize));
				}
				// Last column
				else if (blockPos.x == sideSize - 1)
				{
					vertex.addEdge(0, graph.getVertex(i - sideSize));
					vertex.addEdge(2, graph.getVertex(i + sideSize));
					vertex.addEdge(3, graph.getVertex(i - 1));
				}
				// Middle columns
				else
				{
					vertex.addEdge(0, graph.getVertex(i - sideSize));
					vertex.addEdge(1, graph.getVertex(i + 1));
					vertex.addEdge(2, graph.getVertex(i + sideSize));
					vertex.addEdge(3, graph.getVertex(i - 1));
				}
			}
			// Last row
			else
			{
				// First column
				if (blockPos.x == 0)
				{
					vertex.addEdge(0, graph.getVertex(i - sideSize));
					vertex.addEdge(1, graph.getVertex(i + 1));
				}
				// Last column
				else if (blockPos.x == sideSize - 1)
				{
					vertex.addEdge(0, graph.getVertex(i - sideSize));
					vertex.addEdge(3, graph.getVertex(i - 1));
				}
				// Middle columns
				else
				{
					vertex.addEdge(0, graph.getVertex(i - sideSize));
					vertex.addEdge(1, graph.getVertex(i + 1));
					vertex.addEdge(3, graph.getVertex(i - 1));
				}
			}
		}
	}
}
