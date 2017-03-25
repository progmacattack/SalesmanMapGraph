package roadgraph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import geography.GeographicPoint;
import util.GraphLoader;

public class MapGraphTester {

	MapGraph mapGraph;
	
	@Before
	public void setup() {
		mapGraph = new MapGraph();
	}
	
	@Test
	public void addVertex() {
		GeographicPoint location1 = new GeographicPoint(1.5, 1.2);
		GeographicPoint location2 = new GeographicPoint(1.1, 3.1);
		assertTrue(mapGraph.addVertex(location1));
		assertTrue(mapGraph.addVertex(location2));
		assertEquals("Should have two vertices", 2, mapGraph.getNumVertices());
		
		assertFalse("Null location returns false on add attempt", mapGraph.addVertex(null));
		GeographicPoint location1Duplicate = new GeographicPoint(1.5, 1.2);
		assertFalse("Existing location returns false on add attempt", mapGraph.addVertex(location1Duplicate));
	}
	
	@Test
	public void getVertices() {
		GeographicPoint location1 = new GeographicPoint(1.5, 1.2);
		mapGraph.addVertex(location1);
		GeographicPoint location2 = new GeographicPoint(1.7, 0.2);
		mapGraph.addVertex(location2);
		assertTrue("Location 1 should be in set", mapGraph.getVertices().contains(location1));
		assertTrue("Location 2 should be in set", mapGraph.getVertices().contains(location2));
	}
	
	@Test
	public void getNumVertices() {
		GeographicPoint location1 = new GeographicPoint(1.5, 1.2);
		mapGraph.addVertex(location1);
		assertEquals("Should have one vertex", 1, mapGraph.getNumVertices());
		
		MapGraph mp2 = new MapGraph();
		assertEquals("Should have zero vertex", 0, mp2.getNumVertices());
	}
	
	@Test
	public void addEdge() {
		GeographicPoint location1 = new GeographicPoint(1.5, 1.2);
		GeographicPoint location2 = new GeographicPoint(1.1, 3.1);
		try {
			mapGraph.addEdge(location1, location2, "Jahns Road", "Road", 5);
		} catch (IllegalArgumentException iae) {
			assertNotNull("Should have exception if locations not added to mapgraph", iae);
		}
		
		try {
			mapGraph.addEdge(location1, location2, "Jahns Road", "Road", -0.1);
		} catch (IllegalArgumentException iae) {
			assertNotNull("Should have exception if length is < 0", iae);
		}
		
		mapGraph.addVertex(location1);
		mapGraph.addVertex(location2);
		mapGraph.addEdge(location1, location2, "Jahns Road", "Road", 5);
	}
	
	@Test
	public void getNumEdges() {
		GeographicPoint location1 = new GeographicPoint(1.5, 1.2);
		GeographicPoint location2 = new GeographicPoint(1.1, 3.1);
		mapGraph.addVertex(location1);
		mapGraph.addVertex(location2);
		
		assertEquals("Should have zero edges", 0, mapGraph.getNumEdges());
		
		mapGraph.addEdge(location1, location2, "Jahns Road", "Road", 5);
		assertEquals("Should have one edge", 1, mapGraph.getNumEdges());
		
		mapGraph.addEdge(location2, location1, "Jahns Road", "Road", 5);
		assertEquals("Should have two edges", 2, mapGraph.getNumEdges());
	}
	
	@Test
	public void testBFS() {
		System.out.println("Creating map...");
		MapGraph map = new MapGraph();
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/pleasantridgetestdata.map", map);
		System.out.println("Done loading. Map info: ");
		
		assertEquals("Expecting 7 vertices", 7, map.getNumVertices());
		assertEquals("Expecting 14 vertices", 14, map.getNumEdges());
		GeographicPoint start = new GeographicPoint(1.0, -1.0);
		GeographicPoint goal = new GeographicPoint(3.0, 1.0);
		List<GeographicPoint> path = map.bfs(start, goal);
		System.out.println("Expecting path (1,-1) -> (2,0) -> (3,-1) -> (3,1)");
		System.out.println("Actual BFS path: " + path);
		Assert.assertEquals("First lat should be 1", 1.0, path.get(0).getX(), 0.1);
		Assert.assertEquals("First lon should be -1", -1.0, path.get(0).getY(), 0.1);
		Assert.assertEquals("Second lat should be 2", 2.0, path.get(1).getX(), 0.1);
		Assert.assertEquals("Second lon should be 0", 0.0, path.get(1).getY(), 0.1);
		Assert.assertEquals("Third lat should be 3", 3.0, path.get(2).getX(), 0.1);
		Assert.assertEquals("Third lon should be -1", -1.0, path.get(2).getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 3", 3.0, path.get(3).getX(), 0.1);
		Assert.assertEquals("Fourth lon should be 1", 1.0, path.get(3).getY(), 0.1);
		
		try {
			map.bfs(start, new GeographicPoint(4, 3));
		} catch (IllegalArgumentException iae) {
			assertNotNull("Second argument is not in mapgraph", iae);
		}
	}
	
	@Test
	public void testDijkstra() {
		System.out.println("Creating map...");
		MapGraph map = new MapGraph();
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/pleasantridgetestdata.map", map);
		System.out.println("Done loading. Map info: ");
		
		assertEquals("Expecting 7 vertices", 7, map.getNumVertices());
		assertEquals("Expecting 14 vertices", 14, map.getNumEdges());
		GeographicPoint start = new GeographicPoint(1.0, -1.0);
		GeographicPoint goal = new GeographicPoint(3.0, 1.0);
		List<GeographicPoint> path = map.dijkstra(start, goal);
		System.out.println("Expecting path (1,-1) -> (2,0) -> (3,-1) -> (3,1)");
		System.out.println("Actual Dijkstra path: " + path);
		Assert.assertEquals("First lat should be 1", 1.0, path.get(0).getX(), 0.1);
		Assert.assertEquals("First lon should be -1", -1.0, path.get(0).getY(), 0.1);
		Assert.assertEquals("Second lat should be 2", 2.0, path.get(1).getX(), 0.1);
		Assert.assertEquals("Second lon should be 0", 0.0, path.get(1).getY(), 0.1);
		Assert.assertEquals("Third lat should be 3", 3.0, path.get(2).getX(), 0.1);
		Assert.assertEquals("Third lon should be -1", -1.0, path.get(2).getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 3", 3.0, path.get(3).getX(), 0.1);
		Assert.assertEquals("Fourth lon should be 1", 1.0, path.get(3).getY(), 0.1);
		
		try {
			map.dijkstra(start, new GeographicPoint(4, 3));
		} catch (IllegalArgumentException iae) {
			assertNotNull("Second argument is not in mapgraph", iae);
		}
	}
	
	@Test
	public void testDijkstra2() {
		System.out.println("Creating map...");
		MapGraph map = new MapGraph();
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", map);
		System.out.println("Done loading. Map info: ");
		
		GeographicPoint start = new GeographicPoint(1.0, 1.0);
		GeographicPoint goal = new GeographicPoint(8.0, -1.0);
		List<GeographicPoint> path = map.dijkstra(start, goal);
		System.out.println("Expecting path (1,1), (4,1), (5,1), (6.5,0), and (8,-1)");
		System.out.println("Actual Dijkstra path: " + path);
		Assert.assertEquals("First lat should be 1", 1.0, path.get(0).getX(), 0.1);
		Assert.assertEquals("First lon should be 1", 1.0, path.get(0).getY(), 0.1);
		Assert.assertEquals("Second lat should be 4", 4.0, path.get(1).getX(), 0.1);
		Assert.assertEquals("Second lon should be 1", 1.0, path.get(1).getY(), 0.1);
		Assert.assertEquals("Third lat should be 5", 5.0, path.get(2).getX(), 0.1);
		Assert.assertEquals("Third lon should be 1", 1.0, path.get(2).getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 6.5", 6.5, path.get(3).getX(), 0.1);
		Assert.assertEquals("Fourth lon should be 0", 0.0, path.get(3).getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 8.0", 8.0, path.get(4).getX(), 0.1);
		Assert.assertEquals("Fourth lon should be -1.0", -1.0, path.get(4).getY(), 0.1);
		
		try {
			map.dijkstra(start, new GeographicPoint(4, 3));
		} catch (IllegalArgumentException iae) {
			assertNotNull("Second argument is not in mapgraph", iae);
		}
	}
	
	@Test
	public void dijkstraVisited() {
		System.out.println("Creating map...");
		MapGraph map = new MapGraph();
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", map);
		System.out.println("Done loading. Map info: ");
		
		GeographicPoint start = new GeographicPoint(1.0, 1.0);
		GeographicPoint goal = new GeographicPoint(8.0, -1.0);
		map.dijkstra(start, goal);
		List<MapNode> visited = map.getVisited();
		System.out.println("Expecting dijkstra visited (1,1), (4,1), (4,2), (4,0), (5,1), (4,-1), (6.5,0), (7,3), and (8,-1)");
		String visitedStr = "";
		for(MapNode node: visited) {
			visitedStr += "(" + node.getLocation().getX() + ", " + node.getLocation().getY() + "), ";
		}
		System.out.println("Actual dijkstra visited: " + visitedStr);
		
		Assert.assertEquals("First lat should be 1", 1.0, visited.get(0).getLocation().getX(), 0.1);
		Assert.assertEquals("First lon should be 1", 1.0, visited.get(0).getLocation().getY(), 0.1);
		Assert.assertEquals("Second lat should be 4", 4.0, visited.get(1).getLocation().getX(), 0.1);
		Assert.assertEquals("Second lon should be 1", 1.0, visited.get(1).getLocation().getY(), 0.1);
		Assert.assertEquals("Third lat should be 4", 4.0, visited.get(2).getLocation().getX(), 0.1);
		Assert.assertEquals("Third lon should be 2", 2.0, visited.get(2).getLocation().getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 4", 4.0, visited.get(3).getLocation().getX(), 0.1);
		Assert.assertEquals("Fourth lon should be 0", 0.0, visited.get(3).getLocation().getY(), 0.1);
		Assert.assertEquals("Fifth lat should be 5", 5.0, visited.get(4).getLocation().getX(), 0.1);
		Assert.assertEquals("Fifth lon should be 1", 1.0, visited.get(4).getLocation().getY(), 0.1);
		Assert.assertEquals("Sixth lat should be 4", 4.0, visited.get(5).getLocation().getX(), 0.1);
		Assert.assertEquals("Sixth lon should be -1", -1.0, visited.get(5).getLocation().getY(), 0.1);
		Assert.assertEquals("Seventh lat should be 6.5", 6.5, visited.get(6).getLocation().getX(), 0.1);
		Assert.assertEquals("Seventh lon should be 0", 0.0, visited.get(6).getLocation().getY(), 0.1);
		Assert.assertEquals("Eighth lat should be 7", 7.0, visited.get(7).getLocation().getX(), 0.1);
		Assert.assertEquals("Eighth lon should be 3", 3.0, visited.get(7).getLocation().getY(), 0.1);
		Assert.assertEquals("Ninth lat should be 8.0", 8.0, visited.get(8).getLocation().getX(), 0.1);
		Assert.assertEquals("Ninth lon should be -1.0", -1.0, visited.get(8).getLocation().getY(), 0.1);
		
		try {
			map.dijkstra(start, new GeographicPoint(4, 3));
		} catch (IllegalArgumentException iae) {
			assertNotNull("Second argument is not in mapgraph", iae);
		}
	}
	
	@Test
	public void testAStarPath() {
		System.out.println("Creating map...");
		MapGraph map = new MapGraph();
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", map);
		System.out.println("Done loading. Map info: ");
		
		GeographicPoint start = new GeographicPoint(1.0, 1.0);
		GeographicPoint goal = new GeographicPoint(8.0, -1.0);
		List<GeographicPoint> path = map.aStarSearch(start, goal);
		System.out.println("Expecting path (1,1), (4,1), (5,1), (6.5,0), and (8,-1)");
		System.out.println("Actual Dijkstra path: " + path);
		Assert.assertEquals("First lat should be 1", 1.0, path.get(0).getX(), 0.1);
		Assert.assertEquals("First lon should be 1", 1.0, path.get(0).getY(), 0.1);
		Assert.assertEquals("Second lat should be 4", 4.0, path.get(1).getX(), 0.1);
		Assert.assertEquals("Second lon should be 1", 1.0, path.get(1).getY(), 0.1);
		Assert.assertEquals("Third lat should be 5", 5.0, path.get(2).getX(), 0.1);
		Assert.assertEquals("Third lon should be 1", 1.0, path.get(2).getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 6.5", 6.5, path.get(3).getX(), 0.1);
		Assert.assertEquals("Fourth lon should be 0", 0.0, path.get(3).getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 8.0", 8.0, path.get(4).getX(), 0.1);
		Assert.assertEquals("Fourth lon should be -1.0", -1.0, path.get(4).getY(), 0.1);
		
		try {
			map.dijkstra(start, new GeographicPoint(4, 3));
		} catch (IllegalArgumentException iae) {
			assertNotNull("Second argument is not in mapgraph", iae);
		}
	}
	
	@Test
	public void testAstarVisited() {
		System.out.println("Creating map...");
		MapGraph map = new MapGraph();
		System.out.println("Loading map data...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", map);
		System.out.println("Done loading. Map info: ");
		
		GeographicPoint start = new GeographicPoint(1.0, 1.0);
		GeographicPoint goal = new GeographicPoint(8.0, -1.0);
		map.aStarSearch(start, goal);
		List<MapNode> visited = map.getVisited();
		System.out.println("Expecting visited (1,1), (4,1), (5,1), (6.5,0), and (8,-1)");
		String visitedStr = "";
		for(MapNode node: visited) {
			visitedStr += "(" + node.getLocation().getX() + ", " + node.getLocation().getY() + "), ";
		}
		System.out.println("Actual a-star visited: " + visitedStr);
		
		Assert.assertEquals("First lat should be 1", 1.0, visited.get(0).getLocation().getX(), 0.1);
		Assert.assertEquals("First lon should be 1", 1.0, visited.get(0).getLocation().getY(), 0.1);
		Assert.assertEquals("Second lat should be 4", 4.0, visited.get(1).getLocation().getX(), 0.1);
		Assert.assertEquals("Second lon should be 1", 1.0, visited.get(1).getLocation().getY(), 0.1);
		Assert.assertEquals("Third lat should be 5", 5.0, visited.get(2).getLocation().getX(), 0.1);
		Assert.assertEquals("Third lon should be 1", 1.0, visited.get(2).getLocation().getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 6.5", 6.5, visited.get(3).getLocation().getX(), 0.1);
		Assert.assertEquals("Fourth lon should be 0", 0.0, visited.get(3).getLocation().getY(), 0.1);
		Assert.assertEquals("Fourth lat should be 8.0", 8.0, visited.get(4).getLocation().getX(), 0.1);
		Assert.assertEquals("Fourth lon should be -1.0", -1.0, visited.get(4).getLocation().getY(), 0.1);
		
		try {
			map.dijkstra(start, new GeographicPoint(4, 3));
		} catch (IllegalArgumentException iae) {
			assertNotNull("Second argument is not in mapgraph", iae);
		}
	}
}
