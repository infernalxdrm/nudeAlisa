package core.services.reactions;

import core.services.ReactionCommand;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.ReactionAddEvent;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReactionListener {
    public HashMap<Snowflake, ReactionCommand> instructions;

    public ReactionListener(Snowflake id) {
        instructions = new HashMap<>();
    }
//   public Map<Integer,Mono<Void>> getInstructions(Snowflake id){
//       return Listeners.get(id);
//   }


    public static Mono<Void> proceedReaction(ReactionAddEvent e) {


        if (
                e.getMember().get().isBot() //||
            //  instructions == null //||
            //!instructions.containsKey(Objects.hash(e.getEmoji(), e.getMessage()))
        )
            return e.getMessage().then();
        System.out.println((Objects.hash(e.getEmoji(), e.getMessage())));
        //Listeners.get(e.getGuildId().get()).get(e.getMessageId()).execute(e).block();
        try {
            ReactionListener listener = ReactionListenerManager.of(e.getGuildId().get());
            ReactionCommand c = listener.instructions.get(e.getMessageId());
            System.out.println(c);
            if (c == null) return e.getMessage().then();
            c.execute(e).block();
            return e.getMessage().then();
        } catch (Exception es) {
            return e.getMessage().then();
        }

    }

}
