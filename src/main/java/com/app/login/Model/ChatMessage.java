package com.app.login.Model;


public class ChatMessage {
	
	private Long id;
	private String sender;
	private String content;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "ChatMessage [id=" + id + ", sender=" + sender + ", content=" + content + "]";
	}
	

}
