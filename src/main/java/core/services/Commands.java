package core.services;

import AiDungeon.AiDungeon;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sun.xml.internal.bind.v2.TODO;
import com.sun.xml.internal.fastinfoset.tools.FI_StAX_SAX_Or_XML_SAX_SAXEvent;
import core.nudeAlisa;
import core.services.audio.GuildAudioManager;
import core.services.video.TV;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.Reaction;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Commands implements Service {
    public static final HashMap<String, Command> commands=new HashMap<>();
    public static final HashMap<String,Command> altCommands=new HashMap<>();
    public static boolean extraCheck(Message m){
        //add more
        return m.getUserMentionIds().contains(nudeAlisa.client.getSelfId());
    }




    @Override
    public void  setup(Properties properties){
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
                    .flatMap(voiceChannel -> voiceChannel.join(spec->spec.setProvider(
                            GuildAudioManager.of(voiceChannel.getGuildId()).getProvider()
                    )))
       .then());
       // Command for bot to leave the Voice Channel
        // TODO: 9/27/2020 add command
        // Command for playing music
        commands.put("play", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .doOnNext(command -> properties.getPlayerManager().loadItem(command.get(1), new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack audioTrack) {
                        GuildAudioManager.of(event.getGuildId().get()).getScheduler().play(audioTrack,false);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist audioPlaylist) {
                        GuildAudioManager.of(event.getGuildId().get()).getScheduler().getQueue().addAll(audioPlaylist.getTracks());
                    }

                    @Override
                    public void noMatches() {

                    }

                    @Override
                    public void loadFailed(FriendlyException e) {
                            event.getMessage().getChannel()
                                    .map(channel -> channel.createMessage(e.getMessage()))
                                    .then();
                    }
                }))

                .then());
        // Command for showing playlist
        commands.put("q",event -> event.getMessage().getChannel()
        .flatMap(channel->channel.createMessage(GuildAudioManager.of(event.getGuildId().get()).getScheduler().getPlayList()))
        .then());
        // Command for looping the track
        commands.put("loop",event -> event.getMessage().getChannel()
        .flatMap(channel->{
            if (GuildAudioManager.of(event.getGuildId().get()).getScheduler().loop())return channel.createMessage("Now Looped"+"👍");
            return channel.createMessage("Now no more loop 😢");
        })
        .then());
        // Command for looping the queue
        commands.put("qloop",event -> event.getMessage().getChannel()
                .flatMap(channel->{
                    if (GuildAudioManager.of(event.getGuildId().get()).getScheduler().q_loop())return channel.createMessage("Now queue Looped"+"👍");
                    return channel.createMessage("Now no more loop 😢");
                })
                .then());
        // Command for skipping current track
        commands.put("skip",event -> event.getMessage().getChannel()
                .flatMap(channel->{ GuildAudioManager.of(event.getGuildId().get()).getScheduler().skip();
                   return channel.createMessage("Skipped 🤞");
                })
                .then());
       commands.put("imgur",event -> new Parser().get(event,event.getMessage().getContent()));
        commands.put("lightshoot",event -> new Parser().get(event,event.getMessage().getContent()));
       altCommands.put(Properties.id, event -> event.getMessage().getChannel()
            .flatMap(channel ->
                channel.createMessage(properties.aiMessage.getInstance().respond(event.getMessage().getContent().substring(Properties.id.length()-1)))
            )
       .then());
       commands.put("tv",event ->properties.tv.test(event.getMessage()));
       commands.put("photo",event -> properties.tv.photo(event.getMessage(),event.getMessage().getContent().substring(7)));

        // TODO: 11/8/2020 rearrange commands
        ////// AIGUNGEON COMMANDS ////////////
        commands.put("dungeon_start", AiDungeon::init);

    }


}
