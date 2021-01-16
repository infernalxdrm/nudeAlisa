package utils;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
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

    public static String getRequestResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            int responseCode = conn.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseSb = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                responseSb.append(inputLine);
            }

            in.close();
            String response = responseSb.toString();
            if (responseCode != 200) {
                System.err.printf("Response of 'get' request to url {} is not succesful, code is {} and response is {}", url, responseCode, response);
            }

            return response;
        } catch (IOException var8) {
            System.err.printf("IOException occured when processing request to {}, error is {}", urlString, var8.toString());
            return "{}";
        }
    }
}
