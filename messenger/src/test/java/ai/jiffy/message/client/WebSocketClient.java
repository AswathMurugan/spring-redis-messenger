package ai.jiffy.message.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
/**
 * Created by Aswath Murugan on 07/04/22.
 **/
public class WebSocketClient {

  private static WebSocketStompClient stompClient;

  private static StompSessionHandler sessionHandler = new StompSessionHandler();

  private static void websocketInit() {
    List<Transport> transports = new ArrayList<>();
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    container.setDefaultMaxBinaryMessageBufferSize(3 * 1024 * 1024);
    container.setDefaultMaxTextMessageBufferSize(3 * 1024 * 1024);

    StandardWebSocketClient wsc = new StandardWebSocketClient(container);
    transports.add(new WebSocketTransport(wsc));
    org.springframework.web.socket.client.WebSocketClient client = new SockJsClient(transports);
    stompClient = new WebSocketStompClient(client);
    stompClient.setInboundMessageSizeLimit(Integer.MAX_VALUE);
    stompClient.setMessageConverter(new StringMessageConverter());
    System.out.println("Client socket initialisation completed");

    ListenableFuture<StompSession> connect = stompClient.connect("ws://localhost:5000/websocket",
        sessionHandler);

    System.out.println("connected");

  }



  public static void main(String[] args) throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    websocketInit();
    latch.await();
  }


}
