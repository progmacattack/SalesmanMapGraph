package roadgraph;

public class GraphUtility {
	
	//Return a key generated by a start node and end node.
	public static Integer generateHashMapKeyForEdge(MapNode start, MapNode end) {
		return start.hashCode() + end.hashCode();
	}
}