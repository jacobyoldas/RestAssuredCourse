package IsmetHocaAPI.POJO;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ToDo {
  private int userId; //ctrl+shift+alt fold all the collapse lines
  private int id;
  private String unvan; // This actually supposed to be tittle but we can change it
  // if we want to but we have to say when we set this unvan  @JsonProperty("title")

  private Boolean completed;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUnvan() {
    return unvan;
  }

  @JsonProperty("title") // if we dont use this it will give error "it is not matched" that's why we put this
  public void setUnvan(String unvan) {
    this.unvan = unvan;
  }

  public Boolean getCompleted() {
    return completed;
  }

  public void setCompleted(Boolean completed) {
    this.completed = completed;
  }

  @Override
  public String toString() {
    return "ToDo{" +
        "userId=" + userId +
        ", id=" + id +
        ", unvan='" + unvan + '\'' +
        ", completed=" + completed +
        '}';
  }
}
//"userId": 1,
//        "id": 2,
//        "title": "quis ut nam facilis et officia qui",
//        "completed": false

