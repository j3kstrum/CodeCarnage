/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package common.data;

/**
 * The class representing the Game EntityMap.
 * This will contain all pertinent data and could eventually be used as a template for a
 * "GameMapDelta" object for efficiency.
 *
 * @author jacob.ekstrum
 */
public class GameMap {
    // TODO: Populate. I think Sean started some work on this.

    private org.mapeditor.core.Map _staticMap;

    public GameMap(org.mapeditor.core.Map tiledMap) {
        this._staticMap = tiledMap;
    }
}
