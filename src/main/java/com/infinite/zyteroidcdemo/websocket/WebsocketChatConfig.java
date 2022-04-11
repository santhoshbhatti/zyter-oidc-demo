package com.infinite.zyteroidcdemo.websocket;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.infinite.zyteroidcdemo.websocket.security.JWSAuthenticationToken;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@AllArgsConstructor
public class WebsocketChatConfig implements WebSocketMessageBrokerConfigurer {
	@Qualifier("websocket")
	private AuthenticationManager authenticationManager;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocketApp").setAllowedOrigins("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app/");
		registry.enableStompBrokerRelay("/topic/")
		.setRelayHost("localhost").setRelayPort(61613).setClientLogin("guest")
				.setClientPasscode("guest");

	}
	
	@Override
	  public void configureClientInboundChannel(ChannelRegistration registration) {
	    registration.interceptors(new ChannelInterceptor() {
	      @Override
	      public Message<?> preSend(Message<?> message, MessageChannel channel) {
	        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
	        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
	          Optional.ofNullable(accessor.getNativeHeader("Authorization")).ifPresent(ah -> {
	            String bearerToken = ah.get(0).replace("Bearer ", "");
	            log.debug("Received bearer token {}", bearerToken);
	            JWSAuthenticationToken token = (JWSAuthenticationToken) authenticationManager
	                .authenticate(new JWSAuthenticationToken(bearerToken));
	            accessor.setUser(token);
	          });
	        }
	        return message;
	      }
	    });
	  }
}
