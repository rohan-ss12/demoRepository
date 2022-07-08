package package1;

import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

public class JIRA_API_Automation {
	
	@Test
	public void jiraAPI() {
		
		RestAssured.baseURI = "http://localhost:8080";
		
		SessionFilter session = new SessionFilter();
		
		//Login to the JIRA Portal
		
		String loginToJIRA_ResponseBody = given().body("{ \"username\": \"rohan.sonawane\", \"password\": \"#EDC$RFV%TGB6yhn\" }")
				.header("Content-Type","application/json")
				.filter(session)
				.when().post("/rest/auth/1/session")
				.then().assertThat().statusCode(200).extract().asString();
		
		JsonPath js1 = new JsonPath(loginToJIRA_ResponseBody);
		String SessionID = js1.get("session.value");
		System.out.println(SessionID);
		
		//Create issue in the JIRA Portal
		
//		String createIssue_ResponseBody = given().body("{\r\n"
//				+ "    \"fields\":{\r\n"
//				+ "        \"project\":{\r\n"
//				+ "            \"key\" : \"CCP\"\r\n"
//				+ "        },\r\n"
//				+ "        \"summary\": \"Credit Card Defect\",\r\n"
//				+ "        \"description\": \"My First Defect\",\r\n"
//				+ "        \"issuetype\":{\r\n"
//				+ "            \"name\": \"Story\"\r\n"
//				+ "        }\r\n"
//				+ "    }\r\n"
//				+ "}")
//				.header("Content-Type","application/json").header("Cookie","JSESSIONID="+SessionID)
//				
//				.when().post("/rest/api/2/issue")
//				.then().assertThat().statusCode(201).extract().asString();
//		
//		JsonPath js2 = new JsonPath(createIssue_ResponseBody);
//		
//		String issueID = js2.get("id");
//		String issueKey = js2.get("key");
//		
//		System.out.println("Issue ID for the created issue is "+issueID+" and Issue Key for the same is "+issueKey);
		
		
		//Adding comment to the existing Issue
		
		String expectedMessage = "How you doin?";
		
		String addComment_ResponseBody = given().body("{\r\n"
				+ "    \"body\": \""+expectedMessage+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}").pathParam("issueID", "10300")
				.header("Content-Type","application/json")//.header("Cookie","JSESSIONID="+SessionID)
				.filter(session)
				.when().post("/rest/api/2/issue/{issueID}/comment")
				.then().assertThat().statusCode(201).extract().asString();
		
		JsonPath js3 = new JsonPath(addComment_ResponseBody);
		
		String commentID = js3.get("id");
		int cmId = Integer.parseInt(commentID);
		
		System.out.println("Comment ID for the recently added comment is "+commentID);
		
		
		//Adding an Attachment to the Issue
		
		String addAttahcment_response = given().header("X-Atlassian-Token","no-check").filter(session)
		.pathParam("KeytoAddAttachment", "CCP-17")
		.header("Content-Type","multipart/form-data")
		.multiPart("attachmentFile",new File("C:\\Users\\rohan.sonawane\\Atlassian\\Application Data\\Jira\\data\\attachments\\Jira.txt"))
		
		.when().post("/rest/api/2/issue/{KeytoAddAttachment}/attachments")
		.then().assertThat().statusCode(200)
		.extract().asString();
		
		System.out.println("This is an responseBody for adding attachment API \n"+addAttahcment_response);
		
		
		//Get the information of the Issue
		
		String issueDetails = given().filter(session).pathParam("key", "10300").queryParam("fields", "comment")
		.when().get("/rest/api/2/issue/{key}")
		.then().log().all().assertThat().statusCode(200).extract().asString();
		
		JsonPath js4 = new JsonPath(issueDetails);
		
		int commentsCount = js4.get("fields.comment.comments.size()");
		
		for(int i = 0; i<commentsCount; i++) {
			
			String commentIdIssue = js4.get("fields.comment.comments["+i+"].id").toString();
			System.out.println("\ncheck this one \n"+commentIdIssue+"\n");
			int cmIdi = Integer.parseInt(commentIdIssue);
			
			if(cmId==cmIdi) {
				
				String actualMessage = js4.get("fields.comment.comments["+i+"].body").toString();
				System.out.println("this is Actual msg "+actualMessage);
				System.out.println("this is Expected msg "+expectedMessage);
				
				Assert.assertEquals(actualMessage, expectedMessage);
			}
			else {
				
				System.out.println("\n"+commentIdIssue+" is not equal to "+commentID+"\n");
				
				System.out.println("Actual message is not same as Expected Message");
			}
		}
		
		
	}
	
	
	

}
