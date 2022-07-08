package files;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	
	public static void main(String args[]) {
		
		JsonPath js = new JsonPath(ResponseBodies.complexJson());
		
		System.out.println(js.getInt("courses.size()"));
		System.out.println(js.getInt("dashboard.purchaseAmount"));
		System.out.println(js.getString("courses[0].title"));
		System.out.println(js.getString("courses[2].title"));
		
		for(int i = 0; i < js.getInt("courses.size()") ; i++) {
			
			System.out.print(js.getString("courses["+i+"].title"));
			System.out.print("\t");
			System.out.print(js.getInt("courses["+i+"].price"));
			System.out.print("\n");
		}
		
		for(int i = 0; i < js.getInt("courses.size()") ; i++) {
			
			System.out.println(js.getString("courses["+i+"].title"));
			
			if(js.getString("courses["+i+"].title").equals("RPA")) {
				
				int copies = js.getInt("courses["+i+"].copies");
				System.out.println("Number of copies sold for RPA course are : "+copies+".");
			}
		}
	
		int sum = 0;
	for(int i = 0; i < js.getInt("courses.size()") ; i++) {
		
		 sum = sum + (js.getInt("courses["+i+"].price")*js.getInt("courses["+i+"].copies"));
		
	}
	System.out.println("This is Total Amount : "+sum);
	
	if(js.getInt("dashboard.purchaseAmount")==sum) {
		
		System.out.println("This was expected");
	}
	else {
		
		System.out.println("This was not expected");
		
	}

}
}
	
