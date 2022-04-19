package ai.jiffy.message.publisher.ws;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.AbstractSubProtocolEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscriptionListener {

  private static Logger logger = LoggerFactory.getLogger(SubscriptionListener.class);

  @Autowired
  ReactiveRedisMessageListenerContainer container;

  @Autowired
  SimpMessagingTemplate messagingTemplate;

  @Autowired
  StreamReceiver<String, ObjectRecord<String, String>> streamReceiver;

  Map<String, ClientConnection> connections = new ConcurrentHashMap<>();
  Map<String, ClientConnection> streamConnections = new ConcurrentHashMap<>();
  
  @EventListener
  public void onSubscribe(SessionSubscribeEvent event) {
    String channelId = getDestination(event);
    channelId = channelId.substring(channelId.lastIndexOf('/') + 1);
    logger.info("Channel Id {}", channelId);
    PubSubConnection connection = new PubSubConnection(channelId, container, messagingTemplate);
    StreamConnection streamConnection = new StreamConnection(streamReceiver,channelId, messagingTemplate);
    String sessionId = getSessionId(event);
    logger.info("On Application session id {}", sessionId);
    connections.put(sessionId, connection);
    streamConnections.put(sessionId, streamConnection);
  }

  @EventListener
  public void onSocketConnected(SessionConnectedEvent event) {
    StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
    logger.info("[Connected] {}" ,header.getSessionId());
  }

  @EventListener
  public void onSocketDisconnected(SessionDisconnectEvent event)  {
    StompHeaderAccessor header = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = header.getSessionId();

    try{
      ClientConnection connection = connections.get(sessionId);
      connection.close();
      connections.remove(sessionId);

      ClientConnection clientConnection = streamConnections.get(sessionId);
      clientConnection.close();
      streamConnections.remove(sessionId);
    } catch (Exception ex){
      logger.error("Error while closing the connection {}", ex.getMessage(), ex);
    }
    logger.info("Removed session id {}",  sessionId);
    logger.info("[DisConnected] {}", sessionId);
  }

  private String getDestination(AbstractSubProtocolEvent event) {
    return getHeader(event, "simpDestination");
  }

  private String getSessionId(AbstractSubProtocolEvent event) {
    return getHeader(event, "simpSessionId");
  }

  private String getHeader(AbstractSubProtocolEvent event, String name) {
    Object value = event.getMessage().getHeaders().get(name);
    return Objects.requireNonNull(value).toString();
  }
}