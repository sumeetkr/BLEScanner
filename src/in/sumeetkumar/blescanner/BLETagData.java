package in.sumeetkumar.blescanner;

import android.R.string;

public class BLETagData {

	private String macAddress= "";
	private String tagName = "";
	private int signalStrength;

	public BLETagData(String macAddress, String tagName, int signalStrength){
		this.macAddress = macAddress;
		this.tagName = tagName;
		this.signalStrength = signalStrength;
	}

	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public  String gettagName() {
		return tagName;
	}
	public  void settagName(String tagName) {
		this.tagName = tagName;
	}
	public int getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}
}
