package core;

import core.services.Commands;
import core.services.Properties;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class nudeAlisa {
    private static char prefix='+';
    public static GatewayDiscordClient client;
    public static void main(String[] args) {
        if (args.length==0) System.err.print("No argc passed(no token)");

        final GatewayDiscordClient client = DiscordClientBuilder.create(args[0]).build()
                .login()
                .block();
        Properties properties=new Properties(client);
        nudeAlisa.client=client;


        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(Commands.commands.entrySet())
                                .filter(entry -> content.startsWith(prefix + entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();

        client.getEventDispatcher().on(MessageCreateEvent.class)
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                        .flatMap(content -> Flux.fromIterable(Commands.altCommands.entrySet())
                                .filter(entry -> content.startsWith(entry.getKey()))
                                .flatMap(entry -> entry.getValue().execute(event))
                                .next()))
                .subscribe();


        client.onDisconnect().block();
    }
}
