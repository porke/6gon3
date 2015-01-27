package com.handknittedapps.honeycombmatchthree.logic.graph;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import com.handknittedapps.honeycombmatchthree.logic.graph.Graph;
import com.handknittedapps.honeycombmatchthree.logic.graph.GraphVertex;
import com.handknittedapps.honeycombmatchthree.utils.AtomicReference;


public class GraphTests 
{
	private static Graph<Integer> graph = new Graph<Integer>();
	
	@BeforeClass
	public static void initialize()
	{
		final int graphSideSize = 4;
		final int graphSize = graphSideSize * graphSideSize;
		for (int i = 0; i < graphSize; ++i)
		{
			GraphTests.graph.addVertex(i);
		}
		
		for (int i = 0; i < graphSize; ++i)
		{
			GraphVertex<Integer> vertex = GraphTests.graph.getVertex(i);
			
			// Top row
			if (i / graphSideSize == 0)
			{
				// First column
				if (i % graphSideSize == 0)
				{
					vertex.addEdge(1, GraphTests.graph.getVertex(i + 1));
					vertex.addEdge(2, GraphTests.graph.getVertex(i + graphSideSize));
				}
				// Last column
				else if (i % graphSideSize == graphSize - 1)
				{					
					vertex.addEdge(2, GraphTests.graph.getVertex(i + graphSideSize));
					vertex.addEdge(3, GraphTests.graph.getVertex(i - 1));
				}
				// Middle columns
				else
				{
					vertex.addEdge(1, GraphTests.graph.getVertex(i + 1));
					vertex.addEdge(2, GraphTests.graph.getVertex(i + graphSideSize));
					vertex.addEdge(3, GraphTests.graph.getVertex(i - 1));
				}
			}
			// Middle rows
			else if (i / graphSideSize != graphSideSize - 1)
			{
				// First column
				if (i % graphSideSize == 0)
				{
					vertex.addEdge(0, GraphTests.graph.getVertex(i - graphSideSize));
					vertex.addEdge(1, GraphTests.graph.getVertex(i + 1));
					vertex.addEdge(2, GraphTests.graph.getVertex(i + graphSideSize));
				}
				// Last column
				else if (i % graphSideSize == graphSideSize - 1)
				{					
					vertex.addEdge(0, GraphTests.graph.getVertex(i - graphSideSize));
					vertex.addEdge(2, GraphTests.graph.getVertex(i + graphSideSize));
					vertex.addEdge(3, GraphTests.graph.getVertex(i - 1));
				}
				// Middle columns
				else
				{
					vertex.addEdge(0, GraphTests.graph.getVertex(i - graphSideSize));
					vertex.addEdge(1, GraphTests.graph.getVertex(i + 1));
					vertex.addEdge(2, GraphTests.graph.getVertex(i + graphSideSize));
					vertex.addEdge(3, GraphTests.graph.getVertex(i - 1));
				}
			}
			// Last row
			else
			{
				// First column
				if (i % graphSideSize == 0)
				{
					vertex.addEdge(0, GraphTests.graph.getVertex(i - graphSideSize));
					vertex.addEdge(1, GraphTests.graph.getVertex(i + 1));
				}
				// Last column
				else if (i % graphSideSize == graphSideSize - 1)
				{					
					vertex.addEdge(0, GraphTests.graph.getVertex(i - graphSideSize));
					vertex.addEdge(3, GraphTests.graph.getVertex(i - 1));
				}
				// Middle columns
				else
				{
					vertex.addEdge(0, GraphTests.graph.getVertex(i - graphSideSize));
					vertex.addEdge(1, GraphTests.graph.getVertex(i + 1));
					vertex.addEdge(3, GraphTests.graph.getVertex(i - 1));
				}
			}
		}
	}
	
	@Test
	public void moveToEndMiddleInputTest()
	{
		// In
		GraphVertex<Integer> start = GraphTests.graph.getVertex(5);
		int direction = 1;
		
		// Out
		AtomicReference<GraphVertex<Integer>> end = new AtomicReference<GraphVertex<Integer>>();
		int pathLen = Graph.moveToTheEnd(start, direction, end);
		Assert.assertEquals("Invalid path length!", 2, pathLen);
		Assert.assertEquals("Invalid final vertex!", 7, (int)end.get().getData());
	}
	
	@Test
	public void moveToEndBrinkInputTest()
	{
		// In
		GraphVertex<Integer> start = GraphTests.graph.getVertex(13);
		int direction = 2;
		
		// Out
		AtomicReference<GraphVertex<Integer>> end = new AtomicReference<GraphVertex<Integer>>();
		int pathLen = Graph.moveToTheEnd(start, direction, end);
		Assert.assertEquals("Invalid path length!", 0, pathLen);
		Assert.assertEquals("Invalid final vertex!", 13, (int)end.get().getData());
	}
	
	@Test
	public void moveToEndNullDataMiddleInputTest()
	{
		// In
		GraphVertex<Integer> start = GraphTests.graph.getVertex(11);
		int direction = 3;
		
		// Set a vertex to null
		GraphTests.graph.getVertex(8).setData(null);
		
		// Out
		AtomicReference<GraphVertex<Integer>> end = new AtomicReference<GraphVertex<Integer>>();
		int pathLen = Graph.moveToTheEndOfNonEmptyVertices(start, direction, end);
		Assert.assertEquals("Invalid path length!", 2, pathLen);
		Assert.assertEquals("Invalid final vertex!", 9, (int)end.get().getData());
		
		// Restore the vertex
		GraphTests.graph.getVertex(8).setData(8);
	}
}
