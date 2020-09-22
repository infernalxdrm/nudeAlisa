package core.services;

import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class Commands {
    static final HashMap<String, Command> commands=new HashMap<>();
    public static void   setup(){
        //Add all commands down here
       commands.put("hello", event->event.getMessage()
               .getChannel().flatMap(chanel->
               chanel.createMessage("Hello"+event.getMessage().getAuthor().get().getMention())
                       .then()));
       commands.put("test_file",event -> event.getMessage()
       .getChannel()
               .flatMap(chanel->chanel.createMessage(messageCreateSpec -> {
                   try {
                       messageCreateSpec.addFile("test",new FileInputStream("C:/Users/User/Pictures/test.png"));
                   } catch (FileNotFoundException e) {
                       e.printStackTrace();
                   }
               })
               .then()));
       // Command for bot to join Voice Channel
       commands.put("join",event -> Mono.justOrEmpty(event.getMember())
       .flatMap(Member::getVoiceState)
               .flatMap(VoiceState::getChannel)
                    .flatMap(voiceChannel -> voiceChannel.join(spec->spec.setProvider(null)))
       .then());
       // Command for bot to leave the Voice Channel
    }
}
