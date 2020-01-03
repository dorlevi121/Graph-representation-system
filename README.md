# Graph Representation System - Object Oriented Programmin Project
The system allows the user to construct directed graphs and manipulate the graphs using graph algorithms.
The whole system can be operated by UI

### Features
- Create and add vertices;
- Create and add edges;
- Build a directed graph;
- Initialize a graph from file;
- Saves the graph to a file.;
- Check if the graph is connected;
- Check the length of the shortest path between 2 vertices;
- Computes a relatively short path which visit each node in the a list;

![graph](http://i.picasion.com/pic89/cedff83d84e6d46cc654744486dfe692.gif)

### Classes

#### Node (-)
This class represents a vertex in a weighted directed graph.
The node class implementation the node_data interface.
Class variables:
- key (Vertex Number)
- location (Location of vertex n graph)
- weight
     
#### Edge (-)
This class represents a edge in a weighted directed graph.
The edge class implementation the edge_data interface.
* Node src (Source Vertex)
* Node dest (Destination Vertex)
* weight(Weight of edge)

#### DGraph (-)
This class represents a weighted directed graph.
The DGraph class implementation the graph interface.
* vertices (Collection of all the graph vertices)
* edges (Collection of all the graph edges)
* numOfEdges (Number of edges in the graph)

#### Graph_Algo (-)
This class represents a "regular" Graph Theory algorithms.
The Graph_Algo class implementation the graph_algorithms interface.
* g (DGraph type graph)
* init(String file_name) - Initialize a graph from file.
* save(String file_name) - Save the graph.
* isConnected() - Check if the graph is connected.
* shortestPathDist(int src, int dest) - Check the length of the shortest path between 2 vertices.
* shortestPath(int src, int dest) - Return the shortest path between 2 vertices.

#### Graph_GUI
Operations that are applicable on it including:
+ Menu
    + Save Graph
    + Load Graph
    + Add Edge
    + Save as a image
+ Algorithm
    * isConnect
    * Shortest Path
    * Shortest Path Length
    * TSP
    
    ![](http://i.picasion.com/pic89/c92493e32a7efa3c53f3cf2da44ee470.gif)
