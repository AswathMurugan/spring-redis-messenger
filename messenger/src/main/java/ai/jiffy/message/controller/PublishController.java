package ai.jiffy.message.controller;

import ai.jiffy.message.dto.Message;
import ai.jiffy.message.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Aswath Murugan on 06/04/22.
 **/
@RestController
public class PublishController {

  @Autowired
  MessageService service;

  @PostMapping("stream/add/{channelId}")
  public RecordId submitMessage(@RequestBody Message msg,
      @PathVariable(value = "channelId", required = true) String channelId)
      throws JsonProcessingException {
    return service.add(channelId, msg);
  }

  @PostMapping("stream/create/{channelId}")
  public RecordId create(@RequestBody Message msg,
      @PathVariable(value = "channelId", required = true) String channelId,
      @RequestParam(value = "expire", defaultValue = "1", required = false) int expire)
      throws JsonProcessingException {
    return service.create(channelId, expire);
  }

}
