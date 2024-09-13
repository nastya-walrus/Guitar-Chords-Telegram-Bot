package com.example.chordbot.repository;

import com.example.chordbot.entity.Song;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

    @Test
    void findByName() {
        String name = "Алюминиевые огурцы";
        Song song = songRepository.save(new Song(name, "ляляля"));

        Song foundSong = songRepository.findByName(name);
        assertEquals(song, foundSong);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        String name = "Алюминиевые огурцы";
        Song song = songRepository.save(new Song(name, "бла-бла-бла"));

        List<Song> foundSongs = songRepository.findByNameContainingIgnoreCase(name, PageRequest.of(0, 10));

        // 1 var
        assertEquals(List.of(song), foundSongs);
        // 2 var
        assertThat(foundSongs).containsExactly(song);
        // 3 var
        assertEquals(1, foundSongs.size());
        assertEquals(song, foundSongs.getFirst());
    }

    @Test
    void findByNameContainingIgnoreCase_SameWordsInName() {
        String nameFirstSong = "Три счастливых дня";
        String nameSecondSong = "Три сестры";
        String nameThirdSong = "Алюминиевые огурцы";

        Song firstSong = songRepository.save(new Song(nameFirstSong, ""));
        Song secondSong = songRepository.save(new Song(nameSecondSong, ""));
        Song thirdSong = songRepository.save(new Song(nameThirdSong, ""));

        List<Song> foundSongs = songRepository.findByNameContainingIgnoreCase("три", PageRequest.of(0, 10));

        assertThat(foundSongs).containsExactlyInAnyOrder(firstSong, secondSong)
                .doesNotContain(thirdSong);
    }

    @Test
    void findByNameContainingIgnoreCase_withPaging() {
        String nameContains = "три";

        List<String> fullSongNames = List.of(
                "Три счастливых дня",
                "Три сестры",
                "Три трупа на обочине",
                "Три слова",
                "Тридцать три коровы",
                "Три окна",
                "У России три пути",
                "Три белых коня",
                "Три полоски",
                "Океан и три реки"
        );

        songRepository.saveAll(fullSongNames.stream()
                .map(t -> new Song(t, ""))
                .toList()
        );


        List<Song> foundSongsOnZeroPage = songRepository.findByNameContainingIgnoreCase(
                nameContains, PageRequest.of(0, 10)
        );

        assertEquals(10, foundSongsOnZeroPage.size());
        assertEquals(fullSongNames.get(0), foundSongsOnZeroPage.get(0).getName());
        assertEquals(fullSongNames.get(1), foundSongsOnZeroPage.get(1).getName());
        assertEquals(fullSongNames.get(2), foundSongsOnZeroPage.get(2).getName());
        assertEquals(fullSongNames.get(3), foundSongsOnZeroPage.get(3).getName());
        assertEquals(fullSongNames.get(4), foundSongsOnZeroPage.get(4).getName());
        assertEquals(fullSongNames.get(5), foundSongsOnZeroPage.get(5).getName());
        assertEquals(fullSongNames.get(6), foundSongsOnZeroPage.get(6).getName());
        assertEquals(fullSongNames.get(7), foundSongsOnZeroPage.get(7).getName());
        assertEquals(fullSongNames.get(8), foundSongsOnZeroPage.get(8).getName());
        assertEquals(fullSongNames.get(9), foundSongsOnZeroPage.get(9).getName());

    }

}