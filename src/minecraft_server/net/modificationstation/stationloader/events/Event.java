package net.modificationstation.stationloader.events;

public class Event {
	public Event(Object...data){
		eventData = data;
	}
	public final String getEventType(){
		return getClass().getSimpleName();
	}
	protected Object[] eventData;
}
