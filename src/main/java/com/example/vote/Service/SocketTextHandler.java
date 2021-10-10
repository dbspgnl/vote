package com.example.vote.Service;

import java.io.IOException;
import java.util.HashMap;

import com.example.vote.Model.Information;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class SocketTextHandler extends TextWebSocketHandler {

	private Integer count = 0;

	//ArrayList<WebSocketSession> sessions = new ArrayList<>();
	HashMap<String, WebSocketSession> sessions = new HashMap<>();

	// client에서 메시지가 서버로 전송댈때 실행되는 함수.
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String payload = message.getPayload();

		try {

			if(payload.equals("#this#divide#payload")){
				this.count = 0;
				payload = "------------------------";
			}
			else {
				this.count += 1;
			}

			if(payload.equals("#this#reset#count")){
				this.count = 0;
				payload = "";
			}

			Information info = new Information();
			info.setCount(this.count);
			info.setPayload(payload);
			ObjectMapper objectMapper = new ObjectMapper();

			// 접속된 모든 세션에 메시지 전송
			for (String key : sessions.keySet()) {
				WebSocketSession ss = sessions.get(key);
				// ss.sendMessage(new TextMessage(payload));
				ss.sendMessage(new TextMessage(objectMapper.writeValueAsString(info)));
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