package apitest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.Matchers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Task_03_HomeTask_REST_Automation {

	public static final String BASE_ROUTE_PET = "https://petstore.swagger.io/v2";

	public static final String BASE_ROUTE_USER = "https://jsonplaceholder.typicode.com/users";

	static final Path JASON_PATH_PET = Paths.get(System.getProperty("user.dir"), "src", "main", "resources",
			"petcreate.json");

	public static void main(String[] args) throws IOException {

		// pet create and validate
		String userRoute = "/pet";
		String jsonBody = Files.readString(JASON_PATH_PET);
		Response postPetResponse = RestAssured.given().baseUri(BASE_ROUTE_PET).contentType(ContentType.JSON).body(jsonBody)
				.post(userRoute).then().extract().response();

		postPetResponse.then().statusCode(200).contentType("application/json").body("category.name", Matchers.equalTo("dog"))
		.body("name", Matchers.equalTo("snoopie"));
		System.out.println("All test cases validated for created pet" + postPetResponse.asPrettyString());

		// user fetch and validate
		Response getUserResponse = RestAssured.given().baseUri(BASE_ROUTE_USER).when().get().then().extract()
				.response();
		getUserResponse.then().statusCode(200).and().body("size()", Matchers.greaterThan(3)).body("name",
				Matchers.hasItem("Ervin Howell"));

		System.out.println("All test cases validated for fetched user" + getUserResponse.asPrettyString());

	}
}
