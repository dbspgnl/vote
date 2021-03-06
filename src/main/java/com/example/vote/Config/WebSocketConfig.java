package com.example.vote.Config;

import com.example.vote.Service.SocketAvalonCampaign;
import com.example.vote.Service.SocketAvalonExpedition;
import com.example.vote.Service.SocketAvalonRoleSet;
import com.example.vote.Service.SocketScreenShare;
import com.example.vote.Service.SocketTextHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

	@Autowired
	SocketTextHandler socketTextHandler;

	@Autowired
	SocketAvalonExpedition avalonExpedition;

	@Autowired
	SocketAvalonCampaign avalonCampaign;

	@Autowired
	SocketAvalonRoleSet avalonRoleSet;

	@Autowired
	SocketScreenShare screenShare;
	
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(socketTextHandler, "/chat/message");
		registry.addHandler(avalonExpedition, "/avalonExpedition/message");
		registry.addHandler(avalonCampaign, "/avalonCampaign/message");
		registry.addHandler(avalonRoleSet, "/avalonRoleSet/message");
		registry.addHandler(screenShare, "/screenShare/message");
	}

}