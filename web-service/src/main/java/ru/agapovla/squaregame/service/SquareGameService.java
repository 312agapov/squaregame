package ru.agapovla.squaregame.service;

import org.springframework.stereotype.Service;
import ru.agapovla.squaregame.Board;
import ru.agapovla.squaregame.Game;
import ru.agapovla.squaregame.Player;
import ru.agapovla.squaregame.dto.BoardDto;
import ru.agapovla.squaregame.dto.MoveResultDto;
import ru.agapovla.squaregame.dto.SimpleMoveDto;
import ru.agapovla.squaregame.enums.Color;
import ru.agapovla.squaregame.enums.PlayerType;

@Service
public class SquareGameService {

    private Game game;

    public SimpleMoveDto nextMove(BoardDto boardDto) {
        Board board = new Board(boardDto.getSize());

        String cleanData = boardDto.getData().replaceAll("[\\n\\r]", "");
        int size = boardDto.getSize();
        char[] chars = cleanData.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            int x = i % size; // столбец
            int y = i / size; // строка
            char c = chars[i];
            if (c == 'w' || c == 'W') {
                board.placeDot(x, y, Color.WHITE);
            } else if (c == 'b' || c == 'B') {
                board.placeDot(x, y, Color.BLACK);
            }
        }

        Color nextColor;
        if ("w".equalsIgnoreCase(boardDto.getNextPlayerColor())) {
            nextColor = Color.WHITE;
        } else {
            nextColor = Color.BLACK;
        }

        Player nextPlayer = new Player(PlayerType.COMPUTER, nextColor); //будет ходить сейчас, реальный игрок

        // создаём фиктивного второго игрока другого цвета
        Color otherColor;
        if (nextColor == Color.WHITE) {
            otherColor = Color.BLACK;
        } else {
            otherColor = Color.WHITE;
        }
        Player dummy = new Player(PlayerType.COMPUTER, otherColor); // используем для работы engine

        Game tempGame = new Game(board.getSize(), nextPlayer, dummy);
        tempGame.getBoard().getCells().putAll(board.getCells());

        MoveResultDto moveResult = tempGame.compMove();

        // color = цвет игрока, который сделал ход
        return new SimpleMoveDto(
                moveResult.getX(),
                moveResult.getY(),
                nextColor.name().toLowerCase()
        );
    }


    public String startGame(int size, String p1, String p2){
        try {
            String[] parts1 = p1.trim().split("\\s+");
            String[] parts2 = p2.trim().split("\\s+");

            if (parts1.length != 2 || parts2.length != 2) {
                return "Incorrect command";
            }

            PlayerType type1;
            if (parts1[0].equalsIgnoreCase("user")) {
                type1 = PlayerType.USER;
            } else if (parts1[0].equalsIgnoreCase("comp") || parts1[0].equalsIgnoreCase("computer")) {
                type1 = PlayerType.COMPUTER;
            } else {
                return "Incorrect command";
            }

            Color color1;
            if (parts1[1].equalsIgnoreCase("W") || parts1[1].equalsIgnoreCase("WHITE")) {
                color1 = Color.WHITE;
            } else if (parts1[1].equalsIgnoreCase("B") || parts1[1].equalsIgnoreCase("BLACK")) {
                color1 = Color.BLACK;
            } else {
                return "Incorrect command";
            }

            PlayerType type2;
            if (parts2[0].equalsIgnoreCase("user")) {
                type2 = PlayerType.USER;
            } else if (parts2[0].equalsIgnoreCase("comp") || parts2[0].equalsIgnoreCase("computer")) {
                type2 = PlayerType.COMPUTER;
            } else {
                return "Incorrect command";
            }

            Color color2;
            if (parts2[1].equalsIgnoreCase("W") || parts2[1].equalsIgnoreCase("WHITE")) {
                color2 = Color.WHITE;
            } else if (parts2[1].equalsIgnoreCase("B") || parts2[1].equalsIgnoreCase("BLACK")) {
                color2 = Color.BLACK;
            } else {
                return "Incorrect command";
            }

            if (color1 == color2) {
                return "Players cannot have the same color";
            }

            Player player1 = new Player(type1, color1);
            Player player2 = new Player(type2, color2);

            this.game = new Game(size, player1, player2);
            return "New game started";
        } catch (Exception e) {
            return "Incorrect command";
        }
    }

    public SimpleMoveDto move(int x, int y) {
        if (game == null || !game.isActive()) {
            return null;
        }

        if (game.current().getType() != PlayerType.USER) {
            return null;
        }

        // цвет игрока, который делает ход
        Color moveColor = game.current().getColor();

        MoveResultDto moveResultDto = game.move(x, y);

        while (game.isActive() && game.current().getType() == PlayerType.COMPUTER) {
            moveColor = game.current().getColor(); // для компа цвет будет другой
            moveResultDto = game.compMove();
        }

        return new SimpleMoveDto(
                moveResultDto.getX(),
                moveResultDto.getY(),
                moveColor.name().toLowerCase()
        );
    }

    public String help(){
        return "Commands:\n" +
                "  GAME N, U1, U2   - start new game (N>2)\n" +
                "  MOVE X, Y        - make a move\n" +
                "  HELP             - show help\n" +
                "  EXIT             - exit";
    }

    public void exit(){
        this.game = null;
    }
}
