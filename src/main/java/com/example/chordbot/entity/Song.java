package com.example.chordbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString(of = {"id", "name", "text"})
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String text;

    public Song(String name, String text) {
        this.name = name;
        this.text = text;
    }
}
