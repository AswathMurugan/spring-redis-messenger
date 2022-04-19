package ai.jiffy.message.publisher.ws;

import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

/**
 * Created by Aswath Murugan on 07/04/22.
 **/
public class PubSubConnection implements ClientConnection {

  private final Disposable disposable;

  public PubSubConnection(String channelId, ReactiveRedisMessageListenerContainer container,
      SimpMessagingTemplate messagingTemplate){
    Flux<ReactiveSubscription.Message<String, String>> receive = container.receive(new ChannelTopic(channelId));
    disposable = receive.subscribe(msg -> {
      messagingTemplate.convertAndSend(channelId, msg.getMessage());
      System.out.println(msg.getChannel() + " " + msg.getMessage());
    });
  }

  @Override
  public void close() {
    disposable.dispose();
  }
}
