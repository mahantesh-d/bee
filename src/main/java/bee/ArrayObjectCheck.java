package bee;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonAnyFormatVisitor;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;

public class ArrayObjectCheck {

	public static void main(String[] args) throws Exception {
		
		MongoClient mongoClient = new MongoClient("localhost", 27018);
		DB db = mongoClient.getDB("nuxeo");
		
			
		DBCollection collection = db.getCollection("default");
	
		BasicDBObject whereQuery = new BasicDBObject();
	    whereQuery.put("ecm:id", "72b30cf0-f92c-4a27-8dda-6731adcac6cd");
	    
	    BasicDBObject project = new BasicDBObject();
	    project.put("ecm:acp.acl", 1);
	    project.put("_id", 0);
		
		DBCursor find = collection.find(whereQuery, project);
		
		
		try {
		
		while (find.hasNext()) {
			DBObject next = find.next();
			BasicDBList object = (BasicDBList) next.get("ecm:acp");
			for(Object obj: object) {
//				System.out.println(el);
				JSONObject acl = new JSONObject(obj.toString());
				JSONArray aclarray = (JSONArray) acl.get("acl");
//				System.out.println(aclarray.get(0));
//				System.out.println(aclarray);
				for(int i =0; i < aclarray.length(); i++) {
//					System.out.println(aclarray.get(i));
					JSONObject object2 = (JSONObject) aclarray.get(i);
//					System.out.println(object2.get("creator"));
					if(object2.length() > 4) {
						if(object2.get("perm").toString().contains("Everything")
								&& object2.get("creator").toString().contains("366135")
								&& object2.get("user").toString().contains("282980")){
							
							DBObject ins = (DBObject) JSON.parse(object2.toString());
							
//						System.out.println(object2.get("creator"));
//						System.out.println(object2);
//						DBObject ins = new BasicDBObject("CREATOR", object2.get("creator")).
//								append("PERM", object2.get("perm"));
						WriteResult insert2 = collection.insert(ins);
						System.out.println("FIRST INSERT: " + insert2);
						ins.put("perm", "read");
						ins.removeField("_id");
						WriteResult insert = collection.insert(ins);
						System.out.println("SECOND INSERT: " + insert);
					}
					
					}
					
				}
				
				
			}
			
		}
		
		}catch (Exception e) {
			System.out.println("ERROR: " + e);
		}
		


	}

}
