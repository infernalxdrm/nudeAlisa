package core.services.help;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;

import java.time.Instant;

public class mainHelp {
    public  void getHelp(MessageCreateEvent event){
     event.getMessage().getChannel().map(channel -> channel.createEmbed(spec ->
             spec.setColor(Color.RED)
                     .setAuthor("setAuthor", ANY_URL, IMAGE_URL)
                     .setImage(IMAGE_URL)
                     .setTitle("setTitle/setUrl")
                     .setUrl(ANY_URL)
                     .setDescription("setDescription\n" +
                             "big D: is setImage\n" +
                             "small D: is setThumbnail\n" +
                             "<-- setColor")
                     .addField("addField", "inline = true", true)
                     .addField("addFIeld", "inline = true", true)
                     .addField("addFile", "inline = false", false)
                     .setThumbnail(IMAGE_URL)
                     .setFooter("setFooter --> setTimestamp", IMAGE_URL)
                     .setTimestamp(Instant.now())
     ).block());
    }
}
