package com.example.chordbot.service;

import com.example.chordbot.ChordsBot;
import com.example.chordbot.Constants;
import com.example.chordbot.entity.Song;
import com.example.chordbot.repository.MusicianRepository;
import com.example.chordbot.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Optional;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Log
@Service
@RequiredArgsConstructor
public class SendMessageService {

    private final MusicianRepository musicianRepository;
    private final SongRepository songRepository;


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
                        .callbackData("/getSong " + it.getId())
                        .build())
                .map(List::of)
                .toList();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttons);

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

    public void sendMessageWithSongById(BaseAbilityBot abilityBot, Update upd) {

        Long chatId = upd.getCallbackQuery().getMessage().getChatId();
        String songId = upd.getCallbackQuery().getData().substring(Constants.GET_SONG_COMMAND.length() + 1);

        Integer songIdInteger = Integer.valueOf(songId);
        Optional<Song> song = songRepository.findById(songIdInteger);
        abilityBot.silent().execute(
                SendMessage
                        .builder()
                        .chatId(chatId)
                        .text(song.get().getText())
                        .build()
        );
    }
}
