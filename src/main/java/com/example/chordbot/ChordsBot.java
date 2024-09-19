package com.example.chordbot;

import com.example.chordbot.service.SendMessageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static com.example.chordbot.Constants.SECOND_START_TEXT;
import static com.example.chordbot.Constants.START_TEXT;
import static com.example.chordbot.UserState.AWAITING_SEARCH_REQUEST;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Log
@Component
public class ChordsBot extends AbilityBot {

    private final Map<Long, UserState> chatStates = new HashMap<>();
    private final SendMessageService sendMessageService;

    @Autowired
    public ChordsBot(Environment env, SendMessageService sendMessageService) {
        super(env.getProperty("botToken"), "chords_for_songs_bot");
//        chatStates = db.getMap(Constants.CHAT_STATES);
        this.sendMessageService = sendMessageService;
    }

    @Override
    public long creatorId() {
        return 1L;
    }

    public Ability startBot() {
        return Ability
                .builder()
                .name("start")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> replyToStart(ctx.chatId()))
                .build();
    }

    public Ability abilityHui() {
        return Ability
                .builder()
                .name("hui")
                .info(Constants.START_DESCRIPTION)
                .locality(USER)
                .privacy(PUBLIC)
                .action(ctx -> {
                    SendMessage message = new SendMessage();
                    message.setChatId(ctx.chatId());
                    message.setText("Сам нахуй пошёл!");
                    silent.execute(message);
                })
                .build();
    }


    public void replyToStart(long chatId) {
        silent.execute(
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(START_TEXT)
                        .build()
        );
        silent.execute(
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(SECOND_START_TEXT)
                        .build()
        );

        chatStates.put(chatId, AWAITING_SEARCH_REQUEST);
    }


    public Reply replyToSearchRequest() {
        return Reply.of(
                (abilityBot, upd1) -> sendMessageService.sendMessageToSearchRequest((ChordsBot) abilityBot, upd1),

                Flag.TEXT,
                upd -> chatIsActive(getChatId(upd)),
                upd -> chatStates.get(getChatId(upd)).equals(AWAITING_SEARCH_REQUEST)
        );
    }

    public Reply replyGetSong() {
        return Reply.of(
                sendMessageService::sendMessageWithSongById,
                Flag.CALLBACK_QUERY,
                this::isGetSong);
    }

    public void clearChatState(Long chatId) {
        chatStates.remove(chatId);
    }

    private boolean isGetSong(Update upd) {
        return upd.getCallbackQuery().getData().startsWith(Constants.GET_SONG_COMMAND);
    }


    public boolean chatIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}