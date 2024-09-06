package com.example.chordbot.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString(of = {"id", "name"})
@NoArgsConstructor
public class Musician {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;

    @ManyToMany
    private List<Song> songs;

    public Musician(String name) {
        this.name = name;
    }
}
