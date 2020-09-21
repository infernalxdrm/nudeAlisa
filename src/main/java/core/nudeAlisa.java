package core;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;

public class nudeAlisa {
    public static void main(String[] args) {
        if (args.length==0) System.err.print("No argc passed(no token)");
        final GatewayDiscordClient client = DiscordClientBuilder.create(args[0]).build()
                .login()
                .block();
        client.onDisconnect().block();
    }
}
