package IsmetHocaAPI.POJO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

public class Location {
  private String postcode;
  private String country;
  private String countryAbbreviation;
  private ArrayList<Place> places;

  public String getPostcode() {
    return postcode;
  }

  // bu arada bosluk oldugunda hata veriyor o yuzden donusumu icin bu JsonPtoperty ekledik. Bunlarda set methoda yazilir
  @JsonProperty("post code") // yesil yazdigimiz isim JSON dizisinde nasilsa oyle  yazip eslestiriyoruz bosluklu oldugundan
  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getCountryAbbreviation() {
    return countryAbbreviation;
  }

  @JsonProperty("country abbreviation") // bunun icin dependcy de ekliyoruz pom.xml de
  public void setCountryAbbreviation(String countryAbbreviation) {
    this.countryAbbreviation = countryAbbreviation;
  }

  public ArrayList<Place> getPlaces() {
    return places;
  }

  public void setPlaces(ArrayList<Place> places) {
    this.places = places;
  }

  @Override
  public String toString() { // right click Generate and select toString() yazdirimak icin
    return "Location{" +
        "postcode='" + postcode + '\'' +
        ", country='" + country + '\'' +
        ", countryAbbreviation='" + countryAbbreviation + '\'' +
        ", places=" + places +
        '}';
  }
}
