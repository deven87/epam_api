package apitest;

import java.util.Map;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import endpoints.Endpoints;
import io.restassured.response.Response;
import payload.PostResourcePayload;
import routes.UserRoutes;
public class Task_02_HomeTask_REST_Functional {

	public static void main(String[] args) throws JsonProcessingException {

		// get resource and verify its response code and body
		Response getResource = Endpoints.getResource(UserRoutes.POSTS, Map.of("id", "1"));
		verifyResourceResponseCode(getResource, 200);
		verifyResourceResponse(getResource, "userId", 1);

		// post resource and verify its code and body
		PostResourcePayload postResourcePayload = new PostResourcePayload.PostResourcePayloadBuilder().setUserId(1).setTitle("automation").setBody("devendra")
				.build();
		Response postResponse = Endpoints.createResource(UserRoutes.POSTS,
				new ObjectMapper().writeValueAsString(postResourcePayload));
		verifyResourceResponseCode(postResponse, 201);
		verifyResourceResponse(postResponse, "body", "devendra");
		System.out.println(postResponse.asPrettyString());

		// put resource and verify its code
		PostResourcePayload updateResourcePayload = new PostResourcePayload.PostResourcePayloadBuilder()
				.setUserId(1).setTitle("automation").setBody("devendrasingh").build();
		Response updateResponse = Endpoints.modifyResource(UserRoutes.POSTS,
				new ObjectMapper().writeValueAsString(updateResourcePayload), Map.of("id", "1"));
		verifyResourceResponseCode(updateResponse, 200);

		// delete resource and verify its code
		Response deleteResponse = Endpoints.deleteResource(UserRoutes.POSTS, Map.of("id", "1"));
		verifyResourceResponseCode(deleteResponse, 200);
		System.out.println(deleteResponse.asPrettyString());

	}

	/**
	 * verifyResourceCount
	 *
	 * @param response
	 * @param minExpectedCount
	 */
	public static void verifyResourceCount(Response response, int minExpectedCount) {
		int resourceCount = response.jsonPath().getList("$").size();
		System.out.println("resource Count " + resourceCount);
		MatcherAssert.assertThat("Verify response count ", resourceCount,
				Matchers.greaterThan(minExpectedCount));
		System.out.printf("Response data count %d is matched as expected as %d%n", resourceCount, minExpectedCount);
	}

	/**
	 * verifyResourceResponseCode
	 *
	 * @param response
	 * @param expectedResponseCode
	 */
	public static void verifyResourceResponseCode(Response response, int expectedResponseCode) {
		int responseCode = response.getStatusCode();
		MatcherAssert.assertThat("Verify response code ", responseCode, Matchers.equalTo(expectedResponseCode));
		System.out.printf("Expected response code %d is matched in response as %d%n", expectedResponseCode,
				responseCode);
	}

	/**
	 * verifyResourceResponse
	 *
	 * @param response
	 * @param field
	 * @param expectedValue
	 */
	public static void verifyResourceResponse(Response response, String field, Object expectedValue) {
		response.then().body(field, Matchers.equalTo(expectedValue));
		System.out.printf("Expected value for %s is matched in response as %s%n", field, expectedValue);
	}
}
