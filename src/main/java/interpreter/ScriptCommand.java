package interpreter;

import interpreter.enumerations.Command;

import utilties.models.Game;
import java.util.List;

/**
 * This class represents a single line in the user generated script.
 */
public class ScriptCommand {

    private List<Check> checks;
    private Command command;
    private int id;
    private int otherId;

    public ScriptCommand(List<Check> checks, Command command, int id) {
        this.checks = checks;
        this.command = command;
        this.id = id;
        if (id == 1){this.otherId = 0;} else {this.otherId = 1;};
    }

    public Command getCommand() {
        return command;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public boolean performCommand(Game game){

        for (Check c: this.checks){
            if (!c.conditionIsTrue(game)){
                return false;
            }
        }

        doCommand(game);
        return true;
    }


    private void doCommand(Game game){

        switch(this.command){
            case APPROACH:
                game.approach(this.id, this.otherId);
                break;
            case HEAL:
                game.heal(this.id, 20);
                break;
            case DO_NOTHING:
                game.doNothing(this.id);
                break;
            case DEFEND:
                game.defend(this.id);
                break;
        }
    }
}
