package com.zombiefortress.server.Event;

import com.zombiefortress.server.Server;

public class Event {
	
	private String name;
	private boolean cancelled = false;
	
	public Event(String name){
		this.name = name;
	}
	
	public String getEventName(){
		return name;		
	}
	
	public void setCanceled(){
		cancelled = true;
	}
	
	public void callEvent(){
		if(Server.getEventHandlers() != null){
			for(EventHandler handler : Server.getEventHandlers()){
				handler.handleEvent(this);
			}
		}
		if(!cancelled){
			activateEvent();
		}
	}
	
	public void activateEvent(){}
}
