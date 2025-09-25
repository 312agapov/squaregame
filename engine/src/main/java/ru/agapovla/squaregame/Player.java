package ru.agapovla.squaregame;

import lombok.*;
import ru.agapovla.squaregame.enums.Color;
import ru.agapovla.squaregame.enums.PlayerType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Player {

    private PlayerType type;
    private Color color;
}
