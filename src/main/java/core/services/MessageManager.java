package core.services;

import core.services.reactions.ReactionListener;
import core.services.reactions.ReactionListenerManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.reaction.Reaction;
import discord4j.core.object.reaction.ReactionEmoji;
import reactor.core.publisher.Mono;

public class MessageManager {
    public static Mono<Void> createTimedMessage(MessageCreateEvent event,String data,long time){
        final MessageChannel channel = event.getMessage().getChannel().block();
        Message m =channel.createMessage(data).block();
        new Thread(()->{
            for (int i = 0; i < time; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (m!=null)m.delete();
        }).start();
        return event.getMessage().getChannel().then();
    }
    public static void createReactionListener(MessageCreateEvent e,Message message, ReactionEmoji emoji,ReactionCommand c){
        if(message==null) return;
        message.addReaction(emoji).block();
        ReactionListener listener= ReactionListenerManager.of(e.getGuildId().get());
        listener.instructions.put(message.getId(),c);
    }

}
