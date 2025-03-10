package com;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
public class GetPublicKeys extends Thread{
	Sensor sensor;
	String source;
	boolean flag = false;
public GetPublicKeys(Sensor sensor,String source){
	this.sensor = sensor;
	this.source = source;
	start();
}
public static double getDistance(int n1x,int n1y,int n2x,int n2y) {
	int dx = (n1x - n2x) * (n1x - n2x);
	int dy = (n1y - n2y) * (n1y - n2y);
	int total = dx + dy; 
	return Math.sqrt(total);
}
public void run(){
	try{
		Circle sender = null;
		Circle receiver = null;
		for(int i=0;i<sensor.circles.size();i++){
			Circle node = sensor.circles.get(i);
			if(node.getNode().equals(source)){
				sender = node;
				break;
			}
		}
		double distance = getDistance(470,100,sender.x,sender.y);
		if(distance < 100){
			flag = true;
		}
		if(!flag){
			ArrayList<Circle> neighbor = new ArrayList<Circle>();
			for(int i=0;i<sensor.circles.size();i++){
				Circle node = sensor.circles.get(i);
				if(!node.getNode().equals(source)){
					distance = getDistance(sender.x,sender.y,node.x,node.y);
					if(distance < 100){
						neighbor.add(node);
					}
				}
			}
			double min = 1000;
			for(int i=0;i<neighbor.size();i++){
				Circle node = neighbor.get(i);
				distance = getDistance(470,100,node.x,node.y);
				if(distance < min){
					min = distance;
					receiver = node;
				}
			}
			Network.l3.setText("Source : "+sender.getNode()+" Using one hop neighbor "+receiver.getNode()+" to reach Gateway/Full Node");
		}
		Socket socket = new Socket("localhost",3333);
        ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
        Object req[]={"keys",source};
        out.writeObject(req);
        out.flush();
        Object res[]=(Object[])in.readObject();
		byte key[] = (byte[])res[0];
		Main.area.append(sender.getNode()+" received key : "+new String(key)+"\n");
		Network.keys.put(source,key);
		Network.l3.setText(sender.getNode()+" received key : "+new String(key)+"\n");
		for(int k=0;k<2;k++){
			sensor.setSender(sender,receiver,flag);
			sensor.option=1;
			sensor.repaint();
			sleep(40);
			sensor.option=0;
			sensor.repaint();
			sleep(20);
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
}