package com.example.chordbot.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.abilitybots.api.bot.BaseAbilityBot;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.sender.SilentSender;

@SpringBootTest
class TelegramSendMessageServiceTest {

    @Autowired
    private TelegramSendMessageService service;

    @Test
    void replyToStart_ifNotChordsBot() {
        long chatId = 123456;

        BaseAbilityBot mockedBot = Mockito.mock();
        SilentSender mockedSilentSender = Mockito.mock();
        Mockito.when(mockedBot.silent()).thenReturn(mockedSilentSender);

        MessageContext messageContext = MessageContext.newContext(
                Mockito.mock(),
                Mockito.mock(),
                chatId,
                mockedBot
        );

//        service.replyToStart(messageContext);

        Mockito.verify(mockedSilentSender).execute(Mockito.any());
    }
}