package instagram;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class instagram {
    private static final Map<Snowflake, Boolean> enabled = new ConcurrentHashMap<>();
    WebClient client;
    private AtomicReference<HtmlPage> page = new AtomicReference<>();

    private static List<String> getLinksToPreview(String origin) throws IOException {
        URL url = null;
        try {

            url = new URL(origin);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setUseCaches(true);

        httpCon.setRequestMethod("GET");
        BufferedReader in = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(httpCon.getInputStream());
            in = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String inputLine;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(httpCon.getInputStream());
            in = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
            List<String> l = new LinkedList<>();
            l.add(e.getMessage());
            return l;
        }
        StringBuilder a = new StringBuilder();
        List<String> links = new LinkedList<>();
        while ((inputLine = in.readLine()) != null) {
            // System.out.println(inputLine);
            if (inputLine.contains("\"config_width\":1080"))
                Arrays.stream(inputLine.replace("\\u0026", "&")
                        .split("https://"))
                        .filter(po -> !po.contains("150x150"))
                        .forEach(s -> links.add("https://" + (s.split("\"")[0]) + ("\n")));
        }
        in.close();

        //System.out.println(links.toString());
        links.removeIf(s -> s.contains("script"));
        //    links.forEach(System.out::println);
        httpCon.disconnect();
        return links;
    }

    public static Mono<Void> sendPreviews(MessageCreateEvent e) {
        if (!e.getMessage().getContent().contains("https://www.instagram.com/"))
            return e.getMessage().getChannel().then();
        final MessageChannel channel = e.getMessage().getChannel().block();
        getPreviews(e.getMessage().getContent()).forEach(s -> channel.createMessage(s).block());

        return e.getMessage().getChannel().then();
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

    public void login() throws IOException {
        this.client = new WebClient(BrowserVersion.BEST_SUPPORTED);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);

        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);

        client.setIncorrectnessListener((arg0, arg1) -> {
        });

        this.page.set(client.getPage("https://www.instagram.com/accounts/login/?force_classic_login"));
        final HtmlForm form = this.page.get().getForms().get(page.get().getForms().size() - 1);
        System.out.println(form.asXml());
        final HtmlTextInput user = form.getInputByName("username");

        final HtmlPasswordInput password = form.getInputByName("enc_password");
        user.type("Kworker_");
        password.type("Halflife3?");
        //final HtmlButton button=form.getButtonByName("button-green");
        Object o = this.page.get().executeJavaScript("submit").getJavaScriptResult();
        if (o instanceof net.sourceforge.htmlunit.corejs.javascript.NativeArray) {
            System.out.println("ez");
            System.out.println(((NativeArray) o).get(0).toString());
        }

        System.out.println(form.asXml());
    }

}

