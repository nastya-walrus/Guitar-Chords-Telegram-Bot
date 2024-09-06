package com.example.chordbot.repository;

import com.example.chordbot.entity.Musician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicianRepository extends JpaRepository < Musician, Integer> {
}
