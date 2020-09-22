package core.services;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.message.MessageEvent;
import reactor.core.publisher.Mono;

public interface Command {

    public Mono<Void> execute(MessageCreateEvent event);
}
