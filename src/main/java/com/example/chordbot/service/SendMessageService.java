package com.example.chordbot.service;

import com.example.chordbot.ChordsBot;
import com.example.chordbot.entity.Song;
import com.example.chordbot.repository.MusicianRepository;
import com.example.chordbot.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.chordbot.Constants.*;
import static com.example.chordbot.UserState.AWAITING_SEARCH_REQUEST;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Log
@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final MusicianRepository musicianRepository;
    private final SongRepository songRepository;

    public void replyToStart(MessageContext ctx) {
        BaseAbilityBot bot = ctx.bot();
        long chatId = ctx.chatId();
        bot.silent().execute(
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(START_TEXT)
                        .build()
        );

        if (bot instanceof ChordsBot) {
            bot.silent().execute(
                    SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(SECOND_START_TEXT)
                            .build()
            );

            ((ChordsBot) bot).setChatState(chatId, AWAITING_SEARCH_REQUEST);
        }
    }

    public void sendMessageToSearchRequest(ChordsBot abilityBot, Update upd) {

        Long chatId = getChatId(upd);
        Message message = upd.getMessage();

        log.info("triggered replyToSong with " + message);

        List<Song> songs = songRepository.findByNameContainingIgnoreCase(
                message.getText(),
                Pageable.unpaged()
        );

        List<List<InlineKeyboardButton>> buttons = songs.stream()
                .map(it -> InlineKeyboardButton.builder()
                        .text(it.getName())
                        .callbackData("%s %s".formatted(GET_SONG_COMMAND, it.getId()))
                        .build())
                .map(List::of)
                .toList();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);

        if (songs.isEmpty()) {
            abilityBot.silent().execute(
                    SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(NOT_FOUND_SONG_TEXT)
                            .replyMarkup(keyboardMarkup)
                            .build()
            );
        } else {
            abilityBot.silent().execute(
                    SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("Я нашёл")
                            .replyMarkup(keyboardMarkup)
                            .build()
            );

            abilityBot.clearChatState(chatId);
        }
    }

    public void sendMessageWithSongById(BaseAbilityBot abilityBot, Update upd) {

        Long chatId = getChatId(upd);
        String data = upd.getCallbackQuery().getData();

        Integer songId = Integer.valueOf(data.substring(GET_SONG_COMMAND.length() + 1));
        Optional<Song> song = songRepository.findById(songId);

        if (song.isPresent()) {
            abilityBot.silent().execute(
                    SendMessage
                            .builder()
                            .chatId(chatId)
                            .text(song.get().getText())
                            .build()
            );
        } else {
            UUID uuid = UUID.randomUUID();
            abilityBot.silent().execute(
                    SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("Ой-ёй. Ошибочка, номер инцидента = %s. Попробуйте запросить песенки снова.".formatted(
                                    uuid))
                            .build()
            );
            log.warning("Not found song by %s, incident id = %s".formatted(data, uuid));
        }

    }
}
