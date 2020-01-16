package gameClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Server.Game_Server;
import Server.game_service;
import dataStructure.DGraph;
import dataStructure.GraphFruit;
import dataStructure.GraphRobot;

public class AutoGame extends Thread{
	private game_service game;
	private List <GraphRobot> robots;
	private List <GraphFruit> fruits;
	private DGraph graph;

	public AutoGame(int scenarioNumber) {
		game = Game_Server.getServer(scenarioNumber); //Init a new game
		robots = new ArrayList<>();
		fruits = new ArrayList<>();
	}


	private void initGame() {
		graph = new DGraph();
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
}
