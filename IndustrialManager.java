package com;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.jfree.ui.RefineryUtilities;
public class IndustrialManager extends JFrame{
	JLabel l1;
	JPanel p1,p2,p3;
	Font f1;
	JScrollPane jsp;
	JTable table;
	DefaultTableModel dtm;
	ServerSocket server;
	RequestHandler thread;
	JButton b1,b2;
	static HashMap<String,POW> pow = new HashMap<String,POW>();
	static JLabel status;
	static HashMap<String,byte[]> keys = new HashMap<String,byte[]>();
	
public void start(){
	try{
		server = new ServerSocket(3333);
		while(true){
			Socket socket = server.accept();
			socket.setKeepAlive(true);
			thread = new RequestHandler(socket,dtm);
			thread.start();
		}
	}catch(Exception e){
		e.printStackTrace();
	}
}
public IndustrialManager(){
	super("Industrial Manager Server");
	p1 = new JPanel();
	
	p1.setBackground(new Color(223,184,126,234));
	l1 = new JLabel("<HTML><BODY><CENTER>Industrial Manager Server & Key Distribution Center</CENTER></BODY></HTML>".toUpperCase());
	l1.setFont(new Font("Courier New",Font.BOLD,18));
	l1.setForeground(Color.blue);
	p1.add(l1);
	getContentPane().add(p1,BorderLayout.NORTH);

	f1 = new Font("Courier New",Font.BOLD,14);

	p2 = new JPanel();
	p2.setLayout(new BorderLayout());
	
	dtm = new DefaultTableModel(){
		public boolean isCellEditable(int r,int c){
			return false;
		}
	};
	table = new JTable(dtm);
	table.setFont(f1);
	table.getTableHeader().setFont(new Font("Courier New",Font.BOLD,16));
	jsp = new JScrollPane(table);
	p2.add(jsp,BorderLayout.CENTER);
	dtm.addColumn("Node ID");
	dtm.addColumn("Total Transactions");
	dtm.addColumn("Double Spending");
	dtm.addColumn("Node Behaviour Weight");
	dtm.addColumn("POW Hash Value");
	dtm.addColumn("Symmetric Encrypted Value");

	p3 = new JPanel();
	b1 = new JButton("Nodes Behaviour Chart");
	b1.setFont(f1);
	p3.add(b1);
	b1.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			Chart chart1 = new Chart("Nodes Behaviour Chart");
			chart1.pack();
			RefineryUtilities.centerFrameOnScreen(chart1);
			chart1.setVisible(true);
		}
	});

	b2 = new JButton("View Data");
	b2.setFont(f1);
	p3.add(b2);
	b2.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			String input = JOptionPane.showInputDialog(null,"Enter Sensor ID");
			if(input != null){
				try{
					POW p = pow.get(input);
					ViewData vd = new ViewData();
					byte key[] = keys.get(input);
					for(int i=0;i<p.data.size();i++){
						byte dec[] = AES.decrypt(p.data.get(i),key);
						Object row[] = {input,new String(p.data.get(i)),new String(dec)};
						vd.dtm.addRow(row);
					}
					vd.setVisible(true);
					vd.setSize(800,600);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	});

	status = new JLabel();
	status.setFont(f1);
	p3.add(status);
	
	getContentPane().add(p1,BorderLayout.NORTH);
	getContentPane().add(p2,BorderLayout.CENTER);
	getContentPane().add(p3,BorderLayout.SOUTH);
}
public static void main(String a[])throws Exception{
	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	IndustrialManager main = new IndustrialManager();
	main.setVisible(true);
	main.setSize(800,600);
	new ServiceThread(main);
}
}