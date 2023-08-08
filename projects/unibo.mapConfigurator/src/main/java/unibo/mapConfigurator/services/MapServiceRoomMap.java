package unibo.mapConfigurator.services;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import unibo.planner23.Planner23Util;
import unibo.planner23.model.Box;
import unibo.planner23.model.RoomMap;
import unibo.mapConfigurator.interfaces.MapService;
import unibo.mapConfigurator.model.MapConfiguration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;


@Service("gridServiceRoomMap")
public class MapServiceRoomMap implements MapService {

    @Value("${mapservice.destination}")
    private String destination;

    private String landmarkRoomMap;

    @Override
    public boolean compileMap(MapConfiguration mapConfiguration) {
        RoomMap roomMap = RoomMap.getRoomMap();
        StringBuilder sb = new StringBuilder();

        Box tempBox;
        for (int i = 0; i < mapConfiguration.getHeight(); i++) {
            sb.append("|");
            for (int j = 0; j < mapConfiguration.getWidth(); j++) {
                switch (mapConfiguration.getCompact().charAt(i * mapConfiguration.getWidth() + j)) {
                    case 'C': //ColdRoom is Obstacle
                        tempBox = new Box(true, false, false);
                        sb.append("C, ");
                        break;
                    case '0': //0 is Dirty
                        tempBox = new Box(false, true, false);
                        sb.append("0, ");
                        break;
                    case 'H': //Home is Robot initial position
                        tempBox = new Box(false, false, true);
                        sb.append("r, ");
                        break;
                    case '1': //1, I and P are explored and not obstacles
                        tempBox = new Box(false, false, false);
                        sb.append("1, ");
                        break;
                    case 'I':
                        tempBox = new Box(false, false, false);
                        sb.append("I, ");
                        break;
                    case 'P':
                        tempBox = new Box(false, false, false);
                        sb.append("P, ");
                        break;
                    default:
                        return false;
                }
                roomMap.put(j, i, tempBox);
            }
            sb.append(System.lineSeparator());
        }
        landmarkRoomMap = sb.toString();
        return true;
    }

    @Override
    public boolean dumpMap(MapConfiguration mapConfiguration) {
        Planner23Util planner = new Planner23Util();
        try {
            planner.saveRoomMap(destination + mapConfiguration.getName());
            FileWriter file = new FileWriter(destination + mapConfiguration.getName() + "Landmark.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print(landmarkRoomMap);
            writer.close();
        } catch (IOException e) {
            LoggerFactory.getLogger(MapServiceRoomMap.class).error("IOException dumping map \"" + mapConfiguration.getName() + "\""
                    + System.lineSeparator() + e.getMessage());
            return false;
        }
        return true;
    }
}
