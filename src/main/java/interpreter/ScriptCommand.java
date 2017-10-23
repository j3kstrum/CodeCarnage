package interpreter;

import common.BaseLogger;
import interpreter.enumerations.Command;
import utilties.models.Game;

import java.util.List;

/**
 * This class represents a single line in the user generated script.
 */
public class ScriptCommand {

    private List<Check> checks;
    private Command command;
    private static final BaseLogger LOGGER = new BaseLogger("ScriptCommand");

    public ScriptCommand(List<Check> checks, Command command) {
        this.checks = checks;
        this.command = command;
    }

    /**
     * @return the command enum
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return the list of checks for this command
     */
    public List<Check> getChecks() {
        return checks;
    }

    /**
     * Method called to excecute a 'line' of the interpreter.
     *
     * @param game the instance of the game class passed in
     * @param id the id of the player excecuting this script
     * @return true if the command excecuted
     */
    public boolean doCommand(Game game, int id){

        for (Check c: this.checks){
            if (!c.conditionIsTrue(game, id)) {
                return false;
            }
        }
        executeFunction(game, id);
        return true;
    }

    /**
     * Called by doCommand to actually modify the game data with built-in functions
     *
     * @param game the instance of the game class passed in
     * @param id the id of the player executing this script
     */
    private void executeFunction(Game game, int id){

        int otherId;

        if (id == 0){otherId = 1;}else{otherId = 0;}

        if(id == 1){
            LOGGER.debug("Executing attack for player " + id + " "+ this.command);
        }

        switch(this.command){
            case APPROACH:
                game.approach(id, otherId);
                break;
            case ATTACK:
                game.attack(id);
                break;
            case HEAL:
                game.heal(id, 9);
                break;
            case EVADE:
                game.evade(id, otherId);
            case DO_NOTHING:
                game.doNothing(id);
                break;
            case DEFEND:
                game.defend(id);
                break;
            default:
                game.doNothing(id);
                break;
        }
    }
}
