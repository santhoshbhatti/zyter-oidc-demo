package com.infinite.zyteroidcdemo.dto;

import lombok.Data;

@Data
public class WebsocketChatMessage {
	private String type;
	private String sender;
	private String content;
}
