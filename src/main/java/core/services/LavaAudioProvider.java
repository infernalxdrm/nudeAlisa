package core.services;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class LavaAudioProvider extends AudioProvider {
    private final AudioPlayer player;
    private final MutableAudioFrame frame = new MutableAudioFrame();
    public LavaAudioProvider() {
        super();
    }

    public LavaAudioProvider(ByteBuffer buffer) {
        super(buffer);
    }

    @Override
    public ByteBuffer getBuffer() {
        return super.getBuffer();
    }

    @Override
    public boolean provide() {
        return false;
    }
}
