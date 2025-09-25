package ru.agapovla.squaregame;

import lombok.Getter;
import ru.agapovla.squaregame.enums.Color;

import java.util.*;

@Getter
public class Board {
    private final int size; //длина стороны таблицы(квадрата)
    private final Map<String, Color> cells = new HashMap<>();

    public Board(int size) {
        this.size = size;
    }

    //проверка, находится ли точка в пределах игровой доски
    public boolean isInside(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    //проверка, содержит ли клетка точку (если содержит, то вернет false - не пустая, если нет, вернет true - пустая
    public boolean isEmpty(int x, int y) {
        return !cells.containsKey(key(x, y));
    }

    //поставить точку на клетке
    public void placeDot(int x, int y, Color color) {
        cells.put(key(x, y), color);
    }

    //получить цвет точки по ее координатам
    public Color getDotColor(int x, int y) {
        return cells.get(key(x, y));
    }

    //возвращает лист массивов по 2 элемента, с координатами пустых точек на доске
    public List<int[]> emptyCells() {
        List<int[]> result = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (isEmpty(x, y)) {
                    result.add(new int[]{x, y});
                }
            }
        }
        return result;
    }

    // возвращает координаты в строчном виде
    private String key(int x, int y) {
        return x + "," + y;
    }

    //печатаем таблицу
    public void printBoard() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                Color c = getDotColor(x, y);
                if (c == null) {
                    System.out.print(". "); // пустая клетка
                } else if (c == Color.WHITE) {
                    System.out.print("W ");
                } else {
                    System.out.print("B ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}

