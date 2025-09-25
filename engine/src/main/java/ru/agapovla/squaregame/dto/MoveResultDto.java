package ru.agapovla.squaregame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.agapovla.squaregame.enums.Color;

@Getter
@Setter
@AllArgsConstructor
public class MoveResultDto {
    private final boolean valid;     // был ли ход корректным
    private final int x;             // координата x хода
    private final int y;             // координата y хода
    private final boolean gameOver;  // закончилась ли игра
    private final Color winner;      // кто выиграл (null, если нет победителя)
    private final boolean draw;      // ничья
}
