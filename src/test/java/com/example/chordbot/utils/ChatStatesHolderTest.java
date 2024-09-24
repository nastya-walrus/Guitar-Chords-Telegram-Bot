package com.example.chordbot.utils;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChatStatesHolderTest {

    @Test
    void chatIsActive_ifChatStatesWasNotInitialised_shouldReturnFalse() {
        long chatId = 12345;
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>();

        assertFalse(statesHolder.chatIsActive(chatId));
    }


    @Test
    void chatIsActive_ifChatStatesWasInitialized_shouldReturnTrue() {
        long chatId = 12345;
        Object chatState = "state";
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>();

        statesHolder.setChatState(chatId, chatState);
        assertTrue(statesHolder.chatIsActive(chatId));
    }


    @Test
    void chatIsActive_ifAnotherChatStateWasInitialized_shouldReturnFalse() {
        long chatId = 12345;
        Object chatState = "state";
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>();

        statesHolder.setChatState(chatId + 1, chatState);
        assertFalse(statesHolder.chatIsActive(chatId));
    }


    @Test
    void chatIsActive_ifChatStateCleared_shouldReturnFalse() {
        long chatId = 12345;
        Object chatState = "state";
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>();

        statesHolder.setChatState(chatId, chatState);
        statesHolder.clearChatState(chatId);

        assertFalse(statesHolder.chatIsActive(chatId));
    }


    @Test
    void clearChatState() {

        long chatId = 12345;
        Object state = "state";
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>(new HashMap<>(Map.of(chatId, state)));

        Object stateBeforeClear = statesHolder.getChatState(chatId);
        assertEquals(state, stateBeforeClear, "Test was not initialized");

        statesHolder.clearChatState(chatId);

        Object stateAfterClear = statesHolder.getChatState(chatId);
        assertNull(stateAfterClear, "Chat state was not cleared");
    }


    @Test
    void getChatState() {

        long chatId = 12345;
        Object state = "state";
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>(new HashMap<>(Map.of(chatId, state)));

        Object gotState = statesHolder.getChatState(chatId);
        assertEquals(state, gotState, "Got wrong chat state");
    }


    @Test
    void setChatState() {

        long chatId = 12345;
        Object state = "state";
        ChatStatesHolder<Object> statesHolder = new ChatStatesHolder<>();

        statesHolder.setChatState(chatId, state);
        assertEquals(state, statesHolder.getChatState(chatId), "Set wrong chat state");
    }
}