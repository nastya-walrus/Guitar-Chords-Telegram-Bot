package com.example.chordbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class PetProjectApplication {

	public static void main(String[] args) throws TelegramApiException {

		ConfigurableApplicationContext ctx = SpringApplication.run(PetProjectApplication.class, args);
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(ctx.getBean("chordsBot", LongPollingBot.class));
	}

}
