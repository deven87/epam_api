package apitest;

import org.hamcrest.Matchers;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Task_04_HomeTask_REST_Automation {

	public static final String BASE_ROUTE_WEATHER = "https://api.openweathermap.org/data/2.5/weather";

	public static void main(String[] args) throws Exception {

		String apiKey = System.getenv("WEATHER_API");

		if (apiKey == null) {
			throw new Exception("Please create api key and save it in system variable as WEATHER_API");
		}

		// get hyderabad weather
		Response hyderabadWeatherResponse = getWeatherByCityName("hyderabad", apiKey);
		System.out.println(hyderabadWeatherResponse.asPrettyString());

		// get lat and long from the first api response
		String longitude = hyderabadWeatherResponse.then().extract().jsonPath().getString("coord.lon");
		String latitude = hyderabadWeatherResponse.then().extract().jsonPath().getString("coord.lat");

		// get weather based on lat and long
		Response weatherResponseForLatLong = getWeatherByLatAndLong(latitude, longitude, apiKey);
		hyderabadWeatherResponse.then().body("name", Matchers.equalToIgnoringCase("hyderabad"))
		.and().body("sys.country", Matchers.equalTo("IN")).and()
		.body("main.temp_min", Matchers.greaterThan(0.0F)).and().body("main.temp", Matchers.greaterThan(0.0F));

		System.out.println("Validated weather api " + weatherResponseForLatLong.asPrettyString());

	}

	/**
	 * getWeatherByCityName
	 *
	 * @param cityName
	 * @param key
	 * @return weather restassured responser
	 */
	public static Response getWeatherByCityName(String cityName, String key) {
		return RestAssured.given().baseUri(BASE_ROUTE_WEATHER).queryParam("q", cityName).queryParam("appid", key).when()
				.get().then().extract().response();
	}

	/**
	 * getWeatherByLatAndLong
	 *
	 * @param latitude
	 * @param longitude
	 * @param key
	 * @return weather restassured response
	 */
	public static Response getWeatherByLatAndLong(String latitude, String longitude, String key) {
		return RestAssured.given().baseUri(BASE_ROUTE_WEATHER).queryParam("lat", latitude)
				.queryParam("lon", longitude).queryParam("appid", key).when().get().then()
				.extract().response();
	}

}
