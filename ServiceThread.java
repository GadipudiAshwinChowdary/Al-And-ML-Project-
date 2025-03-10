package com;
public class ServiceThread extends Thread{
	IndustrialManager main;
public ServiceThread(IndustrialManager main){
	this.main = main;
	start();
}
public void run(){
	main.start();
}
}