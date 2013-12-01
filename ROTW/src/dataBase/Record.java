package dataBase;

import java.text.DecimalFormat;

public class Record {
	
	private String date;
	private String time;
	private float xDegree;
	private float xMin;
	private float xSec;
	private float yDegree;
	private float yMin;
	private float ySec;
	private float value;
	private int sensor_id;
	
	public Record(String datetime, float xDegree, float xMin, float xSec, float yDegree, float yMin, float ySec, float value, int sensor_id) {
		String[] splitter = datetime.split(" ");
		this.date = splitter[0].replace("-", "/");
		this.time = splitter[1];
		this.xDegree = xDegree;
		this.xMin = xMin;
		this.xSec = xSec;
		this.yDegree = yDegree;
		this.yMin = yMin;
		this.ySec = ySec;
		this.value = value;
		this.sensor_id = sensor_id;
	}
	
	public int getSensor_id() {
		return sensor_id;
	}

	public void setSensor_id(int sensor_id) {
		this.sensor_id = sensor_id;
	}

	public String getDate() {
		return date.replace("/", "-");
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public String toString() {
		return this.date + ";" + this.time + ";" + new DecimalFormat("0.##").format(this.xDegree) + "\u00b0" + 
				new DecimalFormat("0.##").format(this.xMin) + "'" + new DecimalFormat("0.##").format(this.xSec) + "\"" + "-" +
				new DecimalFormat("0.##").format(this.yDegree) + "\u00b0" + new DecimalFormat("0.##").format(this.yMin)+
				"'" + new DecimalFormat("0.##").format(this.ySec) + "\";" + new DecimalFormat("0.##").format(this.value);
	}
}
