package com.example.chordbot.repository;

import com.example.chordbot.entity.Musician;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicianRepository extends PagingAndSortingRepository<Musician, Integer>,
        CrudRepository<Musician, Integer> {

    Musician findByName(String name);

    List<Musician> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
