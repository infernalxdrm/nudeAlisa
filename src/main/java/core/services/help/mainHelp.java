package core.services.help;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class mainHelp {
    static public final String photo = "https://i.imgur.com/dzn8huv.png";

    public static Mono<Void> getHelp(MessageCreateEvent event) {
        final MessageChannel channel = event.getMessage().getChannel().block();
        assert channel != null;
        channel.createEmbed(spec ->
             spec.setColor(Color.RED)
                     .setAuthor("Alica bot", "https://github.com/Kw0rker/nudeAlisa", photo)
                     .setImage(photo)
                     .setTitle("User's Guide")
                     .setUrl("https://github.com/Kw0rker/nudeAlisa/wiki/User's-guide")
                     .setDescription("help - prints user's guide\n\n" +
                             "join - makes bot join the voice channel you're currently in\n\n" +
                             "play - plays a music from the link (bot must be in the voice channel. Type join to invite)\n\n" +
                             "q - prints a current playlist\n\n" +
                             "loop - turns on and off current track repeat\n\n" +
                             "qloop - turns on and off playlist repeat\n\n" +
                             "simp - issue a simp card for user tagged\n" +
                             "imgur - sends random pictures form the Imgur\n" +
                             "as a parameter takes the number of pictures user wants to receive (expl 'imgur 10'\n\n" +
                             "lightshot - sends random user's screenshots form the Lightshot\n" +
                             "as a parameter takes the number of pictures user wants to receive (expl 'lightshot 10'\n\n" +
                             "pixelart - turns photo you sent into big discord pixelart\n" +
                             "first parameter is link to the photo second parameter is resolution\n" +
                             "for resolution bigger than vga is better do run discord in the browser ")
                     .addField("Supported resolutions", "https://imgur.com/a/hqeQwv5", true)
                     .addField("Ping", "if you ping Alisa with message, she will reply to you", false)
                     .setThumbnail(photo)
                     .setFooter("Made by Kworker#0101", photo)
                     .setTimestamp(Instant.now())
        ).block();
        return event.getMessage().getChannel().then();
    }
}
