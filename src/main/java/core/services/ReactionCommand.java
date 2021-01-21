package core.services;

import discord4j.core.event.domain.message.ReactionAddEvent;
import reactor.core.publisher.Mono;

public interface ReactionCommand {
    Mono<Void> execute(ReactionAddEvent event);
}
