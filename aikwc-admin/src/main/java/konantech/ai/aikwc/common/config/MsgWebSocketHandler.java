package konantech.ai.aikwc.common.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.KLog;
import konantech.ai.aikwc.repository.KLogRepository;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Component
public class MsgWebSocketHandler extends TextWebSocketHandler {
	
	private Set<WebSocketSession> sessionList = new HashSet<WebSocketSession>();
	
	@Autowired
	private KLogRepository logRepository;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessionList.add(session);
//		String agency = CommonUtil.getUriParamValue(session.getUri().toString(),"agency");
//		sendLogMassage(agency);
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessionList.remove(session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		sessionList.remove(session);
	}
	
	
	public void sendLogMassage(String agency) throws IOException {
		Iterator<WebSocketSession> iterator = sessionList.iterator();
		
		
		while(iterator.hasNext()) {
			WebSocketSession session = iterator.next();
			
			Pageable sort = PageRequest.of(0, 3, Sort.by("create_date").descending());
			
			List<KLog> logs = logRepository.findByAgencyNotRead(agency, sort);

			
			JSONObject obj = new JSONObject();
			JSONArray arr = (JSONArray) CommonUtil.parseToJson(logs);
			obj.put("logs", arr);
			
			TextMessage message = new TextMessage(obj.toString());
			session.sendMessage(message);
			
		}
		
	}
	
}
