package core.services;

import chan_2.chApi;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.nudeAlisa;
import core.services.audio.GuildAudioManager;
import core.services.help.mainHelp;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import instagram.instagram;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;

public class Commands implements Service {
    public static final HashMap<String, Command> commands=new HashMap<>();
    public static final HashMap<String,Command> altCommands=new HashMap<>();
    public static final HashMap<String, Command> specialCase = new HashMap<>();
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
//       commands.put("test_file",event -> event.getMessage()
//       .getChannel()
//               .flatMap(chanel->chanel.createMessage(messageCreateSpec -> {
//                   try {
//                       messageCreateSpec.addFile("test",new FileInputStream("C:/Users/User/Pictures/test.png"));
//                   } catch (FileNotFoundException e) {
//                       e.printStackTrace();
//                   }
//               })
//               .then()));
       // Command for bot to join Voice Channel
       commands.put("join",event -> Mono.justOrEmpty(event.getMember())
       .flatMap(Member::getVoiceState)

               .flatMap(VoiceState::getChannel)
                    .flatMap(voiceChannel -> voiceChannel.join(spec->spec.setProvider(
                            GuildAudioManager.of(voiceChannel.getGuildId()).getProvider()
                    )))
               .flatMap(voiceConnection -> {
                   GuildAudioManager.of(voiceConnection.getGuildId()).saveConnection(voiceConnection);
                   return event.getMessage().getChannel().then();
               })
       .then());


       // Command for bot to leave the Voice Channel
        // TODO: 9/27/2020 add command
        // Command for playing music
        commands.put("play", event -> Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .filter(command -> command.size() != 1)
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
                .flatMap(channel -> channel.createEmbed(spec -> spec.setColor(Color.DARK_GOLDENROD)
                        .setThumbnail(mainHelp.photo)
                        .setDescription(GuildAudioManager.of(event.getGuildId().get()).getScheduler().getPlayList())
                        .setTimestamp(Instant.now())
                        .setTitle("Playlist")
                ))
        .then());
        // Command for looping the track
        commands.put("loop",event -> event.getMessage().getChannel()
        .flatMap(channel->{
            if (GuildAudioManager.of(event.getGuildId().get()).getScheduler().loop())
                return channel.createMessage("Now Looped" + ":thumbsup:");
            return channel.createMessage("Now no more loop :cry:");
        })
        .then());
        // Command for looping the queue
        commands.put("qloop",event -> event.getMessage().getChannel()
                .flatMap(channel->{
                    if (GuildAudioManager.of(event.getGuildId().get()).getScheduler().q_loop())
                        return channel.createMessage("Now queue Looped" + ":thumbsup:");
                    return channel.createMessage("Now no more playlist loop :cry:");
                })
                .then());
        // Command for skipping current track
        commands.put("skip",event -> event.getMessage().getChannel()
                .flatMap(channel->{ GuildAudioManager.of(event.getGuildId().get()).getScheduler().skip();
                    return channel.createMessage("Skipped :fingers_crossed:");
                })
                .then());
       commands.put("imgur",event -> new Parser().get(event,event.getMessage().getContent()));
        commands.put("lightshot", event -> new Parser().get(event, event.getMessage().getContent()));
       altCommands.put(Properties.id, event -> event.getMessage().getChannel()
            .flatMap(channel ->
                channel.createMessage(properties.aiMessage.getInstance().respond(event.getMessage().getContent().substring(Properties.id.length()-1)))
            )
       .then());
       commands.put("tv",event ->properties.tv.test(event.getMessage()));
        commands.put("help", mainHelp::getHelp);
        commands.put("pixelart", event ->
                event.getMessage().getContent().contains(" ") ?
                        properties.tv.photo(event.getMessage(), event.getMessage().getContent().substring(10)) :
                        event.getMessage().getChannel().map(channel -> channel.createMessage("Missing arguments").block()).then())
        ;
        commands.put("simp", event ->
                event.getMessage().getContent().contains(" ") ?
                        properties.ImageFun.simp(event, event.getMessage().getContent().substring(6)) :
                        event.getMessage().getChannel().map(channel -> channel.createMessage("Missing argument").block()).then()); // TODO: 4/6/2021 this fucing line doesnt work fix it
        commands.put("2ch help", chApi::help);
        commands.put("2ch_boards", event -> event.getMessage().getChannel().flatMap(channel -> channel.createMessage(properties._2ch_.getBoards())).then());
        commands.put("2ch_board", event -> properties._2ch_.proceed(event));
        commands.put("dc", event -> GuildAudioManager.of(event.getGuildId().get()).disconnect(event.getGuildId().get()));
        commands.put("disconnect", event -> GuildAudioManager.of(event.getGuildId().get()).disconnect(event.getGuildId().get()));
        commands.put("leave", event -> GuildAudioManager.of(event.getGuildId().get()).disconnect(event.getGuildId().get()));

        //  commands.put("nude", event -> properties.deepNude.getDeepNude(event)); deprecated

        // TODO: 11/8/2020 rearrange commands
        ////// AIGUNGEON COMMANDS ////////////
        // commands.put("dungeon_start", AiDungeon::init);
        altCommands.put("2ch", event -> properties._2ch_.proceed(event));
        specialCase.put("https://instagram", instagram::sendPreviews);

    }


}
