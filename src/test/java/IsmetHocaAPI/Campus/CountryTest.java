package IsmetHocaAPI.Campus;
import IsmetHocaAPI.Campus.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class CountryTest {
  Cookies cookies;

  @BeforeClass
  public void loginCampus() {
    baseURI = "https://demo.mersys.io/";

    Map<String, String> credential = new HashMap<>();
    credential.put("username", "richfield.edu");
    credential.put("password", "Richfield2020!");
    credential.put("rememberMe", "true");

    cookies=
        given()
            .contentType(ContentType.JSON)
            .body(credential)

            .when()
            .post("auth/login")

            .then()
            //.log().cookies()
            .statusCode(200)
            .extract().response().getDetailedCookies()// cookies comes from here
    ;
  }

  String countryID;
  String countryName;
  String countryCode;

  @Test
  public void createCountry(){ // 1. bu
    countryName=getRandomName(); // set ediyorum
    countryCode=getRandomCode();

    Country country=new Country();
    country.setName(countryName); // generateCountrName
    country.setCode(countryCode); // generateCountrCode

    countryID=
        given()
            .cookies(cookies)
            .contentType(ContentType.JSON)
            .body(country)

            .when()
            .post("school-service/api/countries")

            .then()
            .log().body()
            .statusCode(201)
            .extract().jsonPath().getString("id")
    ;

  }



  public String getRandomName() {
    return RandomStringUtils.randomAlphabetic(8).toLowerCase();
  }

  public String getRandomCode() {
    return RandomStringUtils.randomAlphabetic(3).toLowerCase();
  }


  @Test(dependsOnMethods = "createCountry")
  public void createCountryNegative(){ //2. bu

    //"message": "The Country with Name \"France 375\" already exists.",
    //asserting with message

    Country country=new Country();
    country.setName(countryName);
    country.setCode(countryCode);

    given()
        .cookies(cookies)
        .contentType(ContentType.JSON)
        .body(country)

        .when()
        .post("school-service/api/countries")

        .then()
        .log().body()
        .statusCode(400)
        .body("message",equalTo("The Country with Name \""+countryName+"\" already exists."))
    ;
  }

  @Test(dependsOnMethods = "createCountry")
  public void updateCountry(){ // 3. bu

    countryName = getRandomName();

    Country country=new Country();
    country.setId(countryID);
    country.setName(countryName);
    country.setCode(countryCode);

    given()
        .cookies(cookies)
        .contentType(ContentType.JSON)
        .body(country)

        .when()
        .put("school-service/api/countries")

        .then()
        .log().body()
        .statusCode(200)
        .body("name",equalTo(countryName))
    ;
  }

  @Test(dependsOnMethods = "updateCountry")
  public void deleteCountryById(){ //4.bu
    given()
        .cookies(cookies)
        .pathParam("countryID", countryID)

        .when()
        .delete("school-service/api/countries/{countryID}") //comes from pathParam

        .then()
        .log().body()
        .statusCode(200)
    ;
  }

  @Test(dependsOnMethods = "deleteCountryById")
  public void deleteCountryByIdNegative(){ //5. bu
    given()
        .cookies(cookies)
        .pathParam("countryID", countryID)
        .log().uri() // it shows how it looks paramPath actually
        .when()
        .delete("school-service/api/countries/{countryID}") // + countryID 'de olur yazarsak

        .then()
        .log().body()
        .statusCode(400)
    ;
  }

  @Test(dependsOnMethods = "deleteCountryById")
  public void updateCountryNegative(){ //6. bu Postman de de yapti Ismet Hoca bunu
    countryName = getRandomName();

    Country country=new Country();
    country.setId(countryID);
    country.setName(countryName);
    country.setCode(countryCode);

    given()
        .cookies(cookies)
        .contentType(ContentType.JSON)
        .body(country)

        .when()
        .put("school-service/api/countries")

        .then()
        .log().body()
        .statusCode(408)
        .body("message", equalTo("Country not found"));
  }


}
