package bee;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

@Service
public class BeeService {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public int updateACL(String dOCID, String cREATOR, String uSER, String pERM, String tOUPDATEPERM)throws Exception {
		System.out.println("DOC ID:" + dOCID);
		System.out.println("CREATOR: " + cREATOR);
		System.out.println("USER: " + uSER);
		System.out.println("TOUPDATE: " + tOUPDATEPERM);
//		WriteResult wrupdate2 = null;
		int value=0;
		
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection("default");
//		System.out.println(collection.getName());

		
		Criteria C1 = new Criteria("ecm:id").is(dOCID);			
		
		MatchOperation matchStage = Aggregation.match(C1);	
		
		DBObject project = (DBObject) new BasicDBObject("$project", 
				new BasicDBObject("ecm:acp.acl", 1).append("_id", 0));
		
		Aggregation aggregation = Aggregation.newAggregation(matchStage, new CustomGroupOperation(project));
		
		AggregationResults<DBObject> Output = mongoTemplate.aggregate(
				aggregation, "default", DBObject.class);
//		System.out.println(Output.getMappedResults());
				
		try {
		List<DBObject> newOutput = Output.getMappedResults();
		DBObject acp = newOutput.get(0);
		BasicDBList acpObject = (BasicDBList) acp.get("ecm:acp");
//		System.out.println(acpObject);
		for (Object obj: acpObject) {
			JSONObject acl = new JSONObject(obj.toString());
			JSONArray aclArray = (JSONArray) acl.get("acl");
//			System.out.println(aclArray);
			
			for(int i = 0; i < aclArray.length(); i++) {
				JSONObject obj2 = (JSONObject) aclArray.get(i);
				boolean isCreator = obj2.isNull("creator");
				if(!isCreator) {
				if(obj2.get("creator").toString().contains(cREATOR)
						&& obj2.get("user").toString().contains(uSER)
						&& obj2.get("perm").toString().contains(pERM)) {
					System.out.println(obj2);
					
					DBObject permObj = (DBObject) JSON.parse(obj2.toString());
					DBObject ecmID = new BasicDBObject("ecm:id", dOCID);
					System.out.println(ecmID);
					DBObject pull = new BasicDBObject("$pull", new BasicDBObject("ecm:acp.0.acl", permObj));
					System.out.println(pull);
					
					WriteResult wrupdate = collection.update(ecmID, pull);
					System.out.println(wrupdate.getError());					
					System.out.println(wrupdate.getN());
					
					permObj.put("perm", tOUPDATEPERM);
					permObj.removeField("_id");
					System.out.println("PULL RESULT: " + wrupdate);
					if (wrupdate.getN() > 0) {
						DBObject push = new BasicDBObject("$push", new BasicDBObject("ecm:acp.0.acl", permObj));
						WriteResult	wrupdate2 = collection.update(ecmID, push);
						System.out.println("PUSH RESULT: " + wrupdate2);
						value=wrupdate2.getN();
					}else {
						
						System.out.println("Permission not updated");
						return value;
					}
					
				}//else {
//					System.out.println("Data not found");
////					return -1;
//					return value;			
//				}
				}
				
			}
				
		}
	}catch (Exception e) {
		System.out.println("ERROR: " + e);
		return value;
	}
		return value;
	
	}
	
	public int deleteUser(String dOCID, String cREATOR, String uSER, String pERM)throws Exception {
		
//		WriteResult wrupdate2 = null;
		int value=0;
		
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection("default");
//		System.out.println(collection.getName());

		
		Criteria C1 = new Criteria("ecm:id").is(dOCID);			
		
		MatchOperation matchStage = Aggregation.match(C1);	
		
		DBObject project = (DBObject) new BasicDBObject("$project", 
				new BasicDBObject("ecm:acp.acl", 1).append("_id", 0));
		
		Aggregation aggregation = Aggregation.newAggregation(matchStage, new CustomGroupOperation(project));
		
		AggregationResults<DBObject> Output = mongoTemplate.aggregate(
				aggregation, "default", DBObject.class);
//		System.out.println(Output.getMappedResults());
				
		try {
		List<DBObject> newOutput = Output.getMappedResults();
		DBObject acp = newOutput.get(0);
		BasicDBList acpObject = (BasicDBList) acp.get("ecm:acp");
//		System.out.println(acpObject);
		for (Object obj: acpObject) {
			JSONObject acl = new JSONObject(obj.toString());
			JSONArray aclArray = (JSONArray) acl.get("acl");
//			System.out.println(aclArray);
			
			for(int i = 0; i < aclArray.length(); i++) {
				JSONObject obj2 = (JSONObject) aclArray.get(i);
				boolean isCreator = obj2.isNull("creator");
				if(!isCreator) {
				if(obj2.get("creator").toString().contains(cREATOR)
						&& obj2.get("user").toString().contains(uSER)
						&& obj2.get("perm").toString().contains(pERM)) {
					System.out.println(obj2);
					
					DBObject permObj = (DBObject) JSON.parse(obj2.toString());
					DBObject ecmID = new BasicDBObject("ecm:id", dOCID);
					System.out.println(ecmID);
					DBObject pull_acl = new BasicDBObject("$pull", new BasicDBObject("ecm:acp.0.acl", permObj));
					System.out.println(pull_acl);
					
					WriteResult wrupdate = collection.update(ecmID, pull_acl);
					System.out.println(wrupdate.getError());					
					System.out.println(wrupdate.getN());
					
//					permObj.put("perm", tOUPDATEPERM);
					permObj.removeField("_id");
					System.out.println("PULL RESULT: " + wrupdate);
					if (wrupdate.getN() > 0) {
						DBObject push_racl = new BasicDBObject("$pull", new BasicDBObject("ecm:racl", uSER));
						WriteResult	wrupdate2 = collection.update(ecmID, push_racl);
						System.out.println("PUSH RESULT: " + wrupdate2);
						value=wrupdate2.getN();
					}else {
						
						System.out.println("No User to DELETE");
						return value;
					}
					
				}//else {
//					System.out.println("Data not found");
////					return -1;
//					return value;			
//				}
				}
				
			}
				
		}
	}catch (Exception e) {
		System.out.println("ERROR: " + e);
		return value;
	}
		return value;
	
	}

	public boolean insertUpdateAudit(String uSERID, UpdateAudit updateAudit) {
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection("AMAZE_CC_AUDIT");
		boolean writeresult = false;
		
		String getACCNo = getAccountNo(updateAudit);
		Date datecreated = new Date();
		
		BasicDBObject insertdata = new BasicDBObject();
		
		if (getACCNo != null) {
			insertdata.put("USERID", uSERID);
			insertdata.put("CATEGORY", updateAudit.getNewCategory());
			insertdata.put("SOURCE_ACCOUNT_NUMBER", getACCNo);
			insertdata.put("CREATED_DATE", datecreated);
			
			WriteResult writeResult = collection.insert(insertdata);
			writeresult = true;
			System.out.println(writeResult);
			
//			writeresult =  writeResult.getN();
		}
//		insertdata.put("USERID", uSERID);
//		insertdata.put("CATEGORY", updateAudit.getNewCategory());
//		insertdata.put("SOURCE_ACCOUNT_NUMBER", getACCNo);
//		insertdata.put("CREATED_DATE", datecreated);
//		
//		WriteResult writeResult = collection.insert(insertdata);
//		
//		System.out.println(writeResult);
//		
		return writeresult;
		
		// TODO Auto-generated method stub
		
	}

	private String getAccountNo(UpdateAudit updateAudit) {
		DB db = mongoTemplate.getDb();
		DBCollection collection = db.getCollection("AMAZE_TRANSACTION_TIMELINE");
		
		Criteria c1 = new Criteria("EVENTID").is(updateAudit.getEventId());
		Criteria c2 = new Criteria("SOURCE_SYSTEM_CODE").is("30");
		
		MatchOperation matchstage = Aggregation.match(c1.andOperator(c2));
		
		DBObject project = new BasicDBObject("$project", new BasicDBObject("SOURCE_ACCOUNT_NUMBER", 1).append("_id", 0));
		
		Aggregation aggregation = Aggregation.newAggregation(matchstage, new CustomGroupOperation(project));
		
		AggregationResults<DBObject> output = mongoTemplate.aggregate(aggregation, "AMAZE_TRANSACTION_TIMELINE", DBObject.class);
		System.out.println("output data ="+output);
		System.out.println("output data ="+output.getMappedResults().size());
		int size=output.getMappedResults().size();
		String source_acc_no=null;
		if(size>0) {
		source_acc_no = output.getMappedResults().get(0).get("SOURCE_ACCOUNT_NUMBER").toString();
		}
		// TODO Auto-generated method stub
		return source_acc_no;
	}

}
