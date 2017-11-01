package engine.core;

import common.BaseLogger;
import common.exceptions.LoadMapFailedException;
import org.mapeditor.core.Map;
import org.mapeditor.io.MapReader;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by abarganier on 10/27/17.
 *
 * Context object used to provide dependencies to the Engine class.
 * Contains features for asynchronous loading of hosted map files.
 */
public class EngineContext {

    private static final BaseLogger CTX_LOGGER = new BaseLogger("EngineContext");
    private static final String MAP_URL = "https://www.cse.buffalo.edu/~jacobeks/codecarnage/me/r/game-map.tmx";

    private Future<Map> mapFuture;

    public EngineContext() {
    }

    public String getMapUrl() {
        return MAP_URL;
    }

    public Future<Map> getMapFuture() {
        if (mapFuture == null) {
            throw new IllegalStateException("Map Future uninitialized!");
        }
        return mapFuture;
    }

    public void loadMapAsync() {
        if (mapFuture == null || mapFuture.isCancelled()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Callable<Map> loadMapTask = () -> {
                try {
                    URL mapPath = new URL(MAP_URL);
                    MapReader mr = new MapReader();
                    Map map = mr.readMap(mapPath);
                    CTX_LOGGER.info("Finished loading map.");
                    return map;
                } catch (Exception e) {
                    CTX_LOGGER.fatal("FATAL: Map failed to load in EngineContext Future.");
                    throw new LoadMapFailedException("Failed to load map from host " + MAP_URL);
                }
            };
            mapFuture = executor.submit(loadMapTask);
        } else if (mapFuture.isDone()) {
            CTX_LOGGER.warning("Warning: called loadMapAsync when map was already loaded. Aborting task.");
        } else {
            CTX_LOGGER.warning("Warning: called loadMapAsync while map was already loading. Letting map load finish...");
        }
    }
}
