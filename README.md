# SalesmanMapGraph
OVERALL DESIGN STRUCTURE FOR MAP GRAPH AND OPTIONAL EXTENSIONS

CLASSES: MapGraph

		 SalesmanMapGraph extends MapGraph

		 MapNode

		 MapEdge

		 GraphUtility

		 MapGraphTester

		 SalesmanMapGraphTester



---------------

class MapGraph

---------------

Map<GeographicPoint, MapNode> verticesMap;  //all vertices in graph. map relates nodes to geographicpoints

Map<Integer, Edge> edgeMap; //a map of all edges

List<MapNode> visited;



public methods

---------------

int getNumVertices()

set<GeographicPoint> getVertices()

int getNumEdges()

boolean addVertex(GeographicPoint location)

void addEdge(GeographicPoint from, GeographicPoint to, String roadName,

		String roadType, double length)

List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) //breadth-first search

List<GeographicPoint> bfs(GeographicPoint start, 

		GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) //has front-end hook

List<GeographicPoint> dijkstra(GeographicPoint start, GeographicPoint goal)

List<GeographicPoint> dijkstra(GeographicPoint start, 

		GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) //has front-end hook

List<GeographicPoint> aStarSearch(GeographicPoint start, GeographicPoint goal)

List<GeographicPoint> aStarSearch(GeographicPoint start, 

		 GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) //has front-end hook			

void main(String[] args) //contains some tests



getters for private members



---------------------------------------

class SalesmanMapGraph extends MapGraph

----------------------------------------

List<GeographicPoint> destinations;

List<GeographicPoint> suggestedRoute;



public methods

addDestination(GeographicPoint destination) - add a destination

addOrigin(GeographicPoint origin) - add an origin

bestPathGreedy() - find a route from origin to all destinations and back to origin using greedy algorithm



-----------------------------------

class MapNode implements Comparable

-----------------------------------

GeographicPoint location

List<MapNode> outgoingNeighbors

double distanceTraveled

predictedDistanceToDestination



public methods

---------------

addOutgoingNeighbor(MapNode neighbor)

int compareTo(MapNode other)

clearDistances()

getters and setters





----------

class Edge

----------

String roadType

double length

MapNode begin

MapNode end



public methods

--------------

getters and setters





-------------------

class GraphUtility

--------------------

static Integer generateHashMapKeyForEdge(MapNode start, MapNode end) //create a key for each edge based on origin and destination



----------------------------------------------------------

classes MapGraphTester and SalesmanMapGraphTester

----------------------------------------------------------

test class




The extensions adds a subclass, SalesmanMapGraph extending MapGraph. The subclass provides
a structure for solving the so-call 'traveling salesman' problem (TSP) of traveling from an origin
to multiple destinations and then back to the origin. While other algorithms could be added,
the class initially contains a method to find a solution to TSP using a 'greedy' approach.

One problem I faced was, how do I determine the distance from the origin to all destinations that
must be visited? Another problem -- how do I track which destinations I've already visited? Yet another
problem -- the destinations added in the SalesmanMapGraph are likely 'major' destinations: there may be
many edges between them. How can I take all these edges and abstract them into a single distance representing
the hop?

The subclass creates an ordered list of 'major' destinations to be visited, beginng and ending with the origin.
The class relies heavily on the astar search algorithm implemented in the superclass. Given an unordered list
of 'major' destination, the subclass looks at astar path from one destination to another and tracks the total
distance of the path by summing the distance from each edge in the path from origin to the 'major' destionation.
This 'major' destination -- the closest one -- is then removed from the list of unordered
'major' destinations and becomes the starting point for the next hop.  This process is then repeated between
the origin and each 'major' destination. The closest destination is added to the ordered list of 'major' destinations
and the process is repeated. Eventually, the list of unordered 'major' destinations is empty and the ordered
list is complete.  The complete ordered list is the path determined by the 'greedy' algorithm.
