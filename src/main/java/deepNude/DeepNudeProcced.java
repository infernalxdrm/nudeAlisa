package deepNude;

import core.services.video.ImagePr;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeepNudeProcced {

    public Mono<Void> getDeepNude(MessageCreateEvent event) {
        event.getMessage().getAttachments().forEach(attachment -> {
            ImagePr.writeImage(ImagePr.readImage(attachment.getUrl()), "/DeepNude_NoWatermark_withModel/input.png", "png");
            synchronized (DeepNudeProcced.class) {
                try {
                    String invoke = "python3 /DeepNude_NoWatermark_withModel/main.py;";
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
                                        r.addFile("nude.png", new FileInputStream("/DeepNude_NoWatermark_withModel/output.png"));
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
