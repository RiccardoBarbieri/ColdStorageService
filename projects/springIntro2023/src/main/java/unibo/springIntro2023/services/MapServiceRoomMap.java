package unibo.springIntro2023.services;

import unibo.planner23.Planner23Util;
import unibo.planner23.model.RoomMap;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import unibo.springIntro2023.interfaces.MapService;
import unibo.springIntro2023.model.MapConfiguration;



@Service("gridServiceRoomMap")
public class MapServiceRoomMap implements MapService {

    @Override
    public void dumpMap(MapConfiguration mapConfiguration) {
        LoggerFactory.getLogger(MapServiceRoomMap.class).info("MapServiceRoomMap dumpMap " + mapConfiguration);
        for (int i = 0; i < mapConfiguration.getHeight(); i++) {
            for (int j = 0; j < mapConfiguration.getWidth(); j++) {
                Planner23Util planner = new Planner23Util();
                RoomMap roomMap = RoomMap.getRoomMap();

            }
        }
    }
}
