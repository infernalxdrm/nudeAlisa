package core.services;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.voice.AudioProvider;

import java.util.ArrayList;

public class Properties  {
    ArrayList<Service> services=new ArrayList<>();
    AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    final AudioPlayer player = playerManager.createPlayer();
    // We will be creating LavaPlayerAudioProvider in the next step
    AudioProvider provider = new LavaAudioProvider(player);
    final TrackScheduler scheduler = new TrackScheduler(player);

    public Properties(){
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        AudioSourceManagers.registerRemoteSources(playerManager);
        //add all services down here
        services.add(new Commands());

        //...
        //init setup for services you added
        services.forEach(service -> service.setup(this));
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
