package core.services;

import answerable.AI;
import chan_2.chApi;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import core.ReactionListener;
import core.services.audio.LavaAudioProvider;
import core.services.audio.TrackScheduler;
import core.services.video.TV;
import deepNude.DeepNudeProcced;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.voice.AudioProvider;
import imageFun.imageFun;

import java.util.ArrayList;

public class Properties  {
    GatewayDiscordClient client;
    ArrayList<Service> services=new ArrayList<>();
    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    final AudioPlayer player = playerManager.createPlayer();
    // We will be creating LavaPlayerAudioProvider in the next step
    AudioProvider provider = new LavaAudioProvider(player);
    public ReactionListener listener;
    DeepNudeProcced deepNude = new DeepNudeProcced();
    chApi _2ch_ = new chApi(this);
    final TrackScheduler scheduler = new TrackScheduler(player);
    final imageFun ImageFun = new imageFun();
    public static Snowflake id;// TODO: 9/29/2020 its better to check by id not by string
    final AI aiMessage = new AI();//new AI();
    final TV tv=new TV();

    public Properties(GatewayDiscordClient client){
        this.client=client;
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        //add all services down here
        services.add(new Commands());

        //...
        //init setup for services you added
        services.forEach(service -> service.setup(this));
    }

    public ReactionListener getListener() {
        return listener;
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public AudioProvider getAudioProvider() {
        return provider;
    }

    public TrackScheduler getScheduler() {
        return scheduler;
    }
}
