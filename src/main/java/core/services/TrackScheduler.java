package core.services;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public final class TrackScheduler implements AudioLoadResultHandler {

    private final AudioPlayer player;
    Queue<AudioTrack> playlist = new LinkedList<>();
    LinkedList<AudioTrack> loopList=new LinkedList<>();
    private boolean isLooped=false;
    private AudioTrack current_track;
    private boolean anotherThreadPossessing=false;


    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
        //checks the playlist and plays music when one is finished
        new Thread(()->{
            int numberInLoopQ=0;
            while (true) {
                try {
                    if (playlist.isEmpty() || anotherThreadPossessing){
                        Thread.sleep(200);
                        continue;
                    }
                    synchronized (TrackScheduler.class) {
                        TrackScheduler.class.wait();
                        Thread.sleep(200);
                        //if looped and no loop queue play single track
                        if (player.getPlayingTrack() == null) {
                            if (isLooped && loopList.size() == 0 && current_track != null) {
                                player.playTrack(current_track.makeClone());
                            }
                            // if looped and loop queue isnt empty loop the queue list
                            else if (isLooped) {
                                AudioTrack track = loopList.get(numberInLoopQ++).makeClone();
                                current_track = track;
                                player.playTrack(track);
                                if (numberInLoopQ >= loopList.size()) numberInLoopQ = 0;
                            }
                            //else just a normal behavior first in queue
                            else {
                                current_track = playlist.poll();
                                player.playTrack(current_track);
                            }
                        }
                        TrackScheduler.class.notify();
                    }
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

    public String getPlayList() {
        if(current_track==null)return "No music yet 🤷‍♀️";
        StringBuilder builder = new StringBuilder();
        builder.append("1) Currently playing: ")
                .append(current_track.getInfo().title)
                .append("\nBy Author: ").append(current_track.getInfo().author)
                .append("\nDuration: ").append((int)current_track.getInfo().length/60000).append(":")
                .append((int)((current_track.getInfo().length%60000)/1000))
                .append(" Minutes")
                .append("\n\n\n");
        AtomicInteger number_in_q= new AtomicInteger(2);
        playlist.forEach(audioTrack ->
                builder.append(number_in_q.getAndIncrement())
                .append(")").append(audioTrack.getInfo().title)
                .append("\nBy Author: ").append(audioTrack.getInfo().author)
                .append("\nDuration: ").append((int)audioTrack.getInfo().length/60000).append(":")
                .append((int)((audioTrack.getInfo().length%60000)/1000))
                .append(" Minutes")
                .append("\n\n\n"));
        return builder.toString();
    }
    public void stop(){
        player.stopTrack();
    }
    public boolean loop(){
        if (isLooped) {
            isLooped= false;
            return false;
        }
        //loopList.add(player.getPlayingTrack().makeClone());
        isLooped=true;

        return true;
    }
    public boolean q_loop(){
        if (isLooped) {
            isLooped= false;
            loopList.clear();
            return false;
        }
        loopList.addAll(playlist);
        isLooped=true;
        return true;
    }
    public  void skip(){
        if (current_track==null)return;
        if (playlist.size()==0){
            stop();
            return;
        }
        anotherThreadPossessing=true;
        current_track=playlist.poll();
        player.playTrack(current_track);
        anotherThreadPossessing=false;
    }
    public boolean clear(int number){
        return true;
    }
}
