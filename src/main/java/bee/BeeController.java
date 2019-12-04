package bee;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class BeeController {
	@Autowired
	private BeeService beeService;
	
	
	@RequestMapping(value = "/updatePerm", method = RequestMethod.GET)
	public Map<String, Object> updatePerm(@RequestParam String DOCID,
			@RequestParam String PERM, @RequestParam String CREATOR,
			@RequestParam String TOUPDATEPERM, @RequestParam String USER) throws Exception{
		
		Map<String,Object> dataMap = new LinkedHashMap<>();
		dataMap.put("PERMISSION", PERM);
		dataMap.put("CREATOR", CREATOR);
		dataMap.put("ECM:ID", DOCID);
		dataMap.put("USER", USER);
		
		if (USER.isEmpty() || PERM.isEmpty() || CREATOR.isEmpty() || DOCID.isEmpty()) {
			dataMap.put("ERROR", "Parameter value is missing");
			dataMap.put("STATUS", 500);
		}else {
			int updateStatus = beeService.updateACL(DOCID, CREATOR, USER, PERM, TOUPDATEPERM);
			
			if (updateStatus > 0) {
				dataMap.put("STATUS", 200);
			}else {
				dataMap.put("STATUS", 500);
			}
		}

		return dataMap;
		
	}
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
	public Map<String, Object> deleteUser(@RequestParam String DOCID,
			@RequestParam String PERM, @RequestParam String CREATOR,
			@RequestParam String USER) throws Exception{
		
		Map<String,Object> dataMap = new LinkedHashMap<>();
		dataMap.put("PERMISSION", PERM);
		dataMap.put("CREATOR", CREATOR);
		dataMap.put("ECM:ID", DOCID);
		dataMap.put("USER", USER);
		
		if (USER.isEmpty() || PERM.isEmpty() || CREATOR.isEmpty() || DOCID.isEmpty()) {
			dataMap.put("ERROR", "Parameter value is missing");
			dataMap.put("STATUS", 500);
		}else {
			int updateStatus = beeService.deleteUser(DOCID, CREATOR, USER, PERM);
			
			if (updateStatus > 0) {
				dataMap.put("STATUS", 200);
			}else {
				dataMap.put("STATUS", 500);
			}
		}

		return dataMap;
		
	}
	
	@RequestMapping(value = "/insertCCAudit", method = RequestMethod.POST)
	public Map<String, Object> insertCCAudit(
			@RequestBody UpdateAudit updateAudit,
			@RequestParam String USERID, @RequestParam Double RRN,
			@RequestParam String MOBILE, @RequestParam String CHANNEL,
			int... attemptNumber) {
		System.out.println(USERID);
		System.out.println(updateAudit.getOldCategory());
		System.out.println(updateAudit.getNewCategory());
		System.out.println(updateAudit.getEventId());
		
		
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		USERID = USERID.toUpperCase();
		dataMap.put("USERID", USERID);
		dataMap.put("RRN", RRN);
		dataMap.put("CHANNEL", CHANNEL);
		dataMap.put("MOBILE", MOBILE);
		
		boolean updateAudit2 = beeService.insertUpdateAudit(USERID, updateAudit);
		
		if (updateAudit2) {
			dataMap.put("STATUS", 200);
		}else {
			dataMap.put("STATUS", 500);
		}
		
		return dataMap;
	}
	
	
}
