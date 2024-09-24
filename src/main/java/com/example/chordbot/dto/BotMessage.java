package com.example.chordbot.dto;

import lombok.*;

import java.util.List;

/**
 * Сообщение, которое присылает бот
 */
@Builder
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class BotMessage {

    private final String text;
    private final List<Button> buttons;
    private final KeyboardType keyboardType;

    public BotMessage(String text) {
        this.text = text;
        this.buttons = List.of();
        this.keyboardType = KeyboardType.NO_KEYBOARD;
    }

    public KeyboardType getKeyboardType() {
        if (buttons.isEmpty()) return KeyboardType.NO_KEYBOARD;
        return keyboardType;
    }

    @RequiredArgsConstructor
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class Button {
        private final String text;
        private final String callBackData;
    }


    public enum KeyboardType {
        INLINE, REPLY, REMOVE, NO_KEYBOARD
    }
}
