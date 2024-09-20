package com.example.chordbot.utils;

import java.util.HashMap;
import java.util.Map;

public class ChatStatesHolder<V> {

    /**
     * Хранит текущие статусы активных чатов
     * <p> <p>
     * Ключ - chatId, идентификатор чата
     * Значение - статус чата из enum UserState
     */
    private final Map<Long, V> chatStates;

    public ChatStatesHolder() {
        this.chatStates = new HashMap<>();
    }

    public ChatStatesHolder(Map<Long, V> map) {
        this.chatStates = map;
    }

    public boolean chatIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }

    public void clearChatState(Long chatId) {
        chatStates.remove(chatId);
    }

    public V getChatState(Long chatId) {
        return chatStates.get(chatId);
    }

    public void setChatState(Long chatId, V state) {
        chatStates.put(chatId, state);
    }

}
