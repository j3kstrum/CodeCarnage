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

    public ScriptCommand(List<Check> checks, Command command) {
        this.checks = checks;
        this.command = command;
        this.id = -1;
        this.otherId = -1;
    }

    public Command getCommand() {
        return command;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public int getId(){return this.id;}

    public int getOtherId(){return this.otherId;}

    public void setId(int id){
        this.id = id;
        if (id == 0){
            this.otherId = 1;
        } else {
            this.otherId = 0;
        }
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
