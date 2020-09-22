package core;

import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class LavaAudioProvider extends AudioProvider {
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
