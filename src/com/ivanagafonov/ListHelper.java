package com.ivanagafonov;

import java.util.*;

public abstract class ListHelper {
    public static <T> boolean deepEquals2D(List<List<T>> o1, List<List<T>> o2){
        if (o1.size() != o2.size())
            return false;
        for (int i = 0; i < o1.size(); i++) {
            if (o1.get(i).size() != o2.get(i).size())
                return false;
            for (int j = 0; j < o1.get(i).size(); j++) {
                if (!o1.get(i).get(j).equals(o2.get(i).get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static <T extends Cell> List<List<T>> deepCopy2D(List<List<T>> o1) {
        int rows = o1.size();
        int columns = o1.get(0).size();

        List<List<T>> o2 = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            o2.add(i, Collections.synchronizedList(new ArrayList<>(columns)));
            for (int j = 0; j < columns; j++) {
                o2.get(i).add(j, (T) new Cell(o1.get(i).get(j).isCaptured()));
            }
        }

        return o2;
    }
}

