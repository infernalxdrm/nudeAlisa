package core.services.weather;

import lombok.Data;

@Data
public class weatherJs {
    String main;
    String description;
    String temp;
    String feels_like;
    String temp_min;
    String temp_max;
    String pressure;
    String humidity;
    String visibility;
    String speed;
    String deg;
    String gust;
    String country;
    int sunrise;
    int sunset;
    String name;


}
