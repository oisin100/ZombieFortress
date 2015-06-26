package com.zombiefortress.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;

public class Config {
	
	private int port;
	private int maxplayers;	
	private int viewdistance;
	private boolean pluginsEnabled;
	private boolean permissions;
	
	private ArrayList<String> banned;
	private File file;
	private BufferedWriter out;	
	
	public Config(){
		banned = new ArrayList<String>();
		file = new File(getProgramPath()+"/plugins");
		file.mkdir();
		
		try {
			file = new File(getProgramPath()+"/banned-players.yml");
			if(!file.exists())
				file.createNewFile();
			file = new File(getProgramPath()+"/whitelisted-players.yml");
			if(!file.exists())
				file.createNewFile();
		} catch (IOException e2) {
			System.out.println("Failed to find/read/write To file");
			e2.printStackTrace();
		}
		
		file = new File(getProgramPath()+"/config.yml");
		if(!file.exists()){
			try {
				file.createNewFile();
				out = new BufferedWriter(new FileWriter(file));
				out.write("port: 8090");
				out.newLine();
				out.append("maxplayers: 20");
				out.newLine();
				out.append("whitelist: false");
				out.newLine();
				out.append("permissions: false");
				out.newLine();
				out.append("plugins: true");
				out.newLine();
				out.append("view-distance: 10");
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			this.maxplayers = 20;
			this.port = 8090;
		}
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			
			String strLine;

			while ((strLine = br.readLine()) != null)   {
				String[] data = strLine.split(":");
				if(data[0].equalsIgnoreCase("maxplayers")){
					this.maxplayers = Integer.parseInt(data[1].trim());
				}
				if(data[0].equalsIgnoreCase("port")){
					this.port = Integer.parseInt(data[1].trim());
				}
				if(data[0].equalsIgnoreCase("permissions")){
					if(data[1].trim().equalsIgnoreCase("true")){
						this.permissions = true;
					}
					else{
						this.permissions = false;
					}
				}
				if(data[0].equalsIgnoreCase("plugins")){
					if(data[1].trim().equalsIgnoreCase("true")){
						this.pluginsEnabled = true;
					}
					else{
						this.pluginsEnabled = false;
					}
				}
				if(data[0].equalsIgnoreCase("view-distance")){
					this.port = Integer.parseInt(data[1].trim());
				}
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	public static String getProgramPath(){
	      URL url = Server.class.getProtectionDomain().getCodeSource().getLocation();
	      String jarPath = null;
		try {
			jarPath = URLDecoder.decode(url.getFile(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	      String parentPath = new File(jarPath).getParentFile().getPath();
	      return parentPath;
	   }
	
	public int getPort(){
		return port;
	}
	
	public int getMaxPlayers(){
		return maxplayers;
	}
	
	public boolean isPermissionsEnabled(){
		return permissions;
	}
	
	public int getViewDistance(){
		return viewdistance;
	}
	
	public boolean isPluginsEnabled(){
		return pluginsEnabled;
	}
}
