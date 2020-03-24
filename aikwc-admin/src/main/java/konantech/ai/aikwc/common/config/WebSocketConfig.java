package konantech.ai.aikwc.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	CheckStatusHandler statusHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//endPoint 등록 , CORS 처리, sockJS 등록
		registry.addHandler(statusHandler, "/ws/getCollectorStatus").setAllowedOrigins("*").withSockJS();
	}

}
