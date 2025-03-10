package com;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;
import java.util.Random;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.JComboBox;
import java.util.HashMap;
import org.jfree.ui.RefineryUtilities;
public class Network extends JFrame{
	Sensor node;
	JPanel p1,p2;
	JButton b1,b2,b3,b4,b5;
	static JLabel l3;
	JLabel l1;
	JComboBox c1;
	Font f1;
	int size;
	int port;
	static HashMap<String,byte[]> keys = new HashMap<String,byte[]>();
	boolean flag;
	static HashMap<String,String> token = new HashMap<String,String>();
	static HashMap<String,String> lazy = new HashMap<String,String>();
	boolean run = false;
	static ArrayList<String> alldata = new ArrayList<String>();
	static ArrayList<String> process = new ArrayList<String>();
public Network(int sz,int p){
	super("Secure Routing");
	size = sz;
	port = p;
	f1 = new Font("Courier New",Font.BOLD,14);
	node = new Sensor();
	p1 = new JPanel();
	p1.setLayout(new BorderLayout());
	p1.add(node,BorderLayout.CENTER);
	p1.setBackground(new Color(119,69,0));
	getContentPane().add(p1,BorderLayout.CENTER);
	p2 = new JPanel();
	p2.setLayout(new MigLayout("wrap 1")); 
	
	l1 = new JLabel("Sensor ID");
	l1.setFont(f1);
	p2.add(l1,"span,split 3");
	c1 = new JComboBox();
	c1.setFont(f1);
	for(int i=1;i<=size;i++){
		c1.addItem("S"+Integer.toString(i));
	}
	p2.add(c1);

	l3 = new JLabel();
	l3.setFont(f1);
	p2.add(l3);

	b1 = new JButton("Get Keys");
	p2.add(b1,"span,split 5");
	b1.setFont(f1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Runnable r = new Runnable(){
				public void run(){
					setup();
					flag = true;
				}
			};
			new Thread(r).start();
		}
	});

	b2 = new JButton("Generate Transactions");
	p2.add(b2);
	b2.setFont(f1);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			if(flag){
				Runnable r = new Runnable(){
					public void run(){
						send();
					}
				};
				new Thread(r).start();
			}else{
				JOptionPane.showMessageDialog(Network.this,"Run GetKeys first");
			}
		}
	});

	b3 = new JButton("Stop Transactions");
	p2.add(b3);
	b3.setFont(f1);
	b3.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			run = false;
		}
	});

	b5 = new JButton("Processed Vs Unprocessed data storage size");
	p2.add(b5);
	b5.setFont(f1);
	b5.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Chart chart1 = new Chart("Processed Vs Unprocessed Data Storage Size Graph");
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	b4 = new JButton("Exit Simulation");
	p2.add(b4);
	b4.setFont(f1);
	b4.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			System.exit(0);
		}
	});
	
	getContentPane().add(p2,BorderLayout.SOUTH);
	Node.randomNodes(size,800,600,node,400);
	node.option = 0;
	node.repaint();
}
public int getRandom(int size) {
	Random r = new Random();
	return r.nextInt(size);
}
public void send() {
	process.clear();
	alldata.clear();
	try{
		run = true;
		while(run) {
			int amt = getRandom(200);
			if(amt < 150) {
				process.add(amt+"");
				String source = "S"+getRandom(node.circles.size());
				String transaction = "Transaction Amount : "+amt;
				SendTransactions st = new SendTransactions(node,source,transaction);
				st.join();
			}else{
				l3.setText("Corrupted data detected : "+amt);
			}
			alldata.add(amt+"");
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public void setup(){
	try{
		for(int i=1;i<=size;i++){
			String source = "S"+i;
			GetPublicKeys ns = new GetPublicKeys(node,source);
			ns.join();
		}
		JOptionPane.showMessageDialog(this,"Key Receiving Process completed");
	}catch(Exception e){
		e.printStackTrace();
	}
}
}