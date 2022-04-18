package com.jacob.database.game_data;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class PlayedMove {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private PastGame pastGame;

    private int startIndex;
    private int endIndex;
}
