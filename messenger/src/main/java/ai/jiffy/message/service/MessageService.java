package ai.jiffy.message.service;

import ai.jiffy.message.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Aswath Murugan on 13/04/22.
 **/
@Component
public class MessageService {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Autowired
  ReactiveStringRedisTemplate template;

  public RecordId add(String channelId, Message msg) throws JsonProcessingException {
    return template.opsForStream().add(ObjectRecord.create(channelId, mapper.writeValueAsString(msg))).block();
  }


  public RecordId create(String channelId, int expire) throws JsonProcessingException {
    HashMap<String, Object> data = new HashMap<>();
    data.put("dummy", "message");
    Message message = new Message(data);
    RecordId create = template.opsForStream().add(ObjectRecord.create(channelId, mapper.writeValueAsString(message))).block();
    template.expire(channelId, Duration.ofMillis(expire)).block();
    return create;
  }

}
