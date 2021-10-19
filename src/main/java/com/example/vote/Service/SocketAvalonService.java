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
public class SocketAvalonService extends TextWebSocketHandler {

	private Integer playerCtn = 0;
	private Integer setPlayerCtn = 0;
	private Integer agreeCtn = 0;
	private Integer disagreeCtn = 0;
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
		
		try {
			// System.out.println(payload);
			
			// 세션 확인
			if(payload.contains("#sessionTime")){
				// System.out.println(payload);
				return;
			}

			// 인원 설정
			if(payload.contains("#setPlayerCtn")){
				String numString = payload.split(":")[1];
				Integer num = Integer.parseInt(numString);
				setPlayerCtn = num;
				return;
			}

			// 초기화
			if(payload.equals("#toVoteReset")){
				this.disagreeCtn = 0;
				this.agreeCtn = 0;
				this.playerCtn = 0;
				payload = "#toVoteReset";
			}
			else {
				// 투표
				if(payload.equals("#toVoteAgree")){
					this.agreeCtn += 1;
					this.playerCtn += 1;
				}
				else{
					this.disagreeCtn += 1;
					this.playerCtn += 1;
				}

				System.out.println("playerCtn: "+this.playerCtn);
				System.out.println("setPlayerCtn: "+this.setPlayerCtn);
				System.out.println("agreeCtn: "+this.agreeCtn);
				System.out.println("disagreeCtn: "+this.disagreeCtn);
	
				// 결과
				if(this.playerCtn == this.setPlayerCtn){ // 모두 다 투표시
					if(this.agreeCtn > this.disagreeCtn){ //성공
						payload = "#Success";
	
					}
					else{ // 실패
						payload = "#Failure";
					}
					this.disagreeCtn = 0;
					this.agreeCtn = 0;
					this.playerCtn = 0;
				}

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