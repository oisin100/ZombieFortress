package com.zombiefortress.server;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.logging.Logger;

import com.zombiefortress.server.Command.Command;
import com.zombiefortress.server.Command.HelpCommand;
import com.zombiefortress.server.Command.ListCommand;
import com.zombiefortress.server.Command.Sender;
import com.zombiefortress.server.Command.SetObjectCommand;
import com.zombiefortress.server.Command.StopCommand;
import com.zombiefortress.server.Command.VersionCommand;
import com.zombiefortress.server.Object.BaseObject;


public class Server {

	private static Logger logger = Logger.getLogger("Server");

	private static String version = "INDEV_1.4";
	private static int buildno = 19;

	private static DatagramSocket socket;
	private static World world;
	private static ArrayList<Player> players;
	private static ArrayList<Packet> packets;
	private static ArrayList<Packet> removepackets;
	private static boolean stopRequested;
	private static ArrayList<Command> commands;
	private static Config config;

	public static void main(String[] args) {
		config = new Config();
		Calendar calendar = Calendar.getInstance();
		world = new World();
		long time = System.currentTimeMillis();

		packets = new ArrayList<Packet>();
		players = new ArrayList<Player>();
		commands = new ArrayList<Command>();
		
		commands.add(new HelpCommand());
		commands.add(new ListCommand());
		commands.add(new StopCommand());
		commands.add(new VersionCommand());
		commands.add(new SetObjectCommand());
		
		

		sendMessage("Server version: " + version);
		sendMessage("Server build: " + buildno);
		sendMessage("System Date: " + calendar.getTime().getDate()+"/"+calendar.getTime().getMonth()+"/"+calendar.getTime().getYear());

		try {
			socket = new DatagramSocket(config.getPort());
			sendMessage("Started server on port: " + socket.getLocalPort());
		} catch (SocketException e) {
			sendMessage("Failed to create new instance of socket. Server already started on that port?");
			
			stopRequested = true;
		}
		
		if(stopRequested){return;}
		
		PluginManager manager = new PluginManager();
		
		receivePackets();
		commandListenerThread();
		keepAliveThread();

		sendMessage("Server started in: " + (System.currentTimeMillis() - time) +" Milliseconds");
		sendMessage("Type 'Help' For a list of commands");
		sendMessage("Please use the command 'stop' To stop the server");
	}

	public static void sendMessage(String message){
		Date date = Calendar.getInstance().getTime();
		System.out.println("[" + date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+" INFO ] " + message);
	}
	
	public static ArrayList<Player> getPlayers(){
		return players;
	}

	public static void sendPacket(Packet p, Player player){
		String[] data = p.getData();
		sendPacket(p.getType(), p.getData(), player);
	}

	public static void sendPacket(String type, String[] data, Player player){
		sendPacket(type, data.toString(), player);
	}

	public static void sendPacket(String type, String data, Player player){

		new Thread(){

			public void run(){
				try {
					if(player.getAddress().isReachable(20000)){
						byte[] sendData = new byte[1024];
						sendData = (type +":"+ data).getBytes();
						DatagramPacket packet = new DatagramPacket(sendData, sendData.length, player.getAddress(), player.getPort());
						socket.send(packet);
					}
					else{
						System.out.println("Connection timed out");
						players.remove(player);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private static void keepAliveThread() {
		
		new Thread(){
			
			@Override
			public void run(){
				while(!stopRequested){
				}
			}
			
		}.start();
		
	}
	
	private static void commandListenerThread(){
		new Thread(){

			@Override
			public void run(){

				while(!stopRequested){
					String[] data = System.console().readLine("").split(" ");
					for(Command cmd : commands){
						if(cmd.getName().equalsIgnoreCase(data[0])){
							cmd.runCommand(new Sender(null), data);
						}
					}
					if(data[0].equalsIgnoreCase("setobject")){
						if(data.length == 4){
							int x = Integer.parseInt(data[1]);
							int y = Integer.parseInt(data[2]);
							int ID = Integer.parseInt(data[3]);
							for(Player p : players){
								sendPacket("so",ID + "," + x + "," + y, p);
							}
						}
					}
				}
				this.interrupt();
			}

		}.start();
	}
	
	public static void removePlayer(Player p){
		if(players.contains(p)){
			players.remove(p);
			for(Player player : players){
				sendPacket("ru", p.getName(), player); 
			}
		}
	}
	
	public static void addPlayer(Player p){
		if(!players.contains(p)){
			for(Player player : players){
				sendPacket("cu", p.getName(), player);
			}
			players.add(p);
		}
	}

	private static void receivePackets(){

		new Thread(){
			@Override
			public void run() {
				while(!stopRequested){
					byte[] receiveData = new byte[1024];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					try {
						socket.receive(receivePacket);
					} catch (IOException e) {}
					String modifiedSentence = new String(receivePacket.getData());
					String[] dataname = modifiedSentence.split(":");

					String type = dataname[0];

					if(type.equalsIgnoreCase("login")){
						Player p = new Player(receivePacket.getAddress(),receivePacket.getPort(),dataname[1].trim());
						//p.teleport(0, 0);
						addPlayer(p);
						System.out.println(("Player: " + p.getName() + " Logged in with the ip: " + p.getAddress()).trim());
						sendWorld(p);
					}

					if(type.equalsIgnoreCase("chat")){
						Player player = getPlayer(receivePacket.getAddress(), receivePacket.getPort());
						if(dataname[1].startsWith("/")){
							String[] data = dataname[1].split(" ");
							String cmdname = data[0];
							for(Command cmd : commands){
								if(cmd.getName().equalsIgnoreCase(cmdname)){
									cmd.runCommand(new Sender(player), data);
								}
							}
						}
						for(Player p : players){
							p.sendMessage("<" + player.getName() +">: " + dataname[1]);
						}
						System.out.println("<" + player.getName() +">: " + dataname[1]);
					}
					
					if(type.equalsIgnoreCase("pp")){
						Player player = getPlayer(receivePacket.getAddress(), receivePacket.getPort());
						if(player != null){
							String[] data = dataname[1].split(",");
							float x = Float.parseFloat(data[0].trim());
							float y = Float.parseFloat(data[1].trim());
							player.setX(x);
							player.setY(y);
							//.teleport(x, y);
							for(Player p : players){
							if(p != null){
								if(p != player){
									sendPacket("pm", player.getName() + "," + player.getPosX() + "," + player.getPosY(), p);
								}
							}
						}
						}
						
					}
				}
				this.interrupt();
			}		
		}.start();

	}

	protected static void sendWorld(Player p) {
		for(BaseObject obj : getWorld().getObjects()){
			sendPacket("so",obj.getID()+","+obj.getPosX()+","+obj.getPosY(), p);
		}
		sendPacket("finished","finished",p);
	}
	
	public static World getWorld(){
		return world;
	}
	
	public static Player getPlayer(InetAddress address, int port){
		for(Player p : players){
			if(p.getAddress().getHostAddress().equals(address.getHostAddress())){
				if(p.getPort() == port){				
					return p;
				}
			}
		}
		return null;
	}

	@Deprecated
	public static Player getPlayer(String name){
		for(Player p : players){
			if(p.getName().equals("name")){
				return p;
			}
		}
		return null;
	}

	public static void broadcastMessage(String message){
		System.out.println(message);
		for(Player p : players){
			p.sendMessage(message);
		}
	}

	public static void stopServer(){
		sendMessage("Server stopping.");
		socket.close();
		stopRequested = true;
	}

	public static String getVersion() {
		return version;
	}

	public static int getBuildNumber() {
		return buildno;
	}

	public static ArrayList<Command> getCommands() {
		return commands;
	}

	public static Config getConfig() {
		return config;
	}
	
	
}
