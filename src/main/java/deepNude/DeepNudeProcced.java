package deepNude;

import core.services.video.ImagePr;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeepNudeProcced {

    public Mono<Void> getDeepNude(MessageCreateEvent event) {
        event.getMessage().getAttachments().forEach(attachment -> {
            try {
                Files.delete(Paths.get(System.getProperty("user.dir") + "/DeepNude_NoWatermark_withModel/input.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            ImagePr.writeImage(ImagePr.readImage(attachment.getProxyUrl()), System.getProperty("user.dir") + "/DeepNude_NoWatermark_withModel/input.png", "png");
            while (Files.notExists(Paths.get(System.getProperty("user.dir") + "/DeepNude_NoWatermark_withModel/input.png"))) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            synchronized (DeepNudeProcced.class) {
                try {
                    String invoke = "python3 " + System.getProperty("user.dir") + "/DeepNude_NoWatermark_withModel/main.py;";
                    Process deepNude = Runtime.getRuntime().exec(invoke);
                    new Thread(() -> {
                        while (deepNude.isAlive()) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        event.getMessage().getChannel().block().createMessage(
                                r -> {
                                    try {
                                        r.addFile("nude.png", new FileInputStream(System.getProperty("user.dir") + "/DeepNude_NoWatermark_withModel/output.png"));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }

                        ).block();
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return event.getMessage().getChannel().then();
    }
}
