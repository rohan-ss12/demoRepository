package pojoClasses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import files.ResponseBodies;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import package1.Location_SubJson;

public class DemoClass {

	public static void main(String[] args) {
		
		
		pojo1 p1 = new pojo1();
		Location_SubJson LSJ = new Location_SubJson();
		
		p1.setAccuracy("30");
		p1.setAddress("Sahakar Nagar");
		p1.setLanguage("Marathi");
		p1.setName("Sharad Pawar");
		p1.setPhone_number("9930039200");
		p1.setTypes("Getter,Setters");
		p1.setWebsite("https://www.cowin.com");
		LSJ.setLatitude("20");
		LSJ.setLongitude("40");
		p1.setLocation(LSJ);
		
		
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		String response=given().queryParam("key", "qaclick123").header("Content-Type","application/json")
		//.body(ResponseBodies.body1())
				.body(p1)
		.when().post("maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP"))
		.extract().response().asString();
		
		System.out.println("Post Method");
		
		JsonPath js = new JsonPath(response);
	
		
		String place_id = js.getString("place_id");
		
		pojo1 p = given().header("Content-Type","application/json")
		.queryParam("place_id",place_id).queryParam("key","qaclick123").expect().defaultParser(Parser.JSON)
		.when().get("/maps/api/place/get/json").as(pojo1.class);
		
		System.out.println("\n*********\n*********\n**********\n**********");
		
		System.out.println(p.getLocation().getLatitude());
		System.out.println(p.getLocation().getLongitude());
		System.out.println(p.getAccuracy());
		System.out.println(p.getAddress());
		System.out.println(p.getLanguage());
		System.out.println(p.getName());
		System.out.println(p.getPhone_number());
		System.out.println(p.getTypes());
		System.out.println(p.getWebsite());
		
		

	}

}
