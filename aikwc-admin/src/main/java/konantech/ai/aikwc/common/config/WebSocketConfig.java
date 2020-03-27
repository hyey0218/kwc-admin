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
	StatusWebSocketHandler statusHandler;
	
	@Autowired
	MsgWebSocketHandler msgHandler;
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		//endPoint 등록 , CORS 처리, sockJS 등록
		registry.addHandler(statusHandler, "/ws/getStatus").setAllowedOrigins("*").withSockJS();
		registry.addHandler(msgHandler, "/ws/getLogMsg").setAllowedOrigins("*").withSockJS();
	}

}
