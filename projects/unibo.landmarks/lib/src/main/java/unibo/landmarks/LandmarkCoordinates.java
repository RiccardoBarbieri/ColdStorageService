package unibo.landmarks;

import kotlin.Pair;

import java.io.*;
import utils.Utils;
import java.util.List;
import java.util.Map;

public class LandmarkCoordinates implements Serializable {


    private Map<String, List<Pair<Integer, Integer>>> coordinates;

    public LandmarkCoordinates(Map<String, List<Pair<Integer, Integer>>> coordinates) {
        this.coordinates = coordinates;
    }

    public LandmarkCoordinates() {
        this.coordinates = new java.util.HashMap<>();
    }

    public Map<String, List<Pair<Integer, Integer>>> getCoordinates() {
        return coordinates;
    }

    public Pair<Integer, Integer> getCoordinate(String landmark) {
        if (!this.coordinates.containsKey(landmark)) {
            throw new RuntimeException("Landmark " + landmark + " not found");
        }
        return this.coordinates.get(landmark).get(0);
    }

    public Pair<Integer, Integer> getCoordinateClosestToFor(String landmark, Pair<Integer, Integer> coordinate) {
        if (!this.coordinates.containsKey(landmark)) {
            throw new RuntimeException("Landmark " + landmark + " not found");
        }
        List<Pair<Integer, Integer>> coordinates = this.coordinates.get(landmark);
        if (coordinates.size() == 1) {
            return coordinates.get(0);
        }
        Pair<Integer, Integer> closestCoordinate = coordinates.get(0);
        for (Pair<Integer, Integer> coord : coordinates) {
            if (Utils.distance(coord, coordinate) < Utils.distance(closestCoordinate, coordinate)) {
                closestCoordinate = coord;
            }
        }
        return closestCoordinate;
    }

    public void setCoordinates(Map<String, List<Pair<Integer, Integer>>> coordinates) {
        this.coordinates = coordinates;
    }

    public void addCoordinate(String landmark, Pair<Integer, Integer> coordinate) {
        if (!this.coordinates.containsKey(landmark)) {
            this.coordinates.put(landmark, new java.util.ArrayList<>());
        }
        this.coordinates.get(landmark).add(coordinate);
    }

    public void setCoordinate(String landmark, List<Pair<Integer, Integer>> coordinates) {
        this.coordinates.put(landmark, coordinates);
    }

    public void dump(String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileName + "Coordinates.bin");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.flush();
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(String fileName) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(fileName + "Coordinates.bin");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(fileInputStream);
            LandmarkCoordinates landmarkCoordinates = (LandmarkCoordinates) objectInputStream.readObject();
            this.coordinates = landmarkCoordinates.getCoordinates();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
