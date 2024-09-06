package com.example.chordbot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @Column(length = 3500)
    private String text;

    public Song(String name, String text) {
        this.name = name;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", text='" + text.substring(0, 50).replaceAll("\n", " ") + '\'' +
                '}';
    }
}
