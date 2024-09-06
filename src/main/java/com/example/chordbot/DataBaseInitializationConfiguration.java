package com.example.chordbot;

import com.example.chordbot.entity.Musician;
import com.example.chordbot.entity.Song;
import com.example.chordbot.repository.MusicianRepository;
import com.example.chordbot.repository.SongRepository;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Log
@Configuration
public class DataBaseInitializationConfiguration {

    public static final String testText = """
            Куплет 1:
            Здравствуйте, девочки!
            Здравствуйте, мальчики!
            Смотрите на меня в окно
            И мне кидайте свои пальчики, да-а
            
            Припев:
            Ведь я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            Я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            
            Куплет 2:
            Три чукотских мудреца
            Твердят-твердят мне без конца
            Металл не принесет плода
            Игра не стоит свеч, а результат — труда
            
            Припев:
            Но я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            Я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            
            Куплет 3:
            Злое белое колено
            Пытается меня достать
            Колом колено колют вены
            В надежде тайну разгадать — зачем, я-я
            
            Припев:
            Сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            Я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            
            Куплет 4:
            Кнопки, скрепки, клёпки
            Дырки, булки, вилки
            Здесь тракторы пройдут мои
            И упадут в копилку, упадут туда
            
            Припев:
            Где я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            Я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            Я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            Я сажаю алюминиевые огурцы, а-а
            На брезентовом поле
            """;

    @Bean
    CommandLineRunner initSongDatabase(SongRepository songRepository) {
        return args -> {
            List<Song> song = List.of(
                    new Song("Алюминиевые огурцы", testText),
                    new Song("Восьмиклассница", testText),
                    new Song("Пачка сигарет", testText)
            );
        };
    }

    @Bean
    CommandLineRunner initMusicianDatabase(MusicianRepository musicianRepository) {
        return args -> {
            List<Musician> musician = List.of(
                    new Musician("Кино"),
                    new Musician("Найк Борзов")
            );
        };
    }

}
