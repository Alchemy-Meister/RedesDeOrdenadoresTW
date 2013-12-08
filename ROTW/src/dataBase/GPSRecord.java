package dataBase;

import java.text.DecimalFormat;

public class GPSRecord {
	
	private float xDegree;
	private float xMin;
	private float xSec;
	private float yDegree;
	private float yMin;
	private float ySec;
	private int gps_id;
	
	public GPSRecord(float xDegree, float xMin, float xSec, float yDegree, float yMin, float ySec, int gps_id) {
		this.xDegree = xDegree;
		this.xMin = xMin;
		this.xSec = xSec;
		this.yDegree = yDegree;
		this.yMin = yMin;
		this.ySec = ySec;
		this.gps_id = gps_id;
	}
	
	public int getGPS_id() {
		return gps_id;
	}

	public void setGPS_id(int sensor_id) {
		this.gps_id = sensor_id;
	}

	public float getxDegree() {
		return xDegree;
	}

	public void setxDegree(float xDegree) {
		this.xDegree = xDegree;
	}

	public float getxMin() {
		return xMin;
	}

	public void setxMin(float xMin) {
		this.xMin = xMin;
	}

	public float getxSec() {
		return xSec;
	}

	public void setxSec(float xSec) {
		this.xSec = xSec;
	}

	public float getyDegree() {
		return yDegree;
	}

	public void setyDegree(float yDegree) {
		this.yDegree = yDegree;
	}

	public float getyMin() {
		return yMin;
	}

	public void setyMin(float yMin) {
		this.yMin = yMin;
	}

	public float getySec() {
		return ySec;
	}

	public void setySec(float ySec) {
		this.ySec = ySec;
	}

	public String toString() {
		return new DecimalFormat("0.##").format(this.xDegree) + "\u00b0" + 
				new DecimalFormat("0.##").format(this.xMin) + "'" + new DecimalFormat("0.##").format(this.xSec) + "\"" + "-" +
				new DecimalFormat("0.##").format(this.yDegree) + "\u00b0" + new DecimalFormat("0.##").format(this.yMin)+
				"'" + new DecimalFormat("0.##").format(this.ySec) + "\"";
	}
}
