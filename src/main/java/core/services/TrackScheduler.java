package core.services;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.LinkedList;
import java.util.Queue;

public final class TrackScheduler implements AudioLoadResultHandler {

    private final AudioPlayer player;
    Queue<AudioTrack> playlist = new LinkedList<>();

    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
        //checks the playlist and plays music when one is finished
        new Thread(()->{
            while (true) {
                try {
                    if (playlist.isEmpty()){
                        Thread.sleep(200);
                        continue;
                    }
                        Thread.sleep(200);
                    if (player.getPlayingTrack() == null) player.playTrack(playlist.poll());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void trackLoaded(final AudioTrack track) {
        // LavaPlayer found an audio source for us to play
        playlist.add(track);

       // player.playTrack(track);

    }

    @Override
    public void playlistLoaded(final AudioPlaylist playlist) {
        this.playlist.addAll(playlist.getTracks());
        // LavaPlayer found multiple AudioTracks from some playlist
    }

    @Override
    public void noMatches() {
        // LavaPlayer did not find any audio to extract
    }

    @Override
    public void loadFailed(final FriendlyException exception) {
        // LavaPlayer could not parse an audio source for some reason
    }
}
