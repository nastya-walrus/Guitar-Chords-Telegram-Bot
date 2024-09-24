package com.example.chordbot;

import com.example.chordbot.service.MessageService;
import com.example.chordbot.service.TelegramSendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.Flag;
import org.telegram.abilitybots.api.objects.Reply;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.PUBLIC;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Slf4j
@Component
public class ChordsBot extends AbilityBot {

    private final TelegramSendMessageService telegramSendMessageService;
    private final MessageService messageService;

    @Autowired
    public ChordsBot(Environment env, TelegramSendMessageService telegramSendMessageService,
                     MessageService messageService) {
        super(env.getProperty("botToken"), "chords_for_songs_bot");
        this.messageService = messageService;
        this.telegramSendMessageService = telegramSendMessageService;
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
                .action(ctx -> telegramSendMessageService.sendAllMessages(
                        ctx.bot(),
                        ctx.update(),
                        () -> messageService.startMessages(ctx.chatId()))
                )
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
                (abilityBot, upd) -> telegramSendMessageService.sendAllMessages(
                        abilityBot,
                        upd,
                        () -> messageService.searchSongMessages(getChatId(upd), upd.getMessage().getText())),

                Flag.TEXT,
                update -> !update.getMessage().getText().startsWith("/"),
                messageService::readyToSearch
        );
    }

    public Reply replyGetSong() {
        return Reply.of(
                (abilityBot, upd) -> telegramSendMessageService.sendAllMessages(
                        abilityBot,
                        upd,
                        () -> messageService.searchSongByIdMessages(getChatId(upd), upd.getCallbackQuery().getData())),

                Flag.CALLBACK_QUERY,
                this::isGetSong);
    }


    private boolean isGetSong(Update upd) {
        return upd.getCallbackQuery().getData().startsWith(Constants.GET_SONG_COMMAND);
    }
}