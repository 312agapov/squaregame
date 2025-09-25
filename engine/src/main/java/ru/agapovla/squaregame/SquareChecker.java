package ru.agapovla.squaregame;

import ru.agapovla.squaregame.enums.Color;

import java.util.*;

public class SquareChecker {
    public static boolean hasSquare(Map<String, Color> board, Color color) {
        Set<String> pts = new HashSet<>();
        for (Map.Entry<String, Color> entry : board.entrySet()) {
            if (entry.getValue() == color) {
                pts.add(entry.getKey());
            }
        }
        if (pts.size() < 4) {
            return false;
        }

        List<int[]> list = new ArrayList<>();
        for (String s : pts) {
            String[] xy = s.split(",");
            int[] point = new int[2];
            point[0] = Integer.parseInt(xy[0]);
            point[1] = Integer.parseInt(xy[1]);
            list.add(point);
        }

        int L = list.size();
        for (int i = 0; i < L; i++) {
            int[] p = list.get(i);
            for (int j = i + 1; j < L; j++) {
                int[] q = list.get(j);
                int dx = q[0] - p[0];
                int dy = q[1] - p[1];
                if (dx == 0 && dy == 0) {
                    continue;
                }

                // +90
                int[] r = new int[2];
                r[0] = p[0] - dy;
                r[1] = p[1] + dx;
                int[] sPoint = new int[2];
                sPoint[0] = q[0] - dy;
                sPoint[1] = q[1] + dx;
                if (pts.contains(key(r)) && pts.contains(key(sPoint))) {
                    return true;
                }

                // -90
                r[0] = p[0] + dy;
                r[1] = p[1] - dx;
                sPoint[0] = q[0] + dy;
                sPoint[1] = q[1] - dx;
                if (pts.contains(key(r)) && pts.contains(key(sPoint))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String key(int[] xy) {
        return xy[0] + "," + xy[1];
    }
}
