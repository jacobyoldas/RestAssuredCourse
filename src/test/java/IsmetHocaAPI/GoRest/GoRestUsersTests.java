package IsmetHocaAPI.GoRest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class GoRestUsersTests { // Bu karmasik olarak yapilmis API. Campus API Tam olarak olmasi gereken modelde
  // MVC model (Model-View-Controller) bu bize sadece classlarda degisiklik yaprak kodumuzu sabitlemey yardimci olur
  // Cunku her bir api degisikliginde sadece class islme girer yani endpoint for ex. Country class

  @BeforeClass
  void Setup(){
    // RestAssured kendi statik değişkeni tanımlı değer atanıyor.
    baseURI="https://gorest.co.in/public/v2/";
  }
  public String getRandomName()
  {
    return RandomStringUtils.randomAlphabetic(8);
  }
  public String getRandomEmail()
  {
    return RandomStringUtils.randomAlphabetic(8).toLowerCase()+"@gmail.com";
  }

  int userID=0; // i made this global variable then i can reach in other method and i can use
  // in also updateUserObject method as well to update
  User newUser; // bunuda global yaptim kullanabilirim

  @Test
  public void createUserObject(){// Ucuncu olarak bu yontem bu sekli method olusturup burda bir
  // class acarim "User" diye o objeleri tanimlarim ve onlarin getter ve setterlerini generate ederim
    /** Bu daha cok tercih edilen yontem
     */

    newUser=new User();
    newUser.setName(getRandomName());
    newUser.setGender("male");
    newUser.setEmail(getRandomEmail());
    newUser.setStatus("active");

    userID=
        given()
            // api metoduna gitmeden önceki hazırlıklar : token, gidecek body, parametreleri
            .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
            .contentType(ContentType.JSON)
            .body(newUser)
            .log().body()
            .when()
            .post("users")

            .then()
            .log().body()
            .statusCode(201)
            .contentType(ContentType.JSON)
            //.extract().path("id") asagidaki bunun aynisi ama direk cevirilmis oluyor asagida
            .extract().jsonPath().getInt("id"); //convert to integer

    // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
    // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    System.out.println("userID = " + userID);
  }

  @Test(dependsOnMethods = "createUserObject", priority = 1)
  public void updateUserObject() { //Dorduncu is olarak bunu olusturup update ettim bu basamaklar hep Postmanden geliyor
//        Map<String, String> updateUser=new HashMap<>();  // I can also do this way too update the user
//        updateUser.put("name","ismet temur");

    newUser.setName("ismet temur");

    given()
        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
        .contentType(ContentType.JSON)
        .body(newUser)
        .log().body()
        .pathParam("userID", userID)

        .when()
        .put("users/{userID}") //update is "put" and i have to put user_id to update
        // yukarda 81th line da pathParamla olusturup cagirdik

        .then()
        .log().body()
        .statusCode(200)
        .body("name", equalTo("ismet temur")); // check ediom name ayni mi olustrudugumla update ile
  }

  @Test(dependsOnMethods = "createUserObject",priority = 2)
  public void getUserByID(){// Besinci olarak bu islem gene Postmande siralama neyse ona gore gidiom
    given()
        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
        .contentType(ContentType.JSON)
        .log().body()
        .pathParam("userID", userID)

        .when()
        .get("users/{userID}")

        .then()
        .log().body()
        .statusCode(200)
        .body("id", equalTo(userID))
    ;
  }

  @Test(dependsOnMethods = "createUserObject",priority = 3)
  public void deleteUserById(){// Altinci olarak bu islem gene Postmande siralama neyse ona gore gidiom
    given()
        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
        .contentType(ContentType.JSON)
        .log().body()
        .pathParam("userID", userID)

        .when()
        .delete("users/{userID}")

        .then()
        .log().body()
        .statusCode(204)
    ;
  }

  @Test(dependsOnMethods = "deleteUserById")
  public void deleteUserByIdNegative(){// Yedinci olarak bu islem gene Postmande siralama neyse ona gore gidiom
    given()
        .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
        .contentType(ContentType.JSON)
        .log().body()
        .pathParam("userID", userID)

        .when()
        .delete("users/{userID}")

        .then()
        .log().body()
        .statusCode(404);
  }

  @Test
  public void getUsers(){// Sekinci olarak bu islem gene Postmande siralama neyse ona gore gidiom
    Response response=
        given()
            .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")

            .when()
            .get("users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().response();

    // perşembe veya pazartesi, veya salı yapılacak...
    // TODO : 3 usersın id sini alınız (path ve jsonPath ile ayrı ayrı yapınız)
    int idUser3path= response.path("[2].id"); // jsonpathfinder da bakarak aliyoruz. Bu dizinin 3. elemanin idsini getiriyor. Dizi 0 dan baslar
    int idUser3JsonPath = response.jsonPath().getInt("[2].id"); // Yukardakinin aynisi yani 3. eleman ama bu jsonpath ile alinmis oluyor
    System.out.println("idUser3path = " + idUser3path); // yazdirip goruyorum dogrulugunu
    System.out.println("idUser3JsonPath = " + idUser3JsonPath);

    // TODO : Tüm gelen veriyi bir nesneye atınız (google araştırması)
    User[] usersPath=response.as(User[].class); // Tum veriyi bir nesneye atma olayi bole oluyor yani bir dizi arraya atiyor
    System.out.println("Arrays.toString(usersPath) = " + Arrays.toString(usersPath));
    // yukarda yazdirdim nasil gozukuyor diye

    List<User> usersJsonPath=response.jsonPath().getList("", User.class);
    // Yukarda jsonpath daha guzel bir sekilde vermis oluyor buda List olarak veriyor
    System.out.println("usersJsonPath = " + usersJsonPath); // yazdirip bakiyorz
    // Butun bu todo amaclari yaptiklarmizin, API istediginimiz verileri alabilimiyomuz ona bakioz
    // Bu olayi Onuncu  methodla kullandiuk v1
  }

  @Test
  public void getUserByIDExtract(){ //Dokuzuncu olarak bunu yaptik
    // TODO : GetUserByID testinde dönen user ı bir nesneye atınız.
    User user= // User Class
        given()
            .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
            .contentType(ContentType.JSON)
            .pathParam("userID", 3414)

            .when()
            .get("users/{userID}")

            .then()
            .log().body()
            .statusCode(200)
            //.extract().as(User.class) // user bilgilerini almaya yariyor User classtan dizini icinde butun elemanlari
            .extract().jsonPath().getObject("", User.class); // Bunu bitane eleman almak icin kullaniyorum
        // yukardakinide 191. line de hepsini lamam yardim ediyor

    // 191. ve 192. Ayni seyler aslinda ikisinin kullanma yontemi gosteriliyor burda
    System.out.println("user = " + user);
  }

  @Test
  public void getUsersV1() { //Onuncu olarak List kiullnioz burda
    Response response =
        given()
            .header("Authorization", "Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")

            .when()
            .get("https://gorest.co.in/public/v1/users")

            .then()
            //.log().body()
            .statusCode(200)
            .extract().response();

    //response.as();// tüm gelen response uygun nesneler için tüm classların yapılması gerekiyor.
    //yani as() oldugu icin bire bir alman gerekiyor
    // jsonpath daha kolay cunku path veridigimiz icn ondan list kullanioz daha kolay hepsini alabiliriz
    List<User>   dataUsers = response.jsonPath().getList("data", User.class);
    // JSONPATH bir response içindeki bir parçayı nesneye ödnüştürebiliriz. yani  bu "https://gorest.co.in/public/v1/users" bakarsan dizi coklu ondan bole
    System.out.println("dataUsers = " + dataUsers); // yazdirip gorebiliriz ve herzaman jsonpath kullanilabilir...

    // Daha önceki örneklerde (as) Class dönüşümleri için tüm yapıya karşılık gelen
    // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.
    // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
    // imkan veren JSONPATH i kullandık.Böylece tek class ise veri alınmış oldu
    // diğer class lara gerek kalmadan
    // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
    // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

  }

  @Test(enabled = false) //Bunlarin calismasini sitemedim icin bole yaptim
  public void createUser() // ilk bunu olusturdum ayni postmande nasilsa onu adapte ettim buraya
  {
    int userID=
        given()
            // api metoduna gitmeden önceki hazırlıklar : token, gidecek body, parametreleri
            .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
            .contentType(ContentType.JSON)
            .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+ getRandomEmail()+"\", \"status\":\"active\"}")

            .when()
            .post("users")

            .then()
            .log().body()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract().path("id");
    System.out.println("userID = " + userID);
  }
  @Test(enabled = false)
  public void createUserMap() { // Ikinci olarak bole user user olustrurum istersem HasMap  kullanarak sadece
      // object name "new user" cagiririm body bilgileri girmek icin

    Map<String,String> newUser=new HashMap<>();
    newUser.put("name",getRandomName());
    newUser.put("gender","male");
    newUser.put("email", getRandomEmail());
    newUser.put("status","active");

    int userID=
        given()
            // api metoduna gitmeden önceki hazırlıklar : token, gidecek body, parametreleri
            .header("Authorization","Bearer 523891d26e103bab0089022d20f1820be2999a7ad693304f560132559a2a152d")
            .contentType(ContentType.JSON)
            .body(newUser)
            .log().body()
            .when()
            .post("users")

            .then()
            .log().body()
            .statusCode(201)
            .contentType(ContentType.JSON)
            .extract().path("id");
    System.out.println("userID = " + userID);
  }
}

class User{


  private int id;
  private String name;
  private String gender;
  private String email;
  private String status;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() { // Calsitirdigin bunu yazdirip gosteriyor gormek icin bilgileri
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", gender='" + gender + '\'' +
        ", email='" + email + '\'' +
        ", status='" + status + '\'' +
        '}';
  }
}
