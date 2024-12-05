package apitest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Task_06_Jira_API_Automation {

	public static final String JIRA_DOMAIN = "https://karayladevendra.atlassian.net";
	public static final String BASE_ROUTE_JIRA_ISSUE = JIRA_DOMAIN + "/rest/api/3/issue";
	static final Path JASON_PATH_CREATE_ISSUE = Paths.get(System.getProperty("user.dir"), "src", "main", "resources",
			"issuecreatejira.json");
	static final Path JASON_PATH_UPDATE_ISSUE = Paths.get(System.getProperty("user.dir"), "src", "main", "resources",
			"issueupdatejira.json");

	public static void main(String[] args) throws IOException {

		String email = "karayla.devendra@gmail.com";

		// generate jira api token and add it in your system variables by name JIRATOKEN
		String userToken = System.getenv("JIRATOKEN");
		String auth = getBase64EncodedBasicAuthToken(email, userToken);

		// create issue
		String issueCreateJson = Files.readString(JASON_PATH_CREATE_ISSUE);
		Response postResponse = postResponse(issueCreateJson, auth);

		// store created issue id
		String createdIssueId = postResponse.then().extract().jsonPath().getString("id");

		// get the issue details for created issue
		getResponse(createdIssueId, auth);

		// attach image
		Path imagePath = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "bug.png");
		attachImage(imagePath, issueCreateJson, "SCRUM-4/attachments", auth);

		// update issue
		String updateIssuesJson = Files.readString(JASON_PATH_UPDATE_ISSUE);
		putResponse(updateIssuesJson, "SCRUM-4", auth);

		// delete issue
		deleteResponse("SCRUM-4", auth);
	}

	/**
	 * getResponse get Rest Assured Response
	 *
	 * @param baseURI
	 * @param userRoute  user route for your request
	 * @param authHeader base 64 encoded auth header
	 * @return Rest Assured Response
	 */
	public static Response getResponse(String issueId, String authHeader) {
		Response response = RestAssured.given().header("Authorization", authHeader)
				.baseUri(BASE_ROUTE_JIRA_ISSUE).when()
				.get(issueId).then().extract().response();
		return response;
	}

	/**
	 * postResponse
	 *
	 * @param body       json body
	 * @param authHeader username and password basic auth header
	 * @return RestAssured response
	 */
	public static Response postResponse(String body, String authHeader) {
		Response response = RestAssured.given().header("Authorization", authHeader).contentType(ContentType.JSON)
				.baseUri(BASE_ROUTE_JIRA_ISSUE).body(body).when()
				.post().then().extract()
				.response();
		System.out.println(response.asPrettyString());
		return response;
	}

	/**
	 * deleteResponse
	 *
	 * @param userRoute
	 * @param authHeader
	 */
	public static void deleteResponse(String userRoute, String authHeader) {
		Response response = RestAssured.given().header("Authorization", authHeader).baseUri(BASE_ROUTE_JIRA_ISSUE)
				.when().delete(userRoute).then().extract().response();
		System.out.println(response.asPrettyString());
	}

	/**
	 * putResponse
	 *
	 * @param body       update body
	 * @param userRoute
	 * @param authHeader
	 * @return RestAssured response
	 */
	public static Response putResponse(String body, String userRoute, String authHeader) {
		Response response = RestAssured.given().header("Authorization", authHeader).contentType(ContentType.JSON)
				.body(body)
				.baseUri(BASE_ROUTE_JIRA_ISSUE)
				.when().put(userRoute).then().extract().response();
		return response;
	}

	/**
	 * attachImage
	 *
	 * @param filePath
	 * @param body
	 * @param userRoute
	 * @param authHeader
	 * @return RestAssured response
	 */
	public static Response attachImage(Path filePath, String body, String userRoute, String authHeader) {
		File file = new File(filePath.toString());
		Response response = RestAssured.given().header("Authorization", authHeader)
				.header("X-Atlassian-Token", "no-check")
				.contentType(ContentType.MULTIPART)
				.multiPart(file)
				.baseUri(BASE_ROUTE_JIRA_ISSUE)
				.when().post(userRoute).then().extract().response();
		return response;

	}

	/**
	 * getBase64EncodedToken
	 *
	 * @param userName
	 * @param token
	 * @return base 64 encoded string
	 */
	private static String getBase64EncodedBasicAuthToken(String userName, String token) {
		String authHeader = userName + ":" + token;
		return "Basic " + new String(Base64.getEncoder().encode(authHeader.getBytes()));
	}
}
