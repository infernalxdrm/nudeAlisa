package instagram;

import discord4j.common.util.Snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InstagramManager {
    private static final Map<Snowflake, InstagramManager> MANAGERS = new ConcurrentHashMap<>();
    private instagram service;

    private InstagramManager() {
        service = new instagram();
    }

    public static InstagramManager of(Snowflake id) {
        return MANAGERS.computeIfAbsent(id, ignored -> new InstagramManager());
    }

    public instagram getService() {
        return service;
    }

    public void timer() {
    }
}
