package com;
import java.awt.Dimension;
import javax.swing.JComponent;
import java.awt.geom.Rectangle2D;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Color;
public class Sensor extends JComponent{
	public int option=0;
	public ArrayList<Circle> circles = new ArrayList<Circle>();
	float dash1[] = {10.0f};
	BasicStroke dashed = new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f, dash1, 0.0f);
	BasicStroke rect=new BasicStroke(1f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,1f,new float[] {2f},0f);
	Dimension dim;
	Circle source,destination;
	boolean flag;
	int xx,yy,range;
public void	setDiscovery(int x,int y,int r){
	xx = x;
	yy = y;
	range = r;
}
public void setSender(Circle source,Circle destination,boolean flag){
	this.source = source;
	this.destination = destination;
	this.flag = flag;
}
public Sensor() {
	super.setBackground(new Color(255,0,255));
	this.setBackground(new Color(255,0,255));
}
public ArrayList<Circle> getList(){
	return circles;
}
public void removeAll(){
	option=0;
	circles.clear();
	repaint();
}
public void paintComponent(Graphics g1){
	super.paintComponent(g1);
	GradientPaint gradient = new GradientPaint(0, 0, Color.green, 175, 175, Color.yellow,true); 
	Graphics2D g = (Graphics2D)g1;
	g.setPaint(gradient);
	g.setStroke(rect);
	Rectangle2D rectangle = new Rectangle2D.Double(400,10,200,40);
	g.setStroke(rect);
	g.draw(rectangle);
	g.drawString("Industrial Manager",480,40);
	rectangle = new Rectangle2D.Double(430,80,150,30);
	g.setStroke(rect);
	g.draw(rectangle);
	g.drawString("Gateway/Full Node",470,100);
	if(option == 0){
		for(int i=0;i<circles.size();i++){
			Circle circle = circles.get(i);
			circle.draw(g,"fill");
			g.drawString(circle.getNode(),circle.x+10,circle.y+50);
		}
	}
	
	if(option == 1){
		for(int i=0;i<circles.size();i++){
			Circle circle = circles.get(i);
			circle.draw(g,"fill");
			g.drawString(circle.getNode(),circle.x+10,circle.y+50);
		}
		g.setStroke(dashed);
		if(flag){
			g.drawLine(source.x+10,source.y+10,470,110);
		}else{
			g.drawLine(source.x+10,source.y+10,destination.x+20,destination.y);
			g.drawLine(destination.x+10,destination.y+10,470,110);
			g.drawLine(500,80,500,50);
		}
	}
}	
}