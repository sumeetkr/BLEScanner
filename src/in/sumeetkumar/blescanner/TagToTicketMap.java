package in.sumeetkumar.blescanner;

import java.util.HashMap;
import java.util.Map;

public class TagToTicketMap {

//	00:18:31:EF:7D:FC : Kensington Eureka 7DFC
//	00:18:30:EB:54:87 : Kensington Eureka 5487
//	00:18:31:F0:F4:B2 : Kensington Eureka F4B2
//	00:18:31:EF:80:C9 : Kensington Eureka 80C9

	private Map<String, String> map;
	
	public TagToTicketMap(){
	 map = new HashMap<String, String>();
	 	map.put("00:18:30:EB:49:38", "EK7DFC");
	 	map.put("00:18:30:EB:54:87", "EK5487");
	 	map.put("00:18:31:F0:F4:B2", "EKF4B2");
	 	map.put("00:18:31:EF:80:C9", "EK80C9");
	}
	
	public String getTicket(String tagId){
		String ticket = "NA";
		if(map.containsKey(tagId)){
			ticket = map.get(tagId);
		}
		return ticket;
	}
}
