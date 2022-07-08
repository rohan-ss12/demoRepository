package package1;


import org.testng.annotations.Test;

import files.ResponseBodies;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class FirstAPI {
	
	
	@Test
	public void Test1() {
		
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response=given().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(ResponseBodies.body1()).when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.extract().response().asString();
		
		System.out.println("Post Method");
		
		JsonPath js = new JsonPath(response);
	
		
		String place_id = js.getString("place_id");
		
		given().header("Content-Type","application/json")
		.queryParam("place_id",place_id).queryParam("key","qaclick123")
		.when().get("/maps/api/place/get/json")
		.then().assertThat().statusCode(200).log().all();
		
		
		System.out.println("Get Method");
		
		given().header("Content-Type","application/json")
		.queryParam("place_id",place_id).queryParam("key","qaclick123")
		.body("{\r\n"
				+ "\"place_id\":\""+place_id+"\",\r\n"
				+ "\"address\":\"Ambegaon Pathar Pune\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}")
		.when().put("maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).extract().asString();
		
		given().header("Content-Type","application/json")
		.queryParam("place_id",place_id).queryParam("key","qaclick123")
		.when().get("/maps/api/place/get/json")
		.then().assertThat().statusCode(200).log().all();
		
		
		System.out.println("Put Method");
		
		
	}
	
	
	
	

}
