package utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Stream;

public class httpUtil {
    @SneakyThrows
    static  public String getRedirect(String url)  {
        Thread.sleep(30);
        HttpURLConnection con = (HttpURLConnection)(new URL( url ).openConnection());
        con.setInstanceFollowRedirects( false );
        con.connect();
        int responseCode = con.getResponseCode();
        return  con.getHeaderField( "Location" );
    }
    @SneakyThrows
    static public Stream<String> getHtmlStream(String link){
        HttpURLConnection con = (HttpURLConnection)(new URL( link ).openConnection());
        con.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
        InputStreamReader input = new InputStreamReader(con.getInputStream());
        BufferedReader in = new BufferedReader(input);
        return in.lines();
    }
}
