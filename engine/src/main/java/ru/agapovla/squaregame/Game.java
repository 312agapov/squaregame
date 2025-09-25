package ru.agapovla.squaregame;

import lombok.Getter;
import ru.agapovla.squaregame.dto.MoveResultDto;
import ru.agapovla.squaregame.enums.Color;

import java.util.*;

public class Game {

    @Getter
    private final Board board;

    private final Player[] players;
    private int cur = 0;

    @Getter
    private boolean active = true;

    private final Random rnd = new Random();

    public Game(int size, Player p1, Player p2) {
        this.board = new Board(size);
        this.players = new Player[]{p1, p2};
    }

    public Player current() {
        return players[cur];
    }

    //делаем ход, закрашиваем клеточку
    public MoveResultDto move(int x, int y) {
        // проверка корректности хода
        if (!board.isInside(x, y) || !board.isEmpty(x, y)) {
            return new MoveResultDto(false, x, y, false, null, false);
        }

        Color color = current().getColor();
        board.placeDot(x, y, color);

        // проверка, образован ли квадрат
        if (SquareChecker.hasSquare(board.getCells(), color)) {
            active = false;
            return new MoveResultDto(true, x, y, true, color, false);
        }

        // проверка на заполнение доски (ничья)
        if (board.getCells().size() == board.getSize() * board.getSize()) {
            active = false;
            return new MoveResultDto(true, x, y, true, null, true);
        }

        // переключаем игрока
        cur = 1 - cur;

        return new MoveResultDto(true, x, y, false, null, false);
    }

    //ход делает компьютер, закрашивает клеточку
    public MoveResultDto compMove() {
        List<int[]> empties = board.emptyCells();
        if (empties.isEmpty()) {
            // нет доступных ходов
            return new MoveResultDto(false, -1, -1, false, null, false);
        }

        // выбираем случайную пустую клетку
        int[] choice = empties.get(rnd.nextInt(empties.size()));

        // делаем ход через move() и возвращаем результат
        return move(choice[0], choice[1]);
    }
}


