package instagram;

import core.nudeAlisa;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class instagram {
    private static InputStream video = null;
    private static final Map<Snowflake, Boolean> enabled = new ConcurrentHashMap<>();
    // WebClient client;
    HashSet<Integer> added = new HashSet<>();
    //private AtomicReference<HtmlPage> page = new AtomicReference<>();

    private List<String> getLinksToPreview(String origin) throws IOException {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(origin);
        httpPost.setHeader("Cookie", "sessionid=" + nudeAlisa.argc[1]);
        HttpResponse response = httpClient.execute(httpPost);

        BufferedReader in = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(response.getEntity().getContent());
            in = new BufferedReader(inputStreamReader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String inputLine;
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
        //httpCon.disconnect();
        // links.forEach(System.out::println);

        return links;
    }

    public Mono<Void> sendPreviews(MessageCreateEvent e) {
        if (!e.getMessage().getContent().contains("https://www.instagram.com/"))
            return e.getMessage().getChannel().then();
        final MessageChannel channel = e.getMessage().getChannel().block();
        getPreviews(e.getMessage().getContent()).forEach(s -> channel.createMessage(chan -> chan.addFile("1.jpg", s)).block());
        if (video != null) channel.createMessage(ch -> ch.addFile("video.mp4", video)).block();
        added.clear();
        video = null;
        return e.getMessage().getChannel().then();
    }

    private ArrayList<BufferedImage> getBestResolution(List<String> links) {
        //links.forEach(System.out::println);
        ArrayList<BufferedImage> images = new ArrayList<>();

        links.forEach(s -> {
            BufferedImage i = null;
            if (s.contains(".mp4")) {
                proceedVideo(s);
                images.clear();
                return;
            } else i = download(s);
            if (i != null) images.add(i);
        });
        images.sort(Comparator.comparingInt(BufferedImage::getHeight));
        int x = images.get(images.size() - 1).getHeight();
        images.removeIf(image -> image.getHeight() != x);
        // links.forEach(l->links.forEach(s -> System.out.println(StringUtils.difference(s,l))));
        //links.removeIf(s -> !s.contains("p1080x1080") || !s.contains("p2080x1350"));
//        List<Integer> toRemove=new ArrayList<>();
//        for (int i = 0; i < images.size(); i++) {
//            for (int u=x+1;u<images.size();u++){
//                if (images.get(i).get)toRemove.add(u);
//            }
//        }
//        toRemove.forEach(id->images.remove(id));
        return images;
    }

    private void proceedVideo(String s) {
        try {
            video = new URL(s).openStream();
        } catch (IOException e) {
            //  e.printStackTrace();
        }
    }

    @SneakyThrows
    private BufferedImage download(String link) {
        URL url = new URL(link);
        URLConnection conn = url.openConnection();

        // now you get the content length
        int contentLength = conn.getContentLength();
        // you can check size here using contentLength
        if (added.contains(contentLength)) return null;
        else added.add(contentLength);
        InputStream in = conn.getInputStream();
        return ImageIO.read(in);

    }

    @SneakyThrows
    public HashSet<InputStream> getPreviews(String link) {
        HashSet<InputStream> streams = new HashSet<>();
        ImageChecker c = new ImageChecker();
        ArrayList<BufferedImage> images = getBestResolution(getLinksToPreview(link));
        images.forEach(im -> {

            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(im, "jpeg", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                streams.add(new ByteArrayInputStream(os.toByteArray()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return streams;

    }

//    public void login() throws IOException {
//        this.client = new WebClient(BrowserVersion.BEST_SUPPORTED);
//        client.getOptions().setCssEnabled(false);
//        client.getOptions().setUseInsecureSSL(true);
//
//        client.getOptions().setThrowExceptionOnScriptError(false);
//        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
//
//        client.setIncorrectnessListener((arg0, arg1) -> {
//        });
//
//        this.page.set(client.getPage("https://www.instagram.com/accounts/login/?force_classic_login"));
//        final HtmlForm form = this.page.get().getForms().get(page.get().getForms().size() - 1);
//        System.out.println(form.asXml());
//        final HtmlTextInput user = form.getInputByName("username");
//
//        final HtmlPasswordInput password = form.getInputByName("enc_password");
//        user.type("Kworker_");
//        password.type("Halflife3?");
//        //final HtmlButton button=form.getButtonByName("button-green");
//        Object o = this.page.get().executeJavaScript("submit").getJavaScriptResult();
//        if (o instanceof net.sourceforge.htmlunit.corejs.javascript.NativeArray) {
//            System.out.println("ez");
//            System.out.println(((NativeArray) o).get(0).toString());
//        }
//
//        System.out.println(form.asXml());
//    }

    public static boolean check_if_images_are_same(BufferedImage i1, BufferedImage i2) {
        final short offset = 25;
        final short error = 4;
        Random r = new Random();
        int x = r.nextInt(i1.getWidth() - offset);
        int y = r.nextInt(i1.getHeight() - offset);
        int max_equal = offset * offset;
        int equals = 0;
        boolean same = false;
        for (int x_ = 0; x_ < offset; x_++) {
            for (int y_ = 0; y_ < offset; y_++) {
                if (i1.getRGB(x + x_, y + y_) == i2.getRGB(x + x_, y + y_))
                    equals++;//if pixels are same increase counter
            }
        }
        if (equals >= max_equal - error) same = true;
        return same;
    }

}

