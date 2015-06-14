package com.zombiefortress.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class PluginManager{
	
	public ArrayList<ServerPlugin> plugins;
	
	public PluginManager(){
		plugins = new ArrayList<ServerPlugin>();
		loadPlugins();
		onEnable();
	}

	private void onEnable() {
		// TODO Auto-generated method stub
		
	}

	private void loadPlugins() {
		for(File file : new File(Config.getProgramPath() + "/plugins").listFiles()){
			String filetype = file.getName().substring(file.getName().lastIndexOf(".")+1);
			if(filetype.equalsIgnoreCase("jar")){
				URL url = null;
				try {
					url = file.toURI().toURL();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				URL[] urls = new URL[]{url};
				
				ClassLoader cl = new URLClassLoader(urls);
				Class main;
				
				String name = "Unknown";
				String version = "Unknown";
				String author = "Unknown";
				
				try {
					main = cl.loadClass("plugin.Main");
					Object instance = main.newInstance();
					
					
//					File pluginfile = new File(cl.getResource("plugin.yml").getPath());
//					System.out.println(cl.getResource("plugin.yml").toString());
//					if(pluginfile != null){
//						BufferedReader reader = new BufferedReader( new FileReader(pluginfile));
//						String line;
//						while((line = reader.readLine()) != null){
//							System.out.println(line);
//						}
//						
//						reader.close();
//					}

					if(instance instanceof ServerPlugin){
						ServerPlugin plugin = (ServerPlugin)instance;
						
						name = plugin.getName();
						version = plugin.getVersion();
						author = plugin.getAuthor();
						plugin.onEnable();
						
						Server.sendMessage("[" + name + " " + version + "] " +" Was loaded Succesfully");
						plugins.add((ServerPlugin)instance);
					}
				} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				}
				
			}
		}
		int i = 0;
		
		for(ServerPlugin plugin : plugins)
			i++;
		
		Server.sendMessage("Plugins loaded " + i + " Disabled 0");
	}

}
