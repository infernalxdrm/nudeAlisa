package instagram;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class instagram {
    private static final Map<Snowflake, Boolean> enabled = new ConcurrentHashMap<>();

    private static List<String> getLinksToPreview(String origin) throws IOException {
        URL url = new URL(origin);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        //set http request headers
        httpCon.addRequestProperty("Host", "www.cumhuriyet.com.tr");
        httpCon.addRequestProperty("Connection", "keep-alive");
        httpCon.addRequestProperty("Cache-Control", "max-age=0");
        httpCon.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        httpCon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
        httpCon.addRequestProperty("Accept-Encoding", "UTF-8");
        httpCon.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
        //httpCon.addRequestProperty("Cookie", "JSESSIONID=EC0F373FCC023CD3B8B9C1E2E2F7606C; lang=tr; __utma=169322547.1217782332.1386173665.1386173665.1386173665.1; __utmb=169322547.1.10.1386173665; __utmc=169322547; __utmz=169322547.1386173665.1.1.utmcsr=stackoverflow.com|utmccn=(referral)|utmcmd=referral|utmcct=/questions/8616781/how-to-get-a-web-pages-source-code-from-java; __gads=ID=3ab4e50d8713e391:T=1386173664:S=ALNI_Mb8N_wW0xS_wRa68vhR0gTRl8MwFA; scrElm=body");
        HttpURLConnection.setFollowRedirects(false);
        httpCon.setInstanceFollowRedirects(false);
        httpCon.setDoOutput(true);
        httpCon.setUseCaches(true);

        httpCon.setRequestMethod("GET");
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), StandardCharsets.UTF_8));
        } catch (Exception e) {
            return new LinkedList<>();
        }
        String inputLine;
        StringBuilder a = new StringBuilder();
        List<String> links = new LinkedList<>();
        while ((inputLine = in.readLine()) != null)
            if (inputLine.contains("\"config_width\":1080"))
                Arrays.stream(inputLine.replace("\\u0026", "&")
                        .split("https://"))
                        .filter(po -> !po.contains("150x150"))
                        .forEach(s -> links.add("https://" + (s.split("\"")[0]) + ("\n")));
        in.close();

        //System.out.println(links.toString());
        links.removeIf(s -> s.contains("script"));
        httpCon.disconnect();
        return links;
    }

    private static List<String> getBestResolution(List<String> links) {
        links.removeIf(s -> !s.contains("https://instagram.fgum1-1.fna.fbcdn.net/v/t51.2885-15/e35"));
        return links;
    }

    public static HashSet<String> getPreviews(String link) {
        try {
            return new HashSet<>(getBestResolution(getLinksToPreview(link)));
        } catch (IOException e) {
            e.printStackTrace();
            HashSet<String> err = new HashSet<>();
            err.add("ERRORRRRRRRR");
            return err;
        }
    }

    public static Mono<Void> sendPreviews(MessageCreateEvent e) {
        if (!e.getMessage().getContent().contains("https:")) return e.getMessage().getChannel().then();
        final MessageChannel channel = e.getMessage().getChannel().block();
        getPreviews(e.getMessage().getContent()).forEach(s -> channel.createMessage(s).block());

        return e.getMessage().getChannel().then();
    }
}

