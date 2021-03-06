package ai.jiffy.message.conf;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer{

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").withSockJS().setHeartbeatTime(20000).
		setHttpMessageCacheSize(100*2000);

	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {

	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {

	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration arg0) {

	}

	@Override
	public void configureClientOutboundChannel(ChannelRegistration arg0) {

	}

	@Override
	public boolean configureMessageConverters(List<MessageConverter> arg0) {
		return false;
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
		registration.setMessageSizeLimit(200*1024*1024); 
		registration.setSendTimeLimit(60*1024); 
		registration.setSendBufferSizeLimit(200*1024*1024); 
	}


}
