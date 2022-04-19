package ai.jiffy.message.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.data.redis.stream.StreamReceiver.StreamReceiverOptions;

/**
 * Created by Aswath Murugan on 07/04/22.
 **/

@Configuration
public class MessagingConfiguration {

  @Bean
  ReactiveRedisMessageListenerContainer container(ReactiveRedisConnectionFactory factory) {
    return new ReactiveRedisMessageListenerContainer(factory);
  }

  @Bean
  StreamReceiver<String, ObjectRecord<String, String>> streamReceiver(
      ReactiveRedisConnectionFactory factory){
    return StreamReceiver.create(factory,
        StreamReceiverOptions.builder()
        .targetType(String.class).build());
  }

  @Bean
  ReactiveStringRedisTemplate template(ReactiveRedisConnectionFactory factory){
    return new ReactiveStringRedisTemplate(factory);
  }
}
