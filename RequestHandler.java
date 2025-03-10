package com;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.table.DefaultTableModel;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;
public class RequestHandler extends Thread{
	Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
	DefaultTableModel dtm;
	DecimalFormat df = new DecimalFormat("#.####");

public byte[] getRandom(String id){
	StringBuilder sb = new StringBuilder();
	for(int i=0;i<id.length();i++){
		sb.append(id.charAt(i));
	}
	Random r = new Random();
	for(int i=id.length();i<16;i++){
		sb.append(r.nextInt(10)+"");
	}
	return sb.toString().getBytes();
}

public RequestHandler(Socket soc,DefaultTableModel dtm){
	socket = soc;
	this.dtm = dtm;
	try{
		out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }catch(Exception e){
        e.printStackTrace();
    }
}
public void clearTable(){
	for(int i=dtm.getRowCount()-1;i>=0;i--){
		dtm.removeRow(i);
	}
}
public void updateTable(){
	clearTable();
	for(Map.Entry<String,POW> pow : IndustrialManager.pow.entrySet()){
		POW p = pow.getValue();
		double weight = 0;
		if(p.getDoubleSpending() > 0)
			weight = p.getDoubleSpending()/(double)p.data.size();
		Object row[] = {p.getNode(),p.data.size(),p.getDoubleSpending(),weight,p.getHash(),p.getEnc()};
		dtm.addRow(row);
	}
}

@Override
public void run(){
	try{
		Object input[]=(Object[])in.readObject();
        String type=(String)input[0];
		if(type.equals("keys")){
			String node = (String)input[1];
			byte key[] = getRandom(node);
			IndustrialManager.keys.put(node,key);
			Object row[] = {"Node ID : "+node,"Key : "+new String(key)};
			dtm.addRow(row);
			Object res[] = {key};
			out.writeObject(res);
			out.flush();
			IndustrialManager.status.setText(node+" key sent");
		}
		if(type.equals("transaction")){
			String node = (String)input[1];
			byte enc[] = (byte[])input[2];
			String hash = (String)input[3];
			String msg = "";
			if(IndustrialManager.pow.containsKey(node)) {
				POW pow = IndustrialManager.pow.get(node);
				pow.data.add(enc);
				pow.setHash(hash);
				pow.setEnc(enc);
				if(pow.tokens.contains(hash)){
					pow.setDoubleSpending(pow.getDoubleSpending() + 1);
					msg = node+" abnormal behaviour detected. Trying to auth using previous hash";
				}
				else {
					pow.tokens.add(hash);
					msg = node+" normal transaction observed";
				}
			} else {
				POW p = new POW();
				p.setNode(node);
				p.data.add(enc);
				p.tokens.add(hash);
				IndustrialManager.pow.put(node,p);
				msg = node+" normal transaction observed";
			}
			updateTable();
			Object res[] = {msg};
			out.writeObject(res);
			out.flush();
		}
	}catch(Exception e){
        e.printStackTrace();
    }
}
}
