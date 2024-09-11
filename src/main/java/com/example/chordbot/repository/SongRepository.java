package com.example.chordbot.repository;

import com.example.chordbot.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Integer> {

    Song findByName(String name);

    List<Song> findByNameContainingIgnoreCase(String name);

}
