package ai.jiffy.message.publisher.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

/**
 * Created by Aswath Murugan on 12/04/22.
 **/
public class StreamConnection implements ClientConnection {

  private static final Logger logger = LoggerFactory.getLogger(StreamConnection.class);

  private final Disposable disposable;

  public StreamConnection(
      StreamReceiver<String, ObjectRecord<String, String>> streamReceiver,
      String channelId,
      SimpMessagingTemplate messagingTemplate
      ) {

    Flux<ObjectRecord<String, String>> receiver = streamReceiver
        .receive(StreamOffset.fromStart(channelId));
    disposable = receiver.subscribe(msg -> {
      logger.info("Processing message {}", msg);
      String value = msg.getValue();
      messagingTemplate.convertAndSend(  "/topic/" + channelId, value);
    });

  }

  @Override
  public void close() {
    if( disposable != null){
      disposable.dispose();
    }
  }
}
