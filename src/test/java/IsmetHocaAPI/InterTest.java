package IsmetHocaAPI;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import IsmetHocaAPI.InterTest;
public class InterTest { // API KEy Bularak yapma son API konusu
  @Test
  public void testApiKey(){
    given()
        .header("x-api-key","GwMco9Tpstd5vbzBzlzW9I7hr6E1D7w2zEIrhOra")
        // Postman de header icine koyarak ve API Key secmistim ondan API key ile giris yapiyorum
        //Genel veririler API KEY yada token

        .when()
        .get("https://l9njuzrhf3.execute-api.eu-west-1.amazonaws.com/prod/user")

        .then()
        .log().body()
        .statusCode(200)    ;
  }
}
