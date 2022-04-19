package ai.jiffy.message.client;

import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

/**
 * Created by Aswath Murugan on 07/04/22.
 **/
public class StompSessionHandler extends StompSessionHandlerAdapter {

  @Override
  public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    System.out.println("Websocket session is established : " + session.getSessionId());
    session.subscribe("/topic/f46cd901-76c5-43f9-b8a9-c2c3fd1a7d8c_4bed1724-0e44-403b-a4f8-57b507a23972_workflowName_workflowID" , this);
    System.out.println("Subscribed");
  }

  @Override
  public void handleFrame(StompHeaders headers, Object payload) {
    String message = (String) payload;
    System.out.println("Message received");
    System.out.println(message);
  }

}
