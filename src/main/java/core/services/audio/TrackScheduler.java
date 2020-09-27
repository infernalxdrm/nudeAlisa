package core.services.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public final class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    List<AudioTrack>  playlist ;
    List<AudioTrack> loopList;
    private boolean isLooped=false;
    private AudioTrack current_track;
    private boolean anotherThreadPossessing=false;
    private short inLoopPos=0;


    public TrackScheduler(final AudioPlayer player) {
        this.player = player;
        //checks the playlist and plays music when one is finished
        playlist = Collections.synchronizedList(new LinkedList<>());
        loopList= Collections.synchronizedList(new LinkedList<>());
    }

    public List<AudioTrack> getQueue() {
        return playlist;
    }

    public boolean play(final AudioTrack track) {
        return play(track, false);
    }

    public boolean play(final AudioTrack track, final boolean force) {
        final boolean playing = player.startTrack(track, !force);

        if (!playing) {
            playlist.add(track);
        }

        return playing;
    }

    public boolean skip() {
        return !playlist.isEmpty() && play(playlist.remove(0), true);
    }

    @Override
    public void onTrackEnd(final AudioPlayer player, final AudioTrack track, final AudioTrackEndReason endReason) {
        //if lopped only one track
        if (isLooped && loopList.isEmpty()){
            player.playTrack(track.makeClone());
        }
        // if lopped the whole queue
        else if (isLooped){
            player.playTrack(loopList.get(inLoopPos++).makeClone());
            if (inLoopPos>=loopList.size())inLoopPos=0;
        }
        // Advance the player if the track completed naturally (FINISHED) or if the track cannot play (LOAD_FAILED)
       else if (endReason.mayStartNext) {
            skip();
        }
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

    public boolean clear(int number){
        playlist.remove(number);
        return true;
    }
}
