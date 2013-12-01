package dataBase;

import util.Utilities;

public class Sensor {
	private int id;
	private String name;
	private boolean status;
	
	public Sensor(int id, String name, boolean status) {
		this.id = id;
		this.name = name;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String toString() {
		return this.getId() + " " + this.getName() + " " + Utilities.booleanToString(this.isEnabled());
	}	
}
