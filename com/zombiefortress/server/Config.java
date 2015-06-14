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
	private ArrayList<String> banned;
	private File file;
	private BufferedWriter out;
	private boolean permissions;
	
	public Config(){
		banned = new ArrayList<String>();
		file = new File(getProgramPath()+"/plugins");
		file.mkdir();
		
		try {
			file = new File(getProgramPath()+"/bannedplayers.yml");
			if(!file.exists())
				file.createNewFile();
		} catch (IOException e2) {
			System.out.println("Failed to find/read/write To bannedplayers.yml");
			e2.printStackTrace();
		}
		
		try {
			file = new File(getProgramPath()+"/whitelistedplayers.yml");
			if(!file.exists())
				file.createNewFile();
		} catch (IOException e2) {
			System.out.println("Failed to find/read/write To whitelistedplayers.yml");
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
}
