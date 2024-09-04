package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Map;

import static com.example.demo.Constants.START_TEXT;
import static com.example.demo.UserState.AWAITING_SONG;

public class ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    public ResponseHandler(SilentSender sender, DBContext db) {
        this.sender = sender;
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, AWAITING_SONG);
    }

    public void replyToMenu(Long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case AWAITING_SONG -> replyToSong(chatId, message);
            case MUSICIAN_SELECTION -> replyToMusicianSelection(chatId, message);
            case SONG_SELECTION -> replyToSongSelection(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }

    private void stopChat(Long chatId) {
        logger.info("triggered stopChat");

    }

    private void replyToSongSelection(Long chatId, Message message) {
        logger.info("triggered replyToSongSelection");
    }

    private void replyToMusicianSelection(Long chatId, Message message) {
        logger.info("triggered replyToMusicianSelection");

    }

    private void replyToSong(Long chatId, Message message) {
        logger.info("triggered replyToSong");

    }

    private void unexpectedMessage(Long chatId) {
        logger.info("triggered unexpectedMessage");

    }

    public boolean userIsActive(Long chatId) {
        return false;
    }
}