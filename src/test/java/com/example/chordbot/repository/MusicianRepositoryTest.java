package com.example.chordbot.repository;

import com.example.chordbot.entity.Musician;
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

        List<Musician> foundMusicians = musicianRepository.findByNameContainingIgnoreCase(
                name, PageRequest.of(0, 2)
        );

        assertEquals(List.of(musician), foundMusicians);
        assertThat(foundMusicians).containsExactly(musician);
    }

    @Test
    void findByNameContainingIgnoreCase_withPaging() {
        String name = "Найк";

        List<String> secondNames = List.of(
                "Борзов",
                "Моржов",
                "Коржов",
                "Киркоров",
                "Венцеславский",
                "Любимов",
                "Никитин",
                "Игнатов",
                "Ибрагимов",
                "Козлов"
        );

        List<String> fullNames = secondNames.stream()
                .map(it -> name + " " + it)
                .toList();

        musicianRepository.saveAll(fullNames.stream()
                .map(Musician::new)
                .toList()
        );

        List<Musician> foundMusiciansOnZeroPage = musicianRepository.findByNameContainingIgnoreCase(
                name, PageRequest.of(0, 2)
        );

        assertEquals(2, foundMusiciansOnZeroPage.size());
        assertEquals(fullNames.get(0), foundMusiciansOnZeroPage.get(0).getName());
        assertEquals(fullNames.get(1), foundMusiciansOnZeroPage.get(1).getName());


        List<Musician> foundMusiciansOnSecondPage = musicianRepository.findByNameContainingIgnoreCase(
                name, PageRequest.of(1, 3)
        );

        assertEquals(3, foundMusiciansOnSecondPage.size());
        assertEquals(fullNames.get(3), foundMusiciansOnSecondPage.get(0).getName());
        assertEquals(fullNames.get(4), foundMusiciansOnSecondPage.get(1).getName());
        assertEquals(fullNames.get(5), foundMusiciansOnSecondPage.get(2).getName());


        List<Musician> foundMusiciansOnThirdPage = musicianRepository.findByNameContainingIgnoreCase(
                name, PageRequest.of(2, 5)
        );

        assertEquals(0, foundMusiciansOnThirdPage.size());


        List<Musician> foundMusiciansOnThirdPageSizeFour = musicianRepository.findByNameContainingIgnoreCase(
                name, PageRequest.of(2, 4)
        );

        assertEquals(2, foundMusiciansOnThirdPageSizeFour.size());
        assertEquals(fullNames.get(8), foundMusiciansOnThirdPageSizeFour.get(0).getName());
        assertEquals(fullNames.get(9), foundMusiciansOnThirdPageSizeFour.get(1).getName());

    }


}