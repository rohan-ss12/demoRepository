package package1;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.ResponseBodies;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;


public class DynamicJson {
	
	@Test(dataProvider="BooksData")
	
	public void addBook(String BookName, String ISBN, String AISLE) {
		
		
		// Add Book API
		
		RestAssured.baseURI = "http://216.10.245.166";
		String responseBody = given().header("Content-Type","application/json").body(ResponseBodies.addBookPayload(BookName,ISBN,AISLE)).when().
				post("/Library/Addbook.php").then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js  = new JsonPath(responseBody);
		
		String id = js.get("ID");
		
		System.out.println(id);
		
		//Delete Book API
		
		String resposnseBody2 = given().header("Content-Type","application/json")
				.body("{\"ID\" : \""+id+"\"}").when().post("/Library/DeleteBook.php")
				.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js2 = new JsonPath(resposnseBody2);
		
		String msg = js2.get("msg");
		System.out.println("Book with ID "+id+ " : " +msg);
		
				
		
	}
	
	
	@DataProvider (name="BooksData")
	public Object[][] booksData() {
		
		return new Object[][] {{"Dark Hold","DKHL","396"},{"Book of Vishanti","BOV","397"},{"Book of Magic","BOM","398"}};
	}

}
