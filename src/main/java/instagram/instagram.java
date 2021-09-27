package instagram;

import core.nudeAlisa;
import core.services.MessageManager;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.channel.MessageChannel;
import io.github.techgnious.IVCompressor;
import io.github.techgnious.dto.IVSize;
import io.github.techgnious.dto.ImageFormats;
import io.github.techgnious.dto.ResizeResolution;
import io.github.techgnious.dto.VideoFormats;
import io.github.techgnious.exception.VideoException;
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
    private static final short time_delay = 45;//sec
    private int time_sicne_last = 1000;
    // WebClient client;
    HashSet<Integer> added = new HashSet<>();
    private byte[] video_bytes;
    private int post_count = 0;
    private List<InputStream> videos = new LinkedList<>();
    //private AtomicReference<HtmlPage> page = new AtomicReference<>();

    private List<String> getLinksToPreview(String origin)throws Exception  {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost;
        try{
            httpPost = new HttpPost(origin);
        }
        catch (Exception e){
            e.printStackTrace();
            return List.of("");
        }
        httpPost.setHeader("Cookie", "sessionid=" + nudeAlisa.argc[1]);
        HttpResponse response=null;
        try {
            response = httpClient.execute(httpPost);
        }
        catch (Exception exception){
            System.err.println(exception.getMessage());
            return List.of();
        }

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
             if (inputLine.contains("\"config_width\":1080"))
                Arrays.stream(inputLine.replace("\\u0026", "&")
                        .split("https://"))
                        .filter(po -> !po.contains("150x150"))
                        .forEach(s -> links.add("https://" + (s.split("\"")[0]) + ("\n")));
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(links.toString());
        links.removeIf(s -> s.contains("script"));
        //    links.forEach(System.out::println);
        //httpCon.disconnect();
        // links.forEach(System.out::println);

        return links;
    }

    public Mono<Void> sendPreviews(MessageCreateEvent e) {
        final MessageChannel channel = e.getMessage().getChannel().block();
        if (!e.getMessage().getContent().startsWith("https://www.instagram.com/"))
            return e.getMessage().getChannel().then();
        if (time_exeded()) {
            MessageManager.createTimedMessage(e,"Please wait " + (time_delay - time_sicne_last) + " seconds before sending again :heart:",(time_delay - time_sicne_last));
            return e.getMessage().getChannel().then();
        }
        HashSet<InputStream> set = getPreviews(e);
        for(InputStream video:videos){
            byte[]bytes = null;

            try {
                bytes=video.readAllBytes();
                long maxSize=getMaxFileUploadSize(e.getGuild().block());
                if (bytes.length>maxSize){
                    IVCompressor compressor = new IVCompressor();
                    System.out.printf("Original size -> %f MB\n",(double)bytes.length/(1e6));
                    IVSize customRes=new IVSize();

                    int defWidth=1280;
                    int defHeight=720;
                    double a = Math.sqrt((double)((double)maxSize/(double)bytes.length));
                    System.out.printf("resize param -> %f\n",a);
                    int newW= (int) Math.floor(a *(double) defWidth);
                    int newH=(int)Math.floor(a *(double) defHeight);
                    if (newW%2!=0)newW-=1;
                    if (newH%2!=0)newH-=1;
                    customRes.setHeight(newH);

                    customRes.setWidth(newW);
                    bytes=compressor.reduceVideoSize(bytes,VideoFormats.MP4,ResizeResolution.VIDEO_DEFAULT);
                    //bytes=compressor.reduceVideoSizeWithCustomRes(bytes,VideoFormats.MP4,customRes);
                    System.out.printf("New size -> %f MB\n",(double)bytes.length/(1e6));
                }

                video_bytes=bytes;
            } catch (IOException | VideoException ioException) {
                ioException.printStackTrace();
            }

            if (video_bytes!=null)channel.createMessage(ch -> ch.addFile("video.mp4", new ByteArrayInputStream(video_bytes))).block();
        }
        set.forEach(s -> channel.createMessage(chan -> chan.addFile("1.jpg", s)).block());
        added.clear();
        video_bytes=null;
        time_sicne_last = 0;
        videos.clear();
        return e.getMessage().getChannel().then();
    }

    private boolean time_exeded() {
        return time_sicne_last < time_delay;
    }

    private ArrayList<BufferedImage> getBestResolution(List<String> links) {
        //links.forEach(System.out::println);
        ArrayList<BufferedImage> images = new ArrayList<>();

        links.forEach(s -> {
            BufferedImage i = null;
            if (s.contains(".mp4?")) {
                if (s.contains("BaseURL")) return;
                System.out.println(s);
                proceedVideo(s);
                return;
            } else i = download(s);
            if (i != null) images.add(i);
        });
        images.sort(Comparator.comparingInt(im->im.getHeight()*im.getWidth()));
        //if (video != null) images.clear();
        //after sorting the list we got highest resolution at the end of the list
        ArrayList<BufferedImage> selected = new ArrayList<>();
        if (images.size() >= 1) {
            int number_of_post = (images.size()-1)/3;
            for(int a=1;a<=number_of_post;a++){
                selected.add(images.get(images.size()-a));
                //select last as theire best quality
            }
        }
        // links.forEach(l->links.forEach(s -> System.out.println(StringUtils.difference(s,l))));
        //links.removeIf(s -> !s.contains("p1080x1080") || !s.contains("p2080x1350"));
//        List<Integer> toRemove=new ArrayList<>();
//        for (int i = 0; i < images.size(); i++) {
//            for (int u=x+1;u<images.size();u++){
//                if (images.get(i).get)toRemove.add(u);
//            }
//        }
//        toRemove.forEach(id->images.remove(id));
        images.clear();
        return selected;
    }

    private void proceedVideo(String s) {
        try {
            videos.add(new URL(s).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void incrementTimer() {
        if (time_sicne_last == Integer.MAX_VALUE) time_sicne_last = 100;
        time_sicne_last++;
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
    public HashSet<InputStream> getPreviews(MessageCreateEvent event) {
        String link= event.getMessage().getContent();
        HashSet<InputStream> streams = new HashSet<>();
        ImageChecker c = new ImageChecker();
        ArrayList<BufferedImage> images = getBestResolution(getLinksToPreview(link));
        IVCompressor compressor = new IVCompressor();
        long maxSize=getMaxFileUploadSize(event.getGuild().block());
        images.forEach(im -> {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(im, "jpeg", os);// Passing: ​(RenderedImage im, String formatName, OutputStream output)
                byte[] bytes = os.toByteArray();
                if (bytes.length>maxSize){
                    System.out.print(String.format("Original size -> %f MB\n",(double)bytes.length/(1e6)));
                    IVSize customRes=new IVSize();
                    int defWidth=im.getWidth();
                    int defHeight=im.getHeight();
                    double a = Math.sqrt((double)((double)maxSize/(double) bytes.length));
                    int newW= (int) Math.floor(a *(double) defWidth);
                    int newH=(int)Math.floor(a *(double) defHeight);
                    if (newW%2!=0)newW-=1;
                    if (newH%2!=0)newH-=1;
                    customRes.setHeight(newH);
                    customRes.setWidth(newW);
                    bytes=compressor.resizeImageWithCustomRes(bytes, ImageFormats.JPEG,customRes);
                    System.out.print(String.format("New size -> %f MB\n",(double)bytes.length/(1e6)));
                }
                streams.add(new ByteArrayInputStream(bytes));
                bytes=null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return streams;

    }

    /**
     * calculates max file upload size for selected server
     * 8mb for boost level 0 to 1
     * 50mb for boost level 2
     * 100mb for boost level 3
     * @param guild server's guild
     * @return max file size in bytes
     */
    private long getMaxFileUploadSize(Guild guild) {
        int tier = guild.getPremiumTier().getValue();
        System.out.println("Premium tier is "+tier);
        switch (tier){
            case 2:
                return (long) (50 * 1e6);
            case 3:
                return (long) (100 * 1e6);
            default:
                return (long) (8 * 1e6);
        }
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
        if (Math.abs(equals-max_equal)<=error) same = true;
        return same;
    }

}

