package IsmetHocaAPI;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;

import IsmetHocaAPI.POJO.Location;
import IsmetHocaAPI.POJO.Location;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class ZippoTestIsmetHoca {

  @Test
  public void test() {

    given().
        // Response Body(token,send body,parameter)
            when().
        // Method
            then();
    // Assertion and extract
  }

  @Test
  public void statusCodeTest() {

    given()

        .when()
        .get("http://api.zippopotam.us/us/90210")

        .then()
        .log().body() //log.all() return all response body
        .statusCode(200);

  }

  @Test
  public void contentTypeTest() {

    given()

        .when()
        .get("http://api.zippopotam.us/us/90210")

        .then()
        .log().body() //log.all() return all response body
        .statusCode(200)
        .contentType(ContentType.JSON); // You can make .TEXT type too

  }


  @Test
  public void checkStateInResponseBody() {

    given()

        .when()
        .get("http://api.zippopotam.us/us/90210")

        .then()
        .log().body()
        .body("country", equalTo("United States")) // body.country == United States ?
        .statusCode(200);
  }

  /**
   * response body like these { "post code": "90210", "country": "United States", "country
   * abbreviation": "US", "places": [                      this is a list goes 0,1,2 elements
   * [{},{}] { "place name": "Beverly Hills", "longitude": "-118.4065", "state": "California",
   * "state abbreviation": "CA", "latitude": "34.0901" }, { "place name": "adasdas", "longitude":
   * "-118.4065", "state": "gdfgfg", "state abbreviation": "CA", "latitude": "34.0901" } ] }
   */

//    body.id --> to get the id from response body

//    body.country  -> body("country",
//    body.'post code' -> body("post code") there is space between that's why
//    body.'country abbreviation' -> body("country abbreviation",equalTo("US"))
//    body.places[0].'place name' ->  body( "body.places[0].'place name'",equalTo("Beverly Hills")) place belong to body still
//    body.places[0].state -> body("places[0].state",equalTo("California")) when its just like state you DO NOT have to put ""
//    Eger [] dizi olmasaydi sadece {{}} bole olsaydi body.places.state dicektik
  @Test
  public void bodyJsonPathTest2() {

    given()

        .when()
        .get("http://api.zippopotam.us/us/90210")

        .then()
        .log().body()
        .body("places[0].state", equalTo("California")) // birebir eşit mi
        .statusCode(200);
  }

  @Test
  public void bodyJsonPathTest3() {

    given()

        .when()
        .get("http://api.zippopotam.us/tr/01000")

        .then()
        .log().body()
        .body("places.'place name'", hasItem("Çaputçu Köyü"))
        //bir index verilmezse dizinin bütün elemanlarında arar. places'lerin icinde ariyoruz

        // equalTo is not working  that's why we need hasItem
        .statusCode(200);
    // places.state gives all those places and states in the array
    //  "places.'place name'"  bu bilgiler "Çaputçu Köyü" bu item e shaip mi

  }
  @Test
  public void bodyArrayHasSizeTest() {

    given()

        .when()
        .get("http://api.zippopotam.us/us/90210")

        .then()
        .log().body()
        .body("places", hasSize(1)) // verilen path deki Array list'in size kontrolü [{}] only one
        .statusCode(200)
    ;
  }

  @Test
  public void combiningTest() {

    given()

        .when()
        .get("http://api.zippopotam.us/us/90210")

        .then()
        .log().body()
        .body("places", hasSize(1))
        .body("places.state", hasItem("California"))
        .body("places[0].'place name'", equalTo("Beverly Hills"))
        .statusCode(200)
    ;
  }

  @Test
  public void pathParamTest() {

    // Parameter start either after "/" or "?,&"
    //https://campus.techno.study---> this islink for the website
    // /courses-calendar ----> this is parameter

    //https://www.google.com/search---> ?q=cucumber++ ---> & rlz=1C1SQJL_enUS890US890&sxsrf

    given()
        .pathParam("Country","us")
        .pathParam("ZipKod",90210)
        .log().uri() //request linki gostermesi icin

        .when()
        .get("http://api.zippopotam.us/{Country}/{ZipKod}")// yukardaki "us" ayni olmak zorunda

        .then()
        .log().body()

        .statusCode(200)
    ;
  }

  @Test
  public void pathParamTest2() {
    // 90210 dan 90213 kadar test sonuçlarında places in size nın hepsinde 1 gediğini test ediniz.

    for(int i=90210 ;i <=90213 ;i++ ) {

      given()
          .pathParam("Country", "us") //pathParam means go use this parameters in line(182)
          .pathParam("ZipKod", i)
          .log().uri()

          .when()
          .get("http://api.zippopotam.us/{Country}/{ZipKod}")

          .then()
          .log().body()
          .body("places", hasSize(1))
          .statusCode(200)
      ;
    }
  }


  @Test
  public void queryParamTest() {

    //https://gorest.co.in/public/v1/users?page=1

    given()
        .param("page",1) // yukardaki "?" isaretinden sonra "page and 1" This is just param
        //.param("page",2) olabilirde hangi sayfayi cagirceksan
        .log().uri()

        .when()
        .get("https://gorest.co.in/public/v1/users")// this comes from the base link before "?"

        .then()
        .log().body()
        .body("meta.pagination.page", equalTo(1) )// this has no [] that's why ...
        // we assert the page 1 on line (199) page 1 equal or not
        .statusCode(200)
    ;
  }

  @Test
  public void queryParamTest2() {
    //https://gorest.co.in/public/v1/users?page=1

    // we are testing 10 pages after "?" instead of writing back to back. It returns 10 pages

    for (int pageNo = 1; pageNo <= 10; pageNo++) {
      given()
          .param("page", pageNo)
          .log().uri() //request linki

          .when()
          .get("https://gorest.co.in/public/v1/users")

          .then()
          .log().body()
          .body("meta.pagination.page", equalTo(pageNo))
          .statusCode(200)
      ;
    }
  }

  RequestSpecification reqSpec; // we use this the code reuse ability every test has the same
  ResponseSpecification resSpec; // assigned before class the code then we can call them in the @test

  @BeforeClass
  void Setup(){

    // RestAssured kendi statik değişkeni tanımlı değer atanıyor.
    baseURI="https://gorest.co.in/public/v1";

    reqSpec = new RequestSpecBuilder()
        .log(LogDetail.URI)
        .setAccept(ContentType.JSON)
        .build();

    resSpec = new ResponseSpecBuilder()
        .expectStatusCode(200)
        .expectContentType(ContentType.JSON)
        .log(LogDetail.BODY)
        .build();
  }

  @Test
  public void requestResponseSpecification() {
    //https://gorest.co.in/public/v1/users?page=1

    given()
        .param("page",1)
        .spec(reqSpec) // instead this .log().uri() writing

        .when()
        .get("/users")  // url nin başında http yoksa baseUri deki değer otomatik geliyor.

        .then()
        .body("meta.pagination.page", equalTo(1) )
        .spec(resSpec) // instead .log().body() and .statusCode(200) writing
    ;
  }

  // Json exract
  @Test
  public void extractingJsonPath() {

    String placeName=
        given()

            .when()
            .get("http://api.zippopotam.us/us/90210")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().path("places[0].'place name'");
        // extract metodu ile given ile başlayan satır, bir değer döndürür hale geldi,
        // en sonda extract olmalı

    System.out.println("placeName = " + placeName);
  }
  @Test
  public void extractingJsonPathInt() {

    int limit= // id is not string coz no "" that's why we made int
        given()

            .when()
            .get("https://gorest.co.in/public/v1/users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().path("meta.pagination.limit");

    System.out.println("limit = " + limit);
    Assert.assertEquals(limit,10,"test sonucu");
  }

  @Test
  public void extractingJsonPathInt2() {  // https://jsonpathfinder.com/ getting help from here

    int id=
        given()

            .when()
            .get("https://gorest.co.in/public/v1/users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().path("data[2].id");
    ;
    System.out.println("id = " + id);
  }

  @Test
  public void extractingJsonPathIntList() { // list aliyorum data.id ile bu herseyi getirir in arrayde bole[]

    List<Integer> idler=
        given()

            .when()
            .get("https://gorest.co.in/public/v1/users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().path("data.id") // data daki bütün idleri bir List şeklinde verir
        ;

    System.out.println("idler = " + idler);
    Assert.assertTrue(idler.contains(3045));
    }
  @Test
  public void extractingJsonPathStringList() { // getting all names with list

    List<String> names=
        given()

            .when()
            .get("https://gorest.co.in/public/v1/users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().path("data.name") // data daki bütün idleri bir List şeklinde verir
        ;

    System.out.println("isimler = " + names);
    Assert.assertTrue(names.contains("Datta Achari"));
  }
  @Test
  public void extractingJsonPathResponseAll() {

    Response response=
        given()

            .when()
            .get("https://gorest.co.in/public/v1/users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().response(); // bütün body alındı


    //.statusCode(200)
    //.extract().path("data.name")
    // when we use 389. line like this we need only one date assertion so below we did similar way with response.path()
    List<Integer> idler=response.path("data.id");  // same like this  .extract().path("data.name") but we need all
    List<String> names=response.path("data.name");
    int limit=response.path("meta.pagination.limit");

    System.out.println("limit = " + limit);
    System.out.println("names = " + names);
    System.out.println("idler = " + idler);
  }

  @Test
  public void extractingJsonPOJO() {  // POJO means : JSon Object i  (Plain Old Java Object)

    //JSon formatiyla olan API class donusturup OOP kullanip methodlarla cagirdik

    Location location=
        given()

            .when()
            .get("http://api.zippopotam.us/us/90210")

            .then()
            .extract().as(Location.class); // .as() return the Location class we call in POJO we created
    // this helps me to return getter method from location class in pojo package i dont need to create an objext

    System.out.println("yer. = " + location);

    System.out.println("yer.getCountry() = " + location.getCountry());
    System.out.println("yer.getPlaces().get(0).getPlaceName() = " +
        location.getPlaces().get(0).getPlaceName());
  }

//    "post code": "90210",
//            "country": "United States",
//            "country abbreviation": "US",
//
//            "places": [
//            {
//            "place name": "Beverly Hills",
//            "longitude": "-118.4065",
//            "state": "California",
//            "state abbreviation": "CA",
//            "latitude": "34.0901"
//            }
//            ]









}