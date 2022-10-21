package IsmetHocaAPI.Campus.Model;

public class Country { //Create Country Model in Postman that's why we created
  private String name;
  private String code;
  private String id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() { // too see the details
    return "Country{" +
        "name='" + name + '\'' +
        ", code='" + code + '\'' +
        ", id='" + id + '\'' +
        '}';
  }
}
