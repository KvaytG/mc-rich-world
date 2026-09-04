package ru.kvaytg.richauth;

public class PendingLink {

    final long chatId;
    final String code;
    final long createdAt;

    PendingLink(long chatId, String code, long createdAt) {
        this.chatId = chatId;
        this.code = code;
        this.createdAt = createdAt;
    }

}