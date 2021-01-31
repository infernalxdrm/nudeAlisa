package core.services.weather;


import com.google.gson.Gson;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import java.io.IOException;

public class weather {
    public void getByTown(String Town) throws IOException {
        Gson gs = new Gson();
        AsyncHttpClient client = new DefaultAsyncHttpClient();
        client.prepare("GET", "https://community-open-weather-map.p.rapidapi.com/weather?q=" + Town + "%&lat=0&lon=0&callback=test&id=2172797&lang=null&units=%22metric%22%20or%20%22imperial%22&mode=JSON")
                .setHeader("x-rapidapi-key", "03161a7ad2mshc8f785eb571d120p1b8d0cjsne8cb32173b04")
                .setHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .execute()
                .toCompletableFuture()
                .thenAccept(r -> System.out.println(gs.fromJson(r.toString(), weatherJs.class)))
                .join();

        client.close();
    }
}
