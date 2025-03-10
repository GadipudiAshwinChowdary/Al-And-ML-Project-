package com;
import java.util.Random;
import java.awt.Point;
public class Node{
	static int size=40;
	static Sensor g;
public static void randomNodes(int numNodes, int width, int height,Sensor nodes,int region){
	g = nodes;
    randomNodes(numNodes, width, height,region);
}
public static void randomNodes(int numNodes, int width, int height,int region){
	int x = getXPosition(10,width);
	int y = getYPosition(150,height);
	for(int i=1;i<=numNodes;i++){
		boolean flag = checkDistance(x,y);
		if(!flag){
			Circle c = new Circle(new Point(x, y), size);
			c.setNode("S"+i);
			g.circles.add(c);
		}else{
			i = i - 1;
		}
		x = getXPosition(10,width);
		y = getYPosition(150,height);
	}	

	
}
public static int getXPosition(int start,int end){
	Random rn = new Random();
	int range = end - start + 1;
	return rn.nextInt(range) + start;
}
public static int getYPosition(int start,int end){
	Random rn = new Random();
	int range = end - start + 1;
	return rn.nextInt(range) + start;
}
public static boolean checkDistance(int x,int y){
	boolean flag = false;
	for(int i=0;i<g.circles.size();i++){
		Circle node = g.circles.get(i);
		double d = getDistance(x,y,node.x,node.y);
		if(d < 50){
			flag = true;
			break;
		}
	}
	return flag;
}
public static double getDistance(int n1x,int n1y,int n2x,int n2y) {
	int dx = (n1x - n2x) * (n1x - n2x);
	int dy = (n1y - n2y) * (n1y - n2y);
	int total = dx + dy; 
	return Math.sqrt(total);
}
}