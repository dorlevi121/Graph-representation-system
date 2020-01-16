package gameClient;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.GraphFruit;
import dataStructure.GraphRobot;
import dataStructure.edge_data;
import dataStructure.node_data;
import utils.Point3D;
import utils.Range;

public class MyGameGUI extends JFrame implements ActionListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DGraph graph;
	private List<GraphFruit> fruits;
	private List<GraphRobot> robots;
	private game_service game;
	private Range rx = new Range(Integer.MAX_VALUE,Integer.MIN_VALUE);
	private Range ry = new Range(Integer.MAX_VALUE,Integer.MIN_VALUE);


	public MyGameGUI() {
		this.graph = new DGraph();
		fruits = new ArrayList<>();
		robots = new ArrayList<>();
		initGame();
		setVisible(true);

	}



	private void initGame() {
		this.setSize(1400, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setTitle("Graph Maker");
		initMenu();
		graph.init(game.getGraph()); //Init graph
		initFruits();
		initRobots();
	}


	//Initialized the fruits list with all the game fruits
	private void initFruits() {
		this.fruits.clear();
		GraphFruit fruit = new GraphFruit();
		List<String> f = game.getFruits();
		for(String s : f) {
			fruit.initFruit(s);
			this.fruits.add(fruit);
		}
	}


	//Initialized the robot list
	private void initRobots() {
		this.robots.clear();
		GraphRobot robot = new GraphRobot();
		String info = game.toString();
		try {
			JSONObject line = new JSONObject(info);
			JSONObject ttt = line.getJSONObject("GameServer");
			int rs = ttt.getInt("robots");
			for(int a = 0;a<rs;a++) {
				game.addRobot(a);
			}
		}
		catch (JSONException e) {e.printStackTrace();}

		List<String> r = game.getRobots();
		for(String s : r) {
			robot.initRobot(s);
			this.robots.add(robot);
		}
	}

	
	//Init UI menu
	private void initMenu() {
		MenuBar menuBar = new MenuBar();
		this.setMenuBar(menuBar);

		Menu op = new Menu("Game");
		menuBar.add(op);

		MenuItem manual = new MenuItem("Manual");
		manual.addActionListener(this);
		op.add(manual);

		MenuItem auto = new MenuItem("Auto");
		auto.addActionListener(this);
		op.add(auto);

		this.addMouseListener(this);

	}



	@Override
	public void paint(Graphics d) {
		super.paint(d);

		if(graph != null) { setRange(); loadNodes(d);}

		if(!fruits.isEmpty()) loadFruites(d);

		if(!robots.isEmpty()) loadRobots(d);


	}



	//Draw Graph
	private void loadNodes(Graphics d) {
		Collection<node_data> nodes = graph.getV();

		for (node_data n : nodes) {

			Point3D p = n.getLocation(); //Hold node location

			double offsetx = (p.x() - rx.get_min())/(rx.get_max() - rx.get_min());
			double x = 1200 * offsetx + 100; 
			double offsety = (p.y() - ry.get_min())/(ry.get_max() - ry.get_min());
			double y = 400 * offsety;
			y = (400 - y) + 100;

			d.setColor(Color.BLUE); //Node color
			d.fillOval((int)x,(int)y,10,10); //Draw Vertex

			d.drawString(""+n.getKey(), ((int)x)-4, ((int)y)-4); //Draw node key

			//check if there are edges
			if (graph.edgeSize()==0) continue;
			if ((graph.getE(n.getKey())!=null)){
				Collection<edge_data> edges = graph.getE(n.getKey()); //All edges of mode n

				for (edge_data e : edges) {
					Point3D p2 = graph.getNode(e.getDest()).getLocation();

					double offsetx1 = (p2.x() - rx.get_min())/(rx.get_max() - rx.get_min());
					double x1 = 1200 * offsetx1 + 100; 
					double offsety1 = (p2.y() - ry.get_min())/(ry.get_max() - ry.get_min());
					double y1 = 400 * offsety1;
					y1 = (400 - y1) + 100;

					d.setColor(Color.RED); //Edge color
					d.drawLine(((int)x)+5, ((int)y)+5, ((int)x1)+5, ((int)y1)+5); //Draw Line

					d.setColor(Color.YELLOW); //Direction Color
					d.fillOval((int)((p.ix() * 0.1) + (0.9 * p2.ix())) + 7, 1 + (int)((p.iy() * 0.1) + 
							(0.9 * p2.iy())), 7 , 7);
					d.fillOval((int)((((int)x) * 0.1) + (0.9 * ((int)x1))) + 7, 1 + 
							(int)((((int)y) * 0.1) + (0.9 * ((int)y1))) , 7, 7);

					//draw weight
					d.setColor(Color.BLACK);
					String whight = String.valueOf(e.getWeight());
					//					d.drawString(whight.substring(0,3), 3 + (int)((p.ix() * 0.1) + (0.9*p2.ix())) + 
					//							7, 3 + (int)((p.iy() * 0.1) + (0.9 * p2.iy())));
					//					d.drawString(whight.substring(0,3) , 3 + (int)((((int)x) * 0.2) + 
					//							(0.9 * ((int)x1))) + 7 , 3 + (int)((((int)y) * 0.1) + (0.9 * ((int)y1))));
					d.drawString(whight.substring(0,3), 1 + (int)(((int)x * 0.7) + (0.3 * (int)x1) + 7)
							, (int)(((int)y * 0.7) + (0.3 * (int)y1)) - 2);
				}
			}	
		}
	}



	//Draw Fruits
	private void loadFruites(Graphics d) {
		Iterator<String> f_iter =  fruits.iterator();
		while(f_iter.hasNext()) {
			try {
				String fruit = f_iter.next(); //Fruit objects as a string	

				JSONObject FruitAsJson = new JSONObject(fruit); //Convert to JSON statement 
				JSONObject fruitValues = FruitAsJson.getJSONObject("Fruit"); // Holds the values of key's Fruit
				String location = fruitValues.getString("pos"); //Hold the location of fruitValues
				int type = fruitValues.getInt("type"); //Hold the type of fruitValues

				Point3D pBefore = new Point3D(location);
				double offsetx = (pBefore.x() - rx.get_min())/(rx.get_max() - rx.get_min());
				double x = 1200 * offsetx + 100; 
				double offsety = (pBefore.y() - ry.get_min())/(ry.get_max() - ry.get_min());
				double y = 400 * offsety;
				y = (400 - y) + 100;

				Point3D pAfter = new Point3D(x, y);

				if(type<0)
					d.setColor(Color.ORANGE);
				else 
					d.setColor(Color.GREEN);

				d.fillOval((int)pAfter.x() , (int)pAfter.y() , 15, 15); //Draw fruit

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}



	//Draw Robots
	private void loadRobots(Graphics d) {
		for (GraphRobot r : robots) {
			Point3D pBefore = new Point3D(r.getPos());
			double offsetx = (pBefore.x() - rx.get_min())/(rx.get_max() - rx.get_min());
			double x = 1200 * offsetx + 100; 
			double offsety = (pBefore.y() - ry.get_min())/(ry.get_max() - ry.get_min());
			double y = 400 * offsety;
			y = (400 - y) + 100;

			Point3D pAfter = new Point3D(x, y);
			d.setColor(Color.GRAY);
			d.drawOval((int)pAfter.x(), (int)pAfter.y(), 18, 18); //Draw Robot
		}
//		Iterator<String> r_iter =  robots.iterator();
//		while(r_iter.hasNext()) {
//			try {
//
//				String robot = r_iter.next(); //Robot objects as a string	
//				JSONObject robotAsJson = new JSONObject(robot); //Convert to JSON statement 
//				JSONObject robotValues = robotAsJson.getJSONObject("Robot"); // Holds the values of key Robot
//				String location = robotValues.getString("pos"); //Hold the location of robotValues
//
//				Point3D pBefore = new Point3D(location);
//				double offsetx = (pBefore.x() - rx.get_min())/(rx.get_max() - rx.get_min());
//				double x = 1200 * offsetx + 100; 
//				double offsety = (pBefore.y() - ry.get_min())/(ry.get_max() - ry.get_min());
//				double y = 400 * offsety;
//				y = (400 - y) + 100;
//
//				Point3D pAfter = new Point3D(x, y);
//				d.setColor(Color.GRAY);
//				d.drawOval((int)pAfter.x(), (int)pAfter.y(), 18, 18); //Draw Robot
//			}
//			catch (Exception e) {
//				// TODO: handle exception
//			}
//		}

	}



	public static void main(String[] args) {
		MyGameGUI s = new MyGameGUI();
	}



	@Override
	public void actionPerformed(ActionEvent event) {
		String str = event.getActionCommand();

		if(str.equals("Manual")) {
			clearGame();
			String scenario = JOptionPane.showInputDialog("Pick a scenario number between 0 to 23");
			int scanerioNumber = Integer.parseInt(scenario);
			game = Game_Server.getServer(scanerioNumber);

			//Init Robots
			try {
				JSONObject line = new JSONObject(game.toString());
				JSONObject ttt = line.getJSONObject("GameServer");
				int rs = ttt.getInt("robots");
				for (int i = 0; i < rs; i++) 
					game.addRobot(i);

			} catch (JSONException e1) {
				e1.printStackTrace();}

			this.robots = game.getRobots();
			this.fruits = game.getFruits(); //Init fruits
			this.graph.init(game.getGraph()); //Init graph

			repaint();

		}

		else if(str.equals("Auto")) {

		}
	}



	private void clearGame() {
		this.graph = new DGraph();
		this.rx = new Range(Integer.MAX_VALUE,Integer.MIN_VALUE);
		this.ry = new Range(Integer.MAX_VALUE,Integer.MIN_VALUE);
		this.robots.clear();
		this.fruits.clear();
	}



	private void setRange() {
		Collection<node_data> c = graph.getV();
		Iterator<node_data> itrV = c.iterator();
		while(itrV.hasNext()) {
			node_data n = itrV.next();
			Point3D p = n.getLocation();
			double x = p.x();
			double y = p.y();
			if(x < rx.get_min()) rx.set_min(x);
			else if(x > rx.get_max()) rx.set_max(x);
			if(y < ry.get_min()) ry.set_min(y);
			else if(y > ry.get_max()) ry.set_max(y);
		}
	}


	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
