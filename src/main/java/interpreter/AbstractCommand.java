/*
 * Copyright (c) 2017. Licensed under the Apache License 2.0.
 * For full copyright, licensing, and sourcing information,
 * please refer to the CodeCarnage GitHub repository's README.md file
 * (found on https://github.com/j3kstrum/CodeCarnage).
 */

package interpreter;

import javafx.scene.control.Toggle;
import utilties.models.*;
import java.util.List;

// The actual command will be defined when implmenting the abstract class
public abstract class AbstractCommand implements Toggle {

    private int priority;
    private List<Check> checks;

    public AbstractCommand(List<Check> checks, int priority) {
        this.checks = checks;
        this.priority = priority;
    }

    public abstract boolean operation(Game game);

}
