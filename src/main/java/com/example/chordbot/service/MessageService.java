package com.example.chordbot.service;

import com.example.chordbot.UserState;
import com.example.chordbot.dto.BotMessage;
import com.example.chordbot.entity.Song;
import com.example.chordbot.repository.MusicianRepository;
import com.example.chordbot.repository.SongRepository;
import com.example.chordbot.utils.ChatStatesHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.chordbot.Constants.*;
import static com.example.chordbot.UserState.AWAITING_SEARCH_REQUEST;
import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;


@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MusicianRepository musicianRepository;
    private final SongRepository songRepository;
    private final ChatStatesHolder<UserState> chatStates = new ChatStatesHolder<>();


    public List<BotMessage> startMessages(Long chatId) {
        chatStates.setChatState(chatId, AWAITING_SEARCH_REQUEST);
        return List.of(new BotMessage(START_TEXT), new BotMessage(SECOND_START_TEXT));
    }


    public boolean readyToSearch(Update upd) {
        Long chatId = getChatId(upd);
        return !chatStates.chatIsActive(chatId) || chatStates.getChatState(chatId) == AWAITING_SEARCH_REQUEST;
    }


    public List<BotMessage> searchSongMessages(Long chatId, String searchRequest) {
        List<Song> songs = songRepository.findByNameContainingIgnoreCase(
                searchRequest,
                Pageable.unpaged()
        );

        BotMessage botMessage;
        if (songs.isEmpty()) {
            botMessage = new BotMessage(NOT_FOUND_SONG_TEXT);
        } else {
            List<BotMessage.Button> buttonList = songs.stream()
                    .map(it -> new BotMessage.Button(it.getName(), "%s %s".formatted(GET_SONG_COMMAND, it.getId())))
                    .toList();

            botMessage = BotMessage
                    .builder()
                    .keyboardType(BotMessage.KeyboardType.INLINE)
                    .buttons(buttonList)
                    .text("Я нашёл")
                    .build();

            chatStates.clearChatState(chatId);
        }

        return List.of(botMessage);
    }

    public List<BotMessage> searchSongByIdMessages(Long chatId, String data) {
        Integer songId = Integer.valueOf(data.substring(GET_SONG_COMMAND.length() + 1));
        Optional<Song> song = songRepository.findById(songId);

        BotMessage botMessage;
        if (song.isPresent()) {
            botMessage = new BotMessage(song.get().getText());
        } else {
            UUID uuid = UUID.randomUUID();
            botMessage = new BotMessage(SONG_BY_ID_NOT_FOUND.formatted(uuid));
            log.warn("Not found song by {}, incident id = {}", data, uuid);
        }

        return List.of(botMessage);
    }

}
