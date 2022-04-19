package ai.jiffy.message.dto;

import java.util.Map;

/**
 * Created by Aswath Murugan on 13/04/22.
 **/

public class Message {
  Map<String, Object> data;

  public Message(){
  }

  public Message(Map<String, Object> data){
    this.data = data;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }
}
