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
            if (!c.conditionIsTrue(game, id)){
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
     * @param id the id of the player excecuting this script
     */
    private void executeFunction(Game game, int id){

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
            case ATTACK:
                // Keep trying to attack - but if we succeed, we don't attack elsewhere.
                boolean success;
                success = game.attack(id, -1, 0);
                success = success ? success : game.attack(id, 1, 0);
                success = success ? success : game.attack(id, 0, 1);
                success = success ? success : game.attack(id, 0, -1);
                break;
            default:
                game.doNothing(id);
                break;
        }
    }
}
