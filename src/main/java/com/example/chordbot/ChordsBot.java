package com.example.chordbot;

import com.example.chordbot.service.SendMessageService;
import com.example.chordbot.utils.ChatStatesHolder;
import lombok.Getter;
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

import static com.example.chordbot.UserState.AWAITING_SEARCH_REQUEST;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Log
@Component
public class ChordsBot extends AbilityBot {

    @Getter
    private final ChatStatesHolder<UserState> chatStates;
    private final SendMessageService sendMessageService;

    @Autowired
    public ChordsBot(Environment env, SendMessageService sendMessageService) {
        super(env.getProperty("botToken"), "chords_for_songs_bot");
        chatStates = new ChatStatesHolder<>(db.getMap(Constants.CHAT_STATES));
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


    private boolean isGetSong(Update upd) {
        return upd.getCallbackQuery().getData().startsWith(Constants.GET_SONG_COMMAND);
    }


    public boolean readyToSearch(Update upd) {
        Long chatId = getChatId(upd);
        return !chatStates.chatIsActive(chatId) || chatStates.getChatState(chatId) == AWAITING_SEARCH_REQUEST;
    }
}