package algorithms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.GapContent;

import dataStructure.DGraph;
import dataStructure.Edge;
import dataStructure.Node;
import dataStructure.edge_data;
import dataStructure.graph;
import dataStructure.node_data;
/**
 * This empty class represents the set of graph-theory algorithms
 * which should be implemented as part of Ex2 - Do edit this class.
 * @author 
 *
 */
public class Graph_Algo implements graph_algorithms, Serializable{

	private graph g;

	
	//Empty Constructor
	public Graph_Algo() {
		this.g = new DGraph();
	}

	
	//Copy Constructor
	public Graph_Algo(graph dg) {
		this.g = dg;
	}


	/**
	 * Get graph
	 * @return g
	 */
	public graph getG() {
		return g;
	}


	/**
	 * Set graph g
	 * @param g
	 */	public void setG(graph g) {
		this.g = g;
	}
	
	 
	@Override
	public void init(graph g) {
		this.g = (DGraph) g;
	}



	@Override
	public void init(String file_name) {
		try {
			FileInputStream file = new FileInputStream(file_name); 
			ObjectInputStream in = new ObjectInputStream(file); 
			this.g = (graph) in.readObject();
			in.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();		}
	}
	

	@Override
	public void save(String file_name) {
		try{    
			FileOutputStream f=new FileOutputStream(file_name);
			ObjectOutputStream obj=new ObjectOutputStream(f);
			obj.writeObject(this.getG());
			obj.flush();
			obj.close();
		}   
		catch(IOException ex) { 
			ex.printStackTrace();
		} 
	}


	@Override
	public boolean isConnected() {
		//Hold all the vertices in graph g
		if(this.g.getV().size() == 0) return false;

		for (node_data vertex : this.g.getV()) {
			if(checkConnected(vertex) != this.g.nodeSize())
				return false;
			resetTag();
		}
		resetTag();
		return true;
	}


	private int checkConnected(node_data n) {
		if(n.getTag() != 0 || this.g.getE(n.getKey()) == null) return 0;
		n.setTag(1);
		int numOfConnectedVrecies = 1;
		//Hold all the edges of node n
		for (edge_data edge : this.g.getE(n.getKey())) {
			node_data nextNode = this.g.getNode(edge.getDest());
			if(nextNode==null) return numOfConnectedVrecies;
			numOfConnectedVrecies += checkConnected(nextNode);

		}
		return numOfConnectedVrecies;
	}


	private void resetTag() {
		for (node_data vertex : this.g.getV()) 
			vertex.setTag(0);
	}

	@Override
	public double shortestPathDist(int src, int dest) {
		if(dest>this.g.getV().size()) return -1;
		dijakstra(src); //Calculate all the path from src
		return g.getNode(dest).getWeight();
	}

	
	//	This algorithm finding the shortest paths between nodes in a graph.
	private void dijakstra(int src) {
		ArrayList<node_data> checked = new ArrayList<>();
		ArrayList<node_data> unChecked = new ArrayList<>();

		this.g.getNode(src).setWeight(0); //Reset the source node
		//Init all the vertices to infinity
		for (node_data vertex : this.g.getV()) {
			if(vertex.getKey() != src)
				vertex.setWeight(Double.MAX_VALUE);
			unChecked.add(vertex);
		}

		node_data current = this.g.getNode(src);
		while(unChecked.isEmpty() || Infinity(g)) {
			checked.add(current);
			unChecked.remove(current);
			Collection<edge_data> e = this.g.getE(current.getKey());
			if(e==null) break;

			for(edge_data edge : e) {
				node_data destVertex = this.g.getNode(edge.getDest());
				if(current.getWeight()+edge.getWeight()<g.getNode(destVertex.getKey()).getWeight()) {
					g.getNode(destVertex.getKey()).setWeight(edge.getWeight()+current.getWeight()); 
					destVertex.setInfo(""+current.getKey());																		
				}
			}
			current = unvistedmin(unChecked);	
		}
	}


	private boolean Infinity(graph g2) {
		Collection<node_data> s = g.getV();
		for(node_data n : s) {
			if(n.getWeight()==Double.MAX_VALUE) return true;
		}
		return false;
	}


	private node_data unvistedmin(ArrayList<node_data> unChecked) {
		double min = Double.MAX_VALUE;
		node_data mini=null;
		for(int i=0; i<unChecked.size();i++) {
			if(unChecked.get(i).getWeight()<min) {
				min=unChecked.get(i).getWeight();
				mini=unChecked.get(i);
			}
		}
		return mini;
	}


	@Override
	public List<node_data> shortestPath(int src, int dest) {
		if(dest > this.g.getV().size()) return null;
		dijakstra(src);
		List<node_data> path = new ArrayList<>();
		boolean ans = true;
		node_data cur = g.getNode(dest);
		while(ans) {
			path.add(cur);
			cur = g.getNode(Integer.parseInt(cur.getInfo()));
			if(cur.getKey() == src) {
				ans = false;
				path.add(cur);
			}
		}
		return path;
	}

	
	//This function return path that included all the vertex from targets list
	@Override
	public List<node_data> TSP(List<Integer> targets) {
		if(!isConnected()) return null;
		List<node_data> path = new ArrayList<>();

		for (int i = 0; i < targets.size()-1; i++) {
			path.addAll(this.shortestPath(targets.get(i), targets.get(i+1)));
		}
		
		return path;
	}

	@Override
	public graph copy() {
		graph c = new DGraph((DGraph) this.g);
		return c;
	}
	
	
	public String printPath(List <node_data> path) {
		String ans = "";
		for (node_data node : path) {
			ans = node.getKey() + " --> " + ans;
		} 
		
		return ans;
	}

	public static void main(String[] args) {
		Graph_Algo test = new Graph_Algo();
		test.g.addNode(new Node());
		test.g.addNode(new Node(1));
		test.g.addNode(new Node(2));
		test.g.addNode(new Node(3));

		test.g.connect(0, 1, 1);
		test.g.connect(0, 3, 3);
		test.g.connect(3, 0, 3);
		test.g.connect(1, 2, 1);
		test.g.connect(2, 0, 2);
		test.dijakstra(0);
		
		System.out.println(test.shortestPath(0, 2).toString());
		List<Integer> t = new ArrayList<>();
		t.add(0);
		t.add(2);
		System.out.println(test.TSP(t).toString());

		test.save("g.txt");
		test.init("g.txt");
	}

}
