package interpreter;

import interpreter.enumerations.Command;

import java.util.List;

/**
 * This class represents a single line in the user generated script.
 */
public class ScriptCommand {

    private List<Check> checks;
    private Command command;

    public ScriptCommand(List<Check> checks, Command command) {
        this.checks = checks;
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public List<Check> getChecks() {
        return checks;
    }
}
