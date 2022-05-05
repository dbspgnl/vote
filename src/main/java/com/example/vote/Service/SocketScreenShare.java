package com.example.vote.Service;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class SocketScreenShare extends TextWebSocketHandler {

	HashMap<String, WebSocketSession> sessions = new HashMap<>();
	// HashMap<String, String> mapData = new HashMap<>();
	String messageString = "";

	// client에서 메시지가 서버로 전송댈때 실행되는 함수.
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String payload = message.getPayload();

		try {
			System.out.println("chat: "+payload);
			// mapData.put("data", payload);
			messageString = "{ \"data\":\""+payload+"\"}";
			
			for (String key : sessions.keySet()) {
				WebSocketSession ss = sessions.get(key);
				ss.sendMessage(new TextMessage(messageString));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 세션이 생성될때 시작되는 함수
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		super.afterConnectionEstablished(session);
		sessions.put(session.getId(), session);
	}

	// 세션이 끝날때 실행되는 함수
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

		sessions.remove(session.getId());
		super.afterConnectionClosed(session, status);

	}

}