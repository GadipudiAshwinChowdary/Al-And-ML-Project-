package com;
import java.util.ArrayList;
public class POW{
	String node;
	double lazy_tips;
	double double_spending;
	double credit;
	ArrayList<String> lazy = new ArrayList<String>();
	ArrayList<String> tokens = new ArrayList<String>();
	ArrayList<byte[]> data = new ArrayList<byte[]>();
	String hash;
	byte enc[];
public void setHash(String hash){
	this.hash = hash;
}
public String getHash(){
	return hash;
}
public void setEnc(byte[] enc){
	this.enc = enc;
}
public byte[] getEnc(){
	return enc;
}
public void addData(byte b[]){
	data.add(b);
}
public void addLazy(String lz){
	lazy.add(lz);
}
public void addToken(String tok){
	tokens.add(tok);
}
public void setNode(String node){
	this.node = node;
}
public String getNode(){
	return node;
}
public void setLazy(double lazy_tips){
	this.lazy_tips = lazy_tips;
}
public double getLazy(){
	return lazy_tips;
}
public void setDoubleSpending(double double_spending){
	this.double_spending = double_spending;
}
public double getDoubleSpending(){
	return double_spending;
}
public void setCredit(double credit){
	this.credit = credit;
}
public double getCredit(){
	return credit;
}
}