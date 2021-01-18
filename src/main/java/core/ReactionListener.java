package core;

import core.services.Command;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.ReactionAddEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReactionListener {
    public static HashMap<Snowflake,
            Map<Integer, Command>
            > Listeners = new HashMap<>();
    public HashMap<Integer, Command> instructions = new HashMap<>();

    public ReactionListener(Snowflake id) {
        Listeners.put(id, instructions);
    }
//   public Map<Integer,Mono<Void>> getInstructions(Snowflake id){
//       return Listeners.get(id);
//   }


    public static Command proceedReaction(ReactionAddEvent e) {

        Map<Integer, Command> instructions = Listeners.get(e.getGuildId());
        if (
                e.getMember().get().isBot() ||
                        instructions == null ||
                        !instructions.containsKey(Objects.hash(e.getEmoji(), e.getMessage()))
        ) return event -> e.getMessage().then();


        return Listeners.get(e.getGuildId().get()).get(Objects.hash(e.getEmoji(), e.getMessage()));

    }

}
