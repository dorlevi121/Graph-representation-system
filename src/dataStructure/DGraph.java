package dataStructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DGraph implements graph,Serializable{
	//private HashMap<Node id, Node> vertices;
	private HashMap<Integer, node_data> vertices;
	//private HashMap< src vertex(Node), HashMap<dest vertex(Node), edge(Edge)> > edges;
	private HashMap< node_data, HashMap<node_data, edge_data> > edges;
	private int numOfEdges, modeCount ;

	public DGraph() {
		vertices = new HashMap<>();
		edges = new HashMap<>();
		numOfEdges=0;
		modeCount = 0;
	}
	
	
	public DGraph(DGraph dg) {
		this();
		this.vertices.putAll(dg.vertices);
		this.edges.putAll(dg.edges);
		this.numOfEdges = dg.numOfEdges;
		this.modeCount = dg.modeCount;
	}

	@Override
	public node_data getNode(int key) {
		return this.vertices.get(key);
	}
	

	@Override
	public edge_data getEdge(int src, int dest) {
		return edges.get(vertices.get(src)).get(vertices.get(dest));
	}
	

	//If an existing node key is passed then the previous node_data gets replaced by the n. 
	//If a new pair is passed, then the pair gets inserted as a whole.
	@Override
	public void addNode(node_data n) {
		this.vertices.put(n.getKey(), n);
		modeCount++;
	}


	@Override
	public void connect(int src, int dest, double w) {

		if(this.vertices.get(src) == null) {
			Node s = new Node(src);
			this.vertices.put(src, s);
		}
		if(this.vertices.get(dest) == null) {
			Node d = new Node(dest);
			this.vertices.put(dest, d);
		}

		Node tempS = (Node) this.vertices.get(src);
		Node tempD = (Node) this.vertices.get(dest);
		Edge e = new Edge(tempS, tempD, w);
		
		if(this.edges.get(this.vertices.get(src)) == null) {
			this.edges.put(tempS, new HashMap<node_data, edge_data>());
			this.edges.get(tempS).put(tempD, e);
		}

		else 
			this.edges.get(tempS).put(tempD , e);
		
		numOfEdges++;
		modeCount++;
	}


	//The method return a collection view containing all the Nodes of the vertices map.
	@Override
	public Collection<node_data> getV() {
		return this.vertices.values();
	}

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(this.vertices.get(node_id) == null) return null;
		
		else if(this.edges.get(this.vertices.get(node_id)) == null) return null;
		
		return this.edges.get(this.vertices.get(node_id)).values();
	}

	
	@Override
	public node_data removeNode(int key) {
		Node temp = (Node) this.vertices.get(key);
		if(temp == null) return null;

		else if(this.edges.get(temp) == null) {
			for (Map.Entry<node_data, HashMap<node_data, edge_data>> entry : edges.entrySet()) {
				entry.getValue().remove(temp); 
				if(entry.getValue().isEmpty()) this.edges.remove(entry.getKey());
			}
		}

		else {
			this.edges.remove(temp);
			for (Map.Entry<node_data, HashMap<node_data, edge_data>> entry : edges.entrySet()) {
				entry.getValue().remove(temp);
				if(entry.getValue().isEmpty()) this.edges.remove(entry.getKey());
			}
		}
		this.vertices.remove(key);
		modeCount++;
		return temp;
	}
	

	@Override
	public edge_data removeEdge(int src, int dest) {
		Node s = (Node) this.vertices.get(src);
		Node d = (Node) this.vertices.get(dest);
		if(this.edges.get(s) == null || this.edges.get(s).get(d) == null)
			return null;
		
		else {
			Edge temp = (Edge) this.edges.get(s).get(d);
			this.edges.get(s).remove(d);
			if(this.edges.get(s).isEmpty()) {
				this.edges.remove(s);
			}
			numOfEdges--;
			modeCount++;
			return temp;
		}
	}
	
	

	@Override
	public int nodeSize() {
		return this.vertices.size();
	}

	@Override
	public int edgeSize() {
		return this.numOfEdges;
	}

	@Override
	public int getMC() {
		return this.modeCount;
	}


	public static void main(String[] args) {

		DGraph test = new DGraph();

		Node n1 = new Node();
		Node n2 = new Node(1);
		Node n3 = new Node(2);
		Node n4 = new Node(4);
		System.out.println(n2);
		Edge e1 = new Edge();
		Edge e2 = new Edge(n2, n3, 4);
		//System.out.println(e2.tostring());

		test.addNode(n1);
		test.addNode(n2);
		test.addNode(n3);
		test.connect(1, 4, 5);
		test.connect(1, 3, 5);
		test.connect(6, 9, 5);

		Edge e3 = (Edge) test.edges.get(n2).get(test.vertices.get(4));
		System.out.println(test.getE(1).toString());
		System.out.println(test.removeEdge(5, 9));
		System.out.println(test.removeEdge(6, 9));
		System.out.println(test.removeNode(3));


		//System.out.println(test.getV());
	}

}
