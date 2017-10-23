/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package engine.core;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.mapeditor.core.Map;

/**
 * Ticking service used by JavaFX. Used to compute the next turn
 * and other tick elements on a JavaFX slave thread.
 */
public class TickingService extends Service<Map> {

    private Engine _engine;
    private long lastTick = 0;
    private Map _nextTurn = null;

    /**
     * Initialize the service.
     * @param engine The engine to use for ticking.
     */
    public TickingService(Engine engine) {
        _engine = engine;
    }

    /**
     * Generates the task to be used when the service is utilized.
     * @return The task to be called.
     */
    protected Task<Map> createTask() {
        lastTick = System.currentTimeMillis();

        return new Task<Map>() {
            /**
             * Performs an individual tick, then waits until the required tick time has elapsed.
             * @return The Map representing the next turn.
             */
            @Override protected Map call() throws Exception {
                _nextTurn = _engine.tick();
                lastTick = _engine.performWait(lastTick);
                return _nextTurn;
            }
        };
    }

}
