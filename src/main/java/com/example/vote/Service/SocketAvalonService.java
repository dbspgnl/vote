package com.example.vote.Service;

import java.io.IOException;
import java.util.HashMap;

import com.example.vote.Model.Information;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor 
public class SocketAvalonService extends TextWebSocketHandler {

	private Integer playerCtn = 0;
	private Integer setPlayerCtn = 0;
	// private String player1;
	// private String player2;
	// private String player3;
	// private String player4;
	// private String player5;
	// private String player6;
	// private String player7;
	// private String player8;
	// private String player9;
	// private String player0;
	
	HashMap<String, WebSocketSession> sessions = new HashMap<>();

	// client에서 메시지가 서버로 전송댈때 실행되는 함수.
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		String payload = message.getPayload();
		System.out.println("==>avalon: "+payload);
		
		JSONParser parser  = new JSONParser();
		// String json1 = "{\"name\":\"Dave\", \"nationality\":\"korea\"}";
		try {
			parser.parse(payload);
			// Object object = (JSONObject)parser.parse(parser);
		// 	if (object instanceof JSONArray) {
		// 		JSONArray jsonArray = (JSONArray)object;
		// 		// jsonArray 클래스를 이용한 처리
		// 	}
		// 	else if (object instanceof JSONObject) {
		// 		JSONObject jsonObject = (JSONObject)object;
		// 		// jsonObject 클래스를 이용한 처리
		// 	}
		// 	System.out.println("==>object: "+object);
		} catch (ParseException e1) {
		// 	// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {

			if(payload.equals("#this#divide#payload")){
				this.playerCtn = 0;
				payload = "------------------------";
			}
			else {
				this.playerCtn += 1;
			}

			if(payload.equals("#this#reset#count")){
				this.playerCtn = 0;
				payload = "";
			}

			Information info = new Information();
			info.setCount(this.playerCtn);
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