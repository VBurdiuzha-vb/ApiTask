package services;
import app.BaseApi;
import dto.FilmsDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import static requestPath.SwapiRequestPath.PEOPLE;


public class SwapiService extends BaseApi {

    public Response sendGetPeople() {
        response = RestAssured.given()
                .baseUri(BaseApi.baseUrl + PEOPLE.getValue())
                .contentType(ContentType.JSON)
                .get()
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response();
        return response;
    }

    public FilmsDto sendGetFilms(String url) {
        response = RestAssured.given()
                .baseUri(url)
                .contentType(ContentType.JSON)
                .get()
                .then().statusCode(HttpStatus.SC_OK)
                .extract().response();
        return response.as(FilmsDto.class);
    }
}
