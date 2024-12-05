package endpoints;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import routes.BaseRoutes;

public class Endpoints {

	/**
	 * getResource end point for GET resource
	 *
	 * @param resourceRoute
	 * @return RestAssured response
	 */
	public static Response getResource(String resourceRoute) {
		return RestAssured.given().baseUri(BaseRoutes.BASE_ROUTE).when().get(resourceRoute).then().extract()
				.response();
	}

	/**
	 * createResource endpoint for POST resource
	 *
	 * @param resourceRoute
	 * @param body
	 * @return RestAssured response
	 */
	public static Response createResource(String resourceRoute, String body) {
		return RestAssured.given().baseUri(BaseRoutes.BASE_ROUTE)
				.header("Content-type", "application/json; charset=UTF-8").body(body).when()
				.post(resourceRoute).then()
				.extract()
				.response();
	}

	/**
	 * modifyResource PUT resource
	 *
	 * @param resourceRoute
	 * @param body
	 * @param pathParam
	 * @return RestAssured response
	 */
	public static Response modifyResource(String resourceRoute, String body, Map<String, String> pathParam) {
		RequestSpecification requestSpec = RestAssured.given().baseUri(BaseRoutes.BASE_ROUTE);

		requestSpec = getRequestSpecificationWithPathParam(requestSpec, pathParam);

		String resourceWithPath = getPath(pathParam, resourceRoute);

		System.out.println(resourceWithPath);

		return requestSpec.when().put(resourceWithPath).then().extract().response();
	}

	/**
	 * modifyResourcePatch PATCH resource
	 *
	 * @param resourceRoute
	 * @param body
	 * @param pathParam
	 * @return RestAssured response
	 */
	public static Response modifyResourcePatch(String resourceRoute, String body, Map<String, String> pathParam) {
		RequestSpecification requestSpec = RestAssured.given().baseUri(BaseRoutes.BASE_ROUTE);

		requestSpec = getRequestSpecificationWithPathParam(requestSpec, pathParam);

		String resourceWithPath = getPath(pathParam, resourceRoute);

		return requestSpec.when().patch(resourceWithPath).then().extract().response();
	}

	/**
	 * getResource GET resource with path parameter map
	 *
	 * @param resourceRoute
	 * @param pathParam
	 * @return RestAssured response
	 */
	public static Response getResource(String resourceRoute, Map<String, String> pathParam) {

		RequestSpecification requestSpec = RestAssured.given().baseUri(BaseRoutes.BASE_ROUTE);

		requestSpec = getRequestSpecificationWithPathParam(requestSpec, pathParam);

		String resourceWithPath = getPath(pathParam, resourceRoute);

		return requestSpec.when().get(resourceWithPath).then().extract().response();
	}

	/**
	 * deleteResource DELETE resource with given path param map
	 *
	 * @param resourceRoute
	 * @param pathParam
	 * @return RestAssured response
	 */
	public static Response deleteResource(String resourceRoute, Map<String, String> pathParam) {

		RequestSpecification requestSpec = RestAssured.given().baseUri(BaseRoutes.BASE_ROUTE);

		requestSpec = getRequestSpecificationWithPathParam(requestSpec, pathParam);

		String resourceWithPath = getPath(pathParam, resourceRoute);

		return requestSpec.when().delete(resourceWithPath).then().extract().response();
	}

	/**
	 * getRequestSpecificationWithPathParam get RequestSpecification which has all
	 * path param map attached
	 *
	 * @param requestSpec
	 * @param pathParam
	 * @return RequestSpecification with path param
	 */
	private static RequestSpecification getRequestSpecificationWithPathParam(RequestSpecification requestSpec,
			Map<String, String> pathParam) {

		RequestSpecification updatedRequest = requestSpec;
		for (Map.Entry<String, String> entry : pathParam.entrySet()) {
			requestSpec.pathParam(entry.getKey(), entry.getValue());
		}
		return updatedRequest;
	}

	/**
	 * getPath get the user route appended with path param
	 *
	 * @param pathParam
	 * @param resourceRoute
	 * @return user route
	 */
	private static String getPath(Map<String, String> pathParam, String resourceRoute) {

		String routeWithPathParam = resourceRoute;
		for (Map.Entry<String, String> entry : pathParam.entrySet()) {
			routeWithPathParam += "/{" + entry.getKey() + "}";
		}
		return routeWithPathParam;
	}
}
