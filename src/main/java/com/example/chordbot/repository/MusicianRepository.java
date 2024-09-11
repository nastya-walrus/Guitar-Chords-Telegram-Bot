package com.example.chordbot.repository;

import com.example.chordbot.entity.Musician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicianRepository extends JpaRepository < Musician, Integer> {

    Musician findByName(String name);

    List<Musician> findByNameContainingIgnoreCase(String name);
}
