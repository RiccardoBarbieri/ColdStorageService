package utils;

import kotlin.Pair;

public class Utils {

    public static Float distance(Pair<Integer, Integer> coord1, Pair<Integer, Integer> coord2) {
        return (float) Math.sqrt(Math.pow(coord1.getFirst() - coord2.getFirst(), 2) + Math.pow(coord1.getSecond() - coord2.getSecond(), 2));
    }
}
