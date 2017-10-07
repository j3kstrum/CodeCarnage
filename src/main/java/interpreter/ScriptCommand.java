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


    public boolean performCommand(Game game, int id){

        for (Check c: this.checks){
            if (!c.conditionIsTrue(game)){
                return false;
            }
        }

        doCommand(game, id);
        return true;
    }


    private void doCommand(Game game, int id){

        int otherId;

        if (id == 0){otherId = 1;}else{otherId = 0;}

        switch(this.command){
            case APPROACH:
                game.approach(id, otherId);
                break;
            case HEAL:
                game.heal(id, 20);
                break;
            case DO_NOTHING:
                game.doNothing(id);
                break;
            case DEFEND:
                game.defend(id);
                break;
        }
    }
}
