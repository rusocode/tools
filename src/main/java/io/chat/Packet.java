package io.chat;

import java.io.Serializable;
import java.util.*;

/**
 * Serializa el paquete para poder enviarlo a travez de la red como una secuencia de bytes.
 * 
 * @author Ru$o
 * 
 */

public class Packet implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean connected;
	private String nick, message, ip;
	private Map<String, String> map;

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

}
