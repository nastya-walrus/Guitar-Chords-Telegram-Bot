package com.example.chordbot.repository;

import com.example.chordbot.entity.Song;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends PagingAndSortingRepository<Song, Integer>,
        CrudRepository<Song, Integer> {

    Song findByName(String name);

    List<Song> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
