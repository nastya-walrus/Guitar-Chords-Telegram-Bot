package com.example.chordbot.service;

import com.example.chordbot.dto.BotMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.Producer;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static org.telegram.abilitybots.api.util.AbilityUtils.getChatId;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramSendMessageService {


    public void sendAllMessages(
            BaseAbilityBot abilityBot,
            Update upd,
            Producer<List<BotMessage>> messagesProducer
    ) {
        messagesProducer.call().forEach(it -> sendMessage(abilityBot, getChatId(upd), it));
    }


    private void sendMessage(BaseAbilityBot bot, long chatId, BotMessage botMessage) {
        log.info("Requested to send {}, to chatId = {}", botMessage, chatId);

        ReplyKeyboard keyboard = switch (botMessage.getKeyboardType()) {
            case INLINE -> {
                List<List<InlineKeyboardButton>> buttons = botMessage.getButtons().stream()
                        .map(it -> InlineKeyboardButton.builder()
                                .text(it.getText())
                                .callbackData(it.getCallBackData())
                                .build())
                        .map(List::of)
                        .toList();

                yield new InlineKeyboardMarkup(buttons);
            }
            case REPLY -> {
                throw new UnsupportedOperationException("reply keyboard not supported yet");
            }
            case REMOVE -> {
                throw new UnsupportedOperationException("remove keyboard not supported yet");
            }
            case NO_KEYBOARD -> {
                yield null;
            }
        };

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .replyMarkup(keyboard)
                .text(botMessage.getText())
                .build();

        log.info("Sending {}", sendMessage);
        bot.silent().execute(sendMessage);
    }
}
