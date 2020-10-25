package core.services.video;

import discord4j.core.event.domain.message.MessageEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.SneakyThrows;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class TV {
    private static final float size_persent=0.1f;
    public static final String White="⬜";
    public static final String black="⬛";
    public static final String red="🟥";
    public static final String blue="🟦";
    public static final String green="🟩";
    public static final int fullHD_x=1920;
    public static final int fullHD_y=1080;
    public  float toHD=0.666f; //0,6666
    public  Mono<Void> test(Message event){
        StringBuilder builder=new StringBuilder();
        final MessageChannel channel = event.getChannel().block();
        for (int j = 0; j < fullHD_x*toHD*size_persent; j++) {
            builder.append("⬛");
        }
        for (int i = 0; i < fullHD_y*toHD*size_persent; i++) {
            channel.createMessage(builder.toString()).block();
        }
            //builder.append("\n");
        return channel.createMessage("").then();
        //return builder.toString();
    }
    @SneakyThrows
    public  Mono<Void> photo(Message event, String data){
        BufferedImage image = null;
        try {
            String [] dataSet=data.split(" ");
            final String urlStr = dataSet[0]; // link
            switch (dataSet[1].toLowerCase()){ //resolution
                case "hd":
                    toHD=1280f/fullHD_x;
                    break;
                case"fullhd":
                    toHD=1;
                    break;

                case "qqvga":
                    toHD=160f/fullHD_x;
                    break;
                case "hqvga":
                    toHD=240f/fullHD_x;
                    break;

                case "qvga":
                    toHD=320f/fullHD_x;
                    break;

                case "tv":
                    toHD=640f/fullHD_x;
                    break;
                case "vga":
                    toHD=640f/fullHD_x;
                    break;
                case "svga":
                    toHD=800f/fullHD_x;
                    break;
                case "hd+":
                    toHD=1600f/fullHD_x;
                    break;
                case "2k":
                    toHD=2048f/fullHD_x;
                    break;
                case "4k":
                    toHD=3840f/fullHD_x;
                    break;
                default:
                    toHD=0.66f;
                    break;
            }
            final URL url = new URL(urlStr);
            final HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
            image = ImageIO.read(connection.getInputStream()); //reading image from link
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder=new StringBuilder();
        BufferedImage resized=resizeImage(image,(int)(fullHD_x * toHD * size_persent), (int) (image.getHeight()*( (fullHD_x * toHD * size_persent)/image.getWidth())));
        final MessageChannel channel = event.getChannel().block();
        for (int y = 0; y < resized.getHeight(); y++){
            for (int x =0;x <resized.getWidth();x++) {
                int av_pixel=resized.getRGB(x,y);

                     int b = (av_pixel)&0xFF;
                     int g = (av_pixel>>8)&0xFF;
                     int r = (av_pixel>>16)&0xFF;
                     int a = (av_pixel>>24)&0xFF;
                     /* select main color */
                     int rgb = r + g + b;
                     if (rgb>=615)builder.append(White);
                     else if (rgb<153)builder.append(black);
                     else {
                      if (b > g && b > r) builder.append(blue);
                     else if (g > r && g > b) builder.append(green);
                     else  if (r > g && r > b)builder.append(red);
                     /* //////////////////////////////  */
                     }
                 }



            channel.createMessage(builder.toString()).block();
            builder=new StringBuilder();
        }


        //builder.append("\n");
        return event.getChannel().then();
        //return builder.toString();
    }
    static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB );
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }
}
