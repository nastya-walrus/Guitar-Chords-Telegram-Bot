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

import java.util.Map;

import static com.example.chordbot.UserState.AWAITING_SEARCH_REQUEST;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Log
@Component
public class ChordsBot extends AbilityBot {

    /**
     * Хранит текущие статусы активных чатов
     * <p> <p>
     * Ключ - chatId, идентификатор чата
     * Значение - статус чата из enum UserState
     */
    private final Map<Long, UserState> chatStates;
    private final SendMessageService sendMessageService;

    @Autowired
    public ChordsBot(Environment env, SendMessageService sendMessageService) {
        super(env.getProperty("botToken"), "chords_for_songs_bot");
        chatStates = db.getMap(Constants.CHAT_STATES);
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
                .locality(USER)
                .privacy(PUBLIC)
                .action(sendMessageService::replyToStart)
                .build();
    }

    public Ability abilityHui() {
        return Ability
                .builder()
                .name("hui")
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


    public void setChatState(Long chatId, UserState userState) {
        chatStates.put(chatId, userState);
    }


    public Reply replyToSearchRequest() {
        return Reply.of(
                (abilityBot, upd1) -> sendMessageService.sendMessageToSearchRequest((ChordsBot) abilityBot, upd1),

                Flag.TEXT,
                update -> !update.getMessage().getText().startsWith("/"),
                this::readyToSearch
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

    public boolean readyToSearch(Update upd) {
        Long chatId = getChatId(upd);
        return !chatIsActive(chatId) || chatStates.get(chatId) == AWAITING_SEARCH_REQUEST;
    }
}