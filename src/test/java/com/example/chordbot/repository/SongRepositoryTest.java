package com.example.chordbot.repository;

import com.example.chordbot.entity.Song;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

        List<Song> foundSongs = songRepository.findByNameContainingIgnoreCase(name);

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

        List<Song> foundSongs = songRepository.findByNameContainingIgnoreCase("три");

        assertThat(foundSongs).containsExactlyInAnyOrder(firstSong, secondSong)
                .doesNotContain(thirdSong);
    }

}