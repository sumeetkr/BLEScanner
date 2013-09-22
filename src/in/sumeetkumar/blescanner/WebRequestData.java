package in.sumeetkumar.blescanner;

public class WebRequestData {
	public String phoneId;
	public long timeStamp;
	public String  tagUniqueName;
	public String tagId;
	public int tagSignalStrength;
	
	public WebRequestData(String phoneId, String tagUniqueName, int tagSignalStrength, String tagId){
		this.phoneId = phoneId;
		this.timeStamp = System.currentTimeMillis()/1000;
		this.tagUniqueName = tagUniqueName;
		this.tagSignalStrength = tagSignalStrength;
		this.tagId = tagId;
	}
}
