package com.zombiefortress.server;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class PluginManager{
	
	public ArrayList<ServerPlugin> plugins;
	
	public PluginManager(){
		plugins = new ArrayList<ServerPlugin>();
		loadPlugins();
		if(Server.getConfig().isPluginsEnabled()){
			onEnable();
		}
	}

	public void onEnable() {
		for(ServerPlugin plugin : plugins){
			if(plugin.isEnabled())
				plugin.onEnable();
		}
	}
	
	public void onDisable() {
		for(ServerPlugin plugin : plugins){
			plugin.onDisable();
		}
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
					
					if(instance instanceof ServerPlugin){
						ServerPlugin plugin = (ServerPlugin)instance;
						
						name = plugin.getName();
						version = plugin.getVersion();
						author = plugin.getAuthor();
						
						if(getPlugin(name) != null){
							Server.sendMessage("[" + name + " " + version + "] Duplicate Plugin Name found! Only 1 will be enabled!");
							continue;
						}
						plugin.onEnable();
						
						Server.sendMessage("[" + name + " " + version + "] " +" Was loaded Succesfully");
						plugins.add((ServerPlugin)instance);
					}
				} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
					e.printStackTrace();
				}
				
			}
		}
		int enabled = 0;
		int disabled = 0;
		
		for(ServerPlugin plugin : plugins){
			if(plugin.isEnabled()){
				enabled++;
			}
			else{
				disabled++;
			}
		}

		Server.sendMessage("Plugins loaded: " + enabled + " Disabled: "+ disabled);
	}
	
	public ServerPlugin getPlugin(String name){
		for(ServerPlugin plugin : plugins){
			if(plugin.getName().equals(name)){
				return plugin;
			}
		}
		return null;
	}
}
