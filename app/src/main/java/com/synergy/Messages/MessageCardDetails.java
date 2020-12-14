package com.synergy.Messages;

public class MessageCardDetails {
    String messageTitle;
    String messageText;
    String messageTime;
    String messageType;

    public MessageCardDetails(String messageTitle, String messageText, String messageTime, String messageType) {
        this.messageTitle = messageTitle;
        this.messageText = messageText;
        this.messageTime = messageTime;
        this.messageType = messageType;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public String getMessageType() {
        return messageType;
    }
}
