/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
package roadgraph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

import javax.xml.soap.Node;

import geography.GeographicPoint;
import util.GraphLoader;


/**
 * @author UCSD MOOC development team and YOU
 * 
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between 
 *
 */
public class MapGraph {
	private HashMap<GeographicPoint, MapNode> verticesMap;  //a map relating points to mapnodes. all nodes are accessible
															//through this map
	private Map<Integer, Edge> edgeMap; //a map of all edges
	private List<MapNode> visited; //a public and ordered list of visited nodes. Used only for testing.
	/** 
	 * Create a new empty MapGraph 
	 */
	public MapGraph()
	{
		verticesMap = new HashMap<GeographicPoint, MapNode>(); //initialize storage of vertices
		edgeMap = new HashMap<Integer, Edge>();
		visited = new ArrayList<MapNode>();
	}
	
	/**
	 * Get the number of vertices (road intersections) in the graph
	 * @return The number of vertices in the graph.
	 */
	public int getNumVertices()
	{
		return verticesMap.size();
	}
	
	/**
	 * Return the intersections, which are the vertices in this graph.
	 * @return The vertices in this graph as GeographicPoints
	 */
	public Set<GeographicPoint> getVertices()
	{
		return verticesMap.keySet(); //each geographic point is stored as a key in the hashmap
	}
	
	/**
	 * Get the number of road segments in the graph
	 * @return The number of edges in the graph.
	 */
	public int getNumEdges()
	{
		Integer edgeCount = 0;
		for(Entry<GeographicPoint, MapNode> e: verticesMap.entrySet()) { //look at every mapnode in hashmap
			MapNode node = e.getValue();								 //and add the number of neighbors it has
			edgeCount = edgeCount + node.getOutgoingNeighbors().size();  //to the edgecount
		}
		return edgeCount;
	}

	
	
	/** Add a node corresponding to an intersection at a Geographic Point
	 * If the location is already in the graph or null, this method does 
	 * not change the graph.
	 * @param location  The location of the intersection
	 * @return true if a node was added, false if it was not (the node
	 * was already in the graph, or the parameter is null).
	 */
	public boolean addVertex(GeographicPoint location)
	{
		if(location == null) {
			return false;
		}
		
		if(verticesMap.containsKey(location)) { //check to see if we already have this vertex
			return false;
		}
		
		MapNode vertex = new MapNode(location); //create new MapNode to store information about the vertex
		verticesMap.put(location, vertex);		//add this MapNode to this class's hashmap; set location to
		return true;							//be the key
	}
	
	/**
	 * Adds a directed edge to the graph from pt1 to pt2.  
	 * Precondition: Both GeographicPoints have already been added to the graph
	 * @param from The starting point of the edge
	 * @param to The ending point of the edge
	 * @param roadName The name of the road
	 * @param roadType The type of the road
	 * @param length The length of the road, in km
	 * @throws IllegalArgumentException If the points have not already been
	 *   added as nodes to the graph, if any of the arguments is null,
	 *   or if the length is less than 0.
	 */
	public void addEdge(GeographicPoint from, GeographicPoint to, String roadName,
			String roadType, double length) throws IllegalArgumentException {

		Edge edge = new Edge();		//instantiate a new Edge (like MapNode, this is a class I created for the assignment)
		MapNode fromNode = null;
		MapNode toNode = null;
		
		if(from == null || to == null || length < 0) {
			throw new IllegalArgumentException("Both points must be non-null and length must be >= 0");
		}
		
		fromNode = verticesMap.get(from); //get the nodes/vertexes in from this class
		toNode = verticesMap.get(to);	  
		
		if(fromNode == null || toNode == null) {
			throw new IllegalArgumentException("One or both of provided points have not been added to graph");
		} else {
			fromNode.addOutoingNeighbor(toNode);  //add the destination as a neighbor of the origin
			edge.setBegin(fromNode); //setup the edge class with the 
			edge.setEnd(toNode);	//information provided in this method
			edge.setRoadType(roadType);
			edge.setLength(length);
			edgeMap.put(GraphUtility.generateHashMapKeyForEdge(fromNode, toNode), edge);
		}
	}
	

	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return bfs(start, goal, temp);
	}
	
	/** Find the path from start to goal using breadth first search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest (unweighted)
	 *   path from start to goal (including both start and goal).
	 */
	public List<GeographicPoint> bfs(GeographicPoint start, 
			 					     GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{		
		if(!verticesMap.containsKey(start) || !verticesMap.containsKey(goal)) {
			throw new IllegalArgumentException("Either start or goal is not in mapgraph");
		}
		
		MapNode s = verticesMap.get(start); //get the start and
		MapNode g = verticesMap.get(goal);  //goal map nodes
		
		Map<MapNode, MapNode> parentMap = makeParentMap(s, g, nodeSearched); //call the method that implements the BFS algorithm
		
		return getPath(parentMap, s, g);	
		
	}
	
	/**Given a parent map that links child (key) to parent(value), produce an ordered list showing the path
	 * 
	 * @param parentMap map of children(key) to parents(value) mapnodes
	 * @param start the beginning of the route
	 * @param goal the end of the route
	 * @return a list of geographic points representing nodes along the path
	 */
	private LinkedList<GeographicPoint> getPath(Map<MapNode, MapNode> parentMap, MapNode start, MapNode goal) {
		LinkedList<GeographicPoint> path = new LinkedList<GeographicPoint>(); //this will hold the eventual path
		//after creating the parentMap, retrace the steps from finish to start, this becomes the path
		if(parentMap != null) { 
			MapNode cur = goal;
			path.addFirst(goal.getLocation());
			while(!cur.equals(start)) {
				path.addFirst(parentMap.get(cur).getLocation());
				cur = parentMap.get(cur);
			}
		}
		return path;
		
	}

	/** Given a start and goal, return a parent map showing the parents of all visited nodes
	 * By looking at the goal node, the path could be reconstructed. However, this method does not do
	 * this reconstruction
	 * @param s Start MapNode representing the place in the mapgraph to start
	 * @param g Goal MapNode representing the place in the mapgraph to seek out
	 * @param nodeSearched used for visualization on the front end
	 * @return Map<MapNode, MapNode> where the key is the each visited mapnode and the value is the parent of each visited mapnode
	 */
	private Map<MapNode, MapNode> makeParentMap(MapNode s, MapNode g, Consumer<GeographicPoint> nodeSearched) {
		Map<MapNode, MapNode> parentMap = new HashMap<>();
		Queue<MapNode> queue = new LinkedList<MapNode>();
		Set<MapNode> visited = new HashSet<MapNode>();
		
		//this is the breadth-first-search algorithm described in lecture this week
		queue.add(s);
		visited.add(s);
		while(!queue.isEmpty()) {
			MapNode curr = queue.remove();
			if(curr.equals(g)) {
				return parentMap;
			}
			for(MapNode neighbor: curr.getOutgoingNeighbors()) {
				if(!visited.contains(neighbor)) {
					visited.add(neighbor);
					parentMap.put(neighbor, curr);
					queue.add(neighbor);
					nodeSearched.accept(neighbor.getLocation()); //visualization hook
				}
			}
		}
		return null; //if we have arrived this far, there was no possible path
		
	}

	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
		// You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {};
        return dijkstra(start, goal, temp);
	}
	
	/** Find the path from start to goal using Dijkstra's algorithm
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> dijkstra(GeographicPoint start, 
										  GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		// TODO: Implement this method in WEEK 3

		if(!verticesMap.containsKey(start) || !verticesMap.containsKey(goal)) {
			throw new IllegalArgumentException("both start and goal points must be present in graph");
		}
		
		MapNode startNode = verticesMap.get(start);
		MapNode goalNode = verticesMap.get(goal);
		
		Map<MapNode, MapNode> parentMap = new HashMap<>();
		parentMap = findPathWithDijkstra(startNode, goalNode, nodeSearched);
		return getPath(parentMap, startNode, goalNode);
	}

	private Map<MapNode, MapNode> findPathWithDijkstra(MapNode startNode, MapNode goalNode, Consumer<GeographicPoint> nodeSearched) {
		return findPathWithAstar(startNode, goalNode, true, nodeSearched);
	}
	
	private Map<MapNode, MapNode> findPathWithAstar(MapNode startNode, MapNode goalNode, Boolean useDijkstraVariation, Consumer<GeographicPoint> nodeSearched) {
		PriorityQueue<MapNode> pq = new PriorityQueue<>();
		Set<MapNode> visited = new HashSet<>();
		Map<MapNode, MapNode> parentMap = new HashMap<>();
		
		startNode.setDistanceTraveled(0d);
		pq.add(startNode);
		while(!pq.isEmpty()) {
			MapNode curr = pq.poll();
			if(!visited.contains(curr)) {
				visited.add(curr);
				this.visited.add(curr);
				if(curr.equals(goalNode)) {
					for(Entry<GeographicPoint, MapNode> entry: verticesMap.entrySet()) {						
						entry.getValue().clearDistances();
					}
					return parentMap;
				}
				for(MapNode neighbor: curr.getOutgoingNeighbors()) {
					if(!visited.contains(neighbor)) {
						Edge e2 = edgeMap.get(GraphUtility.generateHashMapKeyForEdge(curr, neighbor));
						Double predictedDistanceToDesination = useDijkstraVariation ? 0d : neighbor.getLocation().distance(goalNode.getLocation());
						Double distanceTraveled = curr.getDistanceTraveled() + e2.getLength();
						if(neighbor.getDistanceTraveled() == Double.POSITIVE_INFINITY || distanceTraveled < neighbor.getDistanceTraveled()) {
							neighbor.setDistanceTraveled(curr.getDistanceTraveled() + e2.getLength());
							neighbor.setPredictedDistanceToDestination(predictedDistanceToDesination);
							parentMap.put(neighbor, curr);
							pq.add(neighbor);
							nodeSearched.accept(neighbor.getLocation());
						}
					}
				}
			}
		}
		return null;  //if here, there is no path
	}

	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal) {
		// Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {};
        return aStarSearch(start, goal, temp);
	}
	
	/** Find the path from start to goal using A-Star search
	 * 
	 * @param start The starting location
	 * @param goal The goal location
	 * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
	 * @return The list of intersections that form the shortest path from 
	 *   start to goal (including both start and goal).
	 */
	public List<GeographicPoint> aStarSearch(GeographicPoint start, 
											 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched)
	{
		if(!verticesMap.containsKey(start) || !verticesMap.containsKey(goal)) {
			throw new IllegalArgumentException("both start and goal points must be present in graph");
		}
		
		MapNode startNode = verticesMap.get(start);
		MapNode goalNode = verticesMap.get(goal);
		
		Map<MapNode, MapNode> parentMap = new HashMap<>();
		parentMap = findPathWithAstar(startNode, goalNode, false, nodeSearched);
		
		return getPath(parentMap, startNode, goalNode);
	}
	
	public static void main(String[] args)
	{
		System.out.print("Making a new map...");
		MapGraph firstMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/testdata/simpletest.map", firstMap);
		System.out.println("DONE.");
		
		// You can use this method for testing.  
		
		
		/* Here are some test cases you should try before you attempt 
		 * the Week 3 End of Week Quiz, EVEN IF you score 100% on the 
		 * programming assignment.
		 */
		
	    /*MapGraph simpleTestMap = new MapGraph();
			GraphLoader.loadRoadMap("data/testdata/simpletest.map", simpleTestMap);
			
			GeographicPoint testStart = new GeographicPoint(1.0, 1.0);
			GeographicPoint testEnd = new GeographicPoint(8.0, -1.0);
			
			System.out.println("Test 1 using simpletest: Dijkstra should be 9 and AStar should be 5");
			List<GeographicPoint> testroute = simpleTestMap.dijkstra(testStart,testEnd);
			System.out.println(testroute);
			List<GeographicPoint> testroute2 = simpleTestMap.aStarSearch(testStart,testEnd);
			System.out.println(testroute2);
			
			MapGraph testMap = new MapGraph();
			GraphLoader.loadRoadMap("data/maps/utc.map", testMap);
			
			// A very simple test using real data
			testStart = new GeographicPoint(32.869423, -117.220917);
			testEnd = new GeographicPoint(32.869255, -117.216927);
			System.out.println("Test 2 using utc: Dijkstra should be 13 and AStar should be 5");
			testroute = testMap.dijkstra(testStart,testEnd);
			testroute2 = testMap.aStarSearch(testStart,testEnd);
			
			
			// A slightly more complex test using real data
			testStart = new GeographicPoint(32.8674388, -117.2190213);
			testEnd = new GeographicPoint(32.8697828, -117.2244506);
			System.out.println("Test 3 using utc: Dijkstra should be 37 and AStar should be 10");
			testroute = testMap.dijkstra(testStart,testEnd);
			testroute2 = testMap.aStarSearch(testStart,testEnd);*/
		MapGraph theMap = new MapGraph();
		System.out.print("DONE. \nLoading the map...");
		GraphLoader.loadRoadMap("data/maps/utc.map", theMap);
		System.out.println("DONE.");

		GeographicPoint start = new GeographicPoint(32.8648772, -117.2254046);
		GeographicPoint end = new GeographicPoint(32.8660691, -117.217393);

		List<GeographicPoint> route = theMap.dijkstra(start,end);
		List<GeographicPoint> route2 = theMap.aStarSearch(start,end);
	}

	public List<MapNode> getVisited() {
		return visited;
	}

	public HashMap<GeographicPoint, MapNode> getVerticesMap() {
		return verticesMap;
	}

	public Map<Integer, Edge> getEdgeMap() {
		return edgeMap;
	}
}
