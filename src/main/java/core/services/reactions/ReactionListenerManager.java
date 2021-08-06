package core.services.reactions;

import answerable.answerableManager;
import discord4j.common.util.Snowflake;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ReactionListenerManager {
    private static final Map<Snowflake, ReactionListener> MANAGERS = new ConcurrentHashMap<>();
    public static ReactionListener of(Snowflake id){
        return MANAGERS.computeIfAbsent(id, ReactionListener::new);
    }
}
