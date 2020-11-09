package AiDungeon;

import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

import java.lang.reflect.Type;

public class AIDungeonSession implements StompSessionHandler {
    public final static String single_player_mode_id = "scenario:458612";
    public final static String token = "787098b0-02c5-11eb-bf28-3fd2e02e65d3";
    StompSession stompSession;
    StompHeaders stompHeaders;
    private int id;
    private String char_namee;

    public void subscribe(String message) {
        stompSession.subscribe(message, this);
    }

    public StompSession.Receiptable send(String text, String type) {
        return this.stompSession.send(
                " '''\n" +
                        "            mutation ($input: ContentActionInput) {\n" +
                        "                sendAction(input: $input) {\n" +
                        "                    id\n" +
                        "                    __typename\n" +
                        "                }\n" +
                        "            }",
                String.format("variables = {" +
                        "                'input':{" +
                        "                    'type':%s," +
                        "                    'text':%s," +
                        "                    'id':%d," +
                        "                    'characterName':%s" +
                        "                }" +
                        "            }", type, text, this.id, this.char_namee));


    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        this.stompSession = stompSession;
        this.stompHeaders = stompHeaders;
        //stompSession.send("connection_init","payload:{\"token\":\"787098b0-02c5-11eb-bf28-3fd2e02e65d3\"}");
                /*
                {"type":"connection_init","payload":{"token":"787098b0-02c5-11eb-bf28-3fd2e02e65d3"}}

                 */
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable throwable) {

    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable throwable) {

    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
        return null;
    }

    @Override
    public void handleFrame(StompHeaders stompHeaders, Object payload) {
        Message msg = (Message) payload;
        msg.getHeaders().keySet().forEach(System.out::println);
        System.out.println(msg.getHeaders().get("id"));


    }
}
