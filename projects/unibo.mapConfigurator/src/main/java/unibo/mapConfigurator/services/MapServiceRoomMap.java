package unibo.mapConfigurator.services;

import org.springframework.stereotype.Service;
import unibo.planner23.Planner23Util;
import unibo.planner23.model.Box;
import unibo.planner23.model.RoomMap;
import unibo.mapConfigurator.interfaces.MapService;
import unibo.mapConfigurator.model.MapConfiguration;

import java.io.IOException;


@Service("gridServiceRoomMap")
public class MapServiceRoomMap implements MapService {

    @Override
    public boolean compileMap(MapConfiguration mapConfiguration) {
        RoomMap roomMap = RoomMap.getRoomMap();

        Box tempBox;
        for (int i = 0; i < mapConfiguration.getHeight(); i++) {
            for (int j = 0; j < mapConfiguration.getWidth(); j++) {
                switch (mapConfiguration.getCompact().charAt(i * mapConfiguration.getWidth() + j)) {
                    case 'C': //ColdRoom is Obstacle
                        tempBox = new Box(true, false, false);
                        break;
                    case '0': //0 is Dirty
                        tempBox = new Box(false, true, false);
                        break;
                    case 'H': //Home is Robot initial position
                        tempBox = new Box(false, false, true);
                        break;
                    case '1', 'I', 'P': //1, I and P are explored and not obstacle
                        tempBox = new Box(false, false, false);
                        break;
                    default:
                        return false;
                }
                roomMap.put(j, i, tempBox);
            }
        }
        return true;
    }

    @Override
    public boolean dumpMap(MapConfiguration mapConfiguration) {
        Planner23Util planner = new Planner23Util();
        try {
            planner.saveRoomMap(mapConfiguration.getName());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
