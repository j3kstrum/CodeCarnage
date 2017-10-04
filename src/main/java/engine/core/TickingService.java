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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Ticking service used by JavaFX. Used to compute the next turn
 * and other tick elements on a JavaFX slave thread.
 */
public class TickingService extends Service<Map> {

    private Engine _engine;
    private long lastTick = 0;
    private Map _nextTurn = null;

    private AtomicBoolean _resultReady;

    /**
     * Initialize the service.
     * @param engine The engine to use for ticking.
     */
    public TickingService(Engine engine) {
        _engine = engine;
        _resultReady = new AtomicBoolean();
        _resultReady.set(false);
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
                _resultReady.set(false);
                _nextTurn = _engine.tick();
                lastTick = _engine.performWait(lastTick);
                _resultReady.set(true);
                return _nextTurn;
            }
        };
    }

    /**
     * Forces the service to register the result as not being ready.
     */
    public void forceResultNotReady() {
        this._resultReady.set(false);
    }

    /**
     *
     * @return The time that the last tick began at.
     */
    public long tickStart() {
        return lastTick;
    }

    /**
     *
     * @return True if the computed result is ready, False otherwise.
     */
    public boolean resultReady() {
        return _resultReady.get();
    }

}
