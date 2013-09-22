package in.sumeetkumar.blescanner;

import android.R.string;

public class BLETagData {

	private String macAddress= "";
	private String deviceName = "";
	private int signalStrength;

	public BLETagData(String macAddress, String deviceName, int signalStrength){
		this.macAddress = macAddress;
		this.deviceName = deviceName;
		this.signalStrength = signalStrength;
	}

	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public  String getDeviceName() {
		return deviceName;
	}
	public  void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public int getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) {
		this.signalStrength = signalStrength;
	}
}
