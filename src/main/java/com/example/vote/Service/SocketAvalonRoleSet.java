package com.example.vote.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class SocketAvalonRoleSet extends TextWebSocketHandler {

	private Integer totalPlayerCtn = 0;

    private String merlin;
	
	HashMap<String, WebSocketSession> sessions = new HashMap<>();

	// client에서 메시지가 서버로 전송댈때 실행되는 함수.
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String payload = message.getPayload();
		Map<String, String> mapData = new HashMap<>();
		try {	
            
            System.out.println(payload);

			mapData.put("totalPlayerCtn", this.totalPlayerCtn.toString());
		    sendMessage(mapData);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 세션이 생성될때 시작되는 함수
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		// System.out.println("session.getId():"+session.getId());
		this.totalPlayerCtn ++;
		sessions.put(session.getId(), session);
		Map<String, String> mapData = new HashMap<>();
		mapData.put("totalPlayerCtn", this.totalPlayerCtn.toString());
		sendMessage(mapData);
	}

	// 세션이 끝날때 실행되는 함수
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session.getId());
		this.totalPlayerCtn --;
		super.afterConnectionClosed(session, status);
		Map<String, String> mapData = new HashMap<>();
		mapData.put("totalPlayerCtn", this.totalPlayerCtn.toString());
		sendMessage(mapData);
	}

	// 메시지 json 형식으로 보내는 send 메소드
	private void sendMessage(Map<String, String> mapData) throws JsonProcessingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		for (String sessionKey : sessions.keySet()) {
			WebSocketSession ss = sessions.get(sessionKey);
			ss.sendMessage(new TextMessage(objectMapper.writeValueAsString(mapData)));
		}
	}

}