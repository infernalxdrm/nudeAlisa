package core.services.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.common.util.Snowflake;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Data
public final class GuildAudioManager {

    private static final Map<Snowflake, GuildAudioManager> MANAGERS = new ConcurrentHashMap<>();

    public static GuildAudioManager of(final Snowflake id) {
        return MANAGERS.computeIfAbsent(id, ignored -> new GuildAudioManager());
    }
    private final AudioPlayer player;
    private final TrackScheduler scheduler;
    private final LavaAudioProvider provider;

    private GuildAudioManager() {
        player = LavaAudioProvider.getPlayer();
        scheduler = new TrackScheduler(player);
        provider = new LavaAudioProvider(player);
        player.addListener(scheduler);
    }

    // getters
}