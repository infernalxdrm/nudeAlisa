package imageFun;

import core.services.video.ImagePr;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

public class imageFun {
    public final static String simpLink = "https://i.imgur.com/w9mFq93.png";
    ImagePr imageProcessor;

    public imageFun() {
        imageProcessor = new ImagePr();
    }

    private Image readImage(String url_to) throws IOException {
        final URL url = new URL(url_to);
        final HttpURLConnection connection = (HttpURLConnection) url
                .openConnection();
        connection.setRequestProperty(
                "User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
        return ImageIO.read(connection.getInputStream()); //reading image from link
    }

    public Mono<Void> simp(MessageCreateEvent event, String param) {


        try {
            if (param.contains("@")) {
                LinkedList<Member> members = new LinkedList<>();
                event.getMessage().getUserMentionIds().forEach(id -> members.add(event.getGuild().block().getMemberById(id).block()));
                if (members.size() == 0)
                    return event.getMessage().getChannel().map(channel -> channel.createMessage("User doesnt exist").then()).then();
                Member member = members.get(0);
                BufferedImage im = ImagePr.addText(
                        ImagePr.overlayImages(ImagePr.readImage(simpLink), ImagePr.changeResolution(ImagePr.readImage(member.getAvatarUrl(discord4j.rest.util.Image.Format.PNG).get()

                        ), 220, 325), 49, 74),
                        lol(member.getTag()), 375, 375, new Color(83, 70, 64), 28);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                try {
                    ImageIO.write(im, "png", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                    InputStream is = new ByteArrayInputStream(os.toByteArray());
                    return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(m -> {
                        m.addFile("simp.png", is);
                    })).then();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (param.contains("http")) {
                BufferedImage im = ImagePr.addText(
                        ImagePr.overlayImages(ImagePr.readImage(simpLink), ImagePr.changeResolution(ImagePr.readImage(param), 220, 325), 49, 64),
                        event.getMessage().getAuthor().get().getUsername(), 380, 365, new Color(83, 70, 64), 20);
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(im, "png", os);                          // Passing: ​(RenderedImage im, String formatName, OutputStream output)
                InputStream is = new ByteArrayInputStream(os.toByteArray());
                return event.getMessage().getChannel().flatMap(channel -> channel.createMessage(m -> {
                    m.addFile("simp.png", is);
                })).then();
            }


        } catch (IOException e) {
            e.printStackTrace();
            return event.getMessage().getChannel().flatMap(c -> c.createMessage("Your link is BS")).then();
        }
        return event.getMessage().getChannel().flatMap(c -> c.createMessage("Some Bs happened")).then();
    }

    public String lol(String s) {
        StringBuilder b = new StringBuilder();
        char[] chars = s.toCharArray();
        int a = 0;
        for (char c : chars) {
            b.append(c).append(" ");
            a++;
            if (a == 10) break;
        }
        return b.toString();
    }

}
