package unibo.mapConfigurator.interfaces;

import unibo.mapConfigurator.model.MapConfiguration;

public interface MapService {

    boolean compileMap(MapConfiguration map);

    boolean dumpMap(MapConfiguration map);

}
