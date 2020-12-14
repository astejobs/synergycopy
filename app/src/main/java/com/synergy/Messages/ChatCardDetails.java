package com.synergy.Messages;

public class ChatCardDetails {
    String chatTitle;
    String chatBody;
    String chatTime;

    public ChatCardDetails(String chatTitle, String chatBody, String chatTime) {
        this.chatTitle = chatTitle;
        this.chatBody = chatBody;
        this.chatTime = chatTime;
    }

    public String getChatTitle() {
        return chatTitle;
    }

    public String getChatBody() {
        return chatBody;
    }

    public String getChatTime() {
        return chatTime;
    }
}
