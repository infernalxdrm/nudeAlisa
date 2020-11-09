package AiDungeon;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class AiDungeonParser {
    private AtomicReference<HtmlPage> page = new AtomicReference<>();

    public void init() throws IOException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("token", AIDungeonSession.token);
        WebSocketClient client = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        StompSessionHandler sessionHandler = new AIDungeonSession();
        stompClient.connect("wss://api.aidungeon.io/subscriptions/", sessionHandler, "user agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36");

    }


}
