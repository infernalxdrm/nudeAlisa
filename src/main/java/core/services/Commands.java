package core.services;

import answerable.answerableManager;
import chan_2.chApi;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import core.PerformanceMonitor;
import core.nudeAlisa;
import core.services.audio.GuildAudioManager;
import core.services.help.mainHelp;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Color;
import instagram.InstagramManager;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

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
       commands.put("startHell",event -> event.getMessage().getChannel().flatMap(c->c.createMessage(nudeAlisa.client.getSelf().block().getMention()+" Tell me a fairy tale").then()));
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
        commands.put("play", event -> loadMuisic(properties, event));
        commands.put("p",event -> loadMuisic(properties,event));
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
       commands.put("tv",event ->properties.tv.test(event.getMessage()));
        commands.put("help", mainHelp::getHelp);
        commands.put("pixelart", event ->
                event.getMessage().getContent().split(" ").length==3 ?
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
        commands.put("stat", PerformanceMonitor::__);
        //  commands.put("nude", event -> properties.deepNude.getDeepNude(event)); deprecated

        // TODO: 11/8/2020 rearrange commands
        ////// AIGUNGEON COMMANDS ////////////
        // commands.put("dungeon_start", AiDungeon::init);
        altCommands.put("", event -> event.getMessage().getChannel()
                .flatMap(channel ->
                        getMessage(properties, event, channel)
                )
                .then());
        altCommands.put("2ch", event -> properties._2ch_.proceed(event));
        specialCase.put("https://instagram", e -> InstagramManager.of(e.getGuildId().get())
                .getService().sendPreviews(e)
        );

    }

    private Mono<Void> loadMuisic(Properties properties, MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMessage().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .filter(command -> command.size() != 1)
                .map(command -> command.get(1).split("&list")[0])
                .map(command_l -> command_l.contains("http") ?
                        command_l :
                        "ytsearch:" + getSearchQue(event.getMessage().getContent())
                )
                .doOnNext(url -> properties.getPlayerManager().loadItem(url, new AudioLoadResultHandler() {
                    @Override
                    public void trackLoaded(AudioTrack audioTrack) {
                        GuildAudioManager.of(event.getGuildId().get()).getScheduler().play(audioTrack, false);
                    }

                    @Override
                    public void playlistLoaded(AudioPlaylist audioPlaylist) {
                        if (audioPlaylist.isSearchResult()){
                            // TODO: 7/14/2021 Let user choose from searchedList
                            int selected=0;
                            GuildAudioManager.of(event.getGuildId().get()).getScheduler().play(audioPlaylist.getTracks().get(selected));
                            event.getMessage().getChannel().block().createMessage("Playing🤖 ```" + audioPlaylist.getTracks().get(selected).getInfo().title+"```").block();
                            return;
                        }
                        GuildAudioManager.of(event.getGuildId().get()).getScheduler().getQueue().addAll(audioPlaylist.getTracks());
                    }

                    @Override
                    public void noMatches() {
                        event.getMessage().getChannel().block().createMessage("No matches").block();
                    }

                    @Override
                    public void loadFailed(FriendlyException e) {
                        event.getMessage().getChannel()
                                .map(channel -> channel.createMessage(e.getMessage()))
                                .then();
                    }
                }))

                .then();
    }

    private String getSearchQue(String content) {
        return Arrays.stream(content.split(" ")).skip(1).collect(Collectors.joining(" "));
    }

    private Mono<Void> getMessage(Properties properties, MessageCreateEvent event, MessageChannel channel) {


        if (event.getMessage().getUserMentionIds().contains(Properties.id)) {
            try {
                return channel.createMessage(s->s.setTts(true)
                        .setContent(answerableManager.of(event.getGuildId().get()).getAnwerable()
                                .respond(event.getMessage().getContent()
                                        .substring(nudeAlisa.client.getSelf().block().getMention().length() - 1)
                                        ,event.getGuild().block()))

                ).then();
            } catch (Exception e) {
                e.printStackTrace();
                return event.getMessage().getChannel().then();
            }
        } else return event.getMessage().getChannel().then();
    }


}
