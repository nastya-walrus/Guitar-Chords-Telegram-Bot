package com.example.chordbot.repository;

import com.example.chordbot.entity.Musician;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MusicianRepositoryTest {

    @Autowired
    private MusicianRepository musicianRepository;

    @Test
    void findByName() {
        String name = "Кино";
        Musician musician = musicianRepository.save(new Musician(name));

        Musician foundMusician = musicianRepository.findByName(name);
        assertEquals(musician, foundMusician);
    }

    @Test
    void findByNameContainingIgnoreCase() {
        String name = "Найк Борзов";
        Musician musician = musicianRepository.save(new Musician(name));

        List<Musician> foundMusicians = musicianRepository.findByNameContainingIgnoreCase(name);

        assertEquals(List.of(musician), foundMusicians);
        assertThat(foundMusicians).containsExactly(musician);
    }
}