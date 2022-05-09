package com.example.vote.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

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
public class SocketScreenShare extends TextWebSocketHandler {

	HashMap<String, WebSocketSession> sessions = new HashMap<>();
	HashMap<String, Object> mapData = new HashMap<String, Object>();
	// Float shareTime = null;

	// client에서 메시지가 서버로 전송댈때 실행되는 함수.
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws ParseException {
		String payload = message.getPayload();
		JSONObject json = jsonToObjectParser(payload); 
		// System.out.println(json.get("video_timestamp"));
		
		try {
			System.out.println(json);
			// shareTime = Float.parseFloat(json.get("video_timestamp").toString());
			// System.out.println(shareTime);

			HashMap<String, Object> hashMap = new HashMap<>();
			hashMap.put("video_timestamp", json.get("video_timestamp"));
			hashMap.put("playing", json.get("playing"));
			// hashMap.put("streamable_url", "get_streamable_url(youtube_url)");
			hashMap.put("last_updated", new Timestamp(System.currentTimeMillis()).toString());
			hashMap.put("global_time", 0);
			hashMap.put("client_uid", sessions.keySet().toString());
			// hashMap.put("video_shareTime", shareTime);
					
			JSONObject jsonObject = new JSONObject(hashMap);
			// System.out.println(jsonObject.toJSONString());
			
			for (String key : sessions.keySet()) {
				WebSocketSession ss = sessions.get(key);
				ss.sendMessage(new TextMessage(jsonObject.toJSONString()));
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

	private static JSONObject jsonToObjectParser(String jsonStr) {
		JSONParser parser = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) parser.parse(jsonStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return obj;
	}

}