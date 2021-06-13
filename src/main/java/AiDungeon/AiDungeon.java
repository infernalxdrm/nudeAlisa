package AiDungeon;

import answerable.answerable;
import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.io.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AiDungeon implements answerable {
    private static final Map<Snowflake, AiDungeon> MANAGERS = new ConcurrentHashMap<>();
    Process AiDugeon;
    private BufferedWriter output;
    private BufferedReader input;

    AiDungeon() {
        try {
            Runtime.getRuntime().exec("cd ai-dungeon-cli");
            Runtime.getRuntime().exec("virtualenv -p $(command -v python3) ai-dungeon-cli-venv");
            Runtime.getRuntime().exec("source ai-dungeon-cli-venv/bin/activate");
            Runtime.getRuntime().exec("python3 -m pip install -r requirements.txt");
            AiDugeon = Runtime.getRuntime().exec("./ai_dungeon_cli/__init__.py");
            output = new BufferedWriter(new OutputStreamWriter(AiDugeon.getOutputStream()));
            input = new BufferedReader(new InputStreamReader(AiDugeon.getInputStream()));
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }

    }

    public static Mono<Void> init(MessageCreateEvent e) {
        MANAGERS.computeIfAbsent(e.getGuildId().get(), ignored -> new AiDungeon());
        // TODO: 11/6/2020 add creating new channel for the game etc
        return e.getMessage().getChannel().then();
    }

    @Override
    public String respond(String message,Object o) {
        return null;
    }

    @Override
    public void setReply(String message) {

    }

    @Override
    public void restart() {

    }

    @Override
    public answerable getInstance() {
        return null;
    }
}
