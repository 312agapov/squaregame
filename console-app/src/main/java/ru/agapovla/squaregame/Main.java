package ru.agapovla.squaregame;

import ru.agapovla.squaregame.dto.MoveResultDto;
import ru.agapovla.squaregame.enums.Color;
import ru.agapovla.squaregame.enums.PlayerType;

import java.util.*;

public class Main {
    private static Game game;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (!sc.hasNextLine()) {
                break;
            }
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] tokens = line.split("\\s+");
            String cmd = tokens[0].toUpperCase();

            if (cmd.equals("EXIT")) {
                return;
            } else if (cmd.equals("HELP")) {
                showHelp();
            } else if (cmd.equals("GAME")) {
                startGame(line);
            } else if (cmd.equals("MOVE")) {
                makeMove(line);
            } else {
                System.out.println("Incorrect command");
            }
        }
    }

    //подсказки по командам
    private static void showHelp() {
        System.out.println("Commands:");
        System.out.println("  GAME N, U1, U2   - start new game (N>2)");
        System.out.println("  MOVE X, Y        - make a move");
        System.out.println("  HELP             - show help");
        System.out.println("  EXIT             - exit");
    }

    //запуск игры
    private static void startGame(String line) {
        try {
            String rest = line.substring(4).trim();
            String[] parts = rest.split(",");
            if (parts.length != 3) {
                throw new Exception();
            }

            int n = Integer.parseInt(parts[0].trim());
            if (n <= 2) {
                throw new Exception();
            }

            Player p1 = parsePlayer(parts[1].trim());
            Player p2 = parsePlayer(parts[2].trim());
            if (p1.getColor() == p2.getColor()) {
                throw new Exception();
            }

            game = new Game(n, p1, p2);
            System.out.println("New game started");

            while (game.isActive() && game.current().getType() == PlayerType.COMPUTER) {
                game.compMove();
            }
        } catch (Exception e) {
            System.out.println("Incorrect command");
            game = null;
        }
    }

    //парсим тип игрока и его цвет
    private static Player parsePlayer(String s) {
        String[] tok = s.split("\\s+");
        if (tok.length != 2) {
            throw new RuntimeException();
        }

        PlayerType type;
        if (tok[0].equalsIgnoreCase("user")) {
            type = PlayerType.USER;
        } else if (tok[0].equalsIgnoreCase("comp") || tok[0].equalsIgnoreCase("computer")) {
            type = PlayerType.COMPUTER;
        } else {
            throw new RuntimeException();
        }

        Color color;
        if (tok[1].equalsIgnoreCase("W") || tok[1].equalsIgnoreCase("WHITE")) {
            color = Color.WHITE;
        } else if (tok[1].equalsIgnoreCase("B") || tok[1].equalsIgnoreCase("BLACK")) {
            color = Color.BLACK;
        } else {
            throw new RuntimeException();
        }

        return new Player(type, color);
    }

    //игрок или компьютер делает ход
    private static void makeMove(String line) {
        if (game == null || !game.isActive()) {
            System.out.println("Game not started!");
            return;
        }

        if (game.current().getType() != PlayerType.USER) {
            System.out.println("Incorrect command");
            return;
        }

        try {
            String[] parts = line.substring(4).trim().split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());

            // ход пользователя
            MoveResultDto result = game.move(x, y);
            printMoveResult(result);

            // ходы компьютера
            while (game.isActive() && game.current().getType() == PlayerType.COMPUTER) {
                result = game.compMove();
                printMoveResult(result);
            }

        } catch (Exception e) {
            System.out.println("Incorrect command");
        }
    }

    //вывод в консоль из ДТО
    private static void printMoveResult(MoveResultDto result) {
        // печатаем доску
        game.getBoard().printBoard();

        if (!result.isValid()) {
            System.out.println("Incorrect command");
            return;
        }

        if (result.isGameOver()) {
            if (result.isDraw()) {
                System.out.println("Game finished. Draw");
            } else {
                Color winner = result.getWinner();
                if (winner == Color.WHITE) {
                    System.out.println("Game finished. W wins!");
                } else {
                    System.out.println("Game finished. B wins!");
                }
            }
        } else {
            // ход ещё не завершил игру, можно показать цвет сделанного хода
            String colorStr;
            if (game.current().getColor() == Color.WHITE) {
                colorStr = "B"; // следующий игрок
            } else {
                colorStr = "W";
            }
            System.out.println("Next turn: " + colorStr);
        }
    }
}
