package com.zombiefortress.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

import com.zombiefortress.server.Command.Command;
import com.zombiefortress.server.Command.HelpCommand;
import com.zombiefortress.server.Command.ListCommand;
import com.zombiefortress.server.Command.Sender;
import com.zombiefortress.server.Command.SetObjectCommand;
import com.zombiefortress.server.Command.StopCommand;
import com.zombiefortress.server.Command.VersionCommand;
import com.zombiefortress.server.Event.EventHandler;
import com.zombiefortress.server.Event.EventPlayerLogin;
import com.zombiefortress.server.Object.BaseObject;
import com.zombiefortress.server.Object.EntityZombie;


public class Server {

	private static  ArrayList<Player> forremoveplayers = null;
	private static ArrayList<EventHandler> eventhandlers;
	private static PluginManager pluginmanager;
	private static Logger logger = Logger.getLogger("Server");
	private static String version = "INDEV_1.4";
	private static int buildno = 20;
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

		long time = System.currentTimeMillis();

		forremoveplayers = new ArrayList<Player>();
		packets = new ArrayList<Packet>();
		players = new ArrayList<Player>();
		commands = new ArrayList<Command>();

		sendMessage("Server version: " + version);
		sendMessage("Server build: " + buildno);
		sendMessage("System Date: " + calendar.getTime().getDate()+"/"+calendar.getTime().getMonth()+"/"+calendar.getTime().getYear());
		
		try {
			sendMessage("Starting server on port: " + config.getPort());
			socket = new DatagramSocket(config.getPort());
		} catch (SocketException e) {
			sendMessage("Failed to create new instance of socket. Server already started on that port?");
			e.printStackTrace();
			stopRequested = true;
		}

		if(stopRequested){return;}

		commands.add(new HelpCommand());
		commands.add(new ListCommand());
		commands.add(new StopCommand());
		commands.add(new VersionCommand());
		commands.add(new SetObjectCommand());

		pluginmanager = new PluginManager();

		world = new World();
		world.getObjects().add(new EntityZombie(256,256));

		receivePackets();
		commandListenerThread();
		updateThread();

		sendMessage("Server started in: " + (System.currentTimeMillis() - time) +" Milliseconds");
		sendMessage("Type 'Help' For a list of commands");
		sendMessage("Please use the command 'stop' To stop the server");

		world.update();
	}

	public static void sendMessage(String message){
		Date date = Calendar.getInstance().getTime();
		System.out.println("[" + date.getHours()+":"+date.getMinutes()+":"+date.getSeconds()+" INFO ] " + message);
	}

	public static ArrayList<Player> getPlayers(){
		return players;
	}

	public static void sendPacket(Packet p, Player player){
		sendPacket(p.getType(), p.getData(), player);
	}

	public static void sendPacket(String type, String[] data, Player player){
		sendPacket(type, data.toString(), player);
	}

	public static void sendPacket(String type, String data, Player player){
		try {
			if(!socket.isClosed()){
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
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	private static void updateThread(){
		new Thread(){
			public void run(){
				
				while(!stopRequested){
					ArrayList<Packet> forremoval = new ArrayList<Packet>();
					world.update();
					for(Packet p : packets){
						String[] data = p.getData();
						if(p.getType().equals("login")){
							forremoval.add(p);
							Player player = null;
							try {
								player = new Player(InetAddress.getByName(data[1].replaceAll("/", "")), Integer.parseInt(data[2]), data[0]);
							} catch (NumberFormatException | UnknownHostException e) {
								e.printStackTrace();
							}
							Server.addPlayer(player);
							Server.sendMessage(("Player: " + player.getName() + " Logged in with the ip: " + player.getAddress()).trim());
						}
					}
					for(Packet p : forremoval){
						packets.remove(p);
					}
					for(Player p : forremoveplayers){
						players.remove(p);
					}
					
					forremoveplayers.clear();
					
					for(Player p : players){
						if(System.currentTimeMillis() - p.getLastKeepAlive() >= 20000){
							kickPlayer(p, "You failed to send a keep alive packet.");
						}
					}
					try {
						Thread.sleep(33);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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

	public static void kickPlayer(Player p, String reason){
		if(players.contains(p)){
			forremoveplayers.add(p);
			sendMessage(p.getName() + " Was Kicked for " + reason);
			sendPacket("kick", reason, p);
			for(Player player : players){
				sendPacket("ru", p.getName(), player); 
			}
		}
	}

	public static void addPlayer(Player p){
		if(getPlayer(p.getName()) == null){
			for(Player player : players){
				sendPacket("cu", p.getName(), player);
			}
			players.add(p);
			Server.sendWorld(p);
		}
		else{
			sendPacket("kick", "This player has already connected.", p);
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
					if(type.equalsIgnoreCase("ka")){
						getPlayer(receivePacket.getAddress(), receivePacket.getPort()).keepAlive();
					}
					if(type.equalsIgnoreCase("login")){
						Player p = new Player(receivePacket.getAddress(),receivePacket.getPort(),dataname[1].trim());
						new EventPlayerLogin(p).callEvent();
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
							int x = (int) Float.parseFloat(data[0].trim());
							int y = (int) Float.parseFloat(data[1].trim());
							player.moveX(x);
							player.moveY(y);
							for(Player p : players){
								if(p != null){
									if(p != player){
										sendPacket("pm", player.getName() + "," + player.getPosX() + "," + player.getPosY(), p);
									}
								}
							}
						}
					}
					if(type.equalsIgnoreCase("cd")){
						String[] data = dataname[1].split(",");
						if(data.length == 4){
							int ID = Integer.parseInt(data[0].trim());
							int x = Integer.parseInt(data[1].trim());
							int y = Integer.parseInt(data[2].trim());
							String changeddata = data[3];

							for(BaseObject obj : world.getObjects()){
								if(obj.getPosX() == x){
									if(obj.getPosY() == y){
										if(obj.getID() == ID){
											obj.setData(changeddata);
										}
									}
								}
							}
						}
					}

				}
			}		
		}.start();

	}

	public static void sendWorld(final Player p) {
		new Thread(){
			@Override
			public void run(){

				int objcount = 0;

				for(BaseObject obj : world.getObjects()){
					objcount++;
				}

				sendPacket("total", ""+objcount, p);

				for(final BaseObject obj : world.getObjects()){

					new Thread(){
						@Override
						public void run(){
							if(obj.getID() <= 70){
								sendPacket("so",obj.getID()+","+obj.getPosX()+","+obj.getPosY(), p);
							}
							if(obj.getID() == 100){
								EntityZombie zombie = (EntityZombie) obj;
								sendPacket("az", zombie.getUID() + "," + zombie.getPosX() + "," + zombie.getPosY(), p);
							}
						}
					}.start();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		}.start();
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

	public static Player getPlayer(String name){
		for(Player p : players){
			if(p.getName().equals(name)){
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
		pluginmanager.onDisable();
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

	public PluginManager getPluginManager(){
		return pluginmanager;
	}

	public static ArrayList<EventHandler> getEventHandlers(){
		return eventhandlers;
	}

	public static void registerEventHandler(EventHandler handler){
		if(eventhandlers == null)
			eventhandlers = new ArrayList<EventHandler>();
		eventhandlers.add(handler);
	}
	
	public static ArrayList<Packet> getPackets(){
		return packets;
	}
}
